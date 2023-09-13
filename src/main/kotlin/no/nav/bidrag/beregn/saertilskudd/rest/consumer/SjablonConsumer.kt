package no.nav.bidrag.beregn.saertilskudd.rest.consumer

import no.nav.bidrag.beregn.saertilskudd.rest.exception.SjablonConsumerException
import no.nav.bidrag.commons.web.HttpResponse
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate

private const val SJABLONSJABLONTALL_URL = "/bidrag-sjablon/sjablontall/all"
private const val SJABLONSAMVAERSFRADRAG_URL = "/bidrag-sjablon/samvaersfradrag/all"
private const val SJABLONBIDRAGSEVNE_URL = "/bidrag-sjablon/bidragsevner/all"
private const val SJABLONTRINNVISSKATTESATS_URL = "/bidrag-sjablon/trinnvisskattesats/all"

class SjablonConsumer(private val restTemplate: RestTemplate) {

    fun hentSjablonSjablontall(): HttpResponse<List<Sjablontall>?> {
        return try {
            val sjablonResponse = restTemplate.exchange(
                SJABLONSJABLONTALL_URL,
                HttpMethod.GET,
                null,
                SJABLON_SJABLONTALL_LISTE)

            LOGGER.info("hentSjablonSjablontall fikk http status {} fra bidrag-sjablon", sjablonResponse.statusCode)
            HttpResponse(sjablonResponse)
        } catch (exception: RestClientResponseException) {
            LOGGER.error(
                "hentSjablonSjablontall fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.statusText,
                exception.message
            )
            throw SjablonConsumerException(exception)
        }
    }

    fun hentSjablonSamvaersfradrag(): HttpResponse<List<Samvaersfradrag>?> {
        return try {
            val sjablonResponse = restTemplate.exchange(
                SJABLONSAMVAERSFRADRAG_URL,
                HttpMethod.GET,
                null,
                SJABLON_SAMVAERSFRADRAG_LISTE)

            LOGGER.info("hentSjablonSamvaersfradrag fikk http status {} fra bidrag-sjablon", sjablonResponse.statusCode)
            HttpResponse(sjablonResponse)
        } catch (exception: RestClientResponseException) {
            LOGGER.error(
                "hentSjablonSamvaersfradrag fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.statusText,
                exception.message
            )
            throw SjablonConsumerException(exception)
        }
    }

    fun hentSjablonBidragsevne(): HttpResponse<List<Bidragsevne>?> {
        return try {
            val sjablonResponse = restTemplate.exchange(
                SJABLONBIDRAGSEVNE_URL,
                HttpMethod.GET,
                null,
                SJABLON_BIDRAGSEVNE_LISTE)

            LOGGER.info("hentSjablonBidragsevne fikk http status {} fra bidrag-sjablon", sjablonResponse.statusCode)
            HttpResponse(sjablonResponse)
        } catch (exception: RestClientResponseException) {
            LOGGER.error(
                "hentSjablonBidragsevne fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.statusText,
                exception.message
            )
            throw SjablonConsumerException(exception)
        }
    }

    fun hentSjablonTrinnvisSkattesats(): HttpResponse<List<TrinnvisSkattesats>?> {
        return try {
            val sjablonResponse = restTemplate.exchange(
                SJABLONTRINNVISSKATTESATS_URL,
                HttpMethod.GET,
                null,
                SJABLON_TRINNVIS_SKATTESATS_LISTE)

            LOGGER.info("hentSjablonTrinnvisSkattesats fikk http status {} fra bidrag-sjablon", sjablonResponse.statusCode)
            HttpResponse(sjablonResponse)
        } catch (exception: RestClientResponseException) {
            LOGGER.error(
                "hentSjablonTrinnvisSkattesats fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.statusText,
                exception.message
            )
            throw SjablonConsumerException(exception)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SjablonConsumer::class.java)
        private val SJABLON_SJABLONTALL_LISTE = object : ParameterizedTypeReference<List<Sjablontall>>() {}
        private val SJABLON_SAMVAERSFRADRAG_LISTE = object : ParameterizedTypeReference<List<Samvaersfradrag>>() {}
        private val SJABLON_BIDRAGSEVNE_LISTE = object : ParameterizedTypeReference<List<Bidragsevne>>() {}
        private val SJABLON_TRINNVIS_SKATTESATS_LISTE = object : ParameterizedTypeReference<List<TrinnvisSkattesats>>() {}
    }
}
