package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.time.LocalDate

// Grunnlag
@ApiModel(value = "Grunnlagsdata for søknadsbarn")
data class SoknadsbarnGrunnlag(
    @ApiModelProperty(value = "Søknadsbarnets fødselsdato") var soknadsbarnFodselsdato: LocalDate? = null,
    @ApiModelProperty(value = "Periodisert liste over søknadsbarnets inntekter") var inntektPeriodeListe: List<InntektPeriode>? = null
) {

  fun validerSoknadsbarn() {
    if (soknadsbarnFodselsdato == null) throw UgyldigInputException("soknadsbarnFodselsdato kan ikke være null")
    if (inntektPeriodeListe != null) inntektPeriodeListe!!.map { it.validerInntekt("SB") } else throw UgyldigInputException(
        "SB inntektPeriodeListe kan ikke være null")
  }
}
