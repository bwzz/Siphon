package bwzz.taskmanager;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wanghb
 * @date 15/8/19.
 */
public class TaskManager {

    private List<ITask> readyTasks = new LinkedList<>();

    private List<ITask> runningTasks = new LinkedList<>();

    private final int maxRunningTaskCount;

    private ITaskReporter taskReporter;

    public TaskManager(int maxRunningTaskCount) {
        this.maxRunningTaskCount = maxRunningTaskCount;
    }

    public void setTaskReporter(ITaskReporter taskReporter) {
        if (taskReporter == null) {
            // TODO : mock reporter factory
            this.taskReporter = new ITaskReporter() {
                @Override
                public void onTaskStart(ITask task) {

                }

                @Override
                public void onTaskProgress(ITask task, float percent) {

                }

                @Override
                public void onTaskFinish(ITask task) {

                }
            };
        } else {
            this.taskReporter = taskReporter;
        }
    }

    public void addTask(ITask task) {
        if (!readyTasks.contains(task)) {
            readyTasks.add(task);
            Collections.sort(readyTasks, new Comparator<ITask>() {
                @Override
                public int compare(ITask lhs, ITask rhs) {
                    return 0;
                }
            });
        }
        schedule();
    }

    /**
     * Cancel task who return true when apply ITaskFilter.filter
     * 
     * @param taskFilter : if null, cancel all tasks
     */
    public void cancelTask(ITaskFilter taskFilter) {
        if (taskFilter == null) {
            return;
        }
        applyCancel(taskFilter, readyTasks);
        applyCancel(taskFilter, runningTasks);
    }

    private void applyCancel(ITaskFilter taskFilter, List<ITask> tasks) {
        Iterator<ITask> taskIterator = tasks.iterator();
        while (taskIterator.hasNext()) {
            ITask task = taskIterator.next();
            if (taskFilter == null || taskFilter.filter(task)) {
                taskIterator.remove();
            }
        }
    }

    private void schedule() {
        if (runningTasks.size() >= maxRunningTaskCount) {
            return;
        }
        if (readyTasks.isEmpty()) {
            return;
        }
        ITask task = readyTasks.remove(0);
        runTask(task);
    }

    private void runTask(ITask task) {
        runningTasks.add(task);
        task.run(taskReporterWrapper);
    }

    private void finishTask(ITask doneTask) {
        applyCancel((task) -> task == doneTask, runningTasks);
        schedule();
    }

    private ITaskReporter taskReporterWrapper = new ITaskReporter() {
        @Override
        public void onTaskStart(ITask task) {
            taskReporter.onTaskStart(task);
        }

        @Override
        public void onTaskProgress(ITask task, float percent) {
            taskReporter.onTaskProgress(task, percent);
        }

        @Override
        public void onTaskFinish(ITask task) {
            finishTask(task);
            taskReporter.onTaskFinish(task);
        }
    };

}
