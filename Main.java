import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    public static List<Task> tasks;
    public static Scanner consoleInput;
    public static Stack<Task> editedTaskPath;


    public static void main(String[] args) throws Exception {
        editedTaskPath = new Stack<Task>();
        tasks = new ArrayList<Task>();
        OpenList();
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
                SaveList();
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
            case "modifier":
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
                    editTask(tasks.get(number-1));
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
                        if (desc.length() > 0)
                        desc = desc.substring(0, desc.length()-1);//on mets un substring car on a un retour à la ligne en trop à la fin
                    task.setDescription(desc);
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
                    if (desc.length() > 0)
                        desc = desc.substring(0, desc.length()-1);//on mets un substring car on a un retour à la ligne en trop à la fin
                    task.setDescription(desc);
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
                    editedTaskPath.push(task);

                    System.out.println("Vous allez créer une nouvelle sous-tâche, appuyer sur Entrée...");
                    consoleInput.nextLine();
                    task.getTasks().add(newTask());

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
    public static void editTask(Task toEdit)
    {
        editedTaskPath.push(toEdit);
        if (toEdit instanceof CheckTask)
        {
            CheckTask task = (CheckTask)toEdit;
            boolean finish = false;
            while(!finish)
            {
                clearConsole();
                displayPath();
                task.display(()->{}, ()->{});
                System.out.print("\nActions possibles :\ntitre\ndescription\nvalider <num>\nannuler <num>\nsupprimer <num>\nnouvelle\nretour\n>");
                String choice = consoleInput.next();
                switch(choice)
                {
                case "titre":
                    {
                        consoleInput.nextLine();
                        System.out.println("Donner un titre à la tâche :");
                        task.setTitle(consoleInput.nextLine());
                    }
                    break;
                case "description":
                    {
                        consoleInput.nextLine();
                        System.out.println("Donner une description à la tâche (Entrer deux fois pour terminer) :");
                        String line, desc = "";
                        while ((line = consoleInput.nextLine()).length() > 0)
                            desc += line + '\n';
                        if (desc.length() > 0)
                            desc = desc.substring(0, desc.length()-1);//on mets un substring car on a un retour à la ligne en trop à la fin
                        task.setDescription(desc);
                    }
                    break;
                case "retour":
                    consoleInput.nextLine();
                    finish = true;
                    break;
                case "valider":
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
                        if (number <= 0 || number > task.getSteps().length) {
                            System.out.println("Entrez un nombre valide");
                            break;
                        }
                        task.checkStep(number-1);
                    }
                    break;
                case "annuler":
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
                        if (number <= 0 || number > task.getSteps().length) {
                            System.out.println("Entrez un nombre valide");
                            break;
                        }
                        task.uncheckStep(number-1);
                    }
                    break;
                case "supprimer":
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
                        if (number <= 0 || number > task.getSteps().length) {
                            System.out.println("Entrez un nombre valide");
                            break;
                        }
                        task.removeStep(number-1);
                    }
                    break;
                case "nouvelle":
                    {
                        consoleInput.nextLine();
                        System.out.println("Entrez le nom de chaque étapes (ne mettez rien pour terminer)");
                        String step;
                        while ((step = consoleInput.nextLine()).length() > 0)
                            task.addStep(step);
                    }
                    break;
                default:
                    System.out.println("Commande inconnue");
                }
            }
        }
        else if (toEdit instanceof DescriptiveTask)
        {
            DescriptiveTask task = (DescriptiveTask)toEdit;
            boolean finish = false;
            while(!finish)
            {
                clearConsole();
                displayPath();
                task.display(()->{}, ()->{});
                System.out.print("\nActions possibles :\ntitre\ndescription\nretour\n>");
                String choice = consoleInput.next();
                consoleInput.nextLine();
                switch(choice)
                {
                case "titre":
                    {
                        System.out.println("Donner un titre à la tâche :");
                        task.setTitle(consoleInput.nextLine());
                    }
                    break;
                case "description":
                    {
                        System.out.println("Donner une description à la tâche (Entrer deux fois pour terminer) :");
                        String line, desc = "";
                        while ((line = consoleInput.nextLine()).length() > 0)
                            desc += line + '\n';
                        if (desc.length() > 0)
                            desc = desc.substring(0, desc.length()-1);//on mets un substring car on a un retour à la ligne en trop à la fin
                        task.setDescription(desc);
                    }
                    break;
                case "retour":
                    finish = true;
                    break;
                default:
                    System.out.println("Commande inconnue");
                }
            }
        }
        else if (toEdit instanceof MultiTask)
        {
            MultiTask task = (MultiTask)toEdit;
            boolean finish = false;
            while(!finish)
            {
                clearConsole();
                displayPath();
                task.display(()->{}, ()->{});
                System.out.print("\nActions possibles :\ntitre\nvalider <num>\nannuler <num>\nsupprimer <num>\nmodifier <num>\nnouvelle\nretour\n>");
                String choice = consoleInput.next();
                switch(choice)
                {
                case "titre":
                    {
                        consoleInput.nextLine();
                        System.out.println("Donner un titre à la tâche :");
                        task.setTitle(consoleInput.nextLine());
                    }
                    break;
                case "retour":
                    consoleInput.nextLine();
                    finish = true;
                    break;
                case "valider":
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
                        if (number <= 0 || number > task.getTasks().size()) {
                            System.out.println("Entrez un nombre valide");
                            break;
                        }
                        task.getTasks().get(number-1).check();
                    }
                    break;
                case "annuler":
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
                        if (number <= 0 || number > task.getTasks().size()) {
                            System.out.println("Entrez un nombre valide");
                            break;
                        }
                        task.getTasks().get(number-1).uncheck();
                    }
                    break;
                case "supprimer":
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
                        if (number <= 0 || number > task.getTasks().size()) {
                            System.out.println("Entrez un nombre valide");
                            break;
                        }
                        task.getTasks().remove(number-1);
                    }
                    break;
                case "modifier":
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
                        if (number <= 0 || number > task.getTasks().size()) {
                            System.out.println("Entrez un nombre valide");
                            break;
                        }
                        editTask(task.getTasks().get(number-1));
                    }
                    break;
                case "nouvelle":
                    {
                        consoleInput.nextLine();
                        task.getTasks().add(newTask());
                    }
                    break;
                default:
                    System.out.println("Commande inconnue");
                }
            }
        }
        editedTaskPath.pop();
    }

    public static void OpenList() throws Exception {
        File dataFile = new File("list.ser");
        if (dataFile.exists()) {
            ObjectInputStream reader = new ObjectInputStream(new FileInputStream(dataFile));
            tasks = (List<Task>)reader.readObject();
            reader.close();
        }
        else {
            tasks = new ArrayList<Task>();
        }
    }
    public static void SaveList() throws Exception {
        File dataFile = new File("list.ser");
        dataFile.createNewFile();
        ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(dataFile));
        writer.writeObject(tasks);
        writer.close();
    }
}