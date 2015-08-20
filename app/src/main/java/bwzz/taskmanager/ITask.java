package bwzz.taskmanager;

/**
 * @author wanghb
 * @date 15/8/19.
 */
public interface ITask {
    String getID();

    TaskPriority getTaskPriority();

    long getCreateTime();

    void cancel();

    void run(ITaskReporter taskReportor);
}
