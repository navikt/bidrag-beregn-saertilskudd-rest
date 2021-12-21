package no.nav.bidrag.beregn.saertilskudd.rest.consumer;

import java.util.List;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.SjablonConsumerException;
import no.nav.bidrag.commons.web.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class SjablonConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SjablonConsumer.class);
  private static final ParameterizedTypeReference<List<Sjablontall>> SJABLON_SJABLONTALL_LISTE = new ParameterizedTypeReference<>() {
  };
  private static final ParameterizedTypeReference<List<Samvaersfradrag>> SJABLON_SAMVAERSFRADRAG_LISTE = new ParameterizedTypeReference<>() {
  };
  private static final ParameterizedTypeReference<List<Bidragsevne>> SJABLON_BIDRAGSEVNE_LISTE = new ParameterizedTypeReference<>() {
  };
  private static final ParameterizedTypeReference<List<TrinnvisSkattesats>> SJABLON_TRINNVIS_SKATTESATS_LISTE = new ParameterizedTypeReference<>() {
  };

  private final RestTemplate restTemplate;
  private final String sjablonSjablontallUrl;
  private final String sjablonSamvaersfradragUrl;
  private final String sjablonBidragsevneUrl;
  private final String sjablonTrinnvisSkattesatsUrl;

  public SjablonConsumer(RestTemplate restTemplate, String sjablonBaseUrl) {
    this.restTemplate = restTemplate;
    this.sjablonSjablontallUrl = sjablonBaseUrl + "/sjablontall?all=true";
    this.sjablonSamvaersfradragUrl = sjablonBaseUrl + "/samvaersfradrag?all=true";
    this.sjablonBidragsevneUrl = sjablonBaseUrl + "/bidragsevner?all=true";
    this.sjablonTrinnvisSkattesatsUrl = sjablonBaseUrl + "/trinnvisskattesats?all=true";
  }

  public HttpResponse<List<Sjablontall>> hentSjablonSjablontall() {
    return hentSjablonListe(sjablonSjablontallUrl, SJABLON_SJABLONTALL_LISTE);
//    try {
//      var sjablonResponse = restTemplate.exchange(sjablonSjablontallUrl, HttpMethod.GET, null, SJABLON_SJABLONTALL_LISTE);
//      LOGGER.info("hentSjablonSjablontall fikk http status {} fra bidrag-sjablon", sjablonResponse.getStatusCode());
//      return new HttpResponse<>(sjablonResponse);
//    } catch (RestClientResponseException exception) {
//      LOGGER.error("hentSjablonSjablontall fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.getStatusText(),
//          exception.getMessage());
//      throw new SjablonConsumerException(exception);
//    }
  }

  public HttpResponse<List<Samvaersfradrag>> hentSjablonSamvaersfradrag() {
    return hentSjablonListe(sjablonSamvaersfradragUrl, SJABLON_SAMVAERSFRADRAG_LISTE);
//    try {
//      var sjablonResponse = restTemplate.exchange(sjablonSamvaersfradragUrl, HttpMethod.GET, null, SJABLON_SAMVAERSFRADRAG_LISTE);
//      LOGGER.info("hentSjablonSamvaersfradrag fikk http status {} fra bidrag-sjablon", sjablonResponse.getStatusCode());
//      return new HttpResponse<>(sjablonResponse);
//    } catch (RestClientResponseException exception) {
//      LOGGER.error("hentSjablonSamvaersfradrag fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.getStatusText(),
//          exception.getMessage());
//      throw new SjablonConsumerException(exception);
//    }
  }

  public HttpResponse<List<Bidragsevne>> hentSjablonBidragsevne() {
    return hentSjablonListe(sjablonBidragsevneUrl, SJABLON_BIDRAGSEVNE_LISTE);
//    try {
//      var sjablonResponse = restTemplate.exchange(sjablonBidragsevneUrl, HttpMethod.GET, null, SJABLON_BIDRAGSEVNE_LISTE);
//      LOGGER.info("hentSjablonBidragsevne fikk http status {} fra bidrag-sjablon", sjablonResponse.getStatusCode());
//      return new HttpResponse<>(sjablonResponse);
//    } catch (RestClientResponseException exception) {
//      LOGGER.error("hentSjablonBidragsevne fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.getStatusText(),
//          exception.getMessage());
//      throw new SjablonConsumerException(exception);
//    }
  }

  public HttpResponse<List<TrinnvisSkattesats>> hentSjablonTrinnvisSkattesats() {
    return hentSjablonListe(sjablonTrinnvisSkattesatsUrl, SJABLON_TRINNVIS_SKATTESATS_LISTE);
//    try {
//      var sjablonResponse = restTemplate.exchange(sjablonTrinnvisSkattesatsUrl, HttpMethod.GET, null, SJABLON_TRINNVIS_SKATTESATS_LISTE);
//      LOGGER.info("hentSjablonTrinnvisSkattesats fikk http status {} fra bidrag-sjablon", sjablonResponse.getStatusCode());
//      return new HttpResponse<>(sjablonResponse);
//    } catch (RestClientResponseException exception) {
//      LOGGER.error("hentSjablonTrinnvisSkattesats fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.getStatusText(),
//          exception.getMessage());
//      throw new SjablonConsumerException(exception);
//    }
  }

  public <T> HttpResponse<List<T>> hentSjablonListe(String path, ParameterizedTypeReference<List<T>> responseType) {
    try {
      var sjablonListe = restTemplate.exchange(path, HttpMethod.GET, null, responseType);
      LOGGER.info("Hent {} fikk http status {} fra bidrag-sjablon", path, sjablonListe.getStatusCode());
      return new HttpResponse<>(sjablonListe);
    } catch (RestClientResponseException exception) {
      throw new SjablonConsumerException(exception);
    }
  }
}
