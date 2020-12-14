package no.nav.bidrag.beregn.saertilskudd.rest;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneResultatCore;
import no.nav.bidrag.beregn.bidragsevne.dto.InntektCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddResultatCore;
import no.nav.bidrag.beregn.felles.dto.AvvikCore;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevneCore;
import no.nav.bidrag.beregn.saertilskudd.dto.LopendeBidragCore;
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Samvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.TrinnvisSkattesats;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.AntallBarnIEgetHusholdPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPAndelSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPAndelSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPAndelSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPBidragsevneGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPBidragsevneResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPSamvaersfradragGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPSamvaersfradragResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BidragsevneGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BostatusPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagBarn;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Inntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektBM;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektBMPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektBPBMGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.LopendeBidragBPPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.LopendeBidragGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.NettoSaertilskuddPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Periode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatBeregningBPAndelSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatBeregningBidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatBeregningSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatBeregningSamvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatGrunnlagBPAndelSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatGrunnlagBidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatGrunnlagSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatGrunnlagSamvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatPeriodeBPAndelSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatPeriodeBidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatPeriodeSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatPeriodeSamvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SaerfradragPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SamvaersfradragGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SamvaersklassePeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SkatteklassePeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SoknadsbarnGrunnlag;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.GrunnlagBeregningPeriodisertCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersfradragGrunnlagPerBarnCore;

public class TestUtil {

  // Total særtilskudd
  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlag() {
    return byggTotalSaertilskuddGrunnlag("");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlagUtenBeregnDatoFra() {
    return byggTotalSaertilskuddGrunnlag("beregnDatoFra");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlagUtenBeregnDatoTil() {
    return byggTotalSaertilskuddGrunnlag("beregnDatoTil");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlagUtenSoknadsbarnGrunnlag() {
    return byggTotalSaertilskuddGrunnlag("soknadsbarnGrunnlag");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlagUtenInntektBPBMGrunnlag() {
    return byggTotalSaertilskuddGrunnlag("inntektBPBMGrunnlag");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlagUtenBeregnBPBidragsevneGrunnlag() {
    return byggTotalSaertilskuddGrunnlag("beregnBPBidragsevneGrunnlag");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlagUtenBeregnBPAndelSaertilskuddGrunnlag() {
    return byggTotalSaertilskuddGrunnlag("beregnBPAndelSaertilskuddGrunnlag");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlagUtenBeregnBPSamvaersfradragGrunnlag() {
    return byggTotalSaertilskuddGrunnlag("beregnBPSamvaersfradragGrunnlag");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlagUtenBeregnSaertilskuddGrunnlag() {
    return byggTotalSaertilskuddGrunnlag("beregnSaertilskuddGrunnlag");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggInntektBPBMGrunnlagUtenInntektBPPeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "inntektBPPeriodeListe", "", "", "", "");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggInntektBPBMGrunnlagUtenInntektBMPeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "inntektBMPeriodeListe", "", "", "", "");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggBidragsevneGrunnlagUtenSkatteklassePeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "", "skatteklassePeriodeListe", "", "", "");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggBidragsevneGrunnlagUtenBostatusPeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "", "bostatusPeriodeListe", "", "", "");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdPeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "", "antallBarnIEgetHusholdPeriodeListe", "", "", "");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggBidragsevneGrunnlagUtenSaerfradragPeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "", "saerfradragPeriodeListe", "", "", "");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddPeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "", "", "nettoSaertilskuddPeriodeListe", "", "");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggSamvaersfradragGrunnlagUtenSamvaersklassePeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "", "", "", "samvaersklassePeriodeListe", "");
  }

  public static BeregnTotalSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenLopendeBidragBPPeriodeListe() {
    return byggTotalSaertilskuddGrunnlag("", "", "", "", "", "lopendeBidragBPPeriodeListe");
  }


  // Søknadsbarn
  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlag() {
    return byggSoknadsbarnGrunnlag("");
  }

  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlagUtenSoknadsbarnPersonId() {
    return byggSoknadsbarnGrunnlag("soknadsbarnPersonId");
  }

  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlagUtenSoknadsbarnFodselsdato() {
    return byggSoknadsbarnGrunnlag("soknadsbarnFodselsdato");
  }

  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlagUtenInntektPeriodeListe() {
    return byggSoknadsbarnGrunnlag("inntektPeriodeListe");
  }

  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlagUtenInntektDatoFraTil() {
    return byggSoknadsbarnGrunnlag("inntektDatoFraTil");
  }

  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlagUtenInntektDatoFra() {
    return byggSoknadsbarnGrunnlag("inntektDatoFra");
  }

  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlagUtenInntektDatoTil() {
    return byggSoknadsbarnGrunnlag("inntektDatoTil");
  }

  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlagUtenInntektType() {
    return byggSoknadsbarnGrunnlag("inntektType");
  }

  public static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlagUtenInntektBelop() {
    return byggSoknadsbarnGrunnlag("inntektBelop");
  }


