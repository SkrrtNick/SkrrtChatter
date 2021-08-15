package scripts.api.task;


public interface Task {

    Priority priority();

    boolean validate();

    void execute();

}