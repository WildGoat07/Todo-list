import java.util.*;

public class MultiTask implements Task {

    private List<Task> innerTasks;
    private String title;

    public MultiTask() {
        innerTasks = new ArrayList<Task>();
        uncheck();
        setTitle("");
    }
    public MultiTask(String title)
    {
        this();
        setTitle(title);
    }
    public MultiTask(boolean checked)
    {
        this();
        if (checked)
            check();
        else 
            uncheck();
    }
    public MultiTask(String title, boolean checked)
    {
        this(title);
        if (checked)
            check();
        else 
            uncheck();
    }
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isChecked() {
        ListIterator<Task> it = innerTasks.listIterator();
        while (it.hasNext())
            if (!it.next().isChecked())
                return false;
        return true;
    }

    @Override
    public void check() {
        ListIterator<Task> it = innerTasks.listIterator();
        while (it.hasNext())
            it.next().check();
    }

    @Override
    public void uncheck() {
        ListIterator<Task> it = innerTasks.listIterator();
        while (it.hasNext())
            it.next().uncheck();
    }

    @Override
    public void display(Runnable firstIndentation, Runnable nextIndentations) {
        //on ajoute l'indentation
        firstIndentation.run();
        System.out.print('┬');
        System.out.print(isChecked() ? "☑ " : "☐ ");
        System.out.println(title);
        for (int i = 0;i<innerTasks.size();i++) {
            Task task = innerTasks.get(i);
            if (i == innerTasks.size()-1)
                //si on se trouve à la dernière tâche
                task.display(
                    () -> {
                        //la première indentation de la sous-tâche
                        nextIndentations.run();
                        System.out.print("└");
                    },
                    () -> {
                        //les indentations futures de la sous-tâche
                        nextIndentations.run();
                        System.out.print(" ");
                    }
                );
            else
                task.display(
                    () -> {
                        //la première indentation de la sous-tâche
                        nextIndentations.run();
                        System.out.print("├");
                    },
                    () -> {
                        //les indentations futures de la sous-tâche
                        nextIndentations.run();
                        System.out.print("│");
                    }
                );
        }
    }

    public List<Task> getTasks() {
        return innerTasks;
    }
}