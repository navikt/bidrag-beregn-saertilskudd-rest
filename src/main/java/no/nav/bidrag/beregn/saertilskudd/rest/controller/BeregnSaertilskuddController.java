package no.nav.bidrag.beregn.saertilskudd.rest.controller;

import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnetTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.service.BeregnSaertilskuddService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beregn")
public class BeregnSaertilskuddController {

  private final BeregnSaertilskuddService beregnSaertilskuddService;

  public BeregnSaertilskuddController(BeregnSaertilskuddService beregnSaertilskuddService) {
    this.beregnSaertilskuddService = beregnSaertilskuddService;
  }

  @PostMapping(path = "/saertilskudd")
  public ResponseEntity<BeregnetTotalSaertilskuddResultat> beregnTotalSaertilskudd(
      @RequestBody BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {
    var beregnTotalSaertilskuddResultat = beregnSaertilskuddService.beregn(beregnTotalSaertilskuddGrunnlag);
    return new ResponseEntity<>(beregnTotalSaertilskuddResultat.getResponseEntity().getBody(),
        beregnTotalSaertilskuddResultat.getResponseEntity().getStatusCode());
  }
}
