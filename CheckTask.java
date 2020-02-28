import java.util.*;

import javafx.collections.*;

public class CheckTask extends DescriptiveTask
{
    protected ObservableList<String> checklist;
    protected List<Boolean> checkStatus;
    public CheckTask() {
        super();
        init();
    }
    private void init()
    {
        //on crée un wrapper autour d'une simple ArrayList
        checklist = FXCollections.observableList(new ArrayList<String>());
        //on définit le Listener qui sera appelé en cas de changements
        checklist.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                while (c.next()) {
                    //tant qu'on a des changements
                    if (c.wasAdded()) //si on a ajouté un élément
                        for (int i = c.getFrom();i<c.getTo();i++)
                            checkStatus.add(i, false);
                    if (c.wasRemoved()) //si on a supprimé un élément
                        for (int i = 0;i<c.getRemovedSize();i++)
                            //on supprime les n éléments retirés
                            checkStatus.remove(c.getFrom());
                    if (c.wasPermutated()) {
                        //si on a échangé des éléments
                        Boolean tmp = checkStatus.get(c.getFrom());
                        checkStatus.set(c.getFrom(), checkStatus.get(c.getTo()));
                        checkStatus.set(c.getTo(), tmp);
                    }
                }
            }
        });
    }
    public CheckTask(String title, String description) {
        super(title, description);
        init();
    }
    public CheckTask(boolean checked) {
        super(checked);
        init();
    }
    public CheckTask(String title, String description, boolean checked) {
        super(title, description, checked);
        init();
    }
    /**
     * Returns the list of the checkable steps
     * @return checkable steps of the task
     */
    public List<String> getList() {
        return checklist;
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
        for (String line : description.split("\n")) {
            //on ajoute l'indentation
            nextIndentations.run();
            // on affiche la ligne
            if (checkStatus.size() == 0)
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
                    nextIndentations.run();
                    if (i != checkStatus.size()-1)
                        System.out.print("├─");
                    else
                        System.out.print("└─");
                        System.out.print(checkStatus.get(i) ? "☑ " : "☐ ");
                }
                else {
                    if (i != checkStatus.size()-1)
                    System.out.print("│   ");
                else
                    System.out.print("    ");
                }
                // on affiche la ligne
                System.out.println("  " + line);
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