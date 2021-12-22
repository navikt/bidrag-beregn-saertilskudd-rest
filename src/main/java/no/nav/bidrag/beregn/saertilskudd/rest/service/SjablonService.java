package no.nav.bidrag.beregn.saertilskudd.rest.service;

import java.util.List;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.BidragGcpProxyConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Samvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.TrinnvisSkattesats;
import no.nav.bidrag.commons.web.HttpResponse;
import org.springframework.core.ParameterizedTypeReference;

public class SjablonService {

  private static final ParameterizedTypeReference<List<Sjablontall>> SJABLON_SJABLONTALL_LISTE = new ParameterizedTypeReference<>() {
  };
  private static final ParameterizedTypeReference<List<Samvaersfradrag>> SJABLON_SAMVAERSFRADRAG_LISTE = new ParameterizedTypeReference<>() {
  };
  private static final ParameterizedTypeReference<List<Bidragsevne>> SJABLON_BIDRAGSEVNE_LISTE = new ParameterizedTypeReference<>() {
  };
  private static final ParameterizedTypeReference<List<TrinnvisSkattesats>> SJABLON_TRINNVIS_SKATTESATS_LISTE = new ParameterizedTypeReference<>() {
  };

  private final BidragGcpProxyConsumer bidragGcpProxyConsumer;


  public SjablonService(BidragGcpProxyConsumer bidragGcpProxyConsumer) {
    this.bidragGcpProxyConsumer = bidragGcpProxyConsumer;
  }

  public HttpResponse<List<Sjablontall>> hentSjablonSjablontall() {
    String sjablonSjablontallUrl = "/sjablon/sjablontall?all=true";
    return bidragGcpProxyConsumer.hentSjablonListe(sjablonSjablontallUrl, SJABLON_SJABLONTALL_LISTE);
  }

  public HttpResponse<List<Samvaersfradrag>> hentSjablonSamvaersfradrag() {
    String sjablonSamvaersfradragUrl = "/sjablon/samvaersfradrag?all=true";
    return bidragGcpProxyConsumer.hentSjablonListe(sjablonSamvaersfradragUrl, SJABLON_SAMVAERSFRADRAG_LISTE);
  }

  public HttpResponse<List<Bidragsevne>> hentSjablonBidragsevne() {
    String sjablonBidragsevneUrl = "/sjablon/bidragsevner?all=true";
    return bidragGcpProxyConsumer.hentSjablonListe(sjablonBidragsevneUrl, SJABLON_BIDRAGSEVNE_LISTE);
  }

  public HttpResponse<List<TrinnvisSkattesats>> hentSjablonTrinnvisSkattesats() {
    String sjablonTrinnvisSkattesatsUrl = "/sjablon/trinnvisskattesats?all=true";
    return bidragGcpProxyConsumer.hentSjablonListe(sjablonTrinnvisSkattesatsUrl, SJABLON_TRINNVIS_SKATTESATS_LISTE);
  }
}
