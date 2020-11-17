package no.nav.bidrag.beregn.saertilskudd.rest.service;

import static java.util.Collections.emptyList;

import java.util.Optional;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddResultat;
import no.nav.bidrag.commons.web.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BeregnSaertilskuddService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BeregnSaertilskuddService.class);

  private final SjablonConsumer sjablonConsumer;
  private final SaertilskuddCore saertilskuddCore;

  public BeregnSaertilskuddService(SjablonConsumer sjablonConsumer, SaertilskuddCore saertilskuddCore) {
    this.sjablonConsumer = sjablonConsumer;
    this.saertilskuddCore = saertilskuddCore;
  }

  public HttpResponse<BeregnSaertilskuddResultat> beregn(BeregnSaertilskuddGrunnlag beregnSaertilskuddGrunnlag) {

    // Henter sjabloner
    var sjablonListe = hentSjabloner();

    // Kaller core-modul
    var beregnSaertilskuddResultatCore = saertilskuddCore.beregnSaertilskudd(beregnSaertilskuddGrunnlag.tilCore());

    return HttpResponse
        .from(HttpStatus.OK, new BeregnSaertilskuddResultat(beregnSaertilskuddResultatCore));
  }

  // Henter sjabloner
  private SjablonListe hentSjabloner() {

    // Henter sjabloner for sjablontall
    var sjablonSjablontallListe = Optional.ofNullable(sjablonConsumer.hentSjablonSjablontall().getResponseEntity().getBody()).orElse(emptyList());
    LOGGER.debug("Antall sjabloner hentet av type Sjablontall: {}", sjablonSjablontallListe.size());

    return new SjablonListe(sjablonSjablontallListe);
  }
}
