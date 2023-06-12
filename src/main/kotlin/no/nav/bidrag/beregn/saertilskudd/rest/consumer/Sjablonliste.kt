package no.nav.bidrag.beregn.saertilskudd.rest.consumer

data class SjablonListe(
    var sjablonSjablontallResponse: List<Sjablontall> = emptyList(),
    var sjablonSamvaersfradragResponse: List<Samvaersfradrag> = emptyList(),
    var sjablonBidragsevneResponse: List<Bidragsevne> = emptyList(),
    var sjablonTrinnvisSkattesatsResponse: List<TrinnvisSkattesats> = emptyList()
)
