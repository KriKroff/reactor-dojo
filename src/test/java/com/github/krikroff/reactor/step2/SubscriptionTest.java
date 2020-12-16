package com.github.krikroff.reactor.step2;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionTest {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    public void subscriptionBehaviorExample() {
        final Mono<Integer> integerMono = Mono.fromCallable(() -> atomicInteger.addAndGet(1));

        StepVerifier.create(integerMono).expectNextCount(1).expectComplete().verify();
        StepVerifier.create(integerMono).expectNextCount(1).expectComplete().verify();

        assertTrue(atomicInteger.get() == 9999);
    }


    @Test
    public void subscriptionWithSubscribe() {
        Function<Integer, Consumer<Integer>> consumer = i -> s -> System.out.println(i + " : " + Thread.currentThread().getName());

        final Mono<Integer> integerMono = Mono.fromCallable(() -> {
            System.out.println("1 - " + Thread.currentThread().getName());
            return atomicInteger.addAndGet(1);
        });

        System.out.println("2 - " + Thread.currentThread().getName());
        integerMono
                .doOnNext(consumer.apply(1))
                .subscribeOn(Schedulers.newSingle("Single1"))
                .doOnNext(consumer.apply(2))
                .publishOn(Schedulers.newSingle("Single2"))
                .doOnNext(consumer.apply(3))
                .subscribe((i) -> System.out.println(" 4 - " + Thread.currentThread().getName()));

        assertTrue(atomicInteger.get() == 3);
    }

}
