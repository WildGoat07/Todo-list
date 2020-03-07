import java.text.DecimalFormat;
import java.util.*;

public class Main {
    public static List<Task> tasks;
    public static Scanner consoleInput;
    public static Stack<Task> editedTaskPath;

    public static void main(String[] args) {
        editedTaskPath = new Stack<Task>();
        tasks = new ArrayList<Task>();
        consoleInput = new Scanner(System.in);
        while (true) {
            int digits = 1;
            {
                int nbTasks = tasks.size();
                while (nbTasks > 10)
                {
                    //on compte le nombre de digits du nombre de tâches (5 tâches -> 1 digit, 43 tâches -> 2 digits, etc...)
                    nbTasks /= 10;
                    digits++;
                }
            }
            //on l'utilisera dans une lambda
            final int totalDigits = digits;
            String pattern = "";
            for (int i = 0;i<totalDigits;i++)
                pattern += '0';
            //le formatter permet de modifier la façon dont est transformée une valeur (rajouter des 0 devant par exemple) 
            DecimalFormat formatter = new DecimalFormat(pattern);
            clearConsole();
            for (int i = 0;i<tasks.size();i++) {
                //on l'utilisera dans la future lambda
                final int taskIndex = i + 1;
                tasks.get(i).display(
                () -> {
                    System.out.print(formatter.format(taskIndex));
                    System.out.print('.');
                },
                () -> {
                    for (int j = 0;j<=totalDigits;j++)
                        System.out.print(' ');
                });
            }
            System.out.print("\nActions possibles :\nvalider <num>\nannuler <num>\nsupprimer <num>\nmodifier <num>\nnouvelle\nquitter\n>");
            String choice = consoleInput.next();
            switch(choice) {
            case "quitter" :
                return;
            case "nouvelle" :
                tasks.add(newTask());
                break;
            case "valider" :
                {
                    int number;
                    try {
                        number = consoleInput.nextInt();
                    }
                    catch(Exception e) {
                        System.out.println("Entrez un nombre");
                        consoleInput.nextLine();
                        break;
                    }
                    consoleInput.nextLine();
                    if (number <= 0 || number > tasks.size()) {
                        System.out.println("Entrez un nombre valide");
                        break;
                    }
                    tasks.get(number - 1).check();
                }
                break;
            case "annuler" :
                {
                    int number;
                    try {
                        number = consoleInput.nextInt();
                    }
                    catch(Exception e) {
                        System.out.println("Entrez un nombre");
                        consoleInput.nextLine();
                        break;
                    }
                    consoleInput.nextLine();
                    if (number <= 0 || number > tasks.size()) {
                        System.out.println("Entrez un nombre valide");
                        break;
                    }
                    tasks.get(number - 1).uncheck();
                }
                break;
            case "supprimer" :
                {
                    int number;
                    try {
                        number = consoleInput.nextInt();
                    }
                    catch(Exception e) {
                        System.out.println("Entrez un nombre");
                        consoleInput.nextLine();
                        break;
                    }
                    consoleInput.nextLine();
                    if (number <= 0 || number > tasks.size()) {
                        System.out.println("Entrez un nombre valide");
                        break;
                    }
                    tasks.remove(number-1);
                }
                break;
            default:
                System.out.println("commande inconnue");
                break;
            }
        }
    }
    public static Task newTask()
    {
        while (true)
        {
            clearConsole();
            displayPath();
            System.out.println("Quelle type de tâche créer ? (1/2/3)");
            System.out.println("1. Tâche simple");
            System.out.println("2. Tâche avec cases à cocher");
            System.out.println("3. Tâche contenant d'autres sous-tâches");
            int choice;
            while (true)
                try {
                    choice = consoleInput.nextInt();
                    break;
                }
                catch(Exception e) {
                    System.out.println("Entrez un nombre");
                    consoleInput.nextLine();
                }
            consoleInput.nextLine();
            switch(choice)
            {
            case 1:
                {
                    DescriptiveTask task = new DescriptiveTask();
                    System.out.println("Donner un titre à la nouvelle tâche :");
                    task.setTitle(consoleInput.nextLine());
                    System.out.println("Donner une description à la nouvelle tâche (Entrer deux fois pour terminer) :");
                    String line, desc = "";
                    while ((line = consoleInput.nextLine()).length() > 0)
                        desc += line + '\n';
                    task.setDescription(desc.substring(0, desc.length()-1));//on mets un substring car on a un retour à la ligne en trop à la fin
                    System.out.println("Nouvelle tâche créée avec succès !");
                    return task;
                }
            case 2:
                {
                    CheckTask task = new CheckTask();
                    System.out.println("Donner un titre à la nouvelle tâche :");
                    task.setTitle(consoleInput.nextLine());
                    System.out.println("Donner une description à la nouvelle tâche (Entrer deux fois pour terminer) :");
                    String line, desc = "";
                    while ((line = consoleInput.nextLine()).length() > 0)
                        desc += line + '\n';
                    task.setDescription(desc.substring(0, desc.length()-1));//on mets un substring car on a un retour à la ligne en trop à la fin
                    System.out.println("Entrez le nom de chaque étapes (ne mettez rien pour terminer)");
                    String step;
                    while ((step = consoleInput.nextLine()).length() > 0)
                        task.addStep(step);
                    System.out.println("Nouvelle tâche créée avec succès !");
                    return task;
                }
            case 3:
                {
                    MultiTask task = new MultiTask();
                    System.out.println("Donner un titre à la nouvelle tâche :");
                    task.setTitle(consoleInput.nextLine());
                    boolean end = false;
                    editedTaskPath.push(task);
                    do
                    {
                        System.out.println("Vous allez créer une nouvelle sous-tâche, appuyer sur Entrée...");
                        consoleInput.nextLine();
                        task.getTasks().add(newTask());
                        System.out.println("Voulez-vous continuer d'ajouter des sous-tâches ? (\"o\" pour continuer)");
                        end = (consoleInput.nextLine() != "o");
                    }
                    while (!end);
                    editedTaskPath.pop();
                    System.out.println("Nouvelle tâche créée avec succès !");
                    return task;
                }
            default:
                System.out.println("Réponse incorrecte");
            }
        }
    }
    public static void clearConsole()
    {
        System.out.print(new String(new char[50]).replace("\0", "\r\n"));
    }
    public static void displayPath()
    {
        ListIterator<Task> iterator = editedTaskPath.listIterator();
        System.out.print("Vous vous trouvez ici : /");
        while (iterator.hasNext())
            System.out.print(iterator.next().getTitle() + '/');
        System.out.println();
    }
}