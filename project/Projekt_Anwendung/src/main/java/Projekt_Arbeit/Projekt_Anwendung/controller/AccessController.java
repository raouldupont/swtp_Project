package Projekt_Arbeit.Projekt_Anwendung.controller;

import Projekt_Arbeit.Projekt_Anwendung.dtos.LoginDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.LoginDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessManager;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import Projekt_Arbeit.Projekt_Anwendung.services.AccessService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path ="access")
public class AccessController {

    private AccessService accessService;

    @PostMapping("login/Investor")
    public ResponseEntity<LoginDtoOut> loginForInvestor(@RequestBody LoginDtoIn loginDtoIn)
    {
        return this.accessService.methodForInvestorLogin(loginDtoIn);
    }

    @PostMapping("login/Broker")
    public ResponseEntity<LoginDtoOut> loginForBroker(@RequestBody LoginDtoIn loginDtoIn)
    {
       return this.accessService.methodForBrokerLogin(loginDtoIn);
    }

    @DeleteMapping("/logout/Investor")
    public ResponseEntity<Boolean> logoutForInvestor(@RequestHeader AccessToken accessToken)
    {
       return this.accessService.logoutInvestor(accessToken);
    }

    @DeleteMapping("/logout/Broker")
    public ResponseEntity<Boolean> logoutForBroker(@RequestHeader AccessToken accessToken)
    {
        return this.accessService.logoutBroker(accessToken);
    }

}
