package com.petshop.concurrency;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread-Safe Bounded Buffer implementing Classic Producer-Consumer Pattern
 * using wait(), notify(), and notifyAll() on intrinsic monitor locks (Syllabus Unit 3).
 */
public class AdoptionQueue {
    private final int capacity;
    private final Queue<AdoptionRequest> queue = new LinkedList<>();

    public AdoptionQueue(int capacity) {
        this.capacity = capacity > 0 ? capacity : 5;
    }

    /**
     * Producer method: Appends adoption request to buffer.
     * Waits when buffer is full.
     */
    public synchronized void enqueue(AdoptionRequest request) throws InterruptedException {
        while (queue.size() >= capacity) {
            System.out.printf("  [PRODUCER: %s] Queue FULL (%d/%d). Waiting for worker...%n",
                    Thread.currentThread().getName(), queue.size(), capacity);
            wait();
        }

        queue.offer(request);
        System.out.printf("  [PRODUCER: %s] Enqueued %s | Buffer: %d/%d%n",
                Thread.currentThread().getName(), request, queue.size(), capacity);

        // Notify waiting consumer threads
        notifyAll();
    }

    /**
     * Consumer method: Retrieves next adoption request from buffer.
     * Waits when buffer is empty.
     */
    public synchronized AdoptionRequest dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        AdoptionRequest request = queue.poll();
        System.out.printf("  [CONSUMER: %s] Dequeued %s | Buffer remaining: %d/%d%n",
                Thread.currentThread().getName(), request, queue.size(), capacity);

        // Notify waiting producer threads
        notifyAll();
        return request;
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public int getCapacity() {
        return capacity;
    }
}
