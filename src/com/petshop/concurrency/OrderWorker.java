package com.petshop.concurrency;

import com.petshop.model.Order;
import com.petshop.service.PetService;

/**
 * Background consumer worker thread executing adoption processing (Syllabus Unit 3: Runnable).
 */
public class OrderWorker implements Runnable {
    private final AdoptionQueue adoptionQueue;
    private final PetService petService;
    private volatile boolean running = true;

    public OrderWorker(AdoptionQueue adoptionQueue, PetService petService) {
        this.adoptionQueue = adoptionQueue;
        this.petService = petService;
    }

    public void stopWorker() {
        this.running = false;
    }

    @Override
    public void run() {
        System.out.printf("  [WORKER] %s started and listening for adoption orders.%n",
                Thread.currentThread().getName());

        while (running) {
            try {
                AdoptionRequest req = adoptionQueue.dequeue();
                // Simulate asynchronous background check & paperwork processing
                Thread.sleep(600);

                try {
                    Order order = petService.processAdoption(req.getPetId(), req.getCustomerId(), req.getPayment());
                    System.out.printf("  [WORKER: %s] SUCCESS: Processed order #%d for Pet #%d%n",
                            Thread.currentThread().getName(), order.getOrderId(), order.getPetId());
                } catch (Exception ex) {
                    System.err.printf("  [WORKER: %s] FAILED processing Pet #%d: %s%n",
                            Thread.currentThread().getName(), req.getPetId(), ex.getMessage());
                }
            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
                Thread.currentThread().interrupt();
            }
        }
        System.out.printf("  [WORKER] %s shutting down cleanly.%n", Thread.currentThread().getName());
    }
}
