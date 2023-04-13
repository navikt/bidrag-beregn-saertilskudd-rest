package no.nav.bidrag.beregn.saertilskudd.rest;

import no.nav.bidrag.beregn.bidragsevne.BidragsevneCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.BPsAndelSaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.SaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.BPAndelSaertilskuddCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.BidragsevneCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.SaertilskuddCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.SamvaersfradragCoreMapper;
import no.nav.bidrag.beregn.samvaersfradrag.SamvaersfradragCore;
import no.nav.bidrag.commons.ExceptionLogger;
import no.nav.bidrag.commons.web.CorrelationIdFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeregnSaertilskuddConfig {

  @Bean
  public BidragsevneCore bidragsevneCore() {
    return BidragsevneCore.getInstance();
  }

  @Bean
  public BPsAndelSaertilskuddCore bPsAndelSaertilskuddCoreCore() {
    return BPsAndelSaertilskuddCore.getInstance();
  }

  @Bean
  public SaertilskuddCore saertilskuddCore() {
    return SaertilskuddCore.getInstance();
  }

  @Bean
  public SamvaersfradragCore samvaersfradragCore() {
    return SamvaersfradragCore.getInstance();
  }

  @Bean
  public BidragsevneCoreMapper bidragsevneCoreMapper() {
    return new BidragsevneCoreMapper();
  }

  @Bean
  public BPAndelSaertilskuddCoreMapper bpAndelSaertilskuddCoreMapper() {
    return new BPAndelSaertilskuddCoreMapper();
  }

  @Bean
  public SamvaersfradragCoreMapper samvaersfradragCoreMapper() {
    return new SamvaersfradragCoreMapper();
  }

  @Bean
  public SaertilskuddCoreMapper saertilskuddCoreMapper() {
    return new SaertilskuddCoreMapper();
  }

  @Bean
  public SjablonConsumer sjablonConsumer(@Value("${BIDRAGSJABLON_URL}") String sjablonBaseUrl, RestTemplate restTemplate) {
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

}
