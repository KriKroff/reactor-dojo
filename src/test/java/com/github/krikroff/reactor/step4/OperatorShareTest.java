package com.github.krikroff.reactor.step4;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.krikroff.reactor.TestConst.TO_REPLACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OperatorShareTest {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    public void subscriptionShareExample() {
        final Mono<Integer> integerMono = Mono.fromCallable(() -> atomicInteger.addAndGet(1)).share();

        StepVerifier.create(integerMono).expectNext(TO_REPLACE).expectComplete().verify();
        StepVerifier.create(integerMono).expectNextCount(TO_REPLACE).expectComplete().verify();

        assertTrue(atomicInteger.get() == TO_REPLACE);
    }

    @Test
    public void hotObservableTestCache() throws InterruptedException {
        final Mono<Integer> integerMono = Mono.fromCallable(() -> atomicInteger.addAndGet(1))
                .cache(Duration.ofSeconds(1));

        StepVerifier.create(integerMono).expectNext(TO_REPLACE).expectComplete().verify();
        StepVerifier.create(integerMono).expectNext(TO_REPLACE).expectComplete().verify();
        Thread.sleep(1500);
        StepVerifier.create(integerMono).expectNextCount(1).expectComplete().verify();

        assertTrue(atomicInteger.get() == TO_REPLACE);
    }
}
