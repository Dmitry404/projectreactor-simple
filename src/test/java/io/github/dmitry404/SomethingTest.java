package io.github.dmitry404;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class SomethingTest {
  @Test
  public void createNewFlux_just() {
    Flux<String> source = Flux.just("foo", "bar");

    StepVerifier.create(source)
        .expectNext("foo")
        .expectNext("bar")
        .verifyComplete();
  }

  @Test
  public void createNewMono_just() {
    Mono<String> source = Mono.just("foo");

    StepVerifier.create(source)
        .expectNext("foo")
        .verifyComplete();
  }
}
