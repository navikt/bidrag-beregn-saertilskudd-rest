package no.nav.bidrag.beregn.saertilskudd.rest.exception;

public class UgyldigInputException extends RuntimeException {

  public UgyldigInputException(String melding) {
    super(melding);
  }
}
