package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneResultatCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddResultatCore;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.felles.dto.SjablonInnholdCore;
import no.nav.bidrag.beregn.felles.dto.SjablonNokkelCore;
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonInnholdNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNokkelNavn;
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevnePeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.LopendeBidragPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.LopendeBidrag;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore;

public class SaertilskuddCoreMapper extends CoreMapper {

  public BeregnSaertilskuddGrunnlagCore mapSaertilskuddGrunnlagTilCore(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      BeregnBidragsevneResultatCore beregnBidragsevneResultatCore, BeregnBPsAndelSaertilskuddResultatCore beregnBPsAndelSaertilskuddResultatCore,
      BeregnSamvaersfradragResultatCore beregnSamvaersfradragResultatCore, Integer soknadsBarnId, SjablonListe sjablonListe) {
    // Løp gjennom output fra beregning av bidragsevne og bygg opp ny input-liste til core
    var bidragsevnePeriodeCoreListe =
        beregnBidragsevneResultatCore.getResultatPeriodeListe()
            .stream()
            .map(resultat -> new BidragsevnePeriodeCore(byggReferanseForDelberegning("Delberegning_BP_Bidragsevne", resultat.getPeriode().getDatoFom()),
                new PeriodeCore(resultat.getPeriode().getDatoFom(), resultat.getPeriode().getDatoTil()),
                resultat.getResultatBeregning().getResultatEvneBelop()))
            .collect(toList());

    // Løp gjennom output fra beregning av BPs andel særtilskudd og bygg opp ny input-liste til core
    var bpAndelSaertilskuddPeriodeCoreListe =
        beregnBPsAndelSaertilskuddResultatCore.getResultatPeriodeListe()
            .stream()
            .map(resultat -> new BPsAndelSaertilskuddPeriodeCore(byggReferanseForDelberegning("Delberegning_BP_AndelSaertilskudd", resultat.getPeriode().getDatoFom()),
                new PeriodeCore(resultat.getPeriode().getDatoFom(), resultat.getPeriode().getDatoTil()),
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
                    new SamvaersfradragPeriodeCore(byggReferanseForDelberegning("Delberegning_BP_Samvaersfradrag", resultatperiode.getPeriode().getDatoFom()),
                        new PeriodeCore(resultatperiode.getPeriode().getDatoFom(),
                            resultatperiode.getPeriode().getDatoTil()),
                        resultatberegning.getBarnPersonId(),
                        resultatberegning.getResultatSamvaersfradragBelop())))
            .collect(toList());

    var andreLopendeBidragListe = new ArrayList<LopendeBidragPeriodeCore>();

    for (Grunnlag grunnlag : beregnTotalSaertilskuddGrunnlag.getGrunnlagListe()) {
      if (GrunnlagType.LOPENDE_BIDRAG.equals(grunnlag.getType())) {
        LopendeBidrag lopendeBidrag = grunnlagTilObjekt(grunnlag, LopendeBidrag.class);
        andreLopendeBidragListe.add(lopendeBidrag.tilCore(grunnlag.getReferanse()));
      }
    }

    // Henter aktuelle sjabloner
    var sjablonPeriodeCoreListe = new ArrayList<>(mapSjablonSjablontall(sjablonListe.getSjablonSjablontallResponse(), SAERTILSKUDD,
        beregnTotalSaertilskuddGrunnlag, mapSjablontall()));

    return new BeregnSaertilskuddGrunnlagCore(beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra(), beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil(),
        soknadsBarnId, bidragsevnePeriodeCoreListe, bpAndelSaertilskuddPeriodeCoreListe, andreLopendeBidragListe, samvaersfradragPeriodeCoreListe, sjablonPeriodeCoreListe);
  }

  // Bygger referanse for delberegning
  private String byggReferanseForDelberegning(String delberegning, LocalDate dato) {
    return delberegning + "_" + dato.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
  }
}
