package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.GrunnlagBeregningPeriodisertCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersfradragGrunnlagPerBarnCore
import java.math.BigDecimal

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
