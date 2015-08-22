package bwzz.taskmanager;

import android.support.annotation.Nullable;

/**
 * @author wanghb
 * @date 15/8/19.
 */
public interface ITask<R> {
    String getID();

    TaskPriority getTaskPriority();

    long getCreateTime();

    void cancel();

    boolean isCanceled();

    void run(@Nullable ITaskReporter taskReporter);

    TaskReportHandler getTaskReportHandler();

    R getResult();

    TaskException getTaskException();
}
