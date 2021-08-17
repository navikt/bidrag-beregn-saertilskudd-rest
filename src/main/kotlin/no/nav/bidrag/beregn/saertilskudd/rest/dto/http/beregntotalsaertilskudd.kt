package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneGrunnlagCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevnePeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore
import java.math.BigDecimal
import java.time.LocalDate


// Grunnlag
@Schema(description = "Totalgrunnlaget for en særtilskuddsberegning")
data class BeregnTotalSaertilskuddGrunnlagOld(
        @Schema(description = "Beregn fra-dato") var beregnDatoFra: LocalDate? = null,
        @Schema(description = "Beregn til-dato") var beregnDatoTil: LocalDate? = null,
        @Schema(description = "Søknadsbarn grunnlag") var soknadsbarnGrunnlag: SoknadsbarnGrunnlag? = null,
        @Schema(description = "Inntekt BP/BM grunnlag") var inntektBPBMGrunnlag: InntektBPBMGrunnlag? = null,
        @Schema(description = "Beregn BP bidragsevne grunnlag") var beregnBPBidragsevneGrunnlag: BeregnBPBidragsevneGrunnlag? = null,
        @Schema(description = "Beregn BP andel særtilskudd grunnlag") var beregnBPAndelSaertilskuddGrunnlag: BeregnBPAndelSaertilskuddGrunnlag? = null,
        @Schema(description = "Beregn BP samværsfradrag grunnlag") var beregnBPSamvaersfradragGrunnlag: BeregnBPSamvaersfradragGrunnlag? = null,
        @Schema(description = "Beregn særtilskudd grunnlag") var beregnSaertilskuddGrunnlag: BeregnSaertilskuddGrunnlag? = null
) {

    fun validerTotalSaertilskuddGrunnlag() {
        if (beregnDatoFra != null) beregnDatoFra!! else throw UgyldigInputException("beregnDatoFra kan ikke være null")
        if (beregnDatoTil != null) beregnDatoTil!! else throw UgyldigInputException("beregnDatoTil kan ikke være null")
        if (soknadsbarnGrunnlag != null) soknadsbarnGrunnlag!! else throw UgyldigInputException("soknadsbarnGrunnlag kan ikke være null")
        if (inntektBPBMGrunnlag != null) inntektBPBMGrunnlag!! else throw UgyldigInputException("inntektBPBMGrunnlag kan ikke være null")
        if (beregnBPBidragsevneGrunnlag != null) beregnBPBidragsevneGrunnlag!! else throw UgyldigInputException(
                "beregnBPBidragsevneGrunnlag kan ikke være null"
        )
        if (beregnBPAndelSaertilskuddGrunnlag != null) beregnBPAndelSaertilskuddGrunnlag!! else throw UgyldigInputException(
                "beregnBPAndelSaertilskuddGrunnlag kan ikke være null"
        )
        if (beregnBPSamvaersfradragGrunnlag != null) beregnBPSamvaersfradragGrunnlag!! else throw UgyldigInputException(
                "beregnBPSamvaersfradragGrunnlag kan ikke være null"
        )
        if (beregnSaertilskuddGrunnlag != null) beregnSaertilskuddGrunnlag!! else throw UgyldigInputException(
                "beregnSaertilskuddGrunnlag kan ikke være null"
        )
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

    fun saertilskuddTilCore(
            soknadsbarnPersonId: Int, bidragsevnePeriodeListe: List<BidragsevnePeriodeCore>,
            bpAndelSaertilskuddPeriodeListe: List<BPsAndelSaertilskuddPeriodeCore>, samvaersfradragPeriodeListe: List<SamvaersfradragPeriodeCore>
    ) =
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

// Grunnlag
@Schema(description = "Totalgrunnlaget for en særtilskuddsberegning")
data class BeregnTotalSaertilskuddGrunnlag(
        @Schema(description = "Beregn fra-dato") val beregnDatoFra: LocalDate? = null,
        @Schema(description = "Beregn til-dato") val beregnDatoTil: LocalDate? = null,
        @Schema(description = "Periodisert liste over grunnlagselementer") val grunnlagListe: List<Grunnlag>? = null,
) {

    fun valider() {
        if (beregnDatoFra == null) throw UgyldigInputException("beregnDatoFra kan ikke være null")
        if (beregnDatoTil == null) throw UgyldigInputException("beregnDatoTil kan ikke være null")
        if (grunnlagListe != null) grunnlagListe.map { it.valider() } else throw UgyldigInputException("grunnlagListe kan ikke være null")
    }
}

@Schema(description = "Grunnlag")
data class Grunnlag(
        @Schema(description = "Referanse") val referanse: String? = null,
        @Schema(description = "Type") val type: String? = null,
        @Schema(description = "Innhold") val innhold: JsonNode? = null
) {
    fun valider() {
        if (referanse == null) throw UgyldigInputException("referanse kan ikke være null")
        if (type == null) throw UgyldigInputException("type kan ikke være null")
        if (innhold == null) throw UgyldigInputException("innhold kan ikke være null")
    }
}

// Resultat
@Schema(description = "Totalresultatet av en særtilskuddsberegning")
data class BeregnetTotalSaertilskuddResultat(
        @Schema(description = "Periodisert liste over resultat av særtilskuddsberegning") var beregnetSaertilskuddPeriodeListe: List<ResultatPeriode> = emptyList(),
        @Schema(description = "Liste over grunnlag brukt i beregning") var grunnlagListe: List<ResultatGrunnlag> = emptyList()
) {
    constructor(beregnetSaertilskuddResultat: BeregnSaertilskuddResultatCore, grunnlagListe: List<ResultatGrunnlag>) : this(
            beregnetSaertilskuddPeriodeListe = beregnetSaertilskuddResultat.resultatPeriodeListe.map { ResultatPeriode(it) },
            grunnlagListe = grunnlagListe)
}

@Schema(description = "Resultatet av en beregning for en gitt periode")
data class ResultatPeriode(
        @Schema(description = "Beregnet resultat periode") var periode: Periode = Periode(),
        @Schema(description = "Beregnet resultat innhold") var resultat: ResultatBeregning = ResultatBeregning(),
        @Schema(description = "Beregnet grunnlag innhold") var grunnlagReferanseListe: List<String> = emptyList()
) {
    constructor(resultatPeriode: ResultatPeriodeCore) : this(
            periode = Periode(resultatPeriode.periode),
            resultat = ResultatBeregning(resultatPeriode.resultatListe),
            grunnlagReferanseListe = resultatPeriode.grunnlagReferanseListe
    )
}

@Schema(description = "Resultatet av en beregning")
data class ResultatBeregning(
        @Schema(description = "Resultat beløp") var belop: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "Resultat kode") var kode: String = ""
) {

    constructor(resultatBeregning: ResultatBeregningCore) : this(
            belop = resultatBeregning.belop,
            kode = resultatBeregning.kode
    )
}

@Schema(description = "Grunnlaget for en beregning")
data class ResultatGrunnlag(
        @Schema(description = "Referanse") val referanse: String = "",
        @Schema(description = "Type") val type: String = "",
        @Schema(description = "Innhold") val innhold: JsonNode = ObjectMapper().createObjectNode()
)

@Schema(description = "Resultatet av en total barnebidragsberegning")
data class BeregnTotalSaertilskuddResultat(
        @Schema() var beregnBPBidragsevneResultat: BeregnBPBidragsevneResultat,
        @Schema() var beregnBPAndelSaertilskuddResultat: BeregnBPAndelSaertilskuddResultat,
        @Schema() var beregnBPSamvaersfradragResultat: BeregnBPSamvaersfradragResultat,
        @Schema var beregnSaertilskuddResultat: BeregnSaertilskuddResultat
)

