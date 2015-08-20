package bwzz.taskmanager;

import java.util.Comparator;

/**
 * @author wanghb
 * @date 15/8/19.
 */
public class TaskComparator implements Comparator<ITask> {

    @Override
    public int compare(ITask lhs, ITask rhs) {
        if (lhs.getTaskPriority() != rhs.getTaskPriority()) {
            return rhs.getTaskPriority().ordinal() - lhs.getTaskPriority().ordinal();
        }
        return 0;
    }
}
