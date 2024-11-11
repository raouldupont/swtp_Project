package Projekt_Arbeit.Projekt_Anwendung.controller;

import Projekt_Arbeit.Projekt_Anwendung.dtos.*;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessManager;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import Projekt_Arbeit.Projekt_Anwendung.services.InvestorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/investors")
public class InvestorController
{
    @Autowired
    private InvestorService investorService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/registration")
    public ResponseEntity<LoginDtoOut> investorSignIn(@RequestBody InvestorCreateDtoIn investorCreateDtoIn)
    {
       return this.investorService.createInvestor(investorCreateDtoIn);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(path = "connexion/{id}")
    public void investorConnexion(@RequestBody AccessToken accessToken, @PathVariable int id)
    {
        this.investorService.getInvestorById(accessToken, id);
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/update/{id}")
    public ResponseEntity<InvestorDtoOut> updateInvestor(@RequestHeader AccessToken accessToken, @PathVariable("id") int investorId, @RequestBody InvestorUpdateDtoIn investorUpdateDtoIn){
       return this.investorService.updateInvestor(investorUpdateDtoIn, accessToken, investorId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInvestor(@RequestHeader AccessToken accessToken, @PathVariable("id") int investorId)
    {
       return this.investorService.deleteInvestorById(accessToken, investorId);
    }


}
