package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneResultatCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddResultatCore;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevnePeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.LopendeBidragPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.LopendeBidrag;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore;

public class SaertilskuddCoreMapper extends CoreMapper {

  public BeregnSaertilskuddGrunnlagCore mapSaertilskuddGrunnlagTilCore(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      BeregnBidragsevneResultatCore beregnBidragsevneResultatCore, BeregnBPsAndelSaertilskuddResultatCore beregnBPsAndelSaertilskuddResultatCore,
      BeregnSamvaersfradragResultatCore beregnSamvaersfradragResultatCore, Integer soknadsBarnId) {
    // Løp gjennom output fra beregning av bidragsevne og bygg opp ny input-liste til core
    var bidragsevnePeriodeCoreListe =
        beregnBidragsevneResultatCore.getResultatPeriodeListe()
            .stream()
            .map(resultat -> new BidragsevnePeriodeCore("",
                new PeriodeCore(resultat.getResultatDatoFraTil().getPeriodeDatoFra(), resultat.getResultatDatoFraTil().getPeriodeDatoTil()),
                resultat.getResultatBeregning().getResultatEvneBelop()))
            .collect(toList());

    // Løp gjennom output fra beregning av BPs andel særtilskudd og bygg opp ny input-liste til core
    var bpAndelSaertilskuddPeriodeCoreListe =
        beregnBPsAndelSaertilskuddResultatCore.getResultatPeriodeListe()
            .stream()
            .map(resultat -> new BPsAndelSaertilskuddPeriodeCore("",
                new PeriodeCore(resultat.getResultatDatoFraTil().getPeriodeDatoFra(), resultat.getResultatDatoFraTil().getPeriodeDatoTil()),
                resultat.getResultatBeregning().getResultatAndelProsent(), resultat.getResultatBeregning().getResultatAndelBelop(),
                resultat.getResultatBeregning().getBarnetErSelvforsorget()))
            .collect(toList());

    // Løp gjennom output fra beregning av samværsfradrag og bygg opp ny input-liste til core
    var samvaersfradragPeriodeCoreListe =
        beregnSamvaersfradragResultatCore.getResultatPeriodeListe()
            .stream()
            .flatMap(resultatperiode -> resultatperiode.getResultatBeregningListe()
                .stream()
                .map(resultatberegning ->
                    new SamvaersfradragPeriodeCore("",
                        new PeriodeCore(resultatperiode.getResultatDatoFraTil().getPeriodeDatoFra(),
                            resultatperiode.getResultatDatoFraTil().getPeriodeDatoTil()),
                        resultatberegning.getBarnPersonId(),
                        resultatberegning.getResultatSamvaersfradragBelop())))
            .collect(toList());

    var andreLopendeBidragListe = new ArrayList<LopendeBidragPeriodeCore>();

    for (Grunnlag grunnlag : beregnTotalSaertilskuddGrunnlag.getGrunnlagListe()) {
      if (LOPENDE_BIDRAG_TYPE.equals(grunnlag.getType())) {
        LopendeBidrag lopendeBidrag = jsonNodeTilObjekt(grunnlag.getInnhold(), LopendeBidrag.class);
        andreLopendeBidragListe.add(lopendeBidrag.TilCore(grunnlag.getReferanse()));
//        var soknadsbarnId = hentSoknadsbarnId(grunnlag.getInnhold(), grunnlag.getType());
//        andreLopendeBidragListe.add(mapLopendeBidrag(grunnlag, Integer.parseInt(soknadsbarnId)));
      }
    }

    return new BeregnSaertilskuddGrunnlagCore(beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra(), beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil(),
        soknadsBarnId, bidragsevnePeriodeCoreListe, bpAndelSaertilskuddPeriodeCoreListe, andreLopendeBidragListe, samvaersfradragPeriodeCoreListe);
  }

//  private LopendeBidragPeriodeCore mapLopendeBidrag(Grunnlag grunnlag, int barnPersonId) {
//    JsonNode lopendeBidragBelopNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "belop");
//    evaluerNumberType(lopendeBidragBelopNode, "belop");
//    String lopendeBidragBelop = lopendeBidragBelopNode.asText();
//
//    JsonNode opprinneligBPsAndelUnderholdsKostnadBelopNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "opprinneligBPAndelUnderholdskostnadBelop");
//    evaluerNumberType(opprinneligBPsAndelUnderholdsKostnadBelopNode, "opprinneligBPAndelUnderholdskostnadBelop");
//    String opprinneligBPsAndelUnderholdsKostnadBelop = opprinneligBPsAndelUnderholdsKostnadBelopNode.asText();
//
//    JsonNode opprinneligSamvaersfradragBelopNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "opprinneligSamvaersfradragBelop");
//    evaluerNumberType(opprinneligSamvaersfradragBelopNode, "opprinneligSamvaersfradragBelop");
//    String opprinneligSamvaersfradragBelop = opprinneligSamvaersfradragBelopNode.asText();
//
//    JsonNode opprinneligBidragBelopNode = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "opprinneligBidragBelop");
//    evaluerNumberType(opprinneligBidragBelopNode, "opprinneligBidragBelop");
//    String opprinneligBidragBelop = opprinneligBidragBelopNode.asText();
//    return new LopendeBidragPeriodeCore(grunnlag.getReferanse(), mapPeriode(grunnlag.getInnhold(), grunnlag.getType()), barnPersonId, new BigDecimal(lopendeBidragBelop), new BigDecimal(opprinneligBPsAndelUnderholdsKostnadBelop), new BigDecimal(opprinneligSamvaersfradragBelop), new BigDecimal(opprinneligBidragBelop));
//  }
}
