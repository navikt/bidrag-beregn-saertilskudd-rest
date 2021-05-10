package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.time.LocalDate

// Grunnlag
@Schema(description = "Grunnlagsdata for søknadsbarn")
data class SoknadsbarnGrunnlag(
//TODO Er egentlig personId og fodselsdato for søknadsbarnet nødvendige?
    @Schema(description = "Søknadsbarnets person-id") var soknadsbarnPersonId: Int? = null,
    @Schema(description = "Søknadsbarnets fødselsdato") var soknadsbarnFodselsdato: LocalDate? = null,
    @Schema(description = "Periodisert liste over søknadsbarnets inntekter") var inntektPeriodeListe: List<InntektPeriode>? = null
) {

    fun validerSoknadsbarn() {
        if (soknadsbarnPersonId == null) throw UgyldigInputException("soknadsbarnPersonId kan ikke være null")
        if (soknadsbarnFodselsdato == null) throw UgyldigInputException("soknadsbarnFodselsdato kan ikke være null")
        if (inntektPeriodeListe != null) inntektPeriodeListe!!.map { it.validerInntekt("SB") } else throw UgyldigInputException(
            "SB inntektPeriodeListe kan ikke være null"
        )
    }
}
