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
  private final String sjablonSjablontallUrl;
  private final String sjablonSamvaersfradragUrl;
  private final String sjablonBidragsevneUrl;
  private final String sjablonTrinnvisSkattesatsUrl;

  public SjablonService(BidragGcpProxyConsumer bidragGcpProxyConsumer) {
    this.bidragGcpProxyConsumer = bidragGcpProxyConsumer;
    this.sjablonSjablontallUrl = "/sjablontall?all=true";
    this.sjablonSamvaersfradragUrl = "/samvaersfradrag?all=true";
    this.sjablonBidragsevneUrl = "/bidragsevner?all=true";
    this.sjablonTrinnvisSkattesatsUrl = "/trinnvisskattesats?all=true";
  }

  public HttpResponse<List<Sjablontall>> hentSjablonSjablontall() {
    return bidragGcpProxyConsumer.hentSjablonListe(sjablonSjablontallUrl, SJABLON_SJABLONTALL_LISTE);
  }

  public HttpResponse<List<Samvaersfradrag>> hentSjablonSamvaersfradrag() {
    return bidragGcpProxyConsumer.hentSjablonListe(sjablonSamvaersfradragUrl, SJABLON_SAMVAERSFRADRAG_LISTE);
  }

  public HttpResponse<List<Bidragsevne>> hentSjablonBidragsevne() {
    return bidragGcpProxyConsumer.hentSjablonListe(sjablonBidragsevneUrl, SJABLON_BIDRAGSEVNE_LISTE);
  }

  public HttpResponse<List<TrinnvisSkattesats>> hentSjablonTrinnvisSkattesats() {
    return bidragGcpProxyConsumer.hentSjablonListe(sjablonTrinnvisSkattesatsUrl, SJABLON_TRINNVIS_SKATTESATS_LISTE);
  }
}
