package no.nav.bidrag.beregn.saertilskudd.rest.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;
import java.time.LocalDate;
import no.nav.bidrag.beregn.saertilskudd.rest.BidragBeregnSaertilskuddLocal;
import no.nav.bidrag.beregn.saertilskudd.rest.TestUtil;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.service.BeregnSaertilskuddService;
import no.nav.bidrag.commons.web.HttpResponse;
import no.nav.bidrag.commons.web.test.HttpHeaderTestRestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@DisplayName("BeregnSaertilskuddControllerTest")
@SpringBootTest(classes = BidragBeregnSaertilskuddLocal.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class BeregnSaertilskuddControllerMockTest {

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
  @DisplayName("Skal returnere total Saertilskudd resultat ved gyldig input")
  void skalReturnereTotalSaertilskuddResultatVedGyldigInput() {

    when(beregnSaertilskuddServiceMock.beregn(any(BeregnTotalSaertilskuddGrunnlag.class))).thenReturn(HttpResponse.from(OK,
        new BeregnTotalSaertilskuddResultat(TestUtil.dummyBidragsevneResultat(), TestUtil.dummyNettoBarnetilsynResultat(),
            TestUtil.dummyUnderholdskostnadResultat(), TestUtil.dummyBPsAndelUnderholdskostnadResultat(), TestUtil.dummySamvaersfradragResultat(),
            TestUtil.dummyKostnadsberegnetBidragResultat(), TestUtil.dummySaertilskuddResultat())));

    var url = "http://localhost:" + port + "/bidrag-beregn-Saertilskudd-rest/beregn/Saertilskudd";
    var request = initHttpEntity(TestUtil.byggTotalSaertilskuddGrunnlag());
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnTotalSaertilskuddResultat.class);
    var totalSaertilskuddResultat = responseEntity.getBody();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(OK),
        () -> assertThat(totalSaertilskuddResultat).isNotNull(),

        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoFra())
            .isEqualTo(LocalDate.parse("2017-01-01")),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoTil())
            .isEqualTo(LocalDate.parse("2019-01-01")),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().get(0).getResultatBeregning().getResultat25ProsentInntekt())
            .isEqualTo(BigDecimal.valueOf(100)),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().get(0).getResultatBeregning().getResultatEvneBelop())
            .isEqualTo(BigDecimal.valueOf(100)),

        () -> assertThat(totalSaertilskuddResultat.getBeregnBMNettoBarnetilsynResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBMNettoBarnetilsynResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBMNettoBarnetilsynResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBMNettoBarnetilsynResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoFra())
            .isEqualTo(LocalDate.parse("2017-01-01")),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBMNettoBarnetilsynResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoTil())
            .isEqualTo(LocalDate.parse("2019-01-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBMNettoBarnetilsynResultat().getResultatPeriodeListe().get(0).getResultatBeregningListe().size())
            .isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBMNettoBarnetilsynResultat().getResultatPeriodeListe().get(0).getResultatBeregningListe().get(0)
            .getResultatSoknadsbarnPersonId()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBMNettoBarnetilsynResultat().getResultatPeriodeListe().get(0).getResultatBeregningListe().get(0)
            .getResultatBelop()).isEqualTo(BigDecimal.valueOf(100)),

        () -> assertThat(totalSaertilskuddResultat.getBeregnBMUnderholdskostnadResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBMUnderholdskostnadResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBMUnderholdskostnadResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBMUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatSoknadsbarnPersonId())
            .isEqualTo(1),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBMUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoFra())
            .isEqualTo(LocalDate.parse("2017-01-01")),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBMUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoTil())
            .isEqualTo(LocalDate.parse("2019-01-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBMUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatBelop()).isEqualTo(BigDecimal.valueOf(100)),

        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelUnderholdskostnadResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelUnderholdskostnadResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelUnderholdskostnadResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPAndelUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatSoknadsbarnPersonId())
            .isEqualTo(1),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPAndelUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil()
                .getPeriodeDatoFra()).isEqualTo(LocalDate.parse("2017-01-01")),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPAndelUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil()
                .getPeriodeDatoTil()).isEqualTo(LocalDate.parse("2019-01-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatAndelProsent()).isEqualTo(BigDecimal.valueOf(10)),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelUnderholdskostnadResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatAndelBelop()).isEqualTo(BigDecimal.valueOf(100)),

        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().get(0).getResultatSoknadsbarnPersonId())
            .isEqualTo(1),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoFra())
            .isEqualTo(LocalDate.parse("2017-01-01")),
        () -> assertThat(
            totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoTil())
            .isEqualTo(LocalDate.parse("2019-01-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatBelop()).isEqualTo(BigDecimal.valueOf(100)),

        () -> assertThat(totalSaertilskuddResultat.getBeregnBPKostnadsberegnetBidragResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPKostnadsberegnetBidragResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPKostnadsberegnetBidragResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPKostnadsberegnetBidragResultat().getResultatPeriodeListe().get(0)
            .getResultatSoknadsbarnPersonId()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPKostnadsberegnetBidragResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil()
            .getPeriodeDatoFra()).isEqualTo(LocalDate.parse("2017-01-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPKostnadsberegnetBidragResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil()
            .getPeriodeDatoTil()).isEqualTo(LocalDate.parse("2019-01-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPKostnadsberegnetBidragResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatBelop()).isEqualTo(BigDecimal.valueOf(100)),

        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil()
            .getPeriodeDatoFra()).isEqualTo(LocalDate.parse("2017-01-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatDatoFraTil()
            .getPeriodeDatoTil()).isEqualTo(LocalDate.parse("2019-01-01")),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregningListe()
            .get(0).getResultatSoknadsbarnPersonId()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregningListe()
            .get(0).getResultatBelop()).isEqualTo(BigDecimal.valueOf(100)),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregningListe()
            .get(0).getResultatKode()).isEqualTo("RESULTATKODE")
    );
  }

  @Test
  @DisplayName("Skal returnere 400 Bad Request når input data mangler")
  void skalReturnere400BadRequestNaarInputDataMangler() {

    when(beregnSaertilskuddServiceMock.beregn(any(BeregnTotalSaertilskuddGrunnlag.class))).thenReturn(HttpResponse.from(BAD_REQUEST));

    var url = "http://localhost:" + port + "/bidrag-beregn-Saertilskudd-rest/beregn/Saertilskudd";
    var request = initHttpEntity(TestUtil.byggTotalSaertilskuddGrunnlag());
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnTotalSaertilskuddResultat.class);
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

    var url = "http://localhost:" + port + "/bidrag-beregn-Saertilskudd-rest/beregn/Saertilskudd";
    var request = initHttpEntity(TestUtil.byggTotalSaertilskuddGrunnlag());
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnTotalSaertilskuddResultat.class);
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
