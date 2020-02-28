public class DescriptiveTask implements Task {
    protected String title;
    protected boolean checked;
    protected String description;
    public DescriptiveTask() {
        uncheck();
        setTitle("");
        setDescription("");
    }
    public DescriptiveTask(String title, String description)
    {
        this();
        setTitle(title);
        setDescription(description);
    }
    public DescriptiveTask(boolean checked)
    {
        this();
        if (checked)
            check();
        else
            uncheck();
    }
    public DescriptiveTask(String title, String description, boolean checked)
    {
        this();
        setTitle(title);
        setDescription(description);
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
        return checked;
    }
    @Override
    public void check() {
        checked = true;
    }
    @Override
    public void uncheck() {
        checked = false;
    }
    @Override
    public void display(Runnable firstIndentation, Runnable nextIndentations) {
        //on ajoute l'indentation
        firstIndentation.run();
        System.out.print('─');
        System.out.print(isChecked() ? "☑ " : "☐ ");
        System.out.println(title);
        //pour chaque ligne de la description
        if (description.length() > 0)
            for (String line : description.split("\n")) {
                //on ajoute l'indentation
                nextIndentations.run();
                // on affiche la ligne
                System.out.println("  " + line);
        }
    }
    /**
     * Returns the description of the task
     * @return a short description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Changes the description of the task
     * @param description a short description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}