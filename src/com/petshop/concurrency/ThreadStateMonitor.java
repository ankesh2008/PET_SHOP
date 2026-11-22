package com.petshop.concurrency;

/**
 * Utility for observing and tracing thread lifecycle transitions (Syllabus Unit 3).
 * Traces NEW, RUNNABLE, TIMED_WAITING, WAITING, and TERMINATED states.
 */
public final class ThreadStateMonitor {
    private ThreadStateMonitor() {}

    public static void traceState(Thread thread, String checkpoint) {
        if (thread != null) {
            System.out.printf("  [THREAD TRACE] %-22s | Checkpoint: %-15s | State: %s | Priority: %d | Daemon: %b%n",
                    thread.getName(), checkpoint, thread.getState(), thread.getPriority(), thread.isDaemon());
        }
    }
}
