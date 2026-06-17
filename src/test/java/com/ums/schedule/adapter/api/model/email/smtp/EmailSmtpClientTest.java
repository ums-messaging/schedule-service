package com.ums.schedule.adapter.api.model.email.smtp;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

class EmailSmtpClientTest {

    @Test
    void test() throws InterruptedException, ExecutionException {

        CompletableFuture[] futures = IntStream.range(1, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> new Task("task" + i)))
                .toArray(CompletableFuture[]::new);

        CompletableFuture<Task> lastTask = CompletableFuture.allOf(futures)
                .thenApply(v -> new Task("last task"));

        Task task = lastTask.get();
        System.out.println("result = " + task.getName());
    }

    class Task {
        private String name;

        public Task(String name) {
            this.name = name;
            System.out.println("name = " + name);
//            Thread.sleep(2000);
        }

        public String getName() {
            return name;
        }
    }

}