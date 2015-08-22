package bwzz.taskmanager;

/**
 * Created by wanghb on 15/8/22.
 */
public class TaskException extends Exception {
    public static TaskException wrap(Exception e) {
        return new TaskException(e);
    }

    private Exception wrappedException;

    private TaskException(Exception e) {
        wrappedException = e;
    }

    public Exception getException() {
        return wrappedException;
    }
}
