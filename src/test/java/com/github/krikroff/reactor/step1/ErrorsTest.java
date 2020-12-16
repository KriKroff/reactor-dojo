package com.github.krikroff.reactor.step1;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * How to create a Mono
 */
public class ErrorsTest {

    @Test
    public void errorMono() {
        final Mono<Object> mono = Mono.error(new RuntimeException("Oups"));

        StepVerifier.create(mono).expectErrorMessage("Oups").verify();
    }

    @Test
    public void monoMapError() {
        final Mono<Object> mono = Mono.just("hello")
                .doOnNext(x -> System.out.println("1 - is it Called ?"))
                .map(x -> {
                    throw new RuntimeException("Oups");
                })
                .doOnNext(x -> System.out.println("2 - is it Called ?"));

        StepVerifier.create(mono).expectErrorMessage("Oups").verify();
    }


    @Test
    public void onErrorReturn() {
        final Mono<Object> mono = Mono.error(new RuntimeException("Oups"))
                .onErrorReturn("OK");

        StepVerifier.create(mono).expectNext("TO_REPLACE")
                .verifyComplete();
    }


    @Test
    public void onErrorResume() {
        final Mono<Object> mono = Mono.error(new RuntimeException("Oups"))
                .onErrorResume(t -> Mono.just("OK"));

        StepVerifier.create(mono).expectNext("TO_REPLACE")
                .verifyComplete();
    }


}
