package com.github.krikroff.reactor.step1;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletableFuture;

/**
 * How to create a Mono
 */
public class CreationMonoTest {

    @Test
    public void createMonoStatic() {
        final Mono<String> mono = Mono.just("hello");

        StepVerifier.create(mono)
                .expectNext("hello")
                .verifyComplete();
    }

    @Test
    public void createMonoCallable() {
        final Mono<String> mono = Mono.fromCallable(() -> "hello");

        StepVerifier.create(mono)
                .expectNext("hello")
                .verifyComplete();
    }

    @Test
    public void createMonoFuture() {
        final Mono<String> mono = Mono.fromCompletionStage(CompletableFuture.completedFuture("hello"));
        final Mono<String> mono2 = Mono.fromFuture(CompletableFuture.completedFuture("hello"));

        StepVerifier.create(mono)
                .expectNext("hello")
                .verifyComplete();

        StepVerifier.create(mono2)
                .expectNext("hello")
                .verifyComplete();
    }
    
    @Test
    public void createMono() {
        final Mono<String> mono = Mono.create(stringMonoSink -> {
            stringMonoSink.success("hello");
        });

        StepVerifier.create(mono)
                .expectNext("hello")
                .verifyComplete();
    }

}
