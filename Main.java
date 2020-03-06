import java.text.DecimalFormat;
import java.util.*;

public class Main {
    public static List<Task> tasks;
    public static Scanner consoleInput;
    
    public static void main(String[] args) {
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
            default:
                System.out.println("commande inconnue");
                break;
            }
        }
    }
}