package com.github.krikroff.reactor.step3;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.github.krikroff.reactor.TestConst.TO_REPLACE;

public class BasicOperatorsTest {

    /**
     * MAP
     */
    @Test
    public void mapOperator() {
        final Mono<Integer> monoTest = Mono.just("Hello")
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
    public void flatMap() {
        final Mono<Integer> monoTest = Mono.just("Hello")
                .map(String::toLowerCase)
                .map(String::length)
                .flatMap(length -> Mono.just(200));

        StepVerifier.create(monoTest)
                .expectNext(TO_REPLACE)
                .verifyComplete();
    }
    
    @Test
    public void flatMapTrick() {
        final Mono<Integer> monoTest = Mono.just(Mono.just("hello"))
                .flatMap(Function.identity())
                .map(String::toLowerCase)
                .map(String::length);

        StepVerifier.create(monoTest)
                .expectNext(TO_REPLACE)
                .verifyComplete();
    }

    /**
     * COLLECT
     */
    @Test
    public void fluxToMono() {
        final Mono<Integer> monoTest = Flux.just("hello","hello2")
                .collectList()
                .map(List::size);
        
        StepVerifier.create(monoTest)
                .expectNext(TO_REPLACE)
                .verifyComplete();
        
        // /!\ Flux should not be an infinite stream ;)
    }

    /**
     * TAKE
     */
    @Test
    public void fluxInfiniteToFinite() {
        final Mono<Integer> monoTest = Flux.interval(Duration.ofMillis(100))
                .take(5)
                .collectList()
                .map(List::size);

        StepVerifier.create(monoTest)
                .expectNext(TO_REPLACE)
                .verifyComplete();

        // /!\ If flux should not be an infinite stream ;)
    }
}
