package com.github.krikroff.reactor.step3;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.krikroff.reactor.TestConst.TO_REPLACE;

public class SwitchOperatorsTest {

    @Test
    public void defaultOperator() {
        final Mono<String> monoTest = Mono.just("Hello")
                .filter("Bonjour"::equals)
                .defaultIfEmpty("Hola");

        StepVerifier.create(monoTest)
                .expectNext("TO_REPLACE")
                .verifyComplete();
    }

    @Test
    public void switchOperator() {
        final Mono<String> monoTest = Mono.just("Hello")
                .filter("Bonjour"::equals)
                .switchIfEmpty(Mono.just("Hola")); // /!\ HOT OBSERVABLE

        StepVerifier.create(monoTest)
                .expectNext("TO_REPLACE")
                .verifyComplete();
    }
}
