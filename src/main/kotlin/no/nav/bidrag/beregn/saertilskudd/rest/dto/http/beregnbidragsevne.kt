package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.bidragsevne.dto.AntallBarnIEgetHusholdPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneResultatCore
import no.nav.bidrag.beregn.bidragsevne.dto.BostatusPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.bidragsevne.dto.ResultatGrunnlagCore
import no.nav.bidrag.beregn.bidragsevne.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.SaerfradragPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.SkatteklassePeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.math.BigDecimal

// Grunnlag
@Schema(description = "Grunnlaget for en bidragsevnesberegning for bidragspliktig")
data class BeregnBPBidragsevneGrunnlag(
    @Schema(description = "Periodisert liste over bidragspliktiges skatteklasse") val skatteklassePeriodeListe: List<SkatteklassePeriode>? = null,
    @Schema(description = "Periodisert liste over bidragspliktiges bostatus") val bostatusPeriodeListe: List<BostatusPeriode>? = null,
    @Schema(description = "Periodisert liste over antall barn i bidragspliktiges hushold") val antallBarnIEgetHusholdPeriodeListe: List<AntallBarnIEgetHusholdPeriode>? = null,
    @Schema(description = "Periodisert liste over bidragspliktiges særfradrag") val saerfradragPeriodeListe: List<SaerfradragPeriode>? = null
)

@Schema(description = "Bidragspliktiges skatteklasse")
data class SkatteklassePeriode(
    @Schema(description = "Bidragspliktiges skatteklasse fra-til-dato") var skatteklasseDatoFraTil: Periode? = null,
    @Schema(description = "Bidragspliktiges skatteklasse") var skatteklasseId: Int? = null
) {

    fun tilCore() = SkatteklassePeriodeCore(
        periodeDatoFraTil = if (skatteklasseDatoFraTil != null) skatteklasseDatoFraTil!!.tilCore(
            "skatteklasse"
        ) else throw UgyldigInputException("skatteklasseDatoFraTil kan ikke være null"),
        skatteklasse = if (skatteklasseId != null) skatteklasseId!! else throw UgyldigInputException("skatteklasseId kan ikke være null")
    )
}

@Schema(description = "Bidragspliktiges bostatus")
data class BostatusPeriode(
    @Schema(description = "Bidragspliktiges bostatus fra-til-dato") var bostatusDatoFraTil: Periode? = null,
    @Schema(description = "Bidragspliktiges bostatuskode") var bostatusKode: String? = null
) {

    fun tilCore() = BostatusPeriodeCore(
        periodeDatoFraTil = if (bostatusDatoFraTil != null) bostatusDatoFraTil!!.tilCore(
            "bostatus"
        ) else throw UgyldigInputException("bostatusDatoFraTil kan ikke være null"),
        bostatusKode = if (bostatusKode != null) bostatusKode!! else throw UgyldigInputException("bostatusKode kan ikke være null")
    )
}

@Schema(description = "Antall barn i bidragspliktiges hushold")
data class AntallBarnIEgetHusholdPeriode(
    @Schema(description = "Antall barn i bidragspliktiges hushold fra-til-dato") var antallBarnIEgetHusholdDatoFraTil: Periode? = null,
    @Schema(description = "Antall barn i bidragspliktiges husholde") var antallBarn: BigDecimal? = null
) {

    fun tilCore() = AntallBarnIEgetHusholdPeriodeCore(
        periodeDatoFraTil = if (antallBarnIEgetHusholdDatoFraTil != null) antallBarnIEgetHusholdDatoFraTil!!.tilCore(
            "antallBarnIEgetHushold"
        ) else throw UgyldigInputException("antallBarnIEgetHusholdDatoFraTil kan ikke være null"),
        antallBarn = if (antallBarn != null) antallBarn!! else throw UgyldigInputException("antallBarn kan ikke være null")
    )
}

@Schema(description = "Bidragspliktiges særfradrag")
data class SaerfradragPeriode(
    @Schema(description = "Bidragspliktiges særfradrag fra-til-dato") var saerfradragDatoFraTil: Periode? = null,
    @Schema(description = "Bidragspliktiges særfradrag kode") var saerfradragKode: String? = null
) {

    fun tilCore() = SaerfradragPeriodeCore(
        periodeDatoFraTil = saerfradragDatoFraTil?.tilCore("saerfradrag") ?: throw UgyldigInputException("saerfradragDatoFraTil kan ikke være null"),
        saerfradragKode = saerfradragKode ?: throw UgyldigInputException("saerfradragKode kan ikke være null")
    )
}

// Resultat
@Schema(description = "Resultatet av en bidragsevnesberegning for bidragspliktig")
data class BeregnBPBidragsevneResultat(
    @Schema(description = "Periodisert liste over resultat av bidragsevnesberegning") var resultatPeriodeListe: List<ResultatPeriodeBidragsevne> = emptyList()
) {

    constructor(beregnBidragsevneResultat: BeregnBidragsevneResultatCore) : this(
        resultatPeriodeListe = beregnBidragsevneResultat.resultatPeriodeListe.map { ResultatPeriodeBidragsevne(it) }
    )
}

@Schema(description = "Resultatet av en beregning for en gitt periode")
data class ResultatPeriodeBidragsevne(
    @Schema(description = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode = Periode(),
    @Schema(description = "Beregning resultat innhold") var resultatBeregning: ResultatBeregningBidragsevne = ResultatBeregningBidragsevne(),
    @Schema(description = "Beregning grunnlag innhold") var resultatGrunnlag: ResultatGrunnlagBidragsevne = ResultatGrunnlagBidragsevne()
) {

    constructor(resultatPeriode: ResultatPeriodeCore) : this(
        resultatDatoFraTil = Periode(resultatPeriode.resultatDatoFraTil),
        resultatBeregning = ResultatBeregningBidragsevne(resultatPeriode.resultatBeregning),
        resultatGrunnlag = ResultatGrunnlagBidragsevne(resultatPeriode.resultatGrunnlag)
    )
}

@Schema(description = "Resultatet av en beregning")
data class ResultatBeregningBidragsevne(
    @Schema(description = "Resultatevne beløp") var resultatEvneBelop: BigDecimal = BigDecimal.ZERO
) {

    constructor(resultatBeregning: ResultatBeregningCore) : this(
        resultatEvneBelop = resultatBeregning.resultatEvneBelop
    )
}

@Schema(description = "Grunnlaget for en beregning")
data class ResultatGrunnlagBidragsevne(
    @Schema(description = "Liste over bidragspliktiges inntekter") var inntektListe: List<Inntekt> = emptyList(),
    @Schema(description = "Bidragspliktiges skatteklasse") var skatteklasse: Int = 0,
    @Schema(description = "Bidragspliktiges bostatuskode") var bostatusKode: String = "",
    @Schema(description = "Antall egne barn i bidragspliktiges husstand") var antallEgneBarnIHusstand: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Bidragspliktiges særfradragkode") var saerfradragKode: String = "",
    @Schema(description = "Liste over sjablonperioder") var sjablonListe: List<Sjablon> = emptyList()
) {

    constructor(resultatGrunnlag: ResultatGrunnlagCore) : this(
        inntektListe = resultatGrunnlag.inntektListe.map { Inntekt(it) },
        skatteklasse = resultatGrunnlag.skatteklasse,
        bostatusKode = resultatGrunnlag.bostatusKode,
        antallEgneBarnIHusstand = resultatGrunnlag.antallEgneBarnIHusstand,
        saerfradragKode = resultatGrunnlag.saerfradragkode,
        sjablonListe = resultatGrunnlag.sjablonListe.map { Sjablon(it) }
    )
}
