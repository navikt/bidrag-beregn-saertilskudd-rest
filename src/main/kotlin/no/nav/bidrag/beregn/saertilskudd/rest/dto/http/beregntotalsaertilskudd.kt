package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneGrunnlagCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevnePeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore
import java.time.LocalDate

// Grunnlag
@ApiModel(value = "Totalgrunnlaget for en særtilskuddsberegning")
data class BeregnTotalSaertilskuddGrunnlag(
    @ApiModelProperty(value = "Beregn fra-dato") var beregnDatoFra: LocalDate? = null,
    @ApiModelProperty(value = "Beregn til-dato") var beregnDatoTil: LocalDate? = null,
    @ApiModelProperty(value = "Søknadsbarn grunnlag") var soknadsbarnGrunnlag: SoknadsbarnGrunnlag? = null,
    @ApiModelProperty(value = "Inntekt BP/BM grunnlag") var inntektBPBMGrunnlag: InntektBPBMGrunnlag? = null,
    @ApiModelProperty(value = "Beregn BP bidragsevne grunnlag") var beregnBPBidragsevneGrunnlag: BeregnBPBidragsevneGrunnlag? = null,
    @ApiModelProperty(value = "Beregn BP andel særtilskudd grunnlag") var beregnBPAndelSaertilskuddGrunnlag: BeregnBPAndelSaertilskuddGrunnlag? = null,
    @ApiModelProperty(value = "Beregn BP samværsfradrag grunnlag") var beregnBPSamvaersfradragGrunnlag: BeregnBPSamvaersfradragGrunnlag? = null,
    @ApiModelProperty(value = "Beregn særtilskudd grunnlag") var beregnSaertilskuddGrunnlag: BeregnSaertilskuddGrunnlag? = null
) {

  fun validerTotalSaertilskuddGrunnlag() {
    if (beregnDatoFra != null) beregnDatoFra!! else throw UgyldigInputException("beregnDatoFra kan ikke være null")
    if (beregnDatoTil != null) beregnDatoTil!! else throw UgyldigInputException("beregnDatoTil kan ikke være null")
    if (soknadsbarnGrunnlag != null) soknadsbarnGrunnlag!! else throw UgyldigInputException("soknadsbarnGrunnlag kan ikke være null")
    if (inntektBPBMGrunnlag != null) inntektBPBMGrunnlag!! else throw UgyldigInputException("inntektBPBMGrunnlag kan ikke være null")
    if (beregnBPBidragsevneGrunnlag != null) beregnBPBidragsevneGrunnlag!! else throw UgyldigInputException(
        "beregnBPBidragsevneGrunnlag kan ikke være null")
    if (beregnBPAndelSaertilskuddGrunnlag != null) beregnBPAndelSaertilskuddGrunnlag!! else throw UgyldigInputException(
        "beregnBPAndelSaertilskuddGrunnlag kan ikke være null")
    if (beregnBPSamvaersfradragGrunnlag != null) beregnBPSamvaersfradragGrunnlag!! else throw UgyldigInputException(
        "beregnBPSamvaersfradragGrunnlag kan ikke være null")
    if (beregnSaertilskuddGrunnlag != null) beregnSaertilskuddGrunnlag!! else throw UgyldigInputException(
        "beregnSaertilskuddGrunnlag kan ikke være null")
  }

  fun validerInntekt() {
    if (inntektBPBMGrunnlag!!.inntektBPPeriodeListe != null)
      inntektBPBMGrunnlag!!.inntektBPPeriodeListe!!.map { it.validerInntekt("BP") }
    else throw UgyldigInputException("inntektBPPeriodeListe kan ikke være null")

    if (inntektBPBMGrunnlag!!.inntektBMPeriodeListe != null)
      inntektBPBMGrunnlag!!.inntektBMPeriodeListe!!.map { it.validerInntekt() }
    else throw UgyldigInputException("inntektBMPeriodeListe kan ikke være null")
  }

  fun bidragsevneTilCore(sjablonPeriodeListe: List<SjablonPeriodeCore>) = BeregnBidragsevneGrunnlagCore(
      beregnDatoFra = beregnDatoFra!!,
      beregnDatoTil = beregnDatoTil!!,

      inntektPeriodeListe =
      if (inntektBPBMGrunnlag!!.inntektBPPeriodeListe != null)
        inntektBPBMGrunnlag!!.inntektBPPeriodeListe!!.map { it.tilCoreBidragsevne() }
      else throw UgyldigInputException("inntektBPPeriodeListe kan ikke være null"),

      skatteklassePeriodeListe =
      if (beregnBPBidragsevneGrunnlag!!.skatteklassePeriodeListe != null)
        beregnBPBidragsevneGrunnlag!!.skatteklassePeriodeListe!!.map { it.tilCore() }
      else throw UgyldigInputException("skatteklassePeriodeListe kan ikke være null"),

      bostatusPeriodeListe =
      if (beregnBPBidragsevneGrunnlag!!.bostatusPeriodeListe != null)
        beregnBPBidragsevneGrunnlag!!.bostatusPeriodeListe!!.map { it.tilCore() }
      else throw UgyldigInputException("bostatusPeriodeListe kan ikke være null"),

      antallBarnIEgetHusholdPeriodeListe =
      if (beregnBPBidragsevneGrunnlag!!.antallBarnIEgetHusholdPeriodeListe != null)
        beregnBPBidragsevneGrunnlag!!.antallBarnIEgetHusholdPeriodeListe!!.map { it.tilCore() }
      else throw UgyldigInputException("antallBarnIEgetHusholdPeriodeListe kan ikke være null"),

      saerfradragPeriodeListe =
      if (beregnBPBidragsevneGrunnlag!!.saerfradragPeriodeListe != null)
        beregnBPBidragsevneGrunnlag!!.saerfradragPeriodeListe!!.map { it.tilCore() }
      else throw UgyldigInputException("saerfradragPeriodeListe kan ikke være null"),

      sjablonPeriodeListe = sjablonPeriodeListe
  )

  fun bpAndelSaertilskuddTilCore(sjablonPeriodeListe: List<SjablonPeriodeCore>) = BeregnBPsAndelSaertilskuddGrunnlagCore(
      beregnDatoFra = beregnDatoFra!!,
      beregnDatoTil = beregnDatoTil!!,

      nettoSaertilskuddPeriodeListe =
      if (beregnBPAndelSaertilskuddGrunnlag!!.nettoSaertilskuddPeriodeListe != null)
        beregnBPAndelSaertilskuddGrunnlag!!.nettoSaertilskuddPeriodeListe!!.map { it.tilCore() }
      else throw UgyldigInputException("nettoSaertilskuddPeriodeListe kan ikke være null"),

      inntektBPPeriodeListe =
      if (inntektBPBMGrunnlag!!.inntektBPPeriodeListe != null)
        inntektBPBMGrunnlag!!.inntektBPPeriodeListe!!.map { it.tilCoreBPAndelSaertilskudd("BP") }
      else throw UgyldigInputException("inntektBPPeriodeListe kan ikke være null"),

      inntektBMPeriodeListe =
      if (inntektBPBMGrunnlag!!.inntektBMPeriodeListe != null)
        inntektBPBMGrunnlag!!.inntektBMPeriodeListe!!.map { it.tilCore() }
      else throw UgyldigInputException("inntektBMPeriodeListe kan ikke være null"),

      inntektBBPeriodeListe =
      if (soknadsbarnGrunnlag!!.inntektPeriodeListe != null)
        soknadsbarnGrunnlag!!.inntektPeriodeListe!!.map { it.tilCoreBPAndelSaertilskudd("SB") }
      else throw UgyldigInputException("inntektSBPeriodeListe kan ikke være null"),

      sjablonPeriodeListe = sjablonPeriodeListe
  )

  fun samvaersfradragTilCore(sjablonPeriodeListe: List<SjablonPeriodeCore>) = BeregnSamvaersfradragGrunnlagCore(
      beregnDatoFra = beregnDatoFra!!,
      beregnDatoTil = beregnDatoTil!!,

      samvaersklassePeriodeListe =
      if (beregnBPSamvaersfradragGrunnlag!!.samvaersklassePeriodeListe != null)
        beregnBPSamvaersfradragGrunnlag!!.samvaersklassePeriodeListe!!.map { it.tilCore() }
      else throw UgyldigInputException("samvaersklassePeriodeListe kan ikke være null"),

      sjablonPeriodeListe = sjablonPeriodeListe
  )

  fun saertilskuddTilCore(soknadsbarnPersonId: Int, bidragsevnePeriodeListe: List<BidragsevnePeriodeCore>,
      bpAndelSaertilskuddPeriodeListe: List<BPsAndelSaertilskuddPeriodeCore>, samvaersfradragPeriodeListe: List<SamvaersfradragPeriodeCore>) =
      BeregnSaertilskuddGrunnlagCore(

      beregnDatoFra = beregnDatoFra!!,
      beregnDatoTil = beregnDatoTil!!,
      soknadsbarnPersonId = soknadsbarnPersonId,

      bidragsevnePeriodeListe = bidragsevnePeriodeListe,
      bPsAndelSaertilskuddPeriodeListe = bpAndelSaertilskuddPeriodeListe,
      samvaersfradragPeriodeListe = samvaersfradragPeriodeListe,

      lopendeBidragPeriodeListe =
      if (beregnSaertilskuddGrunnlag!!.lopendeBidragBPPeriodeListe != null)
        beregnSaertilskuddGrunnlag!!.lopendeBidragBPPeriodeListe!!.map { it.tilCore() }
      else throw UgyldigInputException("lopendeBidragBPPeriodeListe kan ikke være null")
  )
}

// Resultat
@ApiModel(value = "Totalresultatet av en særtilskuddsberegning")
data class BeregnTotalSaertilskuddResultat(
    @ApiModelProperty(value = "Beregn BP bidragsevne resultat") var beregnBPBidragsevneResultat: BeregnBPBidragsevneResultat,
    @ApiModelProperty(value = "Beregn BP andel av særtilskudd resultat") var beregnBPAndelSaertilskuddResultat: BeregnBPAndelSaertilskuddResultat,
    @ApiModelProperty(value = "Beregn BP samværsfradrag resultat") var beregnBPSamvaersfradragResultat: BeregnBPSamvaersfradragResultat,
    @ApiModelProperty(value = "Beregn særtilskudd resultat") var beregnSaertilskuddResultat: BeregnSaertilskuddResultat
)
