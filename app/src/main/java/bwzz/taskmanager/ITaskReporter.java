package bwzz.taskmanager;

/**
 * @author wanghb
 * @date 15/8/19.
 */
public interface ITaskReporter {

    void onTaskStart(ITask task);

    void onTaskProgress(ITask task, float percent);

    void onTaskFinish(ITask task);
}
