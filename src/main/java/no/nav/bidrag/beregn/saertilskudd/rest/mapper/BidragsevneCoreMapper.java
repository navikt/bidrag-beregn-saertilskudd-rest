package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import no.nav.bidrag.beregn.bidragsevne.dto.AntallBarnIEgetHusholdPeriodeCore;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneGrunnlagCore;
import no.nav.bidrag.beregn.bidragsevne.dto.BostatusPeriodeCore;
import no.nav.bidrag.beregn.bidragsevne.dto.InntektPeriodeCore;
import no.nav.bidrag.beregn.bidragsevne.dto.SaerfradragPeriodeCore;
import no.nav.bidrag.beregn.bidragsevne.dto.SkatteklassePeriodeCore;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.felles.dto.SjablonInnholdCore;
import no.nav.bidrag.beregn.felles.dto.SjablonNokkelCore;
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonInnholdNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNokkelNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BarnIHusstand;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Bostatus;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;

import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektRolle;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Saerfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Skatteklasse;

public class BidragsevneCoreMapper extends CoreMapper {

  public BeregnBidragsevneGrunnlagCore mapBidragsevneGrunnlagTilCore(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      Map<String, SjablonTallNavn> sjablontallMap, SjablonListe sjablonListe) {

    var inntektBPPeriodeListe = new ArrayList<InntektPeriodeCore>();
    var skatteklassePeriodeCoreListe = new ArrayList<SkatteklassePeriodeCore>();
    var bostatusPeriodeListe = new ArrayList<BostatusPeriodeCore>();
    var antallBarnIEgetHusholdPeriodeListe = new ArrayList<AntallBarnIEgetHusholdPeriodeCore>();
    var saerfradragPeriodeListe = new ArrayList<SaerfradragPeriodeCore>();
    var sjablonPeriodeCoreListe = new ArrayList<SjablonPeriodeCore>();

    // Løper gjennom alle grunnlagene og identifiserer de som skal mappes til bidragsevne core
    for (Grunnlag grunnlag : beregnTotalSaertilskuddGrunnlag.getGrunnlagListe()) {
      switch (grunnlag.getType()) {
        case INNTEKT -> {
          InntektRolle inntektRolle = grunnlagTilObjekt(grunnlag, InntektRolle.class);
          Rolle rolle = inntektRolle.getRolle();
          if (rolle.equals(rolle.BP)) {
            BPInntekt bpInntekt = grunnlagTilObjekt(grunnlag, BPInntekt.class);
            inntektBPPeriodeListe.add(bpInntekt.tilCore(grunnlag.getReferanse()));
          }
        }
        case BARN_I_HUSSTAND -> {
          BarnIHusstand barnIHusstand = grunnlagTilObjekt(grunnlag, BarnIHusstand.class);
          antallBarnIEgetHusholdPeriodeListe.add(barnIHusstand.tilCore(grunnlag.getReferanse()));
        }
        case BOSTATUS -> {
          Bostatus bostatus = grunnlagTilObjekt(grunnlag, Bostatus.class);
          bostatusPeriodeListe.add(bostatus.tilCore(grunnlag.getReferanse()));
        }
        case SAERFRADRAG -> {
          Saerfradrag saerfradrag = grunnlagTilObjekt(grunnlag, Saerfradrag.class);
          saerfradragPeriodeListe.add(saerfradrag.tilCore(grunnlag.getReferanse()));
        }
        case SKATTEKLASSE -> {
          Skatteklasse skatteklasse = grunnlagTilObjekt(grunnlag, Skatteklasse.class);
          skatteklassePeriodeCoreListe.add(skatteklasse.tilCore(grunnlag.getReferanse()));
        }
      }
    }
    // Hent aktuelle sjabloner
    sjablonPeriodeCoreListe.addAll(
        mapSjablonSjablontall(sjablonListe.getSjablonSjablontallResponse(), BIDRAGSEVNE, beregnTotalSaertilskuddGrunnlag, sjablontallMap));
    sjablonPeriodeCoreListe.addAll(mapSjablonBidragsevne(sjablonListe.getSjablonBidragsevneResponse(), beregnTotalSaertilskuddGrunnlag));
    sjablonPeriodeCoreListe
        .addAll(mapSjablonTrinnvisSkattesats(sjablonListe.getSjablonTrinnvisSkattesatsResponse(), beregnTotalSaertilskuddGrunnlag));

    return new BeregnBidragsevneGrunnlagCore(beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra(), beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil(),
        inntektBPPeriodeListe, skatteklassePeriodeCoreListe, bostatusPeriodeListe, antallBarnIEgetHusholdPeriodeListe, saerfradragPeriodeListe,
        sjablonPeriodeCoreListe);
  }

  private List<SjablonPeriodeCore> mapSjablonBidragsevne(List<Bidragsevne> sjablonBidragsevneListe,
      BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {

    var beregnDatoFra = beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra();
    var beregnDatoTil = beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil();

    return sjablonBidragsevneListe
        .stream()
        .filter(sjablon -> (sjablon.getDatoFom().isBefore(beregnDatoTil) && (!(sjablon.getDatoTom().isBefore(beregnDatoFra)))))
        .map(sjablon -> new SjablonPeriodeCore(
            new PeriodeCore(sjablon.getDatoFom(), sjablon.getDatoTom()),
            SjablonNavn.BIDRAGSEVNE.getNavn(),
            singletonList(new SjablonNokkelCore(SjablonNokkelNavn.BOSTATUS.getNavn(), sjablon.getBostatus())),
            asList(new SjablonInnholdCore(SjablonInnholdNavn.BOUTGIFT_BELOP.getNavn(), sjablon.getBelopBoutgift()),
                new SjablonInnholdCore(SjablonInnholdNavn.UNDERHOLD_BELOP.getNavn(), sjablon.getBelopUnderhold()))))
        .collect(toList());
  }
}
