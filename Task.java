public interface Task {
    /**
     * Returns the title of the task
     * @return its title
     */
    public String getTitle();
    /**
     * Changes the title of the task
     * @param title new value of the title
     */
    public void setTitle(String title);

    /**
     * Returns the current state of the task
     * @return true if finished, false otherwise
     */
    public boolean isChecked();
    /**
     * Sets the task as finished
     */
    public void check();
    /**
     * Sets the task as unfinished
     */
    public void uncheck();

    /**
     * Write the task in the console
     * @param firstIndentation function that create the first of this task indentation
     * @param nextIndentations function that create an indentation for the following lines
     */
    public void display(Runnable firstIndentation, Runnable nextIndentations);
}