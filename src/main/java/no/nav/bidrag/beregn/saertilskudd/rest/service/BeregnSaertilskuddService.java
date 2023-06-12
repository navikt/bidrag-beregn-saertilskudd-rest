package no.nav.bidrag.beregn.saertilskudd.rest.service;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static no.nav.bidrag.beregn.saertilskudd.rest.BidragBeregnSaertilskudd.SECURE_LOGGER;
import static no.nav.bidrag.beregn.saertilskudd.rest.mapper.CoreMapper.grunnlagTilObjekt;
import static no.nav.bidrag.beregn.saertilskudd.rest.mapper.CoreMapper.tilJsonNode;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import no.nav.bidrag.beregn.bidragsevne.BidragsevneCore;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneGrunnlagCore;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneResultatCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.BPsAndelSaertilskuddCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddResultatCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatPeriodeCore;
import no.nav.bidrag.beregn.felles.dto.AvvikCore;
import no.nav.bidrag.beregn.felles.dto.IResultatPeriode;
import no.nav.bidrag.beregn.felles.dto.SjablonResultatGrunnlagCore;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.beregn.saertilskudd.SaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevnePeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BMInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPsAndelSaertilskuddResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnetTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BidragsevneResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SBInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SamvaersfradragResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Samvaersklasse;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SjablonResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SoknadsBarnInfo;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.BPAndelSaertilskuddCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.BidragsevneCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.SaertilskuddCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.SamvaersfradragCoreMapper;
import no.nav.bidrag.beregn.samvaersfradrag.SamvaersfradragCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore;
import no.nav.bidrag.commons.web.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BeregnSaertilskuddService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BeregnSaertilskuddService.class);

  private final SjablonConsumer sjablonConsumer;
  private final BidragsevneCore bidragsevneCore;
  private final BPsAndelSaertilskuddCore bpAndelSaertilskuddCore;
  private final SamvaersfradragCore samvaersfradragCore;
  private final SaertilskuddCore saertilskuddCore;
  private final BidragsevneCoreMapper bidragsevneCoreMapper;
  private final BPAndelSaertilskuddCoreMapper bpAndelSaertilskuddCoreMapper;
  private final SamvaersfradragCoreMapper samvaersfradragCoreMapper;
  private final SaertilskuddCoreMapper saertilskuddCoreMapper;

  public BeregnSaertilskuddService(SjablonConsumer sjablonConsumer, BidragsevneCore bidragsevneCore, BPsAndelSaertilskuddCore bpAndelSaertilskuddCore,
      SamvaersfradragCore samvaersfradragCore, SaertilskuddCore saertilskuddCore, BidragsevneCoreMapper bidragsevneCoreMapper,
      BPAndelSaertilskuddCoreMapper bpAndelSaertilskuddCoreMapper, SamvaersfradragCoreMapper samvaersfradragCoreMapper,
      SaertilskuddCoreMapper saertilskuddCoreMapper) {
    this.sjablonConsumer = sjablonConsumer;
    this.bidragsevneCore = bidragsevneCore;
    this.bpAndelSaertilskuddCore = bpAndelSaertilskuddCore;
    this.samvaersfradragCore = samvaersfradragCore;
    this.saertilskuddCore = saertilskuddCore;
    this.bidragsevneCoreMapper = bidragsevneCoreMapper;
    this.bpAndelSaertilskuddCoreMapper = bpAndelSaertilskuddCoreMapper;
    this.samvaersfradragCoreMapper = samvaersfradragCoreMapper;
    this.saertilskuddCoreMapper = saertilskuddCoreMapper;
  }

  public HttpResponse<BeregnetTotalSaertilskuddResultat> beregn(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {

    // Kontroll av felles inputdata
    beregnTotalSaertilskuddGrunnlag.valider();

    // Validerer og henter ut soknadsbarn
    SoknadsBarnInfo soknadsBarnInfo = validerSoknadsbarn(beregnTotalSaertilskuddGrunnlag);

    // Lager en map for sjablontall (id og navn)
    var sjablontallMap = new HashMap<String, SjablonTallNavn>();
    for (SjablonTallNavn sjablonTallNavn : SjablonTallNavn.values()) {
      sjablontallMap.put(sjablonTallNavn.getId(), sjablonTallNavn);
    }

    // Henter sjabloner
    var sjablonListe = hentSjabloner();

    // Bygger grunnlag til core og utfører delberegninger
    return utfoerDelberegninger(beregnTotalSaertilskuddGrunnlag, sjablontallMap, sjablonListe, soknadsBarnInfo.getId());
  }

  //  Validerer at det kun er oppgitt ett SoknadsbarnInfo-grunnlag og at mapping til SoknadsBarnInfo objekt ikke feiler
  private SoknadsBarnInfo validerSoknadsbarn(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {
    List<Grunnlag> soknadsbarnInfoGrunnlag = beregnTotalSaertilskuddGrunnlag.getGrunnlagListe().stream()
        .filter(grunnlag -> grunnlag.getType().equals(GrunnlagType.SOKNADSBARN_INFO)).toList();
    if (soknadsbarnInfoGrunnlag.size() != 1) {
      throw new UgyldigInputException("Det må være nøyaktig ett søknadsbarn i beregningsgrunnlaget");
    }
    SoknadsBarnInfo soknadsBarnInfo = grunnlagTilObjekt(soknadsbarnInfoGrunnlag.get(0), SoknadsBarnInfo.class);

    beregnTotalSaertilskuddGrunnlag.getGrunnlagListe().stream()
        .filter(grunnlag -> grunnlag.getType().equals(GrunnlagType.SAMVAERSKLASSE)).map(grunnlag -> grunnlagTilObjekt(grunnlag, Samvaersklasse.class))
        .forEach(samvaersklasse -> {
          if ((samvaersklasse.getSoknadsbarnId() == soknadsBarnInfo.getId()) &&
              (!samvaersklasse.getSoknadsbarnFodselsdato().equals(soknadsBarnInfo.getFodselsdato()))) {
            throw new UgyldigInputException("Fødselsdato for søknadsbarn stemmer ikke overens med fødselsdato til barnet i Samværsklasse-grunnlaget");
          }
        });
    return soknadsBarnInfo;
  }

  //==================================================================================================================================================

  // Bygger grunnlag til core og kaller delberegninger
  private HttpResponse<BeregnetTotalSaertilskuddResultat> utfoerDelberegninger(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      Map<String, SjablonTallNavn> sjablontallMap, SjablonListe sjablonListe, Integer soknadsBarnId) {

    var grunnlagReferanseListe = new ArrayList<Grunnlag>();

    // ++ Bidragsevne
    var bidragsevneGrunnlagTilCore = bidragsevneCoreMapper.mapBidragsevneGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag, sjablontallMap,
        sjablonListe);
    var bidragsevneResultatFraCore = beregnBidragsevne(bidragsevneGrunnlagTilCore);
    grunnlagReferanseListe.addAll(
        lagGrunnlagListeForDelberegning(beregnTotalSaertilskuddGrunnlag, bidragsevneResultatFraCore.getResultatPeriodeListe(),
            bidragsevneResultatFraCore.getSjablonListe()));

    // ++ BPs andel av særtilskudd
    var bpAndelSaertilskuddGrunnlagTilCore = bpAndelSaertilskuddCoreMapper.mapBPsAndelSaertilskuddGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag,
        sjablontallMap, sjablonListe);
    var bpAndelSaertilskuddResultatFraCore = beregnBPAndelSaertilskudd(bpAndelSaertilskuddGrunnlagTilCore);
    grunnlagReferanseListe.addAll(
        lagGrunnlagListeForDelberegning(beregnTotalSaertilskuddGrunnlag, bpAndelSaertilskuddResultatFraCore.getResultatPeriodeListe(),
            bpAndelSaertilskuddResultatFraCore.getSjablonListe()));
    grunnlagReferanseListe.addAll(
        lagGrunnlagListeForBeregnedeGrunnlagBPsAndelSaertilskudd(bpAndelSaertilskuddResultatFraCore.getResultatPeriodeListe()));

    // ++ Samværsfradrag
    var samvaersfradragGrunnlagTilCore = samvaersfradragCoreMapper.mapSamvaersfradragGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag, sjablonListe);
    var samvaersfradragResultatFraCore = beregnSamvaersfradrag(samvaersfradragGrunnlagTilCore);
    grunnlagReferanseListe.addAll(
        lagGrunnlagListeForDelberegning(beregnTotalSaertilskuddGrunnlag, samvaersfradragResultatFraCore.getResultatPeriodeListe(),
            samvaersfradragResultatFraCore.getSjablonListe()));

    // ++ Særtilskudd (totalberegning)
    var saertilskuddGrunnlagTilCore = saertilskuddCoreMapper.mapSaertilskuddGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag,
        bidragsevneResultatFraCore, bpAndelSaertilskuddResultatFraCore, samvaersfradragResultatFraCore, soknadsBarnId, sjablonListe);
    var saertilskuddResultatFraCore = beregnSaertilskudd(saertilskuddGrunnlagTilCore);

    grunnlagReferanseListe.addAll(lagGrunnlagReferanseListeSaertilskudd(beregnTotalSaertilskuddGrunnlag, saertilskuddResultatFraCore,
        saertilskuddGrunnlagTilCore, bidragsevneResultatFraCore, bpAndelSaertilskuddResultatFraCore, samvaersfradragResultatFraCore));

    var unikeReferanserListe = grunnlagReferanseListe.stream().sorted(comparing(Grunnlag::getReferanse)).distinct().toList();

    // Bygger responsobjekt
    return HttpResponse.Companion.from(HttpStatus.OK, new BeregnetTotalSaertilskuddResultat(saertilskuddResultatFraCore, unikeReferanserListe));
  }

  private List<Grunnlag> lagGrunnlagListeForDelberegning(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      List<? extends IResultatPeriode> resultatPeriodeListe, List<SjablonResultatGrunnlagCore> sjablonListe) {
    var resultatGrunnlagListe = new ArrayList<Grunnlag>();

    // Bygger opp oversikt over alle grunnlag som er brukt i beregningen
    var grunnlagReferanseListe = resultatPeriodeListe.stream()
        .flatMap(resultatPeriodeCore -> resultatPeriodeCore.getGrunnlagReferanseListe().stream()
            .map(String::new))
        .distinct()
        .toList();

    // Matcher mottatte grunnlag med grunnlag som er brukt i beregningen
    resultatGrunnlagListe.addAll(beregnTotalSaertilskuddGrunnlag.getGrunnlagListe().stream()
        .filter(grunnlag -> grunnlagReferanseListe.contains(grunnlag.getReferanse()))
        .map(grunnlag -> new Grunnlag(grunnlag.getReferanse(), grunnlag.getType(), grunnlag.getInnhold()))
        .toList());

    // Danner grunnlag basert på liste over sjabloner som er brukt i beregningen
    resultatGrunnlagListe.addAll(mapSjabloner(sjablonListe));

    return resultatGrunnlagListe;
  }


  private List<Grunnlag> lagGrunnlagListeForBeregnedeGrunnlagBPsAndelSaertilskudd(List<ResultatPeriodeCore> resultatPeriodeCoreListe) {
    List<Grunnlag> beregnedeGrunnlagListe = new ArrayList<>();
    for (var resultatPeriode : resultatPeriodeCoreListe) {
      beregnedeGrunnlagListe.addAll(resultatPeriode.getBeregnedeGrunnlag().getInntektBPListe().stream().map(
          inntektBase -> new Grunnlag(inntektBase.getReferanse(), GrunnlagType.INNTEKT, tilJsonNode(
              new BPInntekt(resultatPeriode.getPeriode().getDatoFom(), resultatPeriode.getPeriode().getDatoTil(), Rolle.BP,
                  inntektBase.getInntektType().toString(), inntektBase.getInntektBelop())))).toList());
      beregnedeGrunnlagListe.addAll(resultatPeriode.getBeregnedeGrunnlag().getInntektBMListe().stream().map(
          inntektBase -> new Grunnlag(inntektBase.getReferanse(), GrunnlagType.INNTEKT, tilJsonNode(
              new BMInntekt(resultatPeriode.getPeriode().getDatoFom(), resultatPeriode.getPeriode().getDatoTil(),
                  inntektBase.getInntektType().toString(), inntektBase.getInntektBelop(), Rolle.BM, inntektBase.getDeltFordel(),
                  inntektBase.getSkatteklasse2())))).toList());
      beregnedeGrunnlagListe.addAll(resultatPeriode.getBeregnedeGrunnlag().getInntektBBListe().stream().map(
          inntektBase -> new Grunnlag(inntektBase.getReferanse(), GrunnlagType.INNTEKT, tilJsonNode(
              new SBInntekt(resultatPeriode.getPeriode().getDatoFom(), resultatPeriode.getPeriode().getDatoTil(), Rolle.SB,
                  inntektBase.getInntektType().toString(), inntektBase.getInntektBelop(), null)))).toList());
    }
    return beregnedeGrunnlagListe;
  }

  // Barnebidrag
  private List<Grunnlag> lagGrunnlagReferanseListeSaertilskudd(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      BeregnSaertilskuddResultatCore beregnSaertilskuddResultatCore, BeregnSaertilskuddGrunnlagCore beregnSaertilskuddGrunnlagCore,
      BeregnBidragsevneResultatCore bidragsevneResultatFraCore, BeregnBPsAndelSaertilskuddResultatCore beregnBPsAndelSaertilskuddResultatCore,
      BeregnSamvaersfradragResultatCore samvaersfradragResultatFraCore) {
    var resultatGrunnlagListe = new ArrayList<Grunnlag>();

    // Bygger opp oversikt over alle grunnlag som er brukt i beregningen
    var grunnlagReferanseListe = beregnSaertilskuddResultatCore.getResultatPeriodeListe().stream()
        .flatMap(resultatPeriodeCore -> resultatPeriodeCore.getGrunnlagReferanseListe().stream()
            .map(String::new))
        .distinct()
        .toList();

    // Matcher mottatte grunnlag med grunnlag som er brukt i beregningen
    resultatGrunnlagListe.addAll(beregnTotalSaertilskuddGrunnlag.getGrunnlagListe().stream()
        .filter(grunnlag -> grunnlagReferanseListe.contains(grunnlag.getReferanse()))
        .map(grunnlag -> new Grunnlag(grunnlag.getReferanse(), grunnlag.getType(), grunnlag.getInnhold()))
        .toList());

    // Mapper ut delberegninger som er brukt som grunnlag
    resultatGrunnlagListe.addAll(beregnSaertilskuddGrunnlagCore.getBidragsevnePeriodeListe().stream()
        .filter(grunnlag -> grunnlagReferanseListe.contains(grunnlag.getReferanse()))
        .map(grunnlag -> new Grunnlag(grunnlag.getReferanse(),
            GrunnlagType.BIDRAGSEVNE, lagInnholdBidragsevne(grunnlag, bidragsevneResultatFraCore)))
        .toList());

    resultatGrunnlagListe.addAll(beregnSaertilskuddGrunnlagCore.getBPsAndelSaertilskuddPeriodeListe().stream()
        .filter(grunnlag -> grunnlagReferanseListe.contains(grunnlag.getReferanse()))
        .map(grunnlag -> new Grunnlag(grunnlag.getReferanse(), GrunnlagType.BPSANDELSAERTILSKUDD,
            lagInnholdBPsAndelSaertilskudd(grunnlag, beregnBPsAndelSaertilskuddResultatCore)))
        .toList());

    resultatGrunnlagListe.addAll(beregnSaertilskuddGrunnlagCore.getSamvaersfradragPeriodeListe().stream()
        .filter(grunnlag -> grunnlagReferanseListe.contains(grunnlag.getReferanse()))
        .map(grunnlag -> new Grunnlag(grunnlag.getReferanse(), GrunnlagType.SAMVAERSFRADRAG,
            lagInnholdSamvaersfradrag(grunnlag, samvaersfradragResultatFraCore)))
        .toList());

    return resultatGrunnlagListe;
  }

  // Mapper ut innhold fra delberegning Bidragsevne
  private JsonNode lagInnholdBidragsevne(BidragsevnePeriodeCore bidragsevnePeriodeCore, BeregnBidragsevneResultatCore beregnBidragsevneResultatCore) {
    var grunnlagReferanseListe = getReferanseListeFromResultatPeriodeCore(beregnBidragsevneResultatCore.getResultatPeriodeListe(),
        bidragsevnePeriodeCore.getPeriodeDatoFraTil().getDatoFom());
    BidragsevneResultatPeriode bidragsevne = new BidragsevneResultatPeriode(bidragsevnePeriodeCore.getPeriodeDatoFraTil().getDatoFom(),
        bidragsevnePeriodeCore.getPeriodeDatoFraTil().getDatoTil(), bidragsevnePeriodeCore.getBidragsevneBelop(), grunnlagReferanseListe);
    return tilJsonNode(bidragsevne);
  }

  // Mapper ut innhold fra delberegning BPsAndelSaertilskudd
  private JsonNode lagInnholdBPsAndelSaertilskudd(BPsAndelSaertilskuddPeriodeCore bPsAndelSaertilskuddPeriodeCore,
      BeregnBPsAndelSaertilskuddResultatCore beregnBPsAndelSaertilskuddResultatCore) {
    var grunnlagReferanseListe = getReferanseListeFromResultatPeriodeCore(beregnBPsAndelSaertilskuddResultatCore.getResultatPeriodeListe(),
        bPsAndelSaertilskuddPeriodeCore.getPeriodeDatoFraTil().getDatoFom());
    BPsAndelSaertilskuddResultatPeriode bPsAndelSaertilskudd = new BPsAndelSaertilskuddResultatPeriode(
        mapDato(bPsAndelSaertilskuddPeriodeCore.getPeriodeDatoFraTil().getDatoFom()),
        mapDato(bPsAndelSaertilskuddPeriodeCore.getPeriodeDatoFraTil().getDatoTil()), bPsAndelSaertilskuddPeriodeCore.getBPsAndelSaertilskuddBelop(),
        bPsAndelSaertilskuddPeriodeCore.getBPsAndelSaertilskuddProsent(), bPsAndelSaertilskuddPeriodeCore.getBarnetErSelvforsorget(),
        grunnlagReferanseListe);
    return tilJsonNode(bPsAndelSaertilskudd);
  }

  // Mapper ut innhold fra delberegning Samvaersfradrag
  private JsonNode lagInnholdSamvaersfradrag(SamvaersfradragPeriodeCore samvaersfradragPeriodeCore,
      BeregnSamvaersfradragResultatCore beregnSamvaersfradragResultatCore) {
    var grunnlagReferanseListe = getReferanseListeFromResultatPeriodeCore(beregnSamvaersfradragResultatCore.getResultatPeriodeListe(),
        samvaersfradragPeriodeCore.getPeriodeDatoFraTil().getDatoFom());
    SamvaersfradragResultatPeriode samvaersfradrag = new SamvaersfradragResultatPeriode(
        mapDato(samvaersfradragPeriodeCore.getPeriodeDatoFraTil().getDatoFom()),
        mapDato(samvaersfradragPeriodeCore.getPeriodeDatoFraTil().getDatoTil()), samvaersfradragPeriodeCore.getSamvaersfradragBelop(),
        samvaersfradragPeriodeCore.getBarnPersonId(), grunnlagReferanseListe);
    return tilJsonNode(samvaersfradrag);
  }

  private static LocalDate mapDato(LocalDate dato) {
    return dato.isAfter(LocalDate.parse("9999-12-31")) ? LocalDate.parse("9999-12-31") : dato;
  }

  private List<Grunnlag> mapSjabloner(List<SjablonResultatGrunnlagCore> sjablonResultatGrunnlagCoreListe) {
    return sjablonResultatGrunnlagCoreListe.stream()
        .map(sjablon -> {
              var sjablonPeriode = new SjablonResultatPeriode(mapDato(sjablon.getPeriode().getDatoFom()), mapDato(sjablon.getPeriode().getDatoTil()),
                  sjablon.getNavn(), sjablon.getVerdi().intValue());
              return new Grunnlag(sjablon.getReferanse(), GrunnlagType.SJABLON, tilJsonNode(sjablonPeriode));
            }
        )
        .toList();
  }

  private List<String> getReferanseListeFromResultatPeriodeCore(List<? extends IResultatPeriode> resultatPeriodeListe, LocalDate datoFom) {
    return resultatPeriodeListe.stream()
        .filter(resultatPeriodeCore -> datoFom.equals(resultatPeriodeCore.getPeriode().getDatoFom()))
        .findFirst()
        .map(IResultatPeriode::getGrunnlagReferanseListe)
        .orElse(emptyList());
  }

  //==================================================================================================================================================

  // Kaller core for beregning av bidragsevne
  private BeregnBidragsevneResultatCore beregnBidragsevne(BeregnBidragsevneGrunnlagCore bidragsevneGrunnlagTilCore) {

    BeregnBidragsevneResultatCore bidragsevneResultatFraCore;

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Bidragsevne - grunnlag for beregning: {}", bidragsevneGrunnlagTilCore);
    }

    // Kaller core-modulen for beregning av bidragsevne
    try {
      bidragsevneResultatFraCore = bidragsevneCore.beregnBidragsevne(bidragsevneGrunnlagTilCore);
    } catch (Exception e) {
      throw new UgyldigInputException("Ugyldig input ved beregning av bidragsevne: " + e.getMessage());
    }

    if (!bidragsevneResultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av bidragsevne. Følgende avvik ble funnet: " + System.lineSeparator()
          + bidragsevneResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining(System.lineSeparator())));
      LOGGER.info("Bidragsevne - grunnlag for beregning:" + System.lineSeparator()
          + "beregnDatoFra= " + bidragsevneGrunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + bidragsevneGrunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "antallBarnIEgetHusholdPeriodeListe= " + bidragsevneGrunnlagTilCore.getAntallBarnIEgetHusholdPeriodeListe() + System.lineSeparator()
          + "bostatusPeriodeListe= " + bidragsevneGrunnlagTilCore.getBostatusPeriodeListe() + System.lineSeparator()
          + "inntektPeriodeListe= " + bidragsevneGrunnlagTilCore.getInntektPeriodeListe() + System.lineSeparator()
          + "særfradragPeriodeListe= " + bidragsevneGrunnlagTilCore.getSaerfradragPeriodeListe() + System.lineSeparator()
          + "skatteklassePeriodeListe= " + bidragsevneGrunnlagTilCore.getSkatteklassePeriodeListe());
      throw new UgyldigInputException("Ugyldig input ved beregning av bidragsevne. Følgende avvik ble funnet: "
          + bidragsevneResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Bidragsevne - resultat av beregning: {}", bidragsevneResultatFraCore.getResultatPeriodeListe());
    }

    return bidragsevneResultatFraCore;
  }

  // Kaller core for beregning av BPs andel av særtilskudd
  private BeregnBPsAndelSaertilskuddResultatCore beregnBPAndelSaertilskudd(
      BeregnBPsAndelSaertilskuddGrunnlagCore bpAndelSaertilskuddGrunnlagTilCore) {

    BeregnBPsAndelSaertilskuddResultatCore bpAndelSaertilskuddResultatFraCore;

    if (SECURE_LOGGER.isDebugEnabled()) {
      SECURE_LOGGER.debug("BPs andel av særtilskudd - grunnlag for beregning: {}", bpAndelSaertilskuddGrunnlagTilCore);
    }

    // Kaller core-modulen for beregning av BPs andel av særtilskudd
    try {
      bpAndelSaertilskuddResultatFraCore = bpAndelSaertilskuddCore.beregnBPsAndelSaertilskudd(bpAndelSaertilskuddGrunnlagTilCore);
    } catch (Exception e) {
      throw new UgyldigInputException("Ugyldig input ved beregning av BPs andel av særtilskudd: " + e.getMessage());
    }

    if (!bpAndelSaertilskuddResultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av BPs andel av særtilskudd. Følgende avvik ble funnet: " + System.lineSeparator()
          + bpAndelSaertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      SECURE_LOGGER.error("Ugyldig input ved beregning av BPs andel av særtilskudd. Følgende avvik ble funnet: " + System.lineSeparator()
          + bpAndelSaertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      SECURE_LOGGER.info("BPs andel av særtilskudd - grunnlag for beregning:" + System.lineSeparator()
          + "beregnDatoFra= " + bpAndelSaertilskuddGrunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + bpAndelSaertilskuddGrunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "nettoSaertilskuddPeriodeListe= " + bpAndelSaertilskuddGrunnlagTilCore.getNettoSaertilskuddPeriodeListe() + System.lineSeparator()
          + "inntektBPPeriodeListe= " + bpAndelSaertilskuddGrunnlagTilCore.getInntektBPPeriodeListe() + System.lineSeparator()
          + "inntektBMPeriodeListe= " + bpAndelSaertilskuddGrunnlagTilCore.getInntektBMPeriodeListe() + System.lineSeparator()
          + "inntektBBPeriodeListe= " + bpAndelSaertilskuddGrunnlagTilCore.getInntektBBPeriodeListe() + System.lineSeparator());
      throw new UgyldigInputException("Ugyldig input ved beregning av BPs andel av særtilskudd. Følgende avvik ble funnet: "
          + bpAndelSaertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    if (SECURE_LOGGER.isDebugEnabled()) {
      SECURE_LOGGER.debug("BPs andel av særtilskudd - resultat av beregning: {}", bpAndelSaertilskuddResultatFraCore.getResultatPeriodeListe());
    }

    return bpAndelSaertilskuddResultatFraCore;
  }

  // Kaller core for beregning av samværsfradrag
  private BeregnSamvaersfradragResultatCore beregnSamvaersfradrag(BeregnSamvaersfradragGrunnlagCore samvaersfradragGrunnlagTilCore) {

    BeregnSamvaersfradragResultatCore samvaersfradragResultatFraCore;

    if (SECURE_LOGGER.isDebugEnabled()) {
      SECURE_LOGGER.debug("Samværsfradrag - grunnlag for beregning: {}", samvaersfradragGrunnlagTilCore);
    }

    // Kaller core-modulen for beregning av samværsfradrag
    try {
      samvaersfradragResultatFraCore = samvaersfradragCore.beregnSamvaersfradrag(samvaersfradragGrunnlagTilCore);
    } catch (Exception e) {
      throw new UgyldigInputException("Ugyldig input ved beregning av samværsfradrag: " + e.getMessage());
    }

    if (!samvaersfradragResultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av samværsfradrag. Følgende avvik ble funnet: " + System.lineSeparator()
          + samvaersfradragResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      SECURE_LOGGER.error("Ugyldig input ved beregning av samværsfradrag. Følgende avvik ble funnet: " + System.lineSeparator()
          + samvaersfradragResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      SECURE_LOGGER.info("Samværsfradrag - grunnlag for beregning: " + System.lineSeparator()
          + "beregnDatoFra= " + samvaersfradragGrunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + samvaersfradragGrunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "samvaersklassePeriodeListe= " + samvaersfradragGrunnlagTilCore.getSamvaersklassePeriodeListe() + System.lineSeparator());
      throw new UgyldigInputException("Ugyldig input ved beregning av samværsfradrag. Følgende avvik ble funnet: "
          + samvaersfradragResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    if (SECURE_LOGGER.isDebugEnabled()) {
      SECURE_LOGGER.debug("Samværsfradrag - resultat av beregning: {}", samvaersfradragResultatFraCore.getResultatPeriodeListe());
    }

    return samvaersfradragResultatFraCore;
  }

  // Kaller core for beregning av særtilskudd
  private BeregnSaertilskuddResultatCore beregnSaertilskudd(BeregnSaertilskuddGrunnlagCore saertilskuddGrunnlagTilCore) {

    BeregnSaertilskuddResultatCore saertilskuddResultatFraCore;

    if (SECURE_LOGGER.isDebugEnabled()) {
      SECURE_LOGGER.debug("Særtilskudd - grunnlag for beregning: {}", saertilskuddGrunnlagTilCore);
    }

    // Kaller core-modulen for beregning av særtilskudd
    try {
      saertilskuddResultatFraCore = saertilskuddCore.beregnSaertilskudd(saertilskuddGrunnlagTilCore);
    } catch (Exception e) {
      throw new UgyldigInputException("Ugyldig input ved beregning av særtilskudd: " + e.getMessage());
    }

    if (!saertilskuddResultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av særtilskudd. Følgende avvik ble funnet: " + System.lineSeparator()
          + saertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      SECURE_LOGGER.error("Ugyldig input ved beregning av særtilskudd. Følgende avvik ble funnet: " + System.lineSeparator()
          + saertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      SECURE_LOGGER.info("Særtilskudd - grunnlag for beregning: " + System.lineSeparator()
          + "beregnDatoFra= " + saertilskuddGrunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + saertilskuddGrunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "soknadsbarnPersonId= " + saertilskuddGrunnlagTilCore.getSoknadsbarnPersonId() + System.lineSeparator()
          + "bidragsevnePeriodeListe= " + saertilskuddGrunnlagTilCore.getBidragsevnePeriodeListe() + System.lineSeparator()
          + "bPsAndelSaertilskuddPeriodeListe= " + saertilskuddGrunnlagTilCore.getBPsAndelSaertilskuddPeriodeListe() + System.lineSeparator()
          + "samvaersfradragPeriodeListe= " + saertilskuddGrunnlagTilCore.getSamvaersfradragPeriodeListe() + System.lineSeparator()
          + "lopendeBidragPeriodeListe= " + saertilskuddGrunnlagTilCore.getLopendeBidragPeriodeListe() + System.lineSeparator());
      throw new UgyldigInputException("Ugyldig input ved beregning av særtilskudd. Følgende avvik ble funnet: "
          + saertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    if (SECURE_LOGGER.isDebugEnabled()) {
      SECURE_LOGGER.debug("Særtilskudd - resultat av beregning: {}", saertilskuddResultatFraCore.getResultatPeriodeListe());
    }

    return saertilskuddResultatFraCore;
  }

  //==================================================================================================================================================

  // Henter sjabloner
  private SjablonListe hentSjabloner() {

    // Henter sjabloner for sjablontall
    var sjablonSjablontallListe = Optional.ofNullable(sjablonConsumer.hentSjablonSjablontall().getResponseEntity().getBody()).orElse(emptyList());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Antall sjabloner hentet av type Sjablontall: {}", sjablonSjablontallListe.size());
    }

    // Henter sjabloner for samværsfradrag
    var sjablonSamvaersfradragListe = Optional.ofNullable(sjablonConsumer.hentSjablonSamvaersfradrag().getResponseEntity().getBody())
        .orElse(emptyList());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Antall sjabloner hentet av type Samværsfradrag: {}", sjablonSamvaersfradragListe.size());
    }

    // Henter sjabloner for bidragsevne
    var sjablonBidragsevneListe = Optional.ofNullable(sjablonConsumer.hentSjablonBidragsevne().getResponseEntity().getBody()).orElse(emptyList());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Antall sjabloner hentet av type Bidragsevne: {}", sjablonBidragsevneListe.size());
    }

    // Henter sjabloner for trinnvis skattesats
    var sjablonTrinnvisSkattesatsListe = Optional.ofNullable(sjablonConsumer.hentSjablonTrinnvisSkattesats().getResponseEntity().getBody())
        .orElse(emptyList());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Antall sjabloner hentet av type Trinnvis skattesats: {}", sjablonTrinnvisSkattesatsListe.size());
    }

    return new SjablonListe(sjablonSjablontallListe, sjablonSamvaersfradragListe, sjablonBidragsevneListe, sjablonTrinnvisSkattesatsListe);
  }
}
