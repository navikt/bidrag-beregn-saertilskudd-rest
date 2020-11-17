package no.nav.bidrag.beregn.saertilskudd.rest.controller;

import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddResultat;
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
  public ResponseEntity<BeregnSaertilskuddResultat> beregnSaertilskudd(
      @RequestBody BeregnSaertilskuddGrunnlag beregnSaertilskuddGrunnlag) {
    var beregnSaertilskuddResultat = beregnSaertilskuddService.beregn(beregnSaertilskuddGrunnlag);
    return new ResponseEntity<>(beregnSaertilskuddResultat.getResponseEntity().getBody(),
        beregnSaertilskuddResultat.getResponseEntity().getStatusCode());
  }
}
