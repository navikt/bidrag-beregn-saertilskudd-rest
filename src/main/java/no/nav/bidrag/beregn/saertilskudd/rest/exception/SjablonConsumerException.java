package no.nav.bidrag.beregn.saertilskudd.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

public class SjablonConsumerException extends RuntimeException {

  private final HttpStatus statusCode;

  public HttpStatus getStatusCode() {
    return statusCode;
  }

  public SjablonConsumerException(RestClientResponseException exception) {
    super(exception);
    this.statusCode = HttpStatus.valueOf(exception.getRawStatusCode());
  }
}
