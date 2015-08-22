package bwzz.taskmanager;

import android.os.Handler;
import android.os.Looper;

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

    private boolean isRunning;

    public TaskManager(int maxRunningTaskCount) {
        this.maxRunningTaskCount = maxRunningTaskCount;
    }

    public void start() {
        isRunning = true;
        schedule();
    }

    public void stop() {
        isRunning = false;
        cancelTask(null);
    }

    public void setTaskReporter(ITaskReporter taskReporter) {
        if (taskReporter == null) {
            // TODO : mock reporter factory
            this.taskReporter = ITaskReporter.EmptyTaskReporter;
        } else {
            this.taskReporter = taskReporter;
        }
    }

    public void addTask(ITask task) {
        if (!readyTasks.contains(task)) {
            readyTasks.add(task);
            Collections.sort(readyTasks, (ITask lhs, ITask rhs) -> {
                int res = lhs.getTaskPriority().compareTo(rhs.getTaskPriority());
                if (res != 0) {
                    long l = lhs.getCreateTime();
                    long r = rhs.getCreateTime();
                    res = l < r ? -1 : (l == r ? 0 : 1);
                }
                return -res;// dec
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
                task.cancel();
                taskIterator.remove();
                reportCancelTask(task);
            }
        }
    }

    private void schedule() {
        if (!isRunning) {
            return;
        }
        if (runningTasks.size() >= maxRunningTaskCount) {
            return;
        }
        if (readyTasks.isEmpty()) {
            return;
        }
        Iterator<ITask> iterator = readyTasks.iterator();
        while (iterator.hasNext()) {
            ITask task = iterator.next();
            iterator.remove();
            if (task.isCanceled()) {
                reportCancelTask(task);
            } else {
                runTask(task);
                break;
            }
        }
    }

    private void runTask(ITask task) {
        runningTasks.add(task);
        task.run(taskReporterWrapper);
    }

    private void finishTask(ITask doneTask) {
        runningTasks.remove(doneTask);
        schedule();
    }

    private void reportCancelTask(ITask task) {
        taskReporterWrapper.onTaskCanceled(task);
    }

    private ITaskReporter taskReporterWrapper = new ITaskReporter() {
        @Override
        public void onTaskStart(ITask task) {
            if (!task.isCanceled()) {
                TaskReportHandler handler = task.getTaskReportHandler();
                if (handler == null) {
                    taskReporter.onTaskStart(task);
                } else {
                    handler.report(taskReporter, TaskReportHandler.What.start, task, 0);
                }
            }
        }

        @Override
        public void onTaskProgress(ITask task, float percent) {
            if (!task.isCanceled()) {
                TaskReportHandler handler = task.getTaskReportHandler();
                if (handler == null) {
                    taskReporter.onTaskProgress(task, percent);
                } else {
                    handler.report(taskReporter, TaskReportHandler.What.progress, task, percent);
                }
            }
        }

        @Override
        public void onTaskCanceled(ITask task) {
            TaskReportHandler handler = task.getTaskReportHandler();
            if (handler == null) {
                taskReporter.onTaskCanceled(task);
            } else {
                handler.report(taskReporter, TaskReportHandler.What.cancel, task, 0);
            }
        }

        @Override
        public void onTaskFinish(ITask task) {
            if (!task.isCanceled()) {
                finishTask(task);
                TaskReportHandler handler = task.getTaskReportHandler();
                if (handler == null) {
                    taskReporter.onTaskFinish(task);
                } else {
                    handler.report(taskReporter, TaskReportHandler.What.finish, task, 0);
                }
            }
        }
    };

}
