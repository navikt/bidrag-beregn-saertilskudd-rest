package no.nav.bidrag.beregn.saertilskudd.rest.controller;

import static java.util.Collections.emptyList;
import static no.nav.bidrag.beregn.saertilskudd.rest.TestUtil.BIDRAGSEVNE_REFERANSE;
import static no.nav.bidrag.beregn.saertilskudd.rest.TestUtil.BPS_ANDEL_SAERTILSKUDD_REFERANSE;
import static no.nav.bidrag.beregn.saertilskudd.rest.TestUtil.SAMVAERSFRADRAG_REFERANSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;
import no.nav.bidrag.beregn.saertilskudd.rest.BidragBeregnSaertilskuddOverridesConfig;
import no.nav.bidrag.beregn.saertilskudd.rest.BidragBeregnSaertilskuddTest;
import no.nav.bidrag.beregn.saertilskudd.rest.SaertilskuddDelberegningResultat;
import java.time.LocalDate;
import java.util.ArrayList;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore;
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatBeregningCore;
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.rest.TestUtil;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnetTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.service.BeregnSaertilskuddService;
import no.nav.bidrag.commons.web.HttpResponse;
import no.nav.bidrag.commons.web.test.HttpHeaderTestRestTemplate;
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@DisplayName("BeregnSaertilskuddControllerTest")
@SpringBootTest(classes = BidragBeregnSaertilskuddTest.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(BidragBeregnSaertilskuddOverridesConfig.class)
@AutoConfigureWireMock(port = 8096)
@EnableMockOAuth2Server
class BeregnSaertilskuddControllerTest {

  @Autowired
  private HttpHeaderTestRestTemplate httpHeaderTestRestTemplate;
  @LocalServerPort
  private int port;
  @MockBean
  private BeregnSaertilskuddService beregnSaertilskuddServiceMock;

  @BeforeEach
  void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("Skal returnere total særtilskudd resultat ved gyldig input")
  void skalReturnereTotalSaertilskuddResultatVedGyldigInput() {

    when(beregnSaertilskuddServiceMock.beregn(any(BeregnTotalSaertilskuddGrunnlag.class))).thenReturn(HttpResponse.from(OK,
        new BeregnetTotalSaertilskuddResultat(new BeregnSaertilskuddResultatCore(new ArrayList<>() {{
          add(new ResultatPeriodeCore(new PeriodeCore(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01")), 1,
              new ResultatBeregningCore(BigDecimal.valueOf(100), "RESULTATKODE"), new ArrayList<>() {{
            add(BIDRAGSEVNE_REFERANSE);
            add(BPS_ANDEL_SAERTILSKUDD_REFERANSE);
            add(SAMVAERSFRADRAG_REFERANSE);
          }}));
        }}, emptyList()), new ArrayList<>() {{
          add(TestUtil.dummyBidragsevneResultat());
          add(TestUtil.dummyBPsAndelSaertilskuddResultat());
          add(TestUtil.dummySamvaersfradragResultat());
        }})));
    var url = "http://localhost:" + port + "/beregn/saertilskudd";
    var request = initHttpEntity(TestUtil.byggTotalSaertilskuddGrunnlag());
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnetTotalSaertilskuddResultat.class);
    var totalSaertilskuddResultat = responseEntity.getBody();

    var saertilskuddDelberegningResultat = new SaertilskuddDelberegningResultat(totalSaertilskuddResultat);

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(OK),
        () -> assertThat(totalSaertilskuddResultat).isNotNull(),

        () -> assertThat(saertilskuddDelberegningResultat.bidragsevneListe.size()).isEqualTo(1),
        () -> assertThat(
            saertilskuddDelberegningResultat.bidragsevneListe.get(0).getDatoFom())
            .isEqualTo(LocalDate.parse("2020-08-01")),
        () -> assertThat(
            saertilskuddDelberegningResultat.bidragsevneListe.get(0).getDatoTil())
            .isEqualTo(LocalDate.parse("2020-09-01")),
        () -> assertThat(
            saertilskuddDelberegningResultat.bidragsevneListe.get(0).getBelop().compareTo(BigDecimal.valueOf(100))).isZero(),

        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.size()).isEqualTo(1),
        () -> assertThat(
            saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.get(0).getDatoFom()).isEqualTo(LocalDate.parse("2020-08-01")),
        () -> assertThat(
            saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.get(0).getDatoTil()).isEqualTo(LocalDate.parse("2020-09-01")),
        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.get(0).getProsent().compareTo(BigDecimal.valueOf(10))).isZero(),
        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.get(0).getBelop().compareTo(BigDecimal.valueOf(100))).isZero(),

        () -> assertThat(saertilskuddDelberegningResultat.samvaersfradragListe.size()).isEqualTo(1),
        () -> assertThat(
            saertilskuddDelberegningResultat.samvaersfradragListe.get(0).getDatoFom()).isEqualTo(LocalDate.parse("2020-08-01")),
        () -> assertThat(
            saertilskuddDelberegningResultat.samvaersfradragListe.get(0).getDatoTil()).isEqualTo(LocalDate.parse("2020-09-01")),
        () -> assertThat(saertilskuddDelberegningResultat.samvaersfradragListe.get(0).getBelop().compareTo(BigDecimal.valueOf(100))).isZero(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().get(0).getPeriode()
            .getDatoFom()).isEqualTo(LocalDate.parse("2020-08-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().get(0).getPeriode()
            .getDatoTil()).isEqualTo(LocalDate.parse("2020-09-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().get(0).getResultat()
            .getBelop().compareTo(BigDecimal.valueOf(100))).isZero(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().get(0).getResultat()
            .getKode()).isEqualTo("RESULTATKODE")
    );
  }

  @Test
  @DisplayName("Skal returnere 400 Bad Request når input data mangler")
  void skalReturnere400BadRequestNaarInputDataMangler() {

    when(beregnSaertilskuddServiceMock.beregn(any(BeregnTotalSaertilskuddGrunnlag.class))).thenReturn(HttpResponse.from(BAD_REQUEST));

    var url = "http://localhost:" + port + "/beregn/saertilskudd";
    var request = initHttpEntity(new BeregnTotalSaertilskuddGrunnlag(LocalDate.parse("2021-08-18"), LocalDate.parse("2021-08-18"), emptyList()));
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnetTotalSaertilskuddResultat.class);
    var totalSaertilskuddResultat = responseEntity.getBody();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST),
        () -> assertThat(totalSaertilskuddResultat).isNull()
    );
  }

  @Test
  @DisplayName("Skal returnere 500 Internal Server Error når kall til servicen feiler")
  void skalReturnere500InternalServerErrorNaarKallTilServicenFeiler() {

    when(beregnSaertilskuddServiceMock.beregn(any(BeregnTotalSaertilskuddGrunnlag.class))).thenReturn(HttpResponse.from(INTERNAL_SERVER_ERROR));

    var url = "http://localhost:" + port + "/beregn/saertilskudd";
    var request = initHttpEntity(new BeregnTotalSaertilskuddGrunnlag(LocalDate.parse("2021-08-18"), LocalDate.parse("2021-08-18"), emptyList()));
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnetTotalSaertilskuddResultat.class);
    var totalSaertilskuddResultat = responseEntity.getBody();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR),
        () -> assertThat(totalSaertilskuddResultat).isNull()
    );
  }

  private <T> HttpEntity<T> initHttpEntity(T body) {
    var httpHeaders = new HttpHeaders();
    return new HttpEntity<>(body, httpHeaders);
  }
}
