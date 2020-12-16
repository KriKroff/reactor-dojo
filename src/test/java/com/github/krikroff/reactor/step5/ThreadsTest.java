package com.github.krikroff.reactor.step5;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadsTest {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private List<String> threadNames = new ArrayList<>();

    private void pushThread(final int i) {
        System.out.println(i + " " + Thread.currentThread().getName().split("-")[0]);
        threadNames.add(i + " " + Thread.currentThread().getName().split("-")[0]);
    }

    @Test
    public void subscriptionBehaviorExample() {
        //current thread is called "main"
        Mono.fromCallable(() -> {
            pushThread(1);
            return atomicInteger.addAndGet(1);
        })
                .doOnNext(i -> pushThread(2))
                .subscribe(i -> pushThread(3));

        assertEquals("", threadNames.get(0));
        assertEquals("", threadNames.get(1));
        assertEquals("", threadNames.get(2));
    }


    @Test
    public void subscriptionWithSubscribe() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        
        //current thread is called "main"
        Mono.fromCallable(() -> {
            pushThread(1);
            return atomicInteger.addAndGet(1);
        })
                .doOnNext(i -> pushThread(2))
                .publishOn(Schedulers.newSingle("th1"))
                .doOnNext(i -> pushThread(3))
                .subscribeOn(Schedulers.newSingle("th2"))
                .doOnNext(i -> pushThread(4))
                .subscribeOn(Schedulers.newSingle("th3"))
                .subscribe(i -> {
                    pushThread(5);
                    countDownLatch.countDown();
                });
        
        countDownLatch.await();
        assertEquals("", threadNames.get(0));
        assertEquals("", threadNames.get(1));
        assertEquals("", threadNames.get(2));
        assertEquals("", threadNames.get(3));
        assertEquals("", threadNames.get(4));

    }

}
