package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class GrunnlagType(@get:JsonValue val value: String) {
    SAERFRADRAG("Saerfradrag"),
    SOKNADSBARN_INFO("SoknadsbarnInfo"),
    SKATTEKLASSE("Skatteklasse"),
    BARN_I_HUSSTAND("BarnIHusstand"),
    BOSTATUS("Bostatus"),
    INNTEKT("Inntekt"),
    NETTO_SAERTILSKUDD("NettoSaertilskudd"),
    SAMVAERSKLASSE("Samvaersklasse"),
    LOPENDE_BIDRAG("LopendeBidrag");

    companion object {

        @JsonCreator
        fun fromString(value: String): GrunnlagType? {
            return values().find { grunnlagType ->  grunnlagType.name.equals(value, true)}
        }
    }
}