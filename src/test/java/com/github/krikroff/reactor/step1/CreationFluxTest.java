package com.github.krikroff.reactor.step1;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

/**
 * How to create a flux
 */
public class CreationFluxTest {

    @Test
    public void createFluxStatic() {
        final Flux<String> fluxStream = Flux.just("hello");

        StepVerifier.create(fluxStream)
                .expectNext("hello")
                .verifyComplete();
    }

    @Test
    public void createFluxCallable() {
        final Flux<String> fluxStream = Flux.fromStream(Stream.of("he", "llo"));

        StepVerifier.create(fluxStream)
                .expectNext("he")
                .expectNext("llo")
                .verifyComplete();
    }

    @Test
    public void createFlux() {
        final Flux<String> fluxStream = Flux.create(stringFluxSink -> {
            System.out.println("Subscribed");
            stringFluxSink.onRequest(l -> System.out.println("Flux was requested :" + l));
            stringFluxSink.next("he");
            stringFluxSink.next("llo");
            stringFluxSink.complete();
        });

        StepVerifier.create(fluxStream)
                .expectNext("he")
                .expectNext("llo")
                .verifyComplete();
        
        StepVerifier.create(fluxStream)
                .expectNext("he")
                .expectNext("llo")
                .verifyComplete();
    }

}
