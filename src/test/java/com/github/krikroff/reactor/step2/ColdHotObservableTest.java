package com.github.krikroff.reactor.step2;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.krikroff.reactor.TestConst.TO_REPLACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cold or Hot Observable - Differences
 */
public class ColdHotObservableTest {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    
    @Test
    public void hotOrColdObservableTest_withCallable() {
        Mono.fromCallable(() -> atomicInteger.addAndGet(1));

        assertTrue(atomicInteger.get() == TO_REPLACE);
    }

    @Test
    public void hotOrColdObservableTest_withJust() {
        Mono.just(atomicInteger.addAndGet(1));
        assertTrue(atomicInteger.get() == TO_REPLACE);
    }

    @Test
    public void hotOrColdObservableTest_withCallableAndSubscribe() {
        final Mono<Integer> integerMono = Mono.fromCallable(() -> atomicInteger.addAndGet(1));
        assertTrue(atomicInteger.get() == TO_REPLACE);
        
        integerMono.subscribe();
        assertTrue(atomicInteger.get() == TO_REPLACE);
    }
    
    @Test
    public void hotOrColdObservableTest_withCompletableFuture() {
        final Mono<Integer> integerMono = Mono.fromFuture(this.createCompletable());
        assertTrue(atomicInteger.get() == TO_REPLACE);
        integerMono.subscribe();
        assertTrue(atomicInteger.get() == TO_REPLACE);
    }
    
    @Test
    public void hotOrColdObservableTest_withSupplier() {
        final Mono<Integer> integerMono = Mono.fromFuture(this::createCompletable);
        assertTrue(atomicInteger.get() == TO_REPLACE);
        integerMono.subscribe();
        assertTrue(atomicInteger.get() == TO_REPLACE);
    }
    
    private CompletableFuture<Integer> createCompletable() {
        // Example a C* call
        return CompletableFuture.completedFuture(atomicInteger.addAndGet(1));
    }

}
