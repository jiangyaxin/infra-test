package com.jyx.infra.datetime;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StopWatch {

    private final String id;

    private boolean keepTaskList = true;

    private final List<TaskInfo> taskList = new ArrayList<>(1);

    private long startTimeNanos;

    private String currentTaskName;

    private TaskInfo lastTaskInfo;

    private int taskCount;

    private long totalTimeNanos;

    public StopWatch() {
        this("");
    }

    public StopWatch(String id) {
        this.id = id;
    }

    public static StopWatch of() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return stopWatch;
    }

    public static StopWatch ofId(String id) {
        StopWatch stopWatch = new StopWatch(id);
        stopWatch.start();
        return stopWatch;
    }

    public static StopWatch ofTask(String taskName) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(taskName);
        return stopWatch;
    }

    public static StopWatch ofIdAndTask(String id, String taskName) {
        StopWatch stopWatch = new StopWatch(id);
        stopWatch.start(taskName);
        return stopWatch;
    }


    public String getId() {
        return this.id;
    }

    public void setKeepTaskList(boolean keepTaskList) {
        this.keepTaskList = keepTaskList;
    }

    public void start() throws IllegalStateException {
        start("");
    }

    public void start(String taskName) throws IllegalStateException {
        if (this.currentTaskName != null) {
            throw new IllegalStateException("Can't start StopWatch: it's already running");
        }
        this.currentTaskName = taskName;
        this.startTimeNanos = System.nanoTime();
    }

    public void stop() throws IllegalStateException {
        if (this.currentTaskName == null) {
            throw new IllegalStateException("Can't stop StopWatch: it's not running");
        }
        long lastTime = System.nanoTime() - this.startTimeNanos;
        this.totalTimeNanos += lastTime;
        this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
        if (this.keepTaskList) {
            this.taskList.add(this.lastTaskInfo);
        }
        ++this.taskCount;
        this.currentTaskName = null;
    }

    public boolean isRunning() {
        return (this.currentTaskName != null);
    }

    public String currentTaskName() {
        return this.currentTaskName;
    }

    public long getLastTaskTimeNanos() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task interval");
        }
        return this.lastTaskInfo.getTimeNanos();
    }

    public long getLastTaskTimeMillis() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task interval");
        }
        return this.lastTaskInfo.getTimeMillis();
    }

    public String getLastTaskName() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task name");
        }
        return this.lastTaskInfo.getTaskName();
    }

    public TaskInfo getLastTaskInfo() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task info");
        }
        return this.lastTaskInfo;
    }

    public long getTotalTimeNanos() {
        return this.totalTimeNanos;
    }

    public long getTotalTimeMillis() {
        return nanosToMillis(this.totalTimeNanos);
    }

    public double getTotalTimeSeconds() {
        return nanosToSeconds(this.totalTimeNanos);
    }

    public int getTaskCount() {
        return this.taskCount;
    }

    public TaskInfo[] getTaskInfo() {
        if (!this.keepTaskList) {
            throw new UnsupportedOperationException("Task info is not being kept!");
        }
        return this.taskList.toArray(new TaskInfo[0]);
    }

    public String shortSummary() {
        return "StopWatch '" + getId() + "': running time = " + getTotalTimeNanos() + " ns";
    }

    public String prettyPrint() {
        long totalMillis = getTotalTimeMillis();
        StringBuilder sb = new StringBuilder(String.format("%s spend %sms", getId(), totalMillis));
        TaskInfo[] taskInfos = getTaskInfo();
        if (taskInfos != null || taskInfos.length > 0) {
            sb.append('\n');
            sb.append("---------------------------------------------\n");
            sb.append("ms         %     Task name\n");
            sb.append("---------------------------------------------\n");
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);

            for (TaskInfo task : taskInfos) {
                long taskMillis = task.getTimeMillis();
                sb.append(taskMillis).append("  ");
                sb.append(totalMillis == 0 ? "000%" : pf.format(taskMillis / (double) totalMillis)).append("  ");
                sb.append(task.getTaskName()).append('\n');
            }

        }
        return sb.toString();
    }

    @Override
    public String toString() {
        long totalMillis = getTotalTimeMillis();
        StringBuilder sb = new StringBuilder(String.format("%s spend %sms", getId(), totalMillis));
        if (this.keepTaskList) {
            for (TaskInfo task : getTaskInfo()) {
                long taskMillis = task.getTimeMillis();
                sb.append("; [").append(task.getTaskName()).append("] took ").append(taskMillis).append("ms");
                long percent = Math.round(100.0 * taskMillis / totalMillis);
                sb.append(" = ").append(percent).append('%');
            }
        }
        return sb.toString();
    }


    private static long nanosToMillis(long duration) {
        return TimeUnit.NANOSECONDS.toMillis(duration);
    }

    private static double nanosToSeconds(long duration) {
        return duration / 1_000_000_000.0;
    }

    public static final class TaskInfo {

        private final String taskName;

        private final long timeNanos;

        TaskInfo(String taskName, long timeNanos) {
            this.taskName = taskName;
            this.timeNanos = timeNanos;
        }

        public String getTaskName() {
            return this.taskName;
        }

        public long getTimeNanos() {
            return this.timeNanos;
        }

        public long getTimeMillis() {
            return nanosToMillis(this.timeNanos);
        }

        public double getTimeSeconds() {
            return nanosToSeconds(this.timeNanos);
        }

    }

}
