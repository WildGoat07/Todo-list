import java.util.*;

public class CheckTask extends DescriptiveTask
{
    protected List<String> checklist;
    protected List<Boolean> checkStatus;
    public CheckTask() {
        super();
    }
    public CheckTask(String title, String description) {
        super(title, description);
        checklist = new ArrayList<String>();
    }
    /**
     * Removes a step
     * @param index index of the step to remove
     */
    public void removeStep(int index)
    {
        checkStatus.remove(index);
        checklist.remove(index);
    }
    /**
     * Adds a new step to the task. The step is by default unfinished
     * @param description the description of the step
     */
    public void addStep(String description)
    {
        checkStatus.add(false);
        checklist.add(description);
    }
    /**
     * Adds a new step to the task
     * @param description the description of the step
     * @param checked true if the step is already finished
     */
    public void addStep(String description, boolean checked)
    {
        checkStatus.add(checked);
        checklist.add(description);
    }
    /**
     * check a step of the task
     * @param index index of the step in the task
     */
    public void checkStep(int index)
    {
        checkStatus.set(index, true);
    }
    /**
     * uncheck a step of the task
     * @param index index of the step in the task
     */
    public void uncheckStep(int index)
    {
        checkStatus.set(index, false);
    }
    /**
     * Returns the status of a step of the task
     * @param index number of the task
     * @return true if the step is done, false otherwise
     */
    public boolean isStepChecked(int index) {
        return checkStatus.get(index);
    }
    @Override
    public void display(Runnable firstIndentation, Runnable nextIndentations) {
        //on ajoute l'indentation
        firstIndentation.run();
        System.out.print('┬');
        System.out.print(isChecked() ? "☑ " : "☐ ");
        System.out.println(title);
        //pour chaque ligne de la description
        if (description.length() > 0)
            for (String line : description.split("\n")) {
                //on ajoute l'indentation
                nextIndentations.run();
                // on affiche la ligne
                if (checkStatus.size() == 0)
                    //si notre liste ne contient aucune étape
                    System.out.println("  " + line);
                else
                    System.out.println("│ " + line);
        }
        for (int i = 0;i<checkStatus.size();i++)
        {
            boolean first = true;
            //pour chaque ligne à cocher
            for (String line : checklist.get(i).split("\n")) {
                if (first) {
                    //si on se trouve à la première ligne (avec la case à cocher)
                    nextIndentations.run();
                    if (i != checkStatus.size()-1)
                        //si on n'est pas la dernière étape
                        System.out.print("├─");
                    else
                        System.out.print("└─");
                        System.out.print(checkStatus.get(i) ? "☑ " : "☐ ");
                }
                else {
                    //si on se trouve sur une éventuelle ligne suivante
                    if (i != checkStatus.size()-1)
                        //si on n'est pas la dernière étape
                        System.out.print("│   ");
                    else
                        System.out.print("    ");
                }
                // on affiche la ligne
                System.out.println(line);
                first = false;
            }
        }
    }
    @Override
    public void check() {
        ListIterator<Boolean> it = checkStatus.listIterator();
        while (it.hasNext()) {
            it.next();
            it.set(true);
        }
    }
    @Override
    public void uncheck() {
        if (checkStatus == null)
            checkStatus = new ArrayList<Boolean>();
        ListIterator<Boolean> it = checkStatus.listIterator();
        while (it.hasNext()) {
            it.next();
            it.set(false);
        }
    }
    @Override
    public boolean isChecked() {
        ListIterator<Boolean> it = checkStatus.listIterator();
        while (it.hasNext())
            if (!it.next())
                return false;
        return true;
    }
}