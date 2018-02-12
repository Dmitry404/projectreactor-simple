package io.github.dmitry404;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

// https://projectreactor.io/docs/core/release/reference/#which.create
public class CreateNewSequenceTest {
  @Test
  public void newSequenceFrom_just_optional() {
    //that emits a T, and I already have: just
    //…​from an Optional<T>: Mono#justOrEmpty(Optional<T>)
    //…​from a potentially null T: Mono#justOrEmpty(T)

    Mono<String> sourceNonNull = Mono.justOrEmpty(Optional.of("foo"));
    Mono<String> sourceNull = Mono.justOrEmpty(nullInsteadOfString());

    StepVerifier.create(sourceNonNull)
        .expectNextCount(1)
        .verifyComplete();

    StepVerifier.create(sourceNull)
        .expectNextCount(0)
        .verifyComplete();
  }

  private String nullInsteadOfString() {
    return null;
  }

  private String stringToChange = "INITIAL_STATE";
  @Test
  public void newSequenceFrom_just_lazilyCaptured_supplier() {
    //that emits a T returned by a method: just as well
    //…​but lazily captured: use Mono#fromSupplier or wrap just inside defer
    Mono<String> sourceFromSupplier = Mono.fromSupplier(() -> {
      stringToChange = "CHANGED_STATE";
      return stringToChange;
    });

    assertThat(stringToChange, is("INITIAL_STATE"));

    StepVerifier.create(sourceFromSupplier)
        .expectNext("CHANGED_STATE")
        .verifyComplete();

    assertThat(stringToChange, is("CHANGED_STATE"));
  }

  @Test
  public void newSequenceFrom_just_lazilyCaptured_defer() {
    //that emits a T returned by a method: just as well
    //…​but lazily captured: use Mono#fromSupplier or wrap just inside defer

    Mono<String> sourceFromDefer = Mono.defer(() -> Mono.just("this approach can be used in map or something"));
    StepVerifier.create(sourceFromDefer)
        .expectNext("this approach can be used in map or something")
        .verifyComplete();
  }

  @Test
  public void newSequence_from_just_varargsArray() {
    //that emits several T I can explicitly enumerate: Flux#just(T...)
    Flux<String> source = Flux.just("Hello", "Worm", "!");

    StepVerifier.create(source)
        .expectNext("Hello", "Worm", "!")
        .expectComplete()
        .verify();
  }
}