  // Inntekt
  public static InntektBPBMGrunnlag byggInntektGrunnlag() {
    return byggInntektGrunnlag("");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBPDatoFraTil() {
    return byggInntektGrunnlag("inntektBPDatoFraTil");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBPDatoFra() {
    return byggInntektGrunnlag("inntektBPDatoFra");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBPDatoTil() {
    return byggInntektGrunnlag("inntektBPDatoTil");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBPType() {
    return byggInntektGrunnlag("inntektBPType");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBPBelop() {
    return byggInntektGrunnlag("inntektBPBelop");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBMDatoFraTil() {
    return byggInntektGrunnlag("inntektBMDatoFraTil");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBMDatoFra() {
    return byggInntektGrunnlag("inntektBMDatoFra");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBMDatoTil() {
    return byggInntektGrunnlag("inntektBMDatoTil");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBMType() {
    return byggInntektGrunnlag("inntektBMType");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBMBelop() {
    return byggInntektGrunnlag("inntektBMBelop");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBMDeltFordel() {
    return byggInntektGrunnlag("inntektBMDeltFordel");
  }

  public static InntektBPBMGrunnlag byggInntektGrunnlagUtenInntektBMSkatteklasse2() {
    return byggInntektGrunnlag("inntektBMSkatteklasse2");
  }


  // Bidragsevne
  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlag() {
    return byggBidragsevneGrunnlag("");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSkatteklasseDatoFraTil() {
    return byggBidragsevneGrunnlag("skatteklasseDatoFraTil");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSkatteklasseDatoFra() {
    return byggBidragsevneGrunnlag("skatteklasseDatoFra");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSkatteklasseDatoTil() {
    return byggBidragsevneGrunnlag("skatteklasseDatoTil");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSkatteklasseId() {
    return byggBidragsevneGrunnlag("skatteklasseId");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusDatoFraTil() {
    return byggBidragsevneGrunnlag("bostatusDatoFraTil");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusDatoFra() {
    return byggBidragsevneGrunnlag("bostatusDatoFra");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusDatoTil() {
    return byggBidragsevneGrunnlag("bostatusDatoTil");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusKode() {
    return byggBidragsevneGrunnlag("bostatusKode");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoFraTil() {
    return byggBidragsevneGrunnlag("antallBarnIEgetHusholdDatoFraTil");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoFra() {
    return byggBidragsevneGrunnlag("antallBarnIEgetHusholdDatoFra");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoTil() {
    return byggBidragsevneGrunnlag("antallBarnIEgetHusholdDatoTil");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarn() {
    return byggBidragsevneGrunnlag("antallBarn");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragDatoFraTil() {
    return byggBidragsevneGrunnlag("saerfradragDatoFraTil");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragDatoFra() {
    return byggBidragsevneGrunnlag("saerfradragDatoFra");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragDatoTil() {
    return byggBidragsevneGrunnlag("saerfradragDatoTil");
  }

  public static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragKode() {
    return byggBidragsevneGrunnlag("saerfradragKode");
  }


  // BPs andel særtilskudd
  public static BeregnBPAndelSaertilskuddGrunnlag byggBPAndelSaertilskuddGrunnlag() {
    return byggBPAndelSaertilskuddGrunnlag("");
  }

  public static BeregnBPAndelSaertilskuddGrunnlag byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddDatoFraTil() {
    return byggBPAndelSaertilskuddGrunnlag("nettoSaertilskuddDatoFraTil");
  }

  public static BeregnBPAndelSaertilskuddGrunnlag byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddDatoFra() {
    return byggBPAndelSaertilskuddGrunnlag("nettoSaertilskuddDatoFra");
  }

  public static BeregnBPAndelSaertilskuddGrunnlag byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddDatoTil() {
    return byggBPAndelSaertilskuddGrunnlag("nettoSaertilskuddDatoTil");
  }

  public static BeregnBPAndelSaertilskuddGrunnlag byggBPAndelSaertilskuddGrunnlagUtenNettoSaertilskuddBelop() {
    return byggBPAndelSaertilskuddGrunnlag("nettoSaertilskuddBelop");
  }


  // Samværsfradrag
  public static BeregnBPSamvaersfradragGrunnlag byggSamvaersfradragGrunnlag() {
    return byggSamvaersfradragGrunnlag("");
  }

  public static BeregnBPSamvaersfradragGrunnlag byggSamvaersfradragGrunnlagUtenSamvaersklasseDatoFraTil() {
    return byggSamvaersfradragGrunnlag("samvaersklasseDatoFraTil");
  }

  public static BeregnBPSamvaersfradragGrunnlag byggSamvaersfradragGrunnlagUtenSamvaersklasseDatoFra() {
    return byggSamvaersfradragGrunnlag("samvaersklasseDatoFra");
  }

  public static BeregnBPSamvaersfradragGrunnlag byggSamvaersfradragGrunnlagUtenSamvaersklasseDatoTil() {
    return byggSamvaersfradragGrunnlag("samvaersklasseDatoTil");
  }

  public static BeregnBPSamvaersfradragGrunnlag byggSamvaersfradragGrunnlagUtenSamvaersklasseBarnPersonId() {
    return byggSamvaersfradragGrunnlag("samvaersklasseBarnPersonId");
  }

  public static BeregnBPSamvaersfradragGrunnlag byggSamvaersfradragGrunnlagUtenSamvaersklasseBarnFodselsdato() {
    return byggSamvaersfradragGrunnlag("samvaersklasseBarnFodselsdato");
  }

  public static BeregnBPSamvaersfradragGrunnlag byggSamvaersfradragGrunnlagUtenSamvaersklasseId() {
    return byggSamvaersfradragGrunnlag("samvaersklasseId");
  }


  // Særtilskudd
  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlag() {
    return byggSaertilskuddGrunnlag("");
  }

  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenLopendeBidragDatoFraTil() {
    return byggSaertilskuddGrunnlag("lopendeBidragDatoFraTil");
  }

  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenLopendeBidragDatoFra() {
    return byggSaertilskuddGrunnlag("lopendeBidragDatoFra");
  }

  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenLopendeBidragDatoTil() {
    return byggSaertilskuddGrunnlag("lopendeBidragDatoTil");
  }

  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenLopendeBidragBarnPersonId() {
    return byggSaertilskuddGrunnlag("lopendeBidragBarnPersonId");
  }

  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenLopendeBidragBelop() {
    return byggSaertilskuddGrunnlag("lopendeBidragBelop");
  }

  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenOpprinneligBPAndelUnderholdskostnadBelop() {
    return byggSaertilskuddGrunnlag("opprinneligBPAndelUnderholdskostnadBelop");
  }

  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenOpprinneligSamvaersfradragBelop() {
    return byggSaertilskuddGrunnlag("opprinneligSamvaersfradragBelop");
  }

  public static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlagUtenOpprinneligBidragBelop() {
    return byggSaertilskuddGrunnlag("opprinneligBidragBelop");
  }


  // Bygger opp BeregnTotalSaertilskuddGrunnlag (felles grunnlag for alle delberegninger)
  private static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlag(String nullVerdi) {
    // "Jukser" litt hvis nullVerdi er numerisk. Verdien brukes da til å knytte inntekt BM til nullVerdi (= søknadsbarn person-id)
    if (StringUtils.isNumeric(nullVerdi)) {
      return byggTotalSaertilskuddGrunnlag("", nullVerdi, "", "", "", "");
    } else {
      return byggTotalSaertilskuddGrunnlag(nullVerdi, "", "", "", "", "");
    }
  }

  private static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlag(String nullVerdi, String inntektVerdi,
      String bidragsevneVerdi, String bpAndelSaertilskuddVerdi, String samvaersfradragVerdi, String saertilskuddVerdi) {
    var beregnDatoFra = (nullVerdi.equals("beregnDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var beregnDatoTil = (nullVerdi.equals("beregnDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var soknadsbarnGrunnlag = (nullVerdi.equals("soknadsbarnGrunnlag") ? null : byggSoknadsbarnGrunnlag(""));
    var inntektGrunnlag = (nullVerdi.equals("inntektBPBMGrunnlag") ? null : byggInntektGrunnlag(inntektVerdi));
    var beregnBidragsevneGrunnlag = (nullVerdi.equals("beregnBPBidragsevneGrunnlag") ? null : byggBidragsevneGrunnlag(bidragsevneVerdi));
    var beregnBPAndelSaertilskuddGrunnlag = (nullVerdi.equals("beregnBPAndelSaertilskuddGrunnlag") ? null
        : byggBPAndelSaertilskuddGrunnlag(bpAndelSaertilskuddVerdi));
    var beregnSamvaersfradragGrunnlag = (nullVerdi.equals("beregnBPSamvaersfradragGrunnlag") ? null
        : byggSamvaersfradragGrunnlag(samvaersfradragVerdi));
    var beregnSaertilskuddGrunnlag = (nullVerdi.equals("beregnSaertilskuddGrunnlag") ? null
        : byggSaertilskuddGrunnlag(saertilskuddVerdi));

    return new BeregnTotalSaertilskuddGrunnlag(beregnDatoFra, beregnDatoTil, soknadsbarnGrunnlag, inntektGrunnlag, beregnBidragsevneGrunnlag,
        beregnBPAndelSaertilskuddGrunnlag, beregnSamvaersfradragGrunnlag, beregnSaertilskuddGrunnlag);
  }

  // Bygger opp SoknadsbarnGrunnlag
  private static SoknadsbarnGrunnlag byggSoknadsbarnGrunnlag(String nullVerdi) {
    var soknadsbarnPersonId = (nullVerdi.equals("soknadsbarnPersonId") ? null : 1);
    var soknadsbarnFodselsdato = (nullVerdi.equals("soknadsbarnFodselsdato") ? null : LocalDate.parse("2010-01-01"));

    SoknadsbarnGrunnlag soknadsbarnGrunnlag;
    if (nullVerdi.equals("inntektPeriodeListe")) {
      soknadsbarnGrunnlag = new SoknadsbarnGrunnlag(soknadsbarnPersonId, soknadsbarnFodselsdato, null);
    } else {
      soknadsbarnGrunnlag = new SoknadsbarnGrunnlag(soknadsbarnPersonId, soknadsbarnFodselsdato,
          singletonList(byggSoknadsbarnInntektPeriode(nullVerdi)));
    }

    return soknadsbarnGrunnlag;
  }

  // Bygger opp InntektPeriode for søknadsbarn
  private static InntektPeriode byggSoknadsbarnInntektPeriode(String nullVerdi) {
    var inntektDatoFra = (nullVerdi.equals("inntektDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var inntektDatoTil = (nullVerdi.equals("inntektDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var inntektType = (nullVerdi.equals("inntektType") ? null : "INNTEKTSOPPL_ARBEIDSGIVER");
    var inntektBelop = (nullVerdi.equals("inntektBelop") ? null : BigDecimal.valueOf(100));

    InntektPeriode inntektPeriode;
    if (nullVerdi.equals("inntektDatoFraTil")) {
      inntektPeriode = new InntektPeriode(null, inntektType, inntektBelop);
    } else {
      inntektPeriode = new InntektPeriode(new Periode(inntektDatoFra, inntektDatoTil), inntektType, inntektBelop);
    }

    return inntektPeriode;
  }

  // Bygger opp InntektBPBMGrunnlag
  private static InntektBPBMGrunnlag byggInntektGrunnlag(String nullVerdi) {
    var inntektBPDatoFra = (nullVerdi.equals("inntektBPDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var inntektBPDatoTil = (nullVerdi.equals("inntektBPDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var inntektBPType = (nullVerdi.equals("inntektBPType") ? null : "INNTEKTSOPPL_ARBEIDSGIVER");
    var inntektBPBelop = (nullVerdi.equals("inntektBPBelop") ? null : BigDecimal.valueOf(100));
    var inntektBMDatoFra = (nullVerdi.equals("inntektBMDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var inntektBMDatoTil = (nullVerdi.equals("inntektBMDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var inntektBMType = (nullVerdi.equals("inntektBMType") ? null : "INNTEKTSOPPL_ARBEIDSGIVER");
    var inntektBMBelop = (nullVerdi.equals("inntektBMBelop") ? null : BigDecimal.valueOf(100));
    var inntektBMDeltFordel = (nullVerdi.equals("inntektBMDeltFordel") ? null : false);
    var inntektBMSkatteklasse2 = (nullVerdi.equals("inntektBMSkatteklasse2") ? null : false);

    List<InntektPeriode> inntektBPPeriodeListe;
    if (nullVerdi.equals("inntektBPPeriodeListe")) {
      inntektBPPeriodeListe = null;
    } else {
      InntektPeriode inntektBPPeriode;
      if (nullVerdi.equals("inntektBPDatoFraTil")) {
        inntektBPPeriode = new InntektPeriode(null, inntektBPType, inntektBPBelop);
      } else {
        inntektBPPeriode = new InntektPeriode(new Periode(inntektBPDatoFra, inntektBPDatoTil), inntektBPType, inntektBPBelop);
      }
      inntektBPPeriodeListe = singletonList(inntektBPPeriode);
    }

    List<InntektBMPeriode> inntektBMPeriodeListe;
    if (nullVerdi.equals("inntektBMPeriodeListe")) {
      inntektBMPeriodeListe = null;
    } else {
      InntektBMPeriode inntektBMPeriode;
      if (nullVerdi.equals("inntektBMDatoFraTil")) {
        inntektBMPeriode = new InntektBMPeriode(null, inntektBMType, inntektBMBelop, inntektBMDeltFordel, inntektBMSkatteklasse2);
      } else {
        inntektBMPeriode = new InntektBMPeriode(new Periode(inntektBMDatoFra, inntektBMDatoTil), inntektBMType, inntektBMBelop,
            inntektBMDeltFordel, inntektBMSkatteklasse2);
      }
      inntektBMPeriodeListe = singletonList(inntektBMPeriode);
    }

    return new InntektBPBMGrunnlag(inntektBPPeriodeListe, inntektBMPeriodeListe);
  }

  // Bygger opp BeregnBPBidragsevneGrunnlag
  private static BeregnBPBidragsevneGrunnlag byggBidragsevneGrunnlag(String nullVerdi) {
    var skatteklasseDatoFra = (nullVerdi.equals("skatteklasseDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var skatteklasseDatoTil = (nullVerdi.equals("skatteklasseDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var skatteklasseId = (nullVerdi.equals("skatteklasseId") ? null : 1);
    var bostatusDatoFra = (nullVerdi.equals("bostatusDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var bostatusDatoTil = (nullVerdi.equals("bostatusDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var bostatusKode = (nullVerdi.equals("bostatusKode") ? null : "MED_ANDRE");
    var antallBarnIEgetHusholdDatoFra = (nullVerdi.equals("antallBarnIEgetHusholdDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var antallBarnIEgetHusholdDatoTil = (nullVerdi.equals("antallBarnIEgetHusholdDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var antallBarn = (nullVerdi.equals("antallBarn") ? null : BigDecimal.ONE);
    var saerfradragDatoFra = (nullVerdi.equals("saerfradragDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var saerfradragDatoTil = (nullVerdi.equals("saerfradragDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var saerfradragKode = (nullVerdi.equals("saerfradragKode") ? null : "HELT");

    List<SkatteklassePeriode> skatteklassePeriodeListe;
    if (nullVerdi.equals("skatteklassePeriodeListe")) {
      skatteklassePeriodeListe = null;
    } else {
      SkatteklassePeriode skatteklassePeriode;
      if (nullVerdi.equals("skatteklasseDatoFraTil")) {
        skatteklassePeriode = new SkatteklassePeriode(null, skatteklasseId);
      } else {
        skatteklassePeriode = new SkatteklassePeriode(new Periode(skatteklasseDatoFra, skatteklasseDatoTil), skatteklasseId);
      }
      skatteklassePeriodeListe = singletonList(skatteklassePeriode);
    }

    List<BostatusPeriode> bostatusPeriodeListe;
    if (nullVerdi.equals("bostatusPeriodeListe")) {
      bostatusPeriodeListe = null;
    } else {
      BostatusPeriode bostatusPeriode;
      if (nullVerdi.equals("bostatusDatoFraTil")) {
        bostatusPeriode = new BostatusPeriode(null, bostatusKode);
      } else {
        bostatusPeriode = new BostatusPeriode(new Periode(bostatusDatoFra, bostatusDatoTil), bostatusKode);
      }
      bostatusPeriodeListe = singletonList(bostatusPeriode);
    }

    List<AntallBarnIEgetHusholdPeriode> antallBarnIEgetHusholdPeriodeListe;
    if (nullVerdi.equals("antallBarnIEgetHusholdPeriodeListe")) {
      antallBarnIEgetHusholdPeriodeListe = null;
    } else {
      AntallBarnIEgetHusholdPeriode antallBarnIEgetHusholdPeriode;
      if (nullVerdi.equals("antallBarnIEgetHusholdDatoFraTil")) {
        antallBarnIEgetHusholdPeriode = new AntallBarnIEgetHusholdPeriode(null, antallBarn);
      } else {
        antallBarnIEgetHusholdPeriode =
            new AntallBarnIEgetHusholdPeriode(new Periode(antallBarnIEgetHusholdDatoFra, antallBarnIEgetHusholdDatoTil), antallBarn);
      }
      antallBarnIEgetHusholdPeriodeListe = singletonList(antallBarnIEgetHusholdPeriode);
    }

    List<SaerfradragPeriode> saerfradragPeriodeListe;
    if (nullVerdi.equals("saerfradragPeriodeListe")) {
      saerfradragPeriodeListe = null;
    } else {
      SaerfradragPeriode saerfradragPeriode;
      if (nullVerdi.equals("saerfradragDatoFraTil")) {
        saerfradragPeriode = new SaerfradragPeriode(null, saerfradragKode);
      } else {
        saerfradragPeriode = new SaerfradragPeriode(new Periode(saerfradragDatoFra, saerfradragDatoTil), saerfradragKode);
      }
      saerfradragPeriodeListe = singletonList(saerfradragPeriode);
    }

    return new BeregnBPBidragsevneGrunnlag(skatteklassePeriodeListe, bostatusPeriodeListe, antallBarnIEgetHusholdPeriodeListe,
        saerfradragPeriodeListe);
  }

  // Bygger opp BeregnBPAndelSaertilskudd
  private static BeregnBPAndelSaertilskuddGrunnlag byggBPAndelSaertilskuddGrunnlag(String nullVerdi) {
    var nettoSaertilskuddDatoFra = (nullVerdi.equals("nettoSaertilskuddDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var nettoSaertilskuddDatoTil = (nullVerdi.equals("nettoSaertilskuddDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var nettoSaertilskuddBelop = (nullVerdi.equals("nettoSaertilskuddBelop") ? null : BigDecimal.valueOf(100));

    List<NettoSaertilskuddPeriode> nettoSaertilskuddPeriodeListe;
    if (nullVerdi.equals("nettoSaertilskuddPeriodeListe")) {
      nettoSaertilskuddPeriodeListe = null;
    } else {
      NettoSaertilskuddPeriode nettoSaertilskuddPeriode;
      if (nullVerdi.equals("nettoSaertilskuddDatoFraTil")) {
        nettoSaertilskuddPeriode = new NettoSaertilskuddPeriode(null, nettoSaertilskuddBelop);
      } else {
        nettoSaertilskuddPeriode = new NettoSaertilskuddPeriode(new Periode(nettoSaertilskuddDatoFra, nettoSaertilskuddDatoTil),
            nettoSaertilskuddBelop);
      }
      nettoSaertilskuddPeriodeListe = singletonList(nettoSaertilskuddPeriode);
    }

    return new BeregnBPAndelSaertilskuddGrunnlag(nettoSaertilskuddPeriodeListe);
  }

  // Bygger opp BeregnBPSamvaersfradragGrunnlag
  private static BeregnBPSamvaersfradragGrunnlag byggSamvaersfradragGrunnlag(String nullVerdi) {
    var samvaersklasseDatoFra = (nullVerdi.equals("samvaersklasseDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var samvaersklasseDatoTil = (nullVerdi.equals("samvaersklasseDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var samvaersklasseBarnPersonId = (nullVerdi.equals("samvaersklasseBarnPersonId") ? null : 1);
    var samvaersklasseBarnFodselsdato = (nullVerdi.equals("samvaersklasseBarnFodselsdato") ? null : LocalDate.parse("2010-01-01"));
    var samvaersklasseId = (nullVerdi.equals("samvaersklasseId") ? null : "00");

    List<SamvaersklassePeriode> samvaersklassePeriodeListe;
    if (nullVerdi.equals("samvaersklassePeriodeListe")) {
      samvaersklassePeriodeListe = null;
    } else {
      SamvaersklassePeriode samvaersklassePeriode;
      if (nullVerdi.equals("samvaersklasseDatoFraTil")) {
        samvaersklassePeriode = new SamvaersklassePeriode(null, samvaersklasseBarnPersonId, samvaersklasseBarnFodselsdato, samvaersklasseId);
      } else {
        samvaersklassePeriode = new SamvaersklassePeriode(new Periode(samvaersklasseDatoFra, samvaersklasseDatoTil), samvaersklasseBarnPersonId,
            samvaersklasseBarnFodselsdato, samvaersklasseId);
      }
      samvaersklassePeriodeListe = singletonList(samvaersklassePeriode);
    }

    return new BeregnBPSamvaersfradragGrunnlag(samvaersklassePeriodeListe);
  }

  // Bygger opp BeregnSaertilskuddGrunnlag
  private static BeregnSaertilskuddGrunnlag byggSaertilskuddGrunnlag(String nullVerdi) {
    var lopendeBidragDatoFra = (nullVerdi.equals("lopendeBidragDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var lopendeBidragDatoTil = (nullVerdi.equals("lopendeBidragDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var lopendeBidragBarnPersonId = (nullVerdi.equals("lopendeBidragBarnPersonId") ? null : 1);
    var lopendeBidragBelop = (nullVerdi.equals("lopendeBidragBelop") ? null : BigDecimal.valueOf(100));
    var opprinneligBPAndelUnderholdskostnadBelop = (nullVerdi.equals("opprinneligBPAndelUnderholdskostnadBelop") ? null : BigDecimal.valueOf(100));
    var opprinneligSamvaersfradragBelop = (nullVerdi.equals("opprinneligSamvaersfradragBelop") ? null : BigDecimal.valueOf(100));
    var opprinneligBidragBelop = (nullVerdi.equals("opprinneligBidragBelop") ? null : BigDecimal.valueOf(100));
    var lopendeBidragResultatKode = (nullVerdi.equals("lopendeBidragResultatKode") ? null : "RESULTATKODE");

    List<LopendeBidragBPPeriode> lopendeBidragBPPeriodeListe;
    if (nullVerdi.equals("lopendeBidragBPPeriodeListe")) {
      lopendeBidragBPPeriodeListe = null;
    } else {
      LopendeBidragBPPeriode lopendeBidragBPPeriode;
      if (nullVerdi.equals("lopendeBidragDatoFraTil")) {
        lopendeBidragBPPeriode = new LopendeBidragBPPeriode(null, lopendeBidragBarnPersonId, lopendeBidragBelop,
            opprinneligBPAndelUnderholdskostnadBelop, opprinneligSamvaersfradragBelop, opprinneligBidragBelop);
      } else {
        lopendeBidragBPPeriode = new LopendeBidragBPPeriode(new Periode(lopendeBidragDatoFra, lopendeBidragDatoTil), lopendeBidragBarnPersonId,
            lopendeBidragBelop, opprinneligBPAndelUnderholdskostnadBelop, opprinneligSamvaersfradragBelop, opprinneligBidragBelop);
      }
      lopendeBidragBPPeriodeListe = singletonList(lopendeBidragBPPeriode);
    }

    return new BeregnSaertilskuddGrunnlag(lopendeBidragBPPeriodeListe);
  }

  // Bygger opp BeregnBidragsevneResultat
  public static BeregnBPBidragsevneResultat dummyBidragsevneResultat() {
    var bidragPeriodeResultatListe = new ArrayList<ResultatPeriodeBidragsevne>();
    bidragPeriodeResultatListe.add(new ResultatPeriodeBidragsevne(new Periode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        new ResultatBeregningBidragsevne(BigDecimal.valueOf(100)),
        new ResultatGrunnlagBidragsevne(singletonList(new Inntekt("INNTEKTSOPPL_ARBEIDSGIVER", BigDecimal.valueOf(500000))), 1, "MED_ANDRE",
            BigDecimal.ONE, "HELT", emptyList())));
    return new BeregnBPBidragsevneResultat(bidragPeriodeResultatListe);
  }

  // Bygger opp BeregnBidragsevneResultatCore
  public static BeregnBidragsevneResultatCore dummyBidragsevneResultatCore() {
    var bidragPeriodeResultatListe = new ArrayList<no.nav.bidrag.beregn.bidragsevne.dto.ResultatPeriodeCore>();
    bidragPeriodeResultatListe.add(new no.nav.bidrag.beregn.bidragsevne.dto.ResultatPeriodeCore(
        new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        new no.nav.bidrag.beregn.bidragsevne.dto.ResultatBeregningCore(BigDecimal.valueOf(100)),
        new no.nav.bidrag.beregn.bidragsevne.dto.ResultatGrunnlagCore(
            singletonList(new InntektCore("INNTEKTSOPPL_ARBEIDSGIVER", BigDecimal.valueOf(500000))), 1, "MED_ANDRE", BigDecimal.ONE, "HELT",
            emptyList())));
    return new BeregnBidragsevneResultatCore(bidragPeriodeResultatListe, emptyList());
  }

  // Bygger opp BeregnBidragsevneResultatCore med avvik
  public static BeregnBidragsevneResultatCore dummyBidragsevneResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i inntektPeriodeListe: datoFra=2018-04-01, datoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnBidragsevneResultatCore(emptyList(), avvikListe);
  }

  // Bygger opp BeregnBPAndelSaertilskuddResultat
  public static BeregnBPAndelSaertilskuddResultat dummyBPsAndelSaertilskuddResultat() {
    var bidragPeriodeResultatListe = new ArrayList<ResultatPeriodeBPAndelSaertilskudd>();
    bidragPeriodeResultatListe
        .add(new ResultatPeriodeBPAndelSaertilskudd(
            new Periode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
            new ResultatBeregningBPAndelSaertilskudd(BigDecimal.valueOf(10), BigDecimal.valueOf(100), false),
            new ResultatGrunnlagBPAndelSaertilskudd(BigDecimal.valueOf(100),
                singletonList(new Inntekt("INNTEKTSOPPL_ARBEIDSGIVER", BigDecimal.valueOf(100000))),
                singletonList(new InntektBM("INNTEKTSOPPL_ARBEIDSGIVER", BigDecimal.valueOf(100000), false, false)),
                singletonList(new Inntekt("INNTEKTSOPPL_ARBEIDSGIVER", BigDecimal.valueOf(100000))),
                emptyList())));
    return new BeregnBPAndelSaertilskuddResultat(bidragPeriodeResultatListe);
  }

  // Bygger opp BeregnBPsAndelSaertilskuddResultatCore
  public static BeregnBPsAndelSaertilskuddResultatCore dummyBPsAndelSaertilskuddResultatCore() {
    var bidragPeriodeResultatListe = new ArrayList<no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatPeriodeCore>();
    bidragPeriodeResultatListe.add(new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatPeriodeCore(
        new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatBeregningCore(BigDecimal.valueOf(10), BigDecimal.valueOf(100), false),
        new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatGrunnlagCore(BigDecimal.valueOf(100),
            singletonList(
                new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektCore("INNTEKTSOPPL_ARBEIDSGIVER", BigDecimal.valueOf(100000), false, false)),
            singletonList(
                new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektCore("INNTEKTSOPPL_ARBEIDSGIVER", BigDecimal.valueOf(100000), false, false)),
            singletonList(
                new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektCore("INNTEKTSOPPL_ARBEIDSGIVER", BigDecimal.valueOf(100000), false, false)),
            emptyList())));
    return new BeregnBPsAndelSaertilskuddResultatCore(bidragPeriodeResultatListe, emptyList());
  }

  // Bygger opp BeregnBPsAndelSaertilskuddResultatCore med avvik
  public static BeregnBPsAndelSaertilskuddResultatCore dummyBPsAndelSaertilskuddResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i inntektBPPeriodeListe: datoFra=2018-04-01, datoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnBPsAndelSaertilskuddResultatCore(emptyList(), avvikListe);
  }

  // Bygger opp BeregnSamvaersfradragResultat
  public static BeregnBPSamvaersfradragResultat dummySamvaersfradragResultat() {
    var bidragPeriodeResultatListe = new ArrayList<ResultatPeriodeSamvaersfradrag>();
    bidragPeriodeResultatListe.add(new ResultatPeriodeSamvaersfradrag(new Periode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        singletonList(new ResultatBeregningSamvaersfradrag(1, BigDecimal.valueOf(100))),
        new ResultatGrunnlagSamvaersfradrag(singletonList(new GrunnlagBarn(1, 9, "00")), emptyList())));
    return new BeregnBPSamvaersfradragResultat(bidragPeriodeResultatListe);
  }

  // Bygger opp BeregnSamvaersfradragResultatCore
  public static BeregnSamvaersfradragResultatCore dummySamvaersfradragResultatCore() {
    var bidragPeriodeResultatListe = new ArrayList<no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatPeriodeCore>();
    bidragPeriodeResultatListe.add(new no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatPeriodeCore(
        new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        singletonList(new no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatBeregningCore(1, BigDecimal.valueOf(100))),
        new GrunnlagBeregningPeriodisertCore(singletonList(new SamvaersfradragGrunnlagPerBarnCore(1, 9, "00")), emptyList())));
    return new BeregnSamvaersfradragResultatCore(bidragPeriodeResultatListe, emptyList());
  }

  // Bygger opp BeregnSamvaersfradragResultatCore med avvik
  public static BeregnSamvaersfradragResultatCore dummySamvaersfradragResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i samvaersklassePeriodeListe: datoFra=2018-04-01, datoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnSamvaersfradragResultatCore(emptyList(), avvikListe);
  }

  // Bygger opp BeregnSaertilskuddResultat
  public static BeregnSaertilskuddResultat dummySaertilskuddResultat() {
    var bidragPeriodeResultatListe = new ArrayList<ResultatPeriodeSaertilskudd>();
    bidragPeriodeResultatListe.add(new ResultatPeriodeSaertilskudd(
        new Periode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        new ResultatBeregningSaertilskudd(BigDecimal.valueOf(100), "RESULTATKODE"),
        new ResultatGrunnlagSaertilskudd(new BidragsevneGrunnlag(BigDecimal.valueOf(100)),
            new BPAndelSaertilskuddGrunnlag(BigDecimal.valueOf(100), BigDecimal.valueOf(100), false),
            singletonList(new SamvaersfradragGrunnlag(1, BigDecimal.valueOf(100))),
            singletonList(new LopendeBidragGrunnlag(1, BigDecimal.valueOf(100), BigDecimal.valueOf(100), BigDecimal.valueOf(100),
                BigDecimal.valueOf(100))))));
    return new BeregnSaertilskuddResultat(bidragPeriodeResultatListe);
  }

  // Bygger opp BeregnSaertilskuddResultatCore
  public static BeregnSaertilskuddResultatCore dummySaertilskuddResultatCore() {
    var bidragPeriodeResultatListe = new ArrayList<no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore>();
    bidragPeriodeResultatListe.add(new no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore(
        new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")), 1,
        new no.nav.bidrag.beregn.saertilskudd.dto.ResultatBeregningCore(BigDecimal.valueOf(100), "RESULTATKODE"),
        new no.nav.bidrag.beregn.saertilskudd.dto.ResultatGrunnlagCore(new BidragsevneCore(BigDecimal.valueOf(100)),
            new BPsAndelSaertilskuddCore(BigDecimal.valueOf(100), BigDecimal.valueOf(100), false),
            singletonList(new LopendeBidragCore(1, BigDecimal.valueOf(100), BigDecimal.valueOf(100), BigDecimal.valueOf(100),
                BigDecimal.valueOf(100))),
            singletonList(new SamvaersfradragCore(1, BigDecimal.valueOf(100))))));
    return new BeregnSaertilskuddResultatCore(bidragPeriodeResultatListe, emptyList());
  }

  // Bygger opp BeregnSaertilskuddResultatCore med avvik
  public static BeregnSaertilskuddResultatCore dummySaertilskuddResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i samvaersfradragPeriodeListe: datoFra=2018-04-01, datoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnSaertilskuddResultatCore(emptyList(), avvikListe);
  }


  // Bygger opp liste av sjabloner av typen Sjablontall
  public static List<Sjablontall> dummySjablonSjablontallListe() {
    var sjablonSjablontallListe = new ArrayList<Sjablontall>();

    sjablonSjablontallListe.add(new Sjablontall("0001", LocalDate.parse("2004-01-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(970)));
    sjablonSjablontallListe.add(new Sjablontall("0001", LocalDate.parse("2019-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(1054)));

    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(2504)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(2577)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(2649)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(2692)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(2775)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(2825)));

    sjablonSjablontallListe.add(new Sjablontall("0004", LocalDate.parse("2012-07-01"), LocalDate.parse("2012-12-31"), BigDecimal.valueOf(12712)));
    sjablonSjablontallListe.add(new Sjablontall("0004", LocalDate.parse("2013-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(0)));

    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(1490)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(1530)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(1570)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(1600)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(1640)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(1670)));

    sjablonSjablontallListe.add(new Sjablontall("0015", LocalDate.parse("2016-01-01"), LocalDate.parse("2016-12-31"), BigDecimal.valueOf(26.07)));
    sjablonSjablontallListe.add(new Sjablontall("0015", LocalDate.parse("2017-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(25.67)));
    sjablonSjablontallListe.add(new Sjablontall("0015", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(25.35)));
    sjablonSjablontallListe.add(new Sjablontall("0015", LocalDate.parse("2019-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(25.05)));

    sjablonSjablontallListe.add(new Sjablontall("0017", LocalDate.parse("2003-01-01"), LocalDate.parse("2003-12-31"), BigDecimal.valueOf(7.8)));
    sjablonSjablontallListe.add(new Sjablontall("0017", LocalDate.parse("2014-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(8.2)));

    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(3150)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(3294)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(3365)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(3417)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(3487)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(3841)));

    sjablonSjablontallListe.add(new Sjablontall("0021", LocalDate.parse("2012-07-01"), LocalDate.parse("2014-06-30"), BigDecimal.valueOf(4923)));
    sjablonSjablontallListe.add(new Sjablontall("0021", LocalDate.parse("2014-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(5100)));
    sjablonSjablontallListe.add(new Sjablontall("0021", LocalDate.parse("2017-07-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(5254)));
    sjablonSjablontallListe.add(new Sjablontall("0021", LocalDate.parse("2018-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(5313)));

    sjablonSjablontallListe.add(new Sjablontall("0022", LocalDate.parse("2012-07-01"), LocalDate.parse("2014-06-30"), BigDecimal.valueOf(2028)));
    sjablonSjablontallListe.add(new Sjablontall("0022", LocalDate.parse("2014-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(2100)));
    sjablonSjablontallListe.add(new Sjablontall("0022", LocalDate.parse("2017-07-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(2163)));
    sjablonSjablontallListe.add(new Sjablontall("0022", LocalDate.parse("2018-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(2187)));

    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(72200)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(73600)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(75000)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(83000)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(85050)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(87450)));

    sjablonSjablontallListe.add(new Sjablontall("0025", LocalDate.parse("2015-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(29)));
    sjablonSjablontallListe.add(new Sjablontall("0025", LocalDate.parse("2018-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(31)));

    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(50400)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(51750)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(53150)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(54750)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(56550)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(51300)));

    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(74250)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(76250)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(78300)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(54750)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(56550)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(51300)));

    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2016-01-01"), LocalDate.parse("2016-12-31"), BigDecimal.valueOf(90850)));
    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2017-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(94850)));
    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(99540)));
    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(102820)));
    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(93273)));

    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2016-01-01"), LocalDate.parse("2016-12-31"), BigDecimal.valueOf(112350)));
    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2017-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(117350)));
    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(99540)));
    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(102820)));
    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(93273)));

    sjablonSjablontallListe.add(new Sjablontall("0039", LocalDate.parse("2016-01-01"), LocalDate.parse("2016-12-31"), BigDecimal.valueOf(13505)));
    sjablonSjablontallListe.add(new Sjablontall("0039", LocalDate.parse("2017-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(13298)));
    sjablonSjablontallListe.add(new Sjablontall("0039", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(13132)));
    sjablonSjablontallListe.add(new Sjablontall("0039", LocalDate.parse("2019-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(12977)));

    sjablonSjablontallListe.add(new Sjablontall("0040", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(23)));
    sjablonSjablontallListe.add(new Sjablontall("0040", LocalDate.parse("2019-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(22)));

    sjablonSjablontallListe.add(new Sjablontall("0041", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(1354)));

    return sjablonSjablontallListe;
  }

  // Bygger opp liste av sjabloner av typen Samvaersfradrag
  public static List<Samvaersfradrag> dummySjablonSamvaersfradragListe() {
    var sjablonSamvaersfradragListe = new ArrayList<Samvaersfradrag>();

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 3, 0, BigDecimal.valueOf(204)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 3, 0, BigDecimal.valueOf(208)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 3, 0, BigDecimal.valueOf(212)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 3, 0, BigDecimal.valueOf(215)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 3, 0, BigDecimal.valueOf(219)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 3, 0, BigDecimal.valueOf(256)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 8, BigDecimal.valueOf(674)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 8, BigDecimal.valueOf(689)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 8, BigDecimal.valueOf(701)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 8, BigDecimal.valueOf(712)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 8, BigDecimal.valueOf(727)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 8, BigDecimal.valueOf(849)));

    return sjablonSamvaersfradragListe;
  }


  // Bygger opp liste av sjabloner av typen Bidragsevne
  public static List<Bidragsevne> dummySjablonBidragsevneListe() {
    var sjablonBidragsevneListe = new ArrayList<Bidragsevne>();

    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(7711), BigDecimal.valueOf(8048)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(8907), BigDecimal.valueOf(8289)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(9156), BigDecimal.valueOf(8521)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(9303), BigDecimal.valueOf(8657)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(9591), BigDecimal.valueOf(8925)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(9764), BigDecimal.valueOf(9818)));

    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(5073), BigDecimal.valueOf(6814)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(5456), BigDecimal.valueOf(7018)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(5609), BigDecimal.valueOf(7215)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(5698), BigDecimal.valueOf(7330)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(5875), BigDecimal.valueOf(7557)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(5981), BigDecimal.valueOf(8313)));

    return sjablonBidragsevneListe;
  }

  // Bygger opp liste av sjabloner av typen TrinnvisSkattesats
  public static List<TrinnvisSkattesats> dummySjablonTrinnvisSkattesatsListe() {
    var sjablonTrinnvisSkattesatsListe = new ArrayList<TrinnvisSkattesats>();

    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(169000), BigDecimal.valueOf(1.4)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(237900), BigDecimal.valueOf(3.3)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(598050), BigDecimal.valueOf(12.4)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(962050), BigDecimal.valueOf(15.4)));

    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(174500), BigDecimal.valueOf(1.9)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(245650), BigDecimal.valueOf(4.2)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(617500), BigDecimal.valueOf(13.2)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(964800), BigDecimal.valueOf(16.2)));

    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(180800), BigDecimal.valueOf(1.9)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(254500), BigDecimal.valueOf(4.2)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(639750), BigDecimal.valueOf(13.2)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(999550), BigDecimal.valueOf(16.2)));

    return sjablonTrinnvisSkattesatsListe;
  }
}
