package no.nav.bidrag.beregn.saertilskudd.rest.dto;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import no.nav.bidrag.beregn.saertilskudd.rest.TestUtil;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DtoTest")
class DtoTest {


  // Total særtilskudd
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnDatoFraErNull() {
    var grunnlag = TestUtil.byggTotalSaertilskuddGrunnlagUtenBeregnDatoFra();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerTotalSaertilskuddGrunnlag)
        .withMessage("beregnDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnDatoTilErNull() {
    var grunnlag = TestUtil.byggTotalSaertilskuddGrunnlagUtenBeregnDatoTil();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerTotalSaertilskuddGrunnlag)
        .withMessage("beregnDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnGrunnlag er null")
  void skalKasteIllegalArgumentExceptionNaarSoknadsbarnGrunnlagErNull() {
    var grunnlag = TestUtil.byggTotalSaertilskuddGrunnlagUtenSoknadsbarnGrunnlag();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerTotalSaertilskuddGrunnlag)
        .withMessage("soknadsbarnGrunnlag kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBPBMGrunnlag er null")
  void skalKasteIllegalArgumentExceptionNaarInntektGrunnlagErNull() {
    var grunnlag = TestUtil.byggTotalSaertilskuddGrunnlagUtenInntektBPBMGrunnlag();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerTotalSaertilskuddGrunnlag)
        .withMessage("inntektBPBMGrunnlag kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnBPBidragsevneGrunnlag er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnBidragsevneGrunnlagErNull() {
    var grunnlag = TestUtil.byggTotalSaertilskuddGrunnlagUtenBeregnBPBidragsevneGrunnlag();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerTotalSaertilskuddGrunnlag)
        .withMessage("beregnBPBidragsevneGrunnlag kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnBPAndelSaertilskuddGrunnlag er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnBPAndelSaertilskuddGrunnlagErNull() {
    var grunnlag = TestUtil.byggTotalSaertilskuddGrunnlagUtenBeregnBPAndelSaertilskuddGrunnlag();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerTotalSaertilskuddGrunnlag)
        .withMessage("beregnBPAndelSaertilskuddGrunnlag kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnBPSamvaersfradragGrunnlag er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnBPSamvaersfradragGrunnlagErNull() {
    var grunnlag = TestUtil.byggTotalSaertilskuddGrunnlagUtenBeregnBPSamvaersfradragGrunnlag();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerTotalSaertilskuddGrunnlag)
        .withMessage("beregnBPSamvaersfradragGrunnlag kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnSaertilskuddGrunnlag er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnSaertilskuddGrunnlagErNull() {
    var grunnlag = TestUtil.byggTotalSaertilskuddGrunnlagUtenBeregnSaertilskuddGrunnlag();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerTotalSaertilskuddGrunnlag)
        .withMessage("beregnSaertilskuddGrunnlag kan ikke være null");
  }


  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBPPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBPPeriodeListeErNull() {
    var grunnlag = TestUtil.byggInntektBPBMGrunnlagUtenInntektBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerInntekt)
        .withMessage("inntektBPPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBMPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBMPeriodeListeErNull() {
    var grunnlag = TestUtil.byggInntektBPBMGrunnlagUtenInntektBMPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerInntekt)
        .withMessage("inntektBMPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når skatteklassePeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarBESkatteklassePeriodeListeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSkatteklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.bidragsevneTilCore(emptyList()))
        .withMessage("skatteklassePeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarBEBostatusPeriodeListeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.bidragsevneTilCore(emptyList()))
        .withMessage("bostatusPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antallBarnIEgetHusholdPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarBEAntallBarnIEgetHusholdPeriodeListeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.bidragsevneTilCore(emptyList()))
        .withMessage("antallBarnIEgetHusholdPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarBESaerfradragPeriodeListeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.bidragsevneTilCore(emptyList()))
        .withMessage("saerfradragPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når nettoSaertilskuddPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarASNettoSaertilskuddPeriodeListeErNull() {
    var grunnlag = TestUtil.byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.bpAndelSaertilskuddTilCore(emptyList()))
        .withMessage("nettoSaertilskuddPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når samvaersklassePeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarSFSamvaersklassePeriodeListeErNull() {
    var grunnlag = TestUtil.byggSamvaersfradragGrunnlagUtenSamvaersklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.samvaersfradragTilCore(emptyList()))
        .withMessage("samvaersklassePeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når lopendeBidragBPPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarLopendeBidragBPPeriodeListeErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class)
        .isThrownBy(() -> grunnlag.saertilskuddTilCore(emptyList(), emptyList(), emptyList(), emptyList()))
        .withMessage("lopendeBidragBPPeriodeListe kan ikke være null");
  }


  // Søknadsbarn
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarSBInntektPeriodeListeErNull() {
    var grunnlag = TestUtil.byggSoknadsbarnGrunnlagUtenInntektPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerSoknadsbarn)
        .withMessage("SB inntektPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnFodselsdato er null")
  void skalKasteIllegalArgumentExceptionNaarSBSoknadsbarnFodselsdatoErNull() {
    var grunnlag = TestUtil.byggSoknadsbarnGrunnlagUtenSoknadsbarnFodselsdato();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerSoknadsbarn)
        .withMessage("soknadsbarnFodselsdato kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarSBInntektDatoFraTilErNull() {
    var grunnlag = TestUtil.byggSoknadsbarnGrunnlagUtenInntektDatoFraTil();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerSoknadsbarn)
        .withMessage("SB inntektDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarSBInntektDatoFraErNull() {
    var grunnlag = TestUtil.byggSoknadsbarnGrunnlagUtenInntektDatoFra();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerSoknadsbarn)
        .withMessage("SB inntektDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarSBInntektDatoTilErNull() {
    var grunnlag = TestUtil.byggSoknadsbarnGrunnlagUtenInntektDatoTil();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerSoknadsbarn)
        .withMessage("SB inntektDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektType er null")
  void skalKasteIllegalArgumentExceptionNaarSBInntektTypeErNull() {
    var grunnlag = TestUtil.byggSoknadsbarnGrunnlagUtenInntektType();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerSoknadsbarn)
        .withMessage("SB inntektType kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBelop er null")
  void skalKasteIllegalArgumentExceptionNaarSBInntektBelopErNull() {
    var grunnlag = TestUtil.byggSoknadsbarnGrunnlagUtenInntektBelop();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::validerSoknadsbarn)
        .withMessage("SB inntektBelop kan ikke være null");
  }


  // Inntekt
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBPDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBPDatoFraTilErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBPDatoFraTil().getInntektBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt("BP"))
        .withMessage("BP inntektDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBPDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBPDatoFraErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBPDatoFra().getInntektBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt("BP"))
        .withMessage("BP inntektDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBPDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBPDatoTilErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBPDatoTil().getInntektBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt("BP"))
        .withMessage("BP inntektDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBPType er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBPTypeErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBPType().getInntektBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt("BP"))
        .withMessage("BP inntektType kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBPBelop er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBPBelopErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBPBelop().getInntektBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt("BP"))
        .withMessage("BP inntektBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBMDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBMDatoFraTilErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBMDatoFraTil().getInntektBMPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt())
        .withMessage("BM inntektDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBMDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBMDatoFraErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBMDatoFra().getInntektBMPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt())
        .withMessage("BM inntektDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBMDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBMDatoTilErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBMDatoTil().getInntektBMPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt())
        .withMessage("BM inntektDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBMType er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBMTypeErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBMType().getInntektBMPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt())
        .withMessage("BM inntektType kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBMBelop er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBMBelopErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBMBelop().getInntektBMPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt())
        .withMessage("BM inntektBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBMDeltFordel er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBMDeltFordelErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBMDeltFordel().getInntektBMPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt())
        .withMessage("BM deltFordel kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBMSkatteklasse2 er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBMSkatteklasse2ErNull() {
    var grunnlag = TestUtil.byggInntektGrunnlagUtenInntektBMSkatteklasse2().getInntektBMPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).validerInntekt())
        .withMessage("BM skatteklasse2 kan ikke være null");
  }


  // Bidragsevne
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når skatteklasseDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarBESkatteklasseDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSkatteklasseDatoFraTil().getSkatteklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("skatteklasseDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når skatteklasseDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarBESkatteklasseDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSkatteklasseDatoFra().getSkatteklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("skatteklasseDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception når skatteklasseDatoTil er null")
  void skalIkkeKasteExceptionNaarBESkatteklasseDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSkatteklasseDatoTil().getSkatteklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("skatteklasseDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når skatteklasseId er null")
  void skalKasteIllegalArgumentExceptionNaarBESkatteklasseErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSkatteklasseId().getSkatteklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("skatteklasseId kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarBEBostatusDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusDatoFraTil().getBostatusPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("bostatusDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarBEBostatusDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusDatoFra().getBostatusPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("bostatusDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception når bostatusDatoTil er null")
  void skalIkkeKasteExceptionNaarBEBostatusDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusDatoTil().getBostatusPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("bostatusDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusKode er null")
  void skalKasteIllegalArgumentExceptionNaarBEBostatusKodeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusKode().getBostatusPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("bostatusKode kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antallBarnIEgetHusholdDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarBEAntallBarnIEgetHusholdDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoFraTil().getAntallBarnIEgetHusholdPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("antallBarnIEgetHusholdDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antallBarnIEgetHusholdDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarBEAntallBarnIEgetHusholdDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoFra().getAntallBarnIEgetHusholdPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("antallBarnIEgetHusholdDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception når antallBarnIEgetHusholdDatoTil er null")
  void skalIkkeKasteExceptionNaarBEAntallBarnIEgetHusholdDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoTil().getAntallBarnIEgetHusholdPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("antallBarnIEgetHusholdDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antallBarn er null")
  void skalKasteIllegalArgumentExceptionNaarBEAntallBarnErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarn().getAntallBarnIEgetHusholdPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("antallBarn kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarBESaerfradragDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragDatoFraTil().getSaerfradragPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("saerfradragDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarBESaerfradragDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragDatoFra().getSaerfradragPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("saerfradragDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception når saerfradragDatoTil er null")
  void skalIkkeKasteExceptionNaarBESaerfradragDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragDatoTil().getSaerfradragPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("saerfradragDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragKode er null")
  void skalKasteIllegalArgumentExceptionNaarBESaerfradragKodeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragKode().getSaerfradragPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("saerfradragKode kan ikke være null");
  }


  // BPs andel særtilskudd
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når nettoSaertilskuddDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarASNettoSaertilskuddDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddDatoFraTil().getNettoSaertilskuddPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("nettoSaertilskuddDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når nettoSaertilskuddDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarASNettoSaertilskuddDatoFraErNull() {
    var grunnlag = TestUtil.byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddDatoFra().getNettoSaertilskuddPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("nettoSaertilskuddDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når nettoSaertilskuddDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarASNettoSaertilskuddDatoTilErNull() {
    var grunnlag = TestUtil.byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddDatoTil().getNettoSaertilskuddPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("nettoSaertilskuddDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når nettoSaertilskuddBelop er null")
  void skalKasteIllegalArgumentExceptionNaarASNettoSaertilskuddBelopErNull() {
    var grunnlag = TestUtil.byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddBelop().getNettoSaertilskuddPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("nettoSaertilskuddBelop kan ikke være null");
  }


  // Samværsfradrag
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når samvaersklasseDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarSFSamvaersklasseDatoFraTilErNull() {
    var grunnlag = TestUtil.byggSamvaersfradragGrunnlagUtenSamvaersklasseDatoFraTil().getSamvaersklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("samvaersklasseDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når samvaersklasseDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarSFSamvaersklasseDatoFraErNull() {
    var grunnlag = TestUtil.byggSamvaersfradragGrunnlagUtenSamvaersklasseDatoFra().getSamvaersklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("samvaersklasseDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når samvaersklasseDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarSFSamvaersklasseDatoTilErNull() {
    var grunnlag = TestUtil.byggSamvaersfradragGrunnlagUtenSamvaersklasseDatoTil().getSamvaersklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("samvaersklasseDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når samvaersklasseId er null")
  void skalKasteIllegalArgumentExceptionNaarSFSamvaersklasseIdErNull() {
    var grunnlag = TestUtil.byggSamvaersfradragGrunnlagUtenSamvaersklasseId().getSamvaersklassePeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag.get(0)::tilCore)
        .withMessage("samvaersklasseId kan ikke være null");
  }


  // Særtilskudd
  @Test
  @DisplayName("Skal kaste IllegalArgumentException når lopendeBidragDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarSTLopendeBidragBPDatoFraTilErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenLopendeBidragDatoFraTil().getLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).tilCore())
        .withMessage("lopendeBidragDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når lopendeBidragDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarSTLopendeBidragBPDatoFraErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenLopendeBidragDatoFra().getLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).tilCore())
        .withMessage("lopendeBidragDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når lopendeBidragDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarSTLopendeBidragBPDatoTilErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenLopendeBidragDatoTil().getLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).tilCore())
        .withMessage("lopendeBidragDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når lopendeBidragBelop er null")
  void skalKasteIllegalArgumentExceptionNaarSTLopendeBidragBelopErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenLopendeBidragBelop().getLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).tilCore())
        .withMessage("lopendeBidragBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når opprinneligBPAndelUnderholdskostnadBelop er null")
  void skalKasteIllegalArgumentExceptionNaarSTOpprinneligBPAndelUnderholdskostnadBelopErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenOpprinneligBPAndelUnderholdskostnadBelop().getLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).tilCore())
        .withMessage("opprinneligBPAndelUnderholdskostnadBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når opprinneligSamvaersfradragBelop er null")
  void skalKasteIllegalArgumentExceptionNaarSTOpprinneligSamvaersfradragBelopErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenOpprinneligSamvaersfradragBelop().getLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).tilCore())
        .withMessage("opprinneligSamvaersfradragBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når opprinneligBidragBelop er null")
  void skalKasteIllegalArgumentExceptionNaarSTOpprinneligBidragBelopErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenOpprinneligBidragBelop().getLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).tilCore())
        .withMessage("opprinneligBidragBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når lopendeBidragResultatKode er null")
  void skalKasteIllegalArgumentExceptionNaarSTLopendeBidragResultatKodeErNull() {
    var grunnlag = TestUtil.byggSaertilskuddGrunnlagUtenLopendeBidragResultatKode().getLopendeBidragBPPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(() -> grunnlag.get(0).tilCore())
        .withMessage("lopendeBidragResultatkode kan ikke være null");
  }
}
