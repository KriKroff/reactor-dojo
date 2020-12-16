package com.github.krikroff.reactor.step3;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.github.krikroff.reactor.TestConst.TO_REPLACE;

public class ZipOperatorsTest {

    @Test
    public void zipOperator() {
        final Mono<Integer> monoTest = Mono.just("Hello")
                .zipWith(Mono.just("man"))
                .map(tuple -> tuple.getT1() + tuple.getT2())
                .map(String::toLowerCase)
                .map(String::length);

        StepVerifier.create(monoTest)
                .expectNext(TO_REPLACE)
                .verifyComplete();
    }

    /**
     * FLATMAP
     */
    @Test
    public void zipFail() {
        final Mono<Integer> monoTest = Mono.just("Hello")
                .zipWith(Mono.empty())
                .map(tuple -> tuple.getT1() + tuple.getT2())
                .map(String::toLowerCase)
                .map(String::length);

        StepVerifier.create(monoTest)
                .expectNext(TO_REPLACE)
                .verifyComplete();
    }

    @Test
    public void zipFail2() {
        final Mono<Integer> monoTest = Mono.empty()
                .zipWith(Mono.just("Hello"))
                .map(tuple -> tuple.getT1() + tuple.getT2())
                .map(String::toLowerCase)
                .map(String::length);

        StepVerifier.create(monoTest)
                .expectNext(TO_REPLACE)
                .verifyComplete();
    }
}
