package bwzz.taskmanager;

import java.io.*;

/**
 * Created by wanghb on 15/8/22.
 */
public class TaskException extends Throwable {
    public static TaskException wrap(Throwable e) {
        return new TaskException(e);
    }

    private Throwable wrappedException;

    private TaskException(Throwable e) {
        wrappedException = e;
    }

    public Throwable getException() {
        return wrappedException;
    }

    @Override
    public String getMessage() {
        return wrappedException.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return wrappedException.getLocalizedMessage();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return wrappedException.getStackTrace();
    }

    @Override
    public void setStackTrace(StackTraceElement[] trace) {
        wrappedException.setStackTrace(trace);
    }

    @Override
    public void printStackTrace() {
        wrappedException.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream err) {
        wrappedException.printStackTrace(err);
    }

    @Override
    public void printStackTrace(PrintWriter err) {
        wrappedException.printStackTrace(err);
    }

    @Override
    public String toString() {
        return wrappedException.toString();
    }

    @Override
    public Throwable initCause(Throwable throwable) {
        return wrappedException.initCause(throwable);
    }

    @Override
    public Throwable getCause() {
        return wrappedException.getCause();
    }

}
