package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.NettoSaertilskuddPeriodeCore;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BMInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektRolle;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.NettoSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;

public class BPAndelSaertilskuddCoreMapper extends CoreMapper {

  public BeregnBPsAndelSaertilskuddGrunnlagCore mapBPsAndelSaertilskuddGrunnlagTilCore(
      BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      Map<String, SjablonTallNavn> sjablontallMap, SjablonListe sjablonListe) {
    var nettoSaertilskuddPeriodeListe = new ArrayList<NettoSaertilskuddPeriodeCore>();
    var inntektBPPeriodeListe = new ArrayList<InntektPeriodeCore>();
    var inntektBMPeriodeListe = new ArrayList<InntektPeriodeCore>();
    var inntektBBPeriodeListe = new ArrayList<InntektPeriodeCore>();

    // Løper gjennom alle grunnlagene og identifiserer de som skal mappes til bidragsevne core
    for (Grunnlag grunnlag : beregnTotalSaertilskuddGrunnlag.getGrunnlagListe()) {
      switch (grunnlag.getType()) {
        case INNTEKT_TYPE -> {
          InntektRolle inntektRolle = jsonNodeTilObjekt(grunnlag.getInnhold(), InntektRolle.class);
          inntektRolle.valider();
          Rolle rolle = inntektRolle.getRolle();
          if (rolle.equals(Rolle.BP)) {
            BPInntekt bpInntekt = jsonNodeTilObjekt(grunnlag.getInnhold(), BPInntekt.class);
            inntektBPPeriodeListe.add(bpInntekt.TilBPsAndelSaertilskuddCore(grunnlag.getReferanse()));
//            inntektBPPeriodeListe.add(mapInntektBPAndelSaertilskudd(grunnlag));
          } else if (rolle.equals(Rolle.BM)) {
            BMInntekt bmInntekt = jsonNodeTilObjekt(grunnlag.getInnhold(), BMInntekt.class);
            inntektBMPeriodeListe.add(bmInntekt.TilCore(grunnlag.getReferanse()));
//            inntektBMPeriodeListe.add(mapInntektBPAndelSaertilskuddBM(grunnlag));
          } else if (rolle.equals(Rolle.BB)) {
            BPInntekt bpInntekt = jsonNodeTilObjekt(grunnlag.getInnhold(), BPInntekt.class);
            inntektBBPeriodeListe.add(bpInntekt.TilBPsAndelSaertilskuddCore(grunnlag.getReferanse()));
//            inntektBBPeriodeListe.add(mapInntektBPAndelSaertilskudd(grunnlag));
          }
        }
        case NETTO_SAERTILSKUDD_TYPE -> {
          NettoSaertilskudd nettoSaertilskudd = jsonNodeTilObjekt(grunnlag.getInnhold(), NettoSaertilskudd.class);
          nettoSaertilskuddPeriodeListe.add(nettoSaertilskudd.TilCore(grunnlag.getReferanse()));
//          nettoSaertilskuddPeriodeListe.add(mapNettoSaertilskudd(grunnlag));
        }
      }
    }

    // Hent aktuelle sjabloner
    var sjablonPeriodeCoreListe = mapSjablonSjablontall(sjablonListe.getSjablonSjablontallResponse(), BP_ANDEL_SAERTILSKUDD,
        beregnTotalSaertilskuddGrunnlag, sjablontallMap);

    return new BeregnBPsAndelSaertilskuddGrunnlagCore(beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra(),
        beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil(), nettoSaertilskuddPeriodeListe, inntektBPPeriodeListe, inntektBMPeriodeListe,
        inntektBBPeriodeListe, sjablonPeriodeCoreListe);
  }

//  private NettoSaertilskuddPeriodeCore mapNettoSaertilskudd(Grunnlag grunnlag) {
//    PeriodeCore periodeCore = mapPeriode(grunnlag.getInnhold(), grunnlag.getType());
//    String nettoSaertilskuddBelop = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "nettoSaertilskuddBelop").asText();
//    return new NettoSaertilskuddPeriodeCore(periodeCore, new BigDecimal(nettoSaertilskuddBelop));
//  }

//  private InntektPeriodeCore mapInntektBPAndelSaertilskuddBM(Grunnlag grunnlag) {
//    String inntektType = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "inntektType").asText();
//    evaluerStringType(inntektType, "inntektType", "Inntekt");
//    JsonNode belopNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "belop");
//    evaluerNumberType(belopNode, "belop");
//    String belop = belopNode.asText();
//    JsonNode deltFordelNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "deltFordel");
//    evauluerBooleanType(deltFordelNode, "deltFordel");
//    boolean deltFordel = deltFordelNode.asBoolean();
//    JsonNode skatteklasse2Node = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "skatteklasse2");
//    evauluerBooleanType(skatteklasse2Node, "skatteklasse2");
//    boolean skatteklasse2 = skatteklasse2Node.asBoolean();
//
//    return new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore(grunnlag.getReferanse(), mapPeriode(grunnlag.getInnhold(), grunnlag.getType()),
//        inntektType, new BigDecimal(belop), deltFordel, skatteklasse2);
//  }
//
//  private InntektPeriodeCore mapInntektBPAndelSaertilskudd(Grunnlag grunnlag) {
//    String inntektType = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "inntektType").asText();
//    evaluerStringType(inntektType, "inntektType", "Inntekt");
//    JsonNode belopNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "belop");
//    evaluerNumberType(belopNode, "belop");
//    String belop = belopNode.asText();
//    return new InntektPeriodeCore(grunnlag.getReferanse(), mapPeriode(grunnlag.getInnhold(), grunnlag.getType()),
//        inntektType, new BigDecimal(belop), false, false);
//  }
}
