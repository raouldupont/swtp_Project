package Projekt_Arbeit.Projekt_Anwendung.controller;

import Projekt_Arbeit.Projekt_Anwendung.dtos.*;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import Projekt_Arbeit.Projekt_Anwendung.services.BrokerService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@NoArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value ="brokers")
public class BrokerController
{
    @Autowired
    private BrokerService brokerService;

    public BrokerController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "registration")
    public ResponseEntity<LoginDtoOut> brokerSignIn(@RequestBody BrokerCreateDtoIn brokerCreateDtoIn)
    {
        return this.brokerService.createBroker(brokerCreateDtoIn);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(path = "connexion/{id}")
    public void brokerConnexion(@RequestBody AccessToken accessToken, @PathVariable int id)
    {
        this.brokerService.getBrokerById(accessToken, id);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("update/{id}")
    public ResponseEntity<BrokerDtoOut> updateBroker(@RequestHeader AccessToken accessToken, @PathVariable("id") int brokerId, @RequestBody BrokerCreateDtoIn brokerCreateDtoIn){
        return this.brokerService.updateBroker(brokerCreateDtoIn, accessToken, brokerId);
    }

    @GetMapping(path = "getBrokers")
    public ResponseEntity<Collection<BrokerDtoOut>> getAllRegisteredBrokers (@RequestHeader AccessToken accessToken)
    {
        return this.brokerService.getAllBrokers(accessToken);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteConnectedBroker(@RequestHeader AccessToken accessToken, @PathVariable("id") int investorId)
    {
       return this.brokerService.deleteBrokerById(accessToken, investorId);
    }

}
