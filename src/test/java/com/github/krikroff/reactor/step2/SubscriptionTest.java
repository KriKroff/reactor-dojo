package com.github.krikroff.reactor.step2;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static com.github.krikroff.reactor.TestConst.TO_REPLACE;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionTest {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    public void subscriptionBehaviorExample() {
        final Mono<Integer> integerMono = Mono.fromCallable(() -> atomicInteger.addAndGet(1));

        StepVerifier.create(integerMono).expectNextCount(1).expectComplete().verify();
        StepVerifier.create(integerMono).expectNextCount(1).expectComplete().verify();

        assertTrue(atomicInteger.get() == TO_REPLACE);
    }

    @Test
    public void subscriptionWithSubscribe() {
        Mono.fromCallable(() -> atomicInteger.addAndGet(1))
                .subscribe();

        assertTrue(atomicInteger.get() == TO_REPLACE);
    }

    @Test
    public void subscriptionWithError() {
        Mono.error(new RuntimeException("Ouuups"))
                .subscribe((i) -> System.out.println("Success with " + i),
                        (t) -> System.out.println("Error : " + t.getMessage()));
    }

}
