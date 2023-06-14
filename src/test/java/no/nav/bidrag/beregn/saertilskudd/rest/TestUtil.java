package no.nav.bidrag.beregn.saertilskudd.rest;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneResultatCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddResultatCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnedeGrunnlagCore;
import no.nav.bidrag.beregn.felles.dto.AvvikCore;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore;
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatBeregningCore;
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Samvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.TrinnvisSkattesats;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BMInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPsAndelSaertilskuddResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BarnIHusstand;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnetTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Bostatus;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.LopendeBidrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.NettoSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SBInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Saerfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Samvaersklasse;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Skatteklasse;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SoknadsBarnInfo;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore;

public class TestUtil {

  public static final String BIDRAGSEVNE_REFERANSE = "BIDRAGSEVNE_REFERANSE";
  public static final String BPS_ANDEL_SAERTILSKUDD_REFERANSE = "BPS_ANDEL_SAERTILSKUDD_REFERANSE";
  public static final String SAMVAERSFRADRAG_REFERANSE = "SAMVAERSFRADRAG_REFERANSE";
  public static final String INNTEKT_REFERANSE = "INNTEKT_REFERANSE";
  public static final String SKATTEKLASSE_REFERANSE = "SKATTEKLASSE_REFERANSE";
  public static final String BOSTATUS_REFERANSE = "BOSTATUS_REFERANSE";
  public static final String BARN_I_HUSSTAND_REFERANSE = "BARN_I_HUSSTAND_REFERANSE";
  public static final String SAMVAERSKLASSE_REFERANSE = "SAMVAERSKLASSE_REFERANSE";


