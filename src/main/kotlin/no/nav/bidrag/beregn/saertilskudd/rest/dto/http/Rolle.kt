package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class Rolle(@get:JsonValue val value: String) {
    BP("BP"),
    BM("BM"),
    SB("SB");

    companion object {

        @JsonCreator
        fun fromString(value: String): Rolle? {
            for (rolle in Rolle.values()) {
                if (rolle.name.equals(value, true)) {
                    return rolle;
                }
            }
            return null
        }
    }
}