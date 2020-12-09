package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.time.LocalDate

// Grunnlag
@ApiModel(value = "Grunnlagsdata for søknadsbarn")
data class SoknadsbarnGrunnlag(
//TODO Er egentlig personId og fodselsdato for søknadsbarnet nødvendige?
    @ApiModelProperty(value = "Søknadsbarnets person-id") var soknadsbarnPersonId: Int? = null,
    @ApiModelProperty(value = "Søknadsbarnets fødselsdato") var soknadsbarnFodselsdato: LocalDate? = null,
    @ApiModelProperty(value = "Periodisert liste over søknadsbarnets inntekter") var inntektPeriodeListe: List<InntektPeriode>? = null
) {

  fun validerSoknadsbarn() {
    if (soknadsbarnPersonId == null) throw UgyldigInputException("soknadsbarnPersonId kan ikke være null")
    if (soknadsbarnFodselsdato == null) throw UgyldigInputException("soknadsbarnFodselsdato kan ikke være null")
    if (inntektPeriodeListe != null) inntektPeriodeListe!!.map { it.validerInntekt("SB") } else throw UgyldigInputException(
        "SB inntektPeriodeListe kan ikke være null")
  }
}