  public static BeregnTotalSaertilskuddGrunnlag byggTotalSaertilskuddGrunnlag() {
    var grunnlagListe = new ArrayList<Grunnlag>();
    grunnlagListe.add(new Grunnlag("Mottatt_SoknadsbarnInfo_SB_1", GrunnlagType.SOKNADSBARN_INFO,
        tilJsonNodeInnhold(new SoknadsBarnInfo(1, LocalDate.parse("2006-08-19")))));
    grunnlagListe.add(new Grunnlag("Mottatt_Inntekt_AG_20200801_SB_1", GrunnlagType.INNTEKT,
        tilJsonNodeInnhold(new SBInntekt(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"),
            Rolle.SB, "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER", BigDecimal.valueOf(0), 1))));
    grunnlagListe.add(new Grunnlag("Mottatt_Inntekt_AG_20200801_BM", GrunnlagType.INNTEKT, tilJsonNodeInnhold(
        new BMInntekt(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER", BigDecimal.valueOf(300000),
            Rolle.BM, false, false))));
    grunnlagListe.add(new Grunnlag("Mottatt_Inntekt_AG_20200801_BM", GrunnlagType.INNTEKT, tilJsonNodeInnhold(
        new BMInntekt(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), "UTVIDET_BARNETRYGD", BigDecimal.valueOf(12688),
            Rolle.BM, false, false))));
    grunnlagListe.add(new Grunnlag("Mottatt_Inntekt_AG_20200801_BP", GrunnlagType.INNTEKT,
        tilJsonNodeInnhold(new BPInntekt(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), Rolle.BP, "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER",
            BigDecimal.valueOf(500000)))));
    grunnlagListe.add(new Grunnlag("Mottatt_BarnIHusstand_20200801", GrunnlagType.BARN_I_HUSSTAND,
        tilJsonNodeInnhold(new BarnIHusstand(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), (double) 0))));
    grunnlagListe.add(new Grunnlag("Mottatt_Bostatus_20200801", GrunnlagType.BOSTATUS,
        tilJsonNodeInnhold(new Bostatus(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), "ALENE"))));
    grunnlagListe.add(new Grunnlag("Mottatt_Saerfradrag_20200801", GrunnlagType.SAERFRADRAG,
        tilJsonNodeInnhold(new Saerfradrag(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), "INGEN"))));
    grunnlagListe.add(new Grunnlag("Mottatt_Skatteklasse_20200801", GrunnlagType.SKATTEKLASSE,
        tilJsonNodeInnhold(new Skatteklasse(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), 1))));
    grunnlagListe.add(new Grunnlag("Mottatt_Netto_Saertilskudd_20200801", GrunnlagType.NETTO_SAERTILSKUDD,
        tilJsonNodeInnhold(new NettoSaertilskudd(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), BigDecimal.valueOf(7000)))));
    grunnlagListe.add(new Grunnlag("Mottatt_Samvaersklasse_20200801_SB_1", GrunnlagType.SAMVAERSKLASSE, tilJsonNodeInnhold(
        new Samvaersklasse(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), 1, LocalDate.parse("2006-08-19"), "01"))));
    grunnlagListe.add(new Grunnlag("Mottatt_LoependeBidrag_20200801_SB_1", GrunnlagType.LOPENDE_BIDRAG, tilJsonNodeInnhold(
        new LopendeBidrag(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), 1, BigDecimal.valueOf(2500), BigDecimal.valueOf(2957),
            BigDecimal.valueOf(2500), BigDecimal.valueOf(457)))));

    return new BeregnTotalSaertilskuddGrunnlag(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"), grunnlagListe);
  }

  public static JsonNode tilJsonNodeInnhold(Object object) {
    jacksonObjectMapper().registerModule(new JavaTimeModule());
    return jacksonObjectMapper().convertValue(object, JsonNode.class);
  }

  public static List<String> hentAlleReferanser(BeregnetTotalSaertilskuddResultat beregnetTotalSaertilskuddResultat) {
    List<String> alleReferanser = new ArrayList<>();

    for (ResultatPeriode resultatPeriode : beregnetTotalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe()) {
      alleReferanser.addAll(resultatPeriode.getGrunnlagReferanseListe());
    }

    for (Grunnlag grunnlag : beregnetTotalSaertilskuddResultat.getGrunnlagListe()) {
      if (grunnlag.getInnhold().has("grunnlagReferanseListe")) {
        var grunnlagReferanseListe = grunnlag.getInnhold().get("grunnlagReferanseListe");
        if (grunnlagReferanseListe.isArray()) {
          for (JsonNode grunnlagReferanse : grunnlagReferanseListe) {
            alleReferanser.add(grunnlagReferanse.asText());
          }
        }
      }
    }
    return alleReferanser;
  }

  // Bygger opp BeregnBidragsevneResultat
  public static Grunnlag dummyBidragsevneResultat() {
    ObjectMapper objectMapper = new ObjectMapper();
    var bidragsevne = new no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BidragsevneResultatPeriode(LocalDate.parse("2020-08-01"),
        LocalDate.parse("2020-09-01"), BigDecimal.valueOf(100), new ArrayList<>() {{
      add(INNTEKT_REFERANSE);
      add(SKATTEKLASSE_REFERANSE);
      add(BOSTATUS_REFERANSE);
      add(BARN_I_HUSSTAND_REFERANSE);
      add(SAMVAERSKLASSE_REFERANSE);
    }});
    return new Grunnlag(BIDRAGSEVNE_REFERANSE, GrunnlagType.BIDRAGSEVNE, objectMapper.valueToTree(bidragsevne));
  }

  // Bygger opp BeregnBidragsevneResultatCore
  public static BeregnBidragsevneResultatCore dummyBidragsevneResultatCore() {
    var bidragPeriodeResultatListe = new ArrayList<no.nav.bidrag.beregn.bidragsevne.dto.ResultatPeriodeCore>();
    bidragPeriodeResultatListe.add(new no.nav.bidrag.beregn.bidragsevne.dto.ResultatPeriodeCore(
        new PeriodeCore(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01")),
        new no.nav.bidrag.beregn.bidragsevne.dto.ResultatBeregningCore(BigDecimal.valueOf(100)),
        new ArrayList<>() {{
          add(INNTEKT_REFERANSE);
          add(SKATTEKLASSE_REFERANSE);
          add(BOSTATUS_REFERANSE);
          add(BARN_I_HUSSTAND_REFERANSE);
          add(SAMVAERSKLASSE_REFERANSE);
        }}));
    return new BeregnBidragsevneResultatCore(bidragPeriodeResultatListe, emptyList(), emptyList());
  }

  // Bygger opp BeregnBidragsevneResultatCore med avvik
  public static BeregnBidragsevneResultatCore dummyBidragsevneResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i inntektPeriodeListe: datoFra=2018-04-01, datoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnBidragsevneResultatCore(emptyList(), emptyList(), avvikListe);
  }

  // Bygger opp BeregnBPAndelSaertilskuddResultat
  public static Grunnlag dummyBPsAndelSaertilskuddResultat() {
    ObjectMapper objectMapper = new ObjectMapper();
    var bpsAndelSaertilskudd = new BPsAndelSaertilskuddResultatPeriode(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01"),
        BigDecimal.valueOf(100), BigDecimal.valueOf(10), false, new ArrayList<>() {{
      add(INNTEKT_REFERANSE);
      add(INNTEKT_REFERANSE);
      add(INNTEKT_REFERANSE);
    }});
    return new Grunnlag(BPS_ANDEL_SAERTILSKUDD_REFERANSE, GrunnlagType.BP_ANDEL_SAERTILSKUDD, objectMapper.valueToTree(bpsAndelSaertilskudd));
  }

  // Bygger opp BeregnBPsAndelSaertilskuddResultatCore
  public static BeregnBPsAndelSaertilskuddResultatCore dummyBPsAndelSaertilskuddResultatCore() {
    var bidragPeriodeResultatListe = new ArrayList<no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatPeriodeCore>();
    bidragPeriodeResultatListe.add(new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatPeriodeCore(
        new PeriodeCore(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01")),
        new no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatBeregningCore(BigDecimal.valueOf(10), BigDecimal.valueOf(100), false),
        new BeregnedeGrunnlagCore(emptyList(), emptyList(), emptyList()),
        new ArrayList<>() {{
          add(INNTEKT_REFERANSE);
          add(INNTEKT_REFERANSE);
          add(INNTEKT_REFERANSE);
        }}));
    return new BeregnBPsAndelSaertilskuddResultatCore(bidragPeriodeResultatListe, emptyList(), emptyList());
  }

  // Bygger opp BeregnBPsAndelSaertilskuddResultatCore med avvik
  public static BeregnBPsAndelSaertilskuddResultatCore dummyBPsAndelSaertilskuddResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i inntektBPPeriodeListe: datoFra=2018-04-01, datoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnBPsAndelSaertilskuddResultatCore(emptyList(), emptyList(), avvikListe);
  }

  // Bygger opp BeregnSamvaersfradragResultat
  public static Grunnlag dummySamvaersfradragResultat() {
    ObjectMapper objectMapper = new ObjectMapper();
    var samvaersfradrag = new no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SamvaersfradragResultatPeriode(LocalDate.parse("2020-08-01"),
        LocalDate.parse("2020-09-01"), BigDecimal.valueOf(100), 1, new ArrayList<>() {{
      add(SAMVAERSFRADRAG_REFERANSE);
    }});
    return new Grunnlag(SAMVAERSFRADRAG_REFERANSE, GrunnlagType.SAMVAERSFRADRAG, objectMapper.valueToTree(samvaersfradrag));
  }

  //
  // Bygger opp BeregnSamvaersfradragResultatCore
  public static BeregnSamvaersfradragResultatCore dummySamvaersfradragResultatCore() {
    var bidragPeriodeResultatListe = new ArrayList<no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatPeriodeCore>();
    bidragPeriodeResultatListe.add(new no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatPeriodeCore(
        new PeriodeCore(LocalDate.parse("2020-08-01"), LocalDate.parse("2020-09-01")),
        singletonList(new no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatBeregningCore(1, BigDecimal.valueOf(100))),
        new ArrayList<>() {{
          add(SAMVAERSFRADRAG_REFERANSE);
        }}));
    return new BeregnSamvaersfradragResultatCore(bidragPeriodeResultatListe, emptyList(), emptyList());
  }

  //
