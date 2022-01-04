package no.nav.bidrag.beregn.saertilskudd.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import no.nav.bidrag.beregn.saertilskudd.rest.TestUtil;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.BidragGcpProxyConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Samvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.TrinnvisSkattesats;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.BidragGcpProxyConsumerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("SjablonServiceTest")
public class SjablonServiceTest {

  private SjablonService sjablonService;

  private BidragGcpProxyConsumer bidragGcpProxyConsumer;

  private RestTemplate restTemplateMock;

  public SjablonServiceTest() {
    this.restTemplateMock = Mockito.mock(RestTemplate.class);
    this.bidragGcpProxyConsumer = new BidragGcpProxyConsumer(restTemplateMock);
    this.sjablonService = new SjablonService(bidragGcpProxyConsumer);
  }

  @Test
  @DisplayName("Skal hente liste av Sjablontall når respons fra tjenesten er OK")
  void skalHenteListeAvSjablontallNaarResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Sjablontall>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonSjablontallListe(), HttpStatus.OK));
    var sjablonResponse = sjablonService.hentSjablonSjablontall();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().size()).isEqualTo(TestUtil.dummySjablonSjablontallListe().size()),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().get(0).getTypeSjablon())
            .isEqualTo(TestUtil.dummySjablonSjablontallListe().get(0).getTypeSjablon())
    );
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException når respons fra tjenesten ikke er OK for Sjablontall")
  void skalKasteRestClientExceptionNaarResponsFraTjenestenIkkeErOkForSjablontall() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Sjablontall>>) any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThatExceptionOfType(BidragGcpProxyConsumerException.class).isThrownBy(() -> sjablonService.hentSjablonSjablontall());
  }

  @Test
  @DisplayName("Skal hente liste av Samvaersfradrag-sjabloner når respons fra tjenesten er OK")
  void skalHenteListeAvSamvaersfradragSjablonerNaarResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Samvaersfradrag>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonSamvaersfradragListe(), HttpStatus.OK));
    var sjablonResponse = sjablonService.hentSjablonSamvaersfradrag();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().size()).isEqualTo(TestUtil.dummySjablonSamvaersfradragListe().size()),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().get(0).getBelopFradrag())
            .isEqualTo(TestUtil.dummySjablonSamvaersfradragListe().get(0).getBelopFradrag())
    );
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException når respons fra tjenesten ikke er OK for Samvaersfradrag")
  void skalKasteRestClientExceptionNaarResponsFraTjenestenIkkeErOkForSamvaersfradrag() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Samvaersfradrag>>) any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThatExceptionOfType(BidragGcpProxyConsumerException.class).isThrownBy(() -> sjablonService.hentSjablonSamvaersfradrag());
  }

  @Test
  @DisplayName("Skal hente liste av Bidragsevne-sjabloner når respons fra tjenesten er OK")
  void skalHenteListeAvBidragsevneSjablonerNaarResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Bidragsevne>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonBidragsevneListe(), HttpStatus.OK));
    var sjablonResponse = sjablonService.hentSjablonBidragsevne();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().size()).isEqualTo(TestUtil.dummySjablonBidragsevneListe().size()),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().get(0).getBelopBoutgift())
            .isEqualTo(TestUtil.dummySjablonBidragsevneListe().get(0).getBelopBoutgift())
    );
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException når respons fra tjenesten ikke er OK for Bidragsevne")
  void skalKasteRestClientExceptionNaarResponsFraTjenestenIkkeErOkForBidragsevne() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Bidragsevne>>) any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThatExceptionOfType(BidragGcpProxyConsumerException.class).isThrownBy(() -> sjablonService.hentSjablonBidragsevne());
  }

  @Test
  @DisplayName("Skal hente liste av TrinnvisSkattesats-sjabloner når respons fra tjenesten er OK")
  void skalHenteListeAvTrinnvisSkattesatsSjablonerNaarResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<TrinnvisSkattesats>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonTrinnvisSkattesatsListe(), HttpStatus.OK));
    var sjablonResponse = sjablonService.hentSjablonTrinnvisSkattesats();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().size()).isEqualTo(TestUtil.dummySjablonTrinnvisSkattesatsListe().size()),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().get(0).getInntektgrense())
            .isEqualTo(TestUtil.dummySjablonTrinnvisSkattesatsListe().get(0).getInntektgrense())
    );
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException når respons fra tjenesten ikke er OK for TrinnvisSkattesats")
  void skalKasteRestClientExceptionNaarResponsFraTjenestenIkkeErOkForTrinnvisSkattesats() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<TrinnvisSkattesats>>) any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThatExceptionOfType(BidragGcpProxyConsumerException.class).isThrownBy(() -> sjablonService.hentSjablonTrinnvisSkattesats());
  }
}