package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
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
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;

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
        case INNTEKT_TYPE -> {
          InntektRolle inntektRolle = jsonNodeTilObjekt(grunnlag.getInnhold(), InntektRolle.class);
          inntektRolle.valider();
          Rolle rolle = inntektRolle.getRolle();
//          String rolle;
//
//          if (grunnlag.getInnhold().has("rolle")) {
//            rolle = grunnlag.getInnhold().get("rolle").asText();
//            evaluerStringType(rolle, "rolle", "Inntekt");
//          } else {
//            throw new UgyldigInputException("rolle i objekt av type Inntekt mangler");
//          }
          if (rolle.equals(Rolle.BP)) {
            BPInntekt bpInntekt = jsonNodeTilObjekt(grunnlag.getInnhold(), BPInntekt.class);
            inntektBPPeriodeListe.add(bpInntekt.TilCore(grunnlag.getReferanse()));
//            inntektBPPeriodeListe.add(mapInntektBidragsevne(bpInntekt));
          }
        }
        case BARN_I_HUSSTAND_TYPE -> {
          BarnIHusstand barnIHusstand = jsonNodeTilObjekt(grunnlag.getInnhold(), BarnIHusstand.class);
          antallBarnIEgetHusholdPeriodeListe.add(barnIHusstand.TilCore(grunnlag.getReferanse()));
//          antallBarnIEgetHusholdPeriodeListe.add(mapBarnIHusstand(grunnlag));
        }
        case BOSTATUS_TYPE -> {
          Bostatus bostatus = jsonNodeTilObjekt(grunnlag.getInnhold(), Bostatus.class);
          bostatusPeriodeListe.add(bostatus.TilCore(grunnlag.getReferanse()));
//          bostatusPeriodeListe.add(mapBostatus(grunnlag));
        }
        case SAERFRADRAG_TYPE -> {
          Saerfradrag saerfradrag = jsonNodeTilObjekt(grunnlag.getInnhold(), Saerfradrag.class);
          saerfradragPeriodeListe.add(saerfradrag.TilCore(grunnlag.getReferanse()));
//          saerfradragPeriodeListe.add(mapSaerfradrag(grunnlag));
        }
        case SKATTEKLASSE_TYPE -> {
          Skatteklasse skatteklasse = jsonNodeTilObjekt(grunnlag.getInnhold(), Skatteklasse.class);
          skatteklassePeriodeCoreListe.add(skatteklasse.TilCore(grunnlag.getReferanse()));
//          skatteklassePeriodeCoreListe.add(mapSkatteklasse(grunnlag));
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

//  private InntektPeriodeCore mapInntektBidragsevne(BPInntekt bpInntekt) {
//    String inntektType = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "inntektType").asText();
//    evaluerStringType(inntektType, "inntektType", "Inntekt");
//    JsonNode belopNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "belop");
//    evaluerNumberType(belopNode, "belop");
//    String belop = belopNode.asText();
//    return new InntektPeriodeCore(grunnlag.getReferanse(), mapPeriode(grunnlag.getInnhold(), grunnlag.getType()),
//        inntektType, new BigDecimal(belop));
//  }
//
//  private SkatteklassePeriodeCore mapSkatteklasse(Grunnlag grunnlag) {
//    JsonNode skatteKlasseIdNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "skatteklasseId");
//    evaluerNumberType(skatteKlasseIdNode, "skatteklasseId");
//    String skatteklasseId = skatteKlasseIdNode.asText();
//    return new SkatteklassePeriodeCore(grunnlag.getReferanse(), mapPeriode(grunnlag.getInnhold(), grunnlag.getType()),
//        Integer.parseInt(skatteklasseId));
//  }
//
//  private SaerfradragPeriodeCore mapSaerfradrag(Grunnlag grunnlag) {
//    String saerfradragKode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "saerfradragKode").asText();
//    evaluerStringType(saerfradragKode, "saerfradragKode", "Saerfradrag");
//    return new SaerfradragPeriodeCore(grunnlag.getReferanse(), mapPeriode(grunnlag.getInnhold(), grunnlag.getType()), saerfradragKode);
//  }
//
//  private AntallBarnIEgetHusholdPeriodeCore mapBarnIHusstand(Grunnlag grunnlag) {
//    JsonNode antallBarnNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "antall");
//    evaluerNumberType(antallBarnNode, "antall");
//    String antallBarn = antallBarnNode.asText();
//    return new AntallBarnIEgetHusholdPeriodeCore(grunnlag.getReferanse(), mapPeriode(grunnlag.getInnhold(), grunnlag.getType()),
//        new BigDecimal(antallBarn));
//  }
//
//  private BostatusPeriodeCore mapBostatus(Grunnlag grunnlag) {
//    String bostatusKode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "bostatusKode").asText();
//    evaluerStringType(bostatusKode, "bostatusKode", "Bostatus");
//    return new BostatusPeriodeCore(grunnlag.getReferanse(), mapPeriode(grunnlag.getInnhold(), grunnlag.getType()), bostatusKode);
//  }

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
