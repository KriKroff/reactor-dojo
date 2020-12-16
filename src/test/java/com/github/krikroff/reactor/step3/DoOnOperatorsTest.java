package com.github.krikroff.reactor.step3;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoOnOperatorsTest {

    /**
     * doOn
     */
    @Test
    public void monoDoOnNext() {
        List<String> messages = new ArrayList<>();
        final Mono<Integer> monoTest = Mono.just("Hello")
                .doOnNext(s -> messages.add("n1"))
                .doOnSuccess(s -> messages.add("s"))
                .doOnNext(s -> messages.add("n2"))
                .map(String::toLowerCase)
                .map(String::length);

        StepVerifier.create(monoTest)
                .expectNextCount(1)
                .verifyComplete();
        assertTrue(String.join(",", messages).equals("")); // example n2,n1,s
    }

    /**
     * doOn
     */
    @Test
    public void fluxDoOnNext() {
        List<String> messages = new ArrayList<>();
        final Flux<String> fluxTest = Flux.just("Hello", "man")
                .doOnNext(s -> messages.add("n1"))
                .doOnNext(s -> messages.add("n2"));

        StepVerifier.create(fluxTest)
                .expectNextCount(2)
                .verifyComplete();
        assertTrue(String.join(",", messages).equals("")); // example n2,n1
    }

    /**
     * doOnNext With Error
     */
    @Test
    public void errorDuringDoOnNext() {
        final Mono<Integer> monoTest = Mono.just("Hello")
                .doOnNext(s -> {
                    throw new RuntimeException("Oups");
                })
                .map(String::toLowerCase)
                .map(String::length);

        StepVerifier.create(monoTest)
                .expectNextCount(1)
                .verifyComplete();
    }

    /**
     * doOnSubscribe
     */
    @Test
    public void doOnSubscribe() {
        List<String> messages = new ArrayList<>();

        final Mono<Integer> monoTest = Mono.just("Hello")
                .doOnNext(s -> messages.add("n1"))
                .doOnSuccess(s -> messages.add("s"))
                .doOnNext(s -> messages.add("n2"))
                .doOnSubscribe(s -> messages.add("sub"))
                .map(String::toLowerCase)
                .map(String::length);
        
        StepVerifier.create(monoTest)
                .expectNextCount(1)
                .verifyComplete();
        
        assertTrue(String.join(",", messages).equals("")); // example n2,n1

    }

}
