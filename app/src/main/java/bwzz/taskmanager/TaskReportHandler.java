package bwzz.taskmanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by wanghb on 15/8/22.
 */
public class TaskReportHandler extends Handler {

    public enum What {
        start, cancel, progress, finish
    }

    public TaskReportHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        HandlerReporter handlerReporter = (HandlerReporter) msg.obj;
        handlerReporter.report();
    }

    public void report(ITaskReporter reporter, What what, ITask task, float percent) {
        HandlerReporter handlerReporter = new HandlerReporter();
        handlerReporter.reporter = reporter;
        handlerReporter.what = what;
        handlerReporter.task = task;
        handlerReporter.percent = percent;
        sendMessage(obtainMessage(0, handlerReporter));
    }

    private static class HandlerReporter {
        private ITaskReporter reporter;
        private What what;
        private ITask task;
        private float percent;

        public void report() {
            switch (what) {
                case start:
                    reporter.onTaskStart(task);
                    break;
                case cancel:
                    reporter.onTaskCanceled(task);
                    break;
                case progress:
                    reporter.onTaskProgress(task, percent);
                    break;
                case finish:
                    reporter.onTaskFinish(task);
                    break;
                default:
                    throw new RuntimeException("report unknown evnet");
            }
        }
    }
}
