package bwzz.taskmanager;

/**
 * Created by wanghb on 15/8/20.
 */
public abstract class AbstractTask<R> implements ITask {
    private String id;
    private long createTime;
    private boolean isCanceled;
    private R result;
    private TaskException taskException;

    public AbstractTask(String id) {
        this.id = id;
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public TaskPriority getTaskPriority() {
        return TaskPriority.normal;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public void cancel() {
        isCanceled = true;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    public R getResult() {
        return result;
    }

    protected void setResult(R result) {
        this.result = result;
    }

    public TaskException getTaskException() {
        return taskException;
    }

    protected void setTaskException(TaskException e) {
        taskException = e;
    }
}
