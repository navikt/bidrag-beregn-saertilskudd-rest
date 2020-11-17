package no.nav.bidrag.beregn.saertilskudd.rest;

import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer;
import no.nav.bidrag.commons.ExceptionLogger;
import no.nav.bidrag.commons.web.CorrelationIdFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BidragBeregnSaertilskudd {

  @Bean
  public SaertilskuddCore saertilskuddCore() {
    return new SaertilskuddCore();
  }

  @Bean
  public SjablonConsumer sjablonConsumer(@Value("${SJABLON_URL}") String sjablonBaseUrl, RestTemplate restTemplate) {
    return new SjablonConsumer(restTemplate, sjablonBaseUrl);
  }

  @Bean
  public ExceptionLogger exceptionLogger() {
    return new ExceptionLogger(BidragBeregnSaertilskudd.class.getSimpleName());
  }

  @Bean
  public CorrelationIdFilter correlationIdFilter() {
    return new CorrelationIdFilter();
  }

	public static void main(String[] args) {
		SpringApplication.run(BidragBeregnSaertilskudd.class, args);
	}
}