//  // Bygger opp BeregnSamvaersfradragResultatCore med avvik
  public static BeregnSamvaersfradragResultatCore dummySamvaersfradragResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i samvaersklassePeriodeListe: datoFra=2018-04-01, datoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnSamvaersfradragResultatCore(emptyList(), emptyList(), avvikListe);
  }

  // Bygger opp BeregnSaertilskuddResultatCore
  public static BeregnSaertilskuddResultatCore dummySaertilskuddResultatCore() {
    var beregnetSaertilskuddPeriodeListe = new ArrayList<ResultatPeriodeCore>();
    beregnetSaertilskuddPeriodeListe.add(new ResultatPeriodeCore(new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        1, new ResultatBeregningCore(BigDecimal.valueOf(100), "RESULTATKODE"),
        List.of(INNTEKT_REFERANSE, BIDRAGSEVNE_REFERANSE)));
    return new BeregnSaertilskuddResultatCore(beregnetSaertilskuddPeriodeListe, emptyList());
  }

  // Bygger opp BeregnSaertilskuddResultatCore med avvik
  public static BeregnSaertilskuddResultatCore dummySaertilskuddResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i samvaersfradragPeriodeListe: datoFra=2018-04-01, datoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnSaertilskuddResultatCore(emptyList(), avvikListe);
  }


  // Bygger opp liste av sjabloner av typen Sjablontall
  public static List<Sjablontall> dummySjablonSjablontallListe() {
    var sjablonSjablontallListe = new ArrayList<Sjablontall>();

    sjablonSjablontallListe.add(new Sjablontall("0001", LocalDate.parse("2004-01-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(970)));
    sjablonSjablontallListe.add(new Sjablontall("0001", LocalDate.parse("2019-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(1054)));

    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(2504)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(2577)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(2649)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(2692)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(2775)));
    sjablonSjablontallListe.add(new Sjablontall("0003", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(2825)));

    sjablonSjablontallListe.add(new Sjablontall("0004", LocalDate.parse("2012-07-01"), LocalDate.parse("2012-12-31"), BigDecimal.valueOf(12712)));
    sjablonSjablontallListe.add(new Sjablontall("0004", LocalDate.parse("2013-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(0)));

    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(1490)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(1530)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(1570)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(1600)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(1640)));
    sjablonSjablontallListe.add(new Sjablontall("0005", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(1670)));

    sjablonSjablontallListe.add(new Sjablontall("0015", LocalDate.parse("2016-01-01"), LocalDate.parse("2016-12-31"), BigDecimal.valueOf(26.07)));
    sjablonSjablontallListe.add(new Sjablontall("0015", LocalDate.parse("2017-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(25.67)));
    sjablonSjablontallListe.add(new Sjablontall("0015", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(25.35)));
    sjablonSjablontallListe.add(new Sjablontall("0015", LocalDate.parse("2019-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(25.05)));

    sjablonSjablontallListe.add(new Sjablontall("0017", LocalDate.parse("2003-01-01"), LocalDate.parse("2003-12-31"), BigDecimal.valueOf(7.8)));
    sjablonSjablontallListe.add(new Sjablontall("0017", LocalDate.parse("2014-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(8.2)));

    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(3150)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(3294)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(3365)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(3417)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(3487)));
    sjablonSjablontallListe.add(new Sjablontall("0019", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(3841)));

    sjablonSjablontallListe.add(new Sjablontall("0021", LocalDate.parse("2012-07-01"), LocalDate.parse("2014-06-30"), BigDecimal.valueOf(4923)));
    sjablonSjablontallListe.add(new Sjablontall("0021", LocalDate.parse("2014-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(5100)));
    sjablonSjablontallListe.add(new Sjablontall("0021", LocalDate.parse("2017-07-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(5254)));
    sjablonSjablontallListe.add(new Sjablontall("0021", LocalDate.parse("2018-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(5313)));

    sjablonSjablontallListe.add(new Sjablontall("0022", LocalDate.parse("2012-07-01"), LocalDate.parse("2014-06-30"), BigDecimal.valueOf(2028)));
    sjablonSjablontallListe.add(new Sjablontall("0022", LocalDate.parse("2014-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(2100)));
    sjablonSjablontallListe.add(new Sjablontall("0022", LocalDate.parse("2017-07-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(2163)));
    sjablonSjablontallListe.add(new Sjablontall("0022", LocalDate.parse("2018-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(2187)));

    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(72200)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(73600)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(75000)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(83000)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(85050)));
    sjablonSjablontallListe.add(new Sjablontall("0023", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(87450)));

    sjablonSjablontallListe.add(new Sjablontall("0025", LocalDate.parse("2015-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(29)));
    sjablonSjablontallListe.add(new Sjablontall("0025", LocalDate.parse("2018-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(31)));

    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(50400)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(51750)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(53150)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(54750)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(56550)));
    sjablonSjablontallListe.add(new Sjablontall("0027", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(51300)));

    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(74250)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(76250)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(78300)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(54750)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(56550)));
    sjablonSjablontallListe.add(new Sjablontall("0028", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(51300)));

    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2016-01-01"), LocalDate.parse("2016-12-31"), BigDecimal.valueOf(90850)));
    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2017-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(94850)));
    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(99540)));
    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(102820)));
    sjablonSjablontallListe.add(new Sjablontall("0030", LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(93273)));

    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2016-01-01"), LocalDate.parse("2016-12-31"), BigDecimal.valueOf(112350)));
    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2017-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(117350)));
    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(99540)));
    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(102820)));
    sjablonSjablontallListe.add(new Sjablontall("0031", LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(93273)));

    sjablonSjablontallListe.add(new Sjablontall("0039", LocalDate.parse("2016-01-01"), LocalDate.parse("2016-12-31"), BigDecimal.valueOf(13505)));
    sjablonSjablontallListe.add(new Sjablontall("0039", LocalDate.parse("2017-01-01"), LocalDate.parse("2017-12-31"), BigDecimal.valueOf(13298)));
    sjablonSjablontallListe.add(new Sjablontall("0039", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(13132)));
    sjablonSjablontallListe.add(new Sjablontall("0039", LocalDate.parse("2019-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(12977)));

    sjablonSjablontallListe.add(new Sjablontall("0040", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(23)));
    sjablonSjablontallListe.add(new Sjablontall("0040", LocalDate.parse("2019-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(22)));

    sjablonSjablontallListe.add(new Sjablontall("0041", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(1354)));

    return sjablonSjablontallListe;
  }

  // Bygger opp liste av sjabloner av typen Samvaersfradrag
  public static List<Samvaersfradrag> dummySjablonSamvaersfradragListe() {
    var sjablonSamvaersfradragListe = new ArrayList<Samvaersfradrag>();

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("00", 99, LocalDate.parse("2013-07-01"), LocalDate.parse("9999-12-31"), 1, 1, BigDecimal.valueOf(0)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 3, 3, BigDecimal.valueOf(204)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 3, 3, BigDecimal.valueOf(208)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 3, 3, BigDecimal.valueOf(212)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 3, 3, BigDecimal.valueOf(215)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 3, 3, BigDecimal.valueOf(219)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 5, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 3, 3, BigDecimal.valueOf(256)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 10, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 3, 3, BigDecimal.valueOf(296)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 10, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 3, 3, BigDecimal.valueOf(301)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 10, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 3, 3, BigDecimal.valueOf(306)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 10, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 3, 3, BigDecimal.valueOf(312)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 10, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 3, 3, BigDecimal.valueOf(318)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 10, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 3, 3, BigDecimal.valueOf(353)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 14, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 3, 3, BigDecimal.valueOf(358)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 14, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 3, 3, BigDecimal.valueOf(378)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 14, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 3, 3, BigDecimal.valueOf(385)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 14, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 3, 3, BigDecimal.valueOf(390)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 14, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 3, 3, BigDecimal.valueOf(400)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 14, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 3, 3, BigDecimal.valueOf(457)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 18, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 3, 3, BigDecimal.valueOf(422)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 18, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 3, 3, BigDecimal.valueOf(436)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 18, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 3, 3, BigDecimal.valueOf(443)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 18, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 3, 3, BigDecimal.valueOf(450)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 18, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 3, 3, BigDecimal.valueOf(460)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 18, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 3, 3, BigDecimal.valueOf(528)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 99, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 3, 3, BigDecimal.valueOf(422)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 99, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 3, 3, BigDecimal.valueOf(436)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 99, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 3, 3, BigDecimal.valueOf(443)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 99, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 3, 3, BigDecimal.valueOf(450)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 99, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 3, 3, BigDecimal.valueOf(460)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("01", 99, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 3, 3, BigDecimal.valueOf(528)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 5, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 8, BigDecimal.valueOf(674)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 5, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 8, BigDecimal.valueOf(689)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 5, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 8, BigDecimal.valueOf(701)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 5, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 8, BigDecimal.valueOf(712)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 5, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 8, BigDecimal.valueOf(727)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 5, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 8, BigDecimal.valueOf(849)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 10, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 8, BigDecimal.valueOf(979)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 10, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 8, BigDecimal.valueOf(998)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 10, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 8, BigDecimal.valueOf(1012)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 10, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 8, BigDecimal.valueOf(1034)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 10, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 8, BigDecimal.valueOf(1052)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 10, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 8, BigDecimal.valueOf(1167)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 14, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 8, BigDecimal.valueOf(1184)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 14, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 8, BigDecimal.valueOf(1252)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 14, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 8, BigDecimal.valueOf(1275)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 14, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 8, BigDecimal.valueOf(1293)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 14, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 8, BigDecimal.valueOf(1323)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 14, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 8, BigDecimal.valueOf(1513)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 18, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 8, BigDecimal.valueOf(1397)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 18, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 8, BigDecimal.valueOf(1444)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 18, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 8, BigDecimal.valueOf(1468)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 18, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 8, BigDecimal.valueOf(1490)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 18, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 8, BigDecimal.valueOf(1525)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 18, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 8, BigDecimal.valueOf(1749)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 99, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 8, BigDecimal.valueOf(1397)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 99, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 8, BigDecimal.valueOf(1444)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 99, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 8, BigDecimal.valueOf(1468)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 99, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 8, BigDecimal.valueOf(1490)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 99, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 8, BigDecimal.valueOf(1525)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("02", 99, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 8, BigDecimal.valueOf(1749)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 5, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 13, BigDecimal.valueOf(1904)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 5, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 13, BigDecimal.valueOf(1953)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 5, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 13, BigDecimal.valueOf(1998)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 5, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 13, BigDecimal.valueOf(2029)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 5, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 13, BigDecimal.valueOf(2082)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 5, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 13, BigDecimal.valueOf(2272)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 10, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 13, BigDecimal.valueOf(2330)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 10, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 13, BigDecimal.valueOf(2385)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 10, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 13, BigDecimal.valueOf(2432)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 10, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 13, BigDecimal.valueOf(2478)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 10, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 13, BigDecimal.valueOf(2536)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 10, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 13, BigDecimal.valueOf(2716)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 14, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 13, BigDecimal.valueOf(2616)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 14, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 13, BigDecimal.valueOf(2739)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 14, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 13, BigDecimal.valueOf(2798)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 14, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 13, BigDecimal.valueOf(2839)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 14, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 13, BigDecimal.valueOf(2914)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 14, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 13, BigDecimal.valueOf(3199)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 18, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 13, BigDecimal.valueOf(2912)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 18, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 13, BigDecimal.valueOf(3007)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 18, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 13, BigDecimal.valueOf(3067)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 18, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 13, BigDecimal.valueOf(3115)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 18, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 13, BigDecimal.valueOf(3196)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 18, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 13, BigDecimal.valueOf(3528)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 99, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 13, BigDecimal.valueOf(2912)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 99, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 13, BigDecimal.valueOf(3007)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 99, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 13, BigDecimal.valueOf(3067)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 99, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 13, BigDecimal.valueOf(3115)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 99, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 13, BigDecimal.valueOf(3196)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("03", 99, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 13, BigDecimal.valueOf(3528)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 5, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 15, BigDecimal.valueOf(2391)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 5, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 15, BigDecimal.valueOf(2452)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 5, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 15, BigDecimal.valueOf(2509)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 5, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 15, BigDecimal.valueOf(2548)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 5, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 15, BigDecimal.valueOf(2614)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 5, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 15, BigDecimal.valueOf(2852)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 10, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 15, BigDecimal.valueOf(2925)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 10, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 15, BigDecimal.valueOf(2994)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 10, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 15, BigDecimal.valueOf(3053)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 10, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 15, BigDecimal.valueOf(3111)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 10, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 15, BigDecimal.valueOf(3184)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 10, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 15, BigDecimal.valueOf(3410)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 14, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 15, BigDecimal.valueOf(3284)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 14, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 15, BigDecimal.valueOf(3428)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 14, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 15, BigDecimal.valueOf(3512)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 14, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 15, BigDecimal.valueOf(3565)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 14, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 15, BigDecimal.valueOf(3658)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 14, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 15, BigDecimal.valueOf(4016)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 18, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 15, BigDecimal.valueOf(3656)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 18, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 15, BigDecimal.valueOf(3774)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 18, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 15, BigDecimal.valueOf(3851)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 18, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 15, BigDecimal.valueOf(3910)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 18, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 15, BigDecimal.valueOf(4012)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 18, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 15, BigDecimal.valueOf(4429)));

    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 99, LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), 0, 15, BigDecimal.valueOf(3656)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 99, LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), 0, 15, BigDecimal.valueOf(3774)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 99, LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), 0, 15, BigDecimal.valueOf(3851)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 99, LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), 0, 15, BigDecimal.valueOf(3910)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 99, LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), 0, 15, BigDecimal.valueOf(4012)));
    sjablonSamvaersfradragListe.add(
        new Samvaersfradrag("04", 99, LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), 0, 15, BigDecimal.valueOf(4429)));

    return sjablonSamvaersfradragListe;
  }


  // Bygger opp liste av sjabloner av typen Bidragsevne
  public static List<Bidragsevne> dummySjablonBidragsevneListe() {
    var sjablonBidragsevneListe = new ArrayList<Bidragsevne>();

    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(7711), BigDecimal.valueOf(8048)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(8907), BigDecimal.valueOf(8289)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(9156), BigDecimal.valueOf(8521)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(9303), BigDecimal.valueOf(8657)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(9591), BigDecimal.valueOf(8925)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("EN", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(9764), BigDecimal.valueOf(9818)));

    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2015-07-01"), LocalDate.parse("2016-06-30"), BigDecimal.valueOf(5073), BigDecimal.valueOf(6814)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2016-07-01"), LocalDate.parse("2017-06-30"), BigDecimal.valueOf(5456), BigDecimal.valueOf(7018)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2017-07-01"), LocalDate.parse("2018-06-30"), BigDecimal.valueOf(5609), BigDecimal.valueOf(7215)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30"), BigDecimal.valueOf(5698), BigDecimal.valueOf(7330)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2019-07-01"), LocalDate.parse("2020-06-30"), BigDecimal.valueOf(5875), BigDecimal.valueOf(7557)));
    sjablonBidragsevneListe.add(
        new Bidragsevne("GS", LocalDate.parse("2020-07-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(5981), BigDecimal.valueOf(8313)));

    return sjablonBidragsevneListe;
  }

  // Bygger opp liste av sjabloner av typen TrinnvisSkattesats
  public static List<TrinnvisSkattesats> dummySjablonTrinnvisSkattesatsListe() {
    var sjablonTrinnvisSkattesatsListe = new ArrayList<TrinnvisSkattesats>();

    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(169000), BigDecimal.valueOf(1.4)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(237900), BigDecimal.valueOf(3.3)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(598050), BigDecimal.valueOf(12.4)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31"), BigDecimal.valueOf(962050), BigDecimal.valueOf(15.4)));

    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(174500), BigDecimal.valueOf(1.9)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(245650), BigDecimal.valueOf(4.2)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(617500), BigDecimal.valueOf(13.2)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31"), BigDecimal.valueOf(964800), BigDecimal.valueOf(16.2)));

    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(180800), BigDecimal.valueOf(1.9)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(254500), BigDecimal.valueOf(4.2)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(639750), BigDecimal.valueOf(13.2)));
    sjablonTrinnvisSkattesatsListe.add(
        new TrinnvisSkattesats(LocalDate.parse("2020-01-01"), LocalDate.parse("9999-12-31"), BigDecimal.valueOf(999550), BigDecimal.valueOf(16.2)));

    return sjablonTrinnvisSkattesatsListe;
  }
}
