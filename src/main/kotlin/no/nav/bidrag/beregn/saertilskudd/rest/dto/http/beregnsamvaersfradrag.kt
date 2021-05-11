package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.GrunnlagBeregningPeriodisertCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersfradragGrunnlagPerBarnCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersklassePeriodeCore
import java.math.BigDecimal
import java.time.LocalDate

// Grunnlag
@Schema(description = "Grunnlaget for en samværsfradragberegning for bidragspliktig")
data class BeregnBPSamvaersfradragGrunnlag(
    @Schema(description = "Periodisert liste over bidragspliktiges samværsklasser") val samvaersklassePeriodeListe: List<SamvaersklassePeriode>? = null
)

@Schema(description = "Bidragspliktiges samværsklasse")
data class SamvaersklassePeriode(
    @Schema(description = "Bidragspliktiges samværsklasse fra-til-dato") var samvaersklasseDatoFraTil: Periode? = null,
    @Schema(description = "Barn person-id") var samvaersklasseBarnPersonId: Int? = null,
    @Schema(description = "Barn fødselsdato") var samvaersklasseBarnFodselsdato: LocalDate? = null,
    @Schema(description = "Bidragspliktiges samværsklasse id") var samvaersklasseId: String? = null
) {

    fun tilCore() = SamvaersklassePeriodeCore(
        samvaersklassePeriodeDatoFraTil = if (samvaersklasseDatoFraTil != null) samvaersklasseDatoFraTil!!.tilCore(
            "samvaersklasse"
        ) else throw UgyldigInputException("samvaersklasseDatoFraTil kan ikke være null"),
        barnPersonId = if (samvaersklasseBarnPersonId != null) samvaersklasseBarnPersonId!! else throw UgyldigInputException(
            "samvaersklasseBarnPersonId kan ikke være null"
        ),
        barnFodselsdato = if (samvaersklasseBarnFodselsdato != null) samvaersklasseBarnFodselsdato!! else throw UgyldigInputException(
            "samvaersklasseBarnFodselsdato kan ikke være null"
        ),
        samvaersklasse = if (samvaersklasseId != null) samvaersklasseId!! else throw UgyldigInputException("samvaersklasseId kan ikke være null")
    )
}


// Resultat
@Schema(description = "Resultatet av en samværsfradragberegning for bidragspliktig")
data class BeregnBPSamvaersfradragResultat(
    @Schema(description = "Periodisert liste over resultat av beregning av samværsfradrag") var resultatPeriodeListe: List<ResultatPeriodeSamvaersfradrag> = emptyList()
) {

    constructor(beregnSamvaersfradragResultat: BeregnSamvaersfradragResultatCore) : this(
        resultatPeriodeListe = beregnSamvaersfradragResultat.resultatPeriodeListe.map { ResultatPeriodeSamvaersfradrag(it) }
    )
}

@Schema(description = "Resultatet av beregning av samværsfradrag for et søknadsbarn for en gitt periode")
data class ResultatPeriodeSamvaersfradrag(
    @Schema(description = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode = Periode(),
    @Schema(description = "Beregning resultat innhold liste") var resultatBeregningListe: List<ResultatBeregningSamvaersfradrag> = emptyList(),
    @Schema(description = "Beregning grunnlag innhold") var resultatGrunnlag: ResultatGrunnlagSamvaersfradrag = ResultatGrunnlagSamvaersfradrag()
) {

    constructor(resultatPeriode: ResultatPeriodeCore) : this(
        resultatDatoFraTil = Periode(resultatPeriode.resultatDatoFraTil),
        resultatBeregningListe = resultatPeriode.resultatBeregningListe.map { ResultatBeregningSamvaersfradrag(it) },
        resultatGrunnlag = ResultatGrunnlagSamvaersfradrag(resultatPeriode.resultatGrunnlag)
    )
}

@Schema(description = "Resultatet av beregning av samværsfradrag")
data class ResultatBeregningSamvaersfradrag(
    @Schema(description = "Barn person-id") var barnPersonId: Int? = null,
    @Schema(description = "Beløp samværsfradrag") var resultatBelop: BigDecimal = BigDecimal.ZERO
) {

    constructor(resultatBeregning: ResultatBeregningCore) : this(
        barnPersonId = resultatBeregning.barnPersonId,
        resultatBelop = resultatBeregning.resultatSamvaersfradragBelop
    )
}

@Schema(description = "Grunnlaget for beregning av samværsfradrag")
data class ResultatGrunnlagSamvaersfradrag(
    @Schema(description = "Liste over grunnlag pr barn") var grunnlagBarnListe: List<GrunnlagBarn> = emptyList(),
    @Schema(description = "Liste over sjablonperioder") var sjablonListe: List<Sjablon> = emptyList()
) {

    constructor(resultatGrunnlag: GrunnlagBeregningPeriodisertCore) : this(
        grunnlagBarnListe = resultatGrunnlag.samvaersfradragGrunnlagPerBarnListe.map { GrunnlagBarn(it) },
        sjablonListe = resultatGrunnlag.sjablonListe.map { Sjablon(it) }
    )
}

@Schema(description = "Grunnlaget for beregning av samværsfradrag - pr barn")
data class GrunnlagBarn(
    @Schema(description = "Barn person-id") var barnPersonId: Int = 0,
    @Schema(description = "Barn alder") var barnAlder: Int = 0,
    @Schema(description = "Samværsklasse Id") var samvaersklasseId: String = ""
) {

    constructor(resultatGrunnlagBarn: SamvaersfradragGrunnlagPerBarnCore) : this(
        barnPersonId = resultatGrunnlagBarn.barnPersonId,
        barnAlder = resultatGrunnlagBarn.barnAlder,
        samvaersklasseId = resultatGrunnlagBarn.samvaersklasse
    )
}
