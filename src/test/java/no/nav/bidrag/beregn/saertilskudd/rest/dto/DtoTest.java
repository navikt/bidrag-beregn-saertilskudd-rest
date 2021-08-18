package no.nav.bidrag.beregn.saertilskudd.rest.dto;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BMInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BarnIHusstand;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Bostatus;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektBase;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.LopendeBidrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.NettoSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SBInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Saerfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Samvaersklasse;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Skatteklasse;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DtoTest")
class DtoTest {

  // BeregnTotalSaertilskuddGrunnlag
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnDatoFraErNull() {
    var grunnlag = new BeregnTotalSaertilskuddGrunnlag(null, LocalDate.parse("2021-08-18"), emptyList());
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("beregnDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnDatoTilErNull() {
    var grunnlag = new BeregnTotalSaertilskuddGrunnlag(LocalDate.parse("2021-08-18"), null, emptyList());
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("beregnDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når grunnlagListe er null")
  void skalKasteIllegalArgumentExceptionNaarGrunnlagListeErNull() {
    var grunnlag = new BeregnTotalSaertilskuddGrunnlag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("grunnlagListe kan ikke være null");
  }

  // Grunnlag
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når referanse er null")
  void skalKasteIllegalArgumentExceptionNaarGrunnlagReferanseErNull() {
    var grunnlag = new Grunnlag(null, GrunnlagType.INNTEKT, jacksonObjectMapper().createObjectNode());
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("referanse kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når type er null")
  void skalKasteIllegalArgumentExceptionNaarGrunnlagTypeErNull() {
    var grunnlag = new Grunnlag("TestReferanse", null, jacksonObjectMapper().createObjectNode());
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("type kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når innhold er null")
  void skalKasteIllegalArgumentExceptionNaarGrunnlagInnholdErNull() {
    var grunnlag = new Grunnlag("TestReferanse", GrunnlagType.INNTEKT, null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("innhold kan ikke være null");
  }

  // InntektBase
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektType er null")
  void skalKasteIllegalArgumentExceptionNaarInntektTypeErNull() {
    var grunnlag = new InntektBase(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), Rolle.BP, null, 400000);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerInntekt)
        .withMessage("inntektType kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når belop er null")
  void skalKasteIllegalArgumentExceptionNaarBelopErNull() {
    var grunnlag = new InntektBase(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), Rolle.BP, "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER", null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerInntekt)
        .withMessage("belop kan ikke være null");
  }

  //BMInntekt
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når deltFordel er null")
  void skalKasteIllegalArgumentExceptionNaarDeltFordelErNull() {
    var grunnlag = new BMInntekt(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER", 400000, Rolle.BM, null, false);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("deltFordel kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når skatteklasse2 er null")
  void skalKasteIllegalArgumentExceptionNaarSkatteklasse2ErNull() {
    var grunnlag = new BMInntekt(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER", 400000, Rolle.BM, false, null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("skatteklasse2 kan ikke være null");
  }

  //SBInntekt
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnId er null")
  void skalKasteIllegalArgumentExceptionNaarSoknadsbarnErNull() {
    var grunnlag = new SBInntekt(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), Rolle.SB, "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER", 400000, null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("soknadsbarnId kan ikke være null");
  }

  //BarnIHusstand
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antall er null")
  void skalKasteIllegalArgumentExceptionNaarAntallErNull() {
    var grunnlag = new BarnIHusstand(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("antall kan ikke være null");
  }

  //Bostatus
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusKode er null")
  void skalKasteIllegalArgumentExceptionNaarBostatusKodeErNull() {
    var grunnlag = new Bostatus(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("bostatusKode kan ikke være null");
  }

  //Saerfradrag
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragKode er null")
  void skalKasteIllegalArgumentExceptionNaarSaerfradragKodeErNull() {
    var grunnlag = new Saerfradrag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("saerfradragKode kan ikke være null");
  }

  //Skatteklasse
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når skatteklasseId er null")
  void skalKasteIllegalArgumentExceptionNaarSkatteklasseIdErNull() {
    var grunnlag = new Skatteklasse(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("skatteklasseId kan ikke være null");
  }

  //NettoSaertilskudd
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når nettoSaertilskuddBelop er null")
  void skalKasteIllegalArgumentExceptionNaarNettoSaertilskuddBelopErNull() {
    var grunnlag = new NettoSaertilskudd(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("nettoSaertilskuddBelop kan ikke være null");
  }

  //Samvaersklasse
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnId er null")
  void skalKasteIllegalArgumentExceptionNaarSamvaersklasseSoknadsbarnIdErNull() {
    var grunnlag = new Samvaersklasse(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null, LocalDate.parse("2008-08-18"), "01");
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("soknadsbarnId kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnFodselsdato er null")
  void skalKasteIllegalArgumentExceptionNaarSamvaersklasseSoknadsbarnFodselsdatoErNull() {
    var grunnlag = new Samvaersklasse(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, null, "01");
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("soknadsbarnFodselsdato kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når samvaersklasseId er null")
  void skalKasteIllegalArgumentExceptionNaarSamvaersklasseSamvaersklasseIdErNull() {
    var grunnlag = new Samvaersklasse(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, LocalDate.parse("2008-08-18"), null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("samvaersklasseId kan ikke være null");
  }

  //LopendeBidrag
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnId er null")
  void skalKasteIllegalArgumentExceptionNaarLopendeBidragSoknadsbarnIdErNull() {
    var grunnlag = new LopendeBidrag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null, 2000, 1500, 2000, 1500);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("soknadsbarnId kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når belop er null")
  void skalKasteIllegalArgumentExceptionNaarLopendeBidragBelopErNull() {
    var grunnlag = new LopendeBidrag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, null, 1500, 2000, 1500);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("belop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når opprinneligBPAndelUnderholdskostnadBelop er null")
  void skalKasteIllegalArgumentExceptionNaarLopendeBidragOpprinneligBPAndelUnderholdskostnadBelopErNull() {
    var grunnlag = new LopendeBidrag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, 2000, null, 2000, 1500);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("opprinneligBPAndelUnderholdskostnadBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når opprinneligBidragBelop er null")
  void skalKasteIllegalArgumentExceptionNaarLopendeBidragOpprinneligBidragBelopErNull() {
    var grunnlag = new LopendeBidrag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, 2000, 1500, null, 1500);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("opprinneligBidragBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når opprinneligSamvaersfradragBelop er null")
  void skalKasteIllegalArgumentExceptionNaarLopendeBidragOpprinneligSamvaersfradragBelopErNull() {
    var grunnlag = new LopendeBidrag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, 2000, 1500, 2000, null);
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::valider)
        .withMessage("opprinneligSamvaersfradragBelop kan ikke være null");
  }
}
