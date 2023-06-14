package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class GrunnlagType(@get:JsonValue val value: String) {
  SAERFRADRAG("SAERFRADRAG"),
  SOKNADSBARN_INFO("SOKNADSBARN_INFO"),
  SKATTEKLASSE("SKATTEKLASSE"),
  BARN_I_HUSSTAND("BARN_I_HUSSTAND"),
  BOSTATUS("BOSTATUS"),
  INNTEKT("INNTEKT"),
  NETTO_SAERTILSKUDD("NETTO_SAERTILSKUDD"),
  SAMVAERSKLASSE("SAMVAERSKLASSE"),
  BIDRAGSEVNE("BIDRAGSEVNE"),
  BP_ANDEL_SAERTILSKUDD("BP_ANDEL_SAERTILSKUDD"),
  SAMVAERSFRADRAG("SAMVAERSFRADRAG"),
  SJABLON("SJABLON"),
  LOPENDE_BIDRAG("LOPENDE_BIDRAG");

  companion object {

    @JsonCreator
    fun fromString(value: String): GrunnlagType? {
      return values().find { grunnlagType -> grunnlagType.name.equals(value, true) }
    }
  }
}
