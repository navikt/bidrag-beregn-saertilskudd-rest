package no.nav.bidrag.beregn.saertilskudd.rest.consumer;

import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddResultatCore;

public class SaertilskuddCore {

  public BeregnSaertilskuddResultatCore beregnSaertilskudd(
      BeregnSaertilskuddGrunnlagCore saertilskuddGrunnlagCore) {
    return new BeregnSaertilskuddResultatCore("Hallo, " + saertilskuddGrunnlagCore.getGrunnlag());
  }
}
