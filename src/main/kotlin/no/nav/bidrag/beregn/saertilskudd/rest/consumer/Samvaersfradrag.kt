package no.nav.bidrag.beregn.saertilskudd.rest.consumer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class Samvaersfradrag(
    val samvaersklasse: String? = null,
    val alderTom: Int? = null,
    val datoFom: LocalDate? = null,
    val datoTom: LocalDate? = null,
    val antDagerTom: Int? = null,
    val antNetterTom: Int? = null,
    val belopFradrag: BigDecimal? = null
)
