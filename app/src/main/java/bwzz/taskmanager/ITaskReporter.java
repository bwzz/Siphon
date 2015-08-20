package bwzz.taskmanager;

/**
 * @author wanghb
 * @date 15/8/19.
 */
public interface ITaskReporter {

    void onTaskStart(ITask task);

    void onTaskProgress(ITask task, float percent);

    void onTaskCanceled(ITask task);

    void onTaskFinish(ITask task);

    ITaskReporter EmptyTaskReporter = new ITaskReporter() {
        @Override
        public void onTaskStart(ITask task) {

        }

        @Override
        public void onTaskProgress(ITask task, float percent) {

        }

        @Override
        public void onTaskCanceled(ITask task) {

        }

        @Override
        public void onTaskFinish(ITask task) {

        }
    };
}
