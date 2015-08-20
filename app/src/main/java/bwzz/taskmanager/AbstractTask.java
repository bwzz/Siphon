package bwzz.taskmanager;

import android.support.annotation.Nullable;

/**
 * Created by wanghb on 15/8/20.
 */
public abstract class AbstractTask implements ITask {
    private String id;
    private long createTime;
    private boolean isCanceled;

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

}
