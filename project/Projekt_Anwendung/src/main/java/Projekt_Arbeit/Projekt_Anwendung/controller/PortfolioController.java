package Projekt_Arbeit.Projekt_Anwendung.controller;

import Projekt_Arbeit.Projekt_Anwendung.dtos.PortfolioDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.PortfolioDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import Projekt_Arbeit.Projekt_Anwendung.services.PortfolioService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping(path = "{id}/All_portfolios")


    public ResponseEntity<Collection<PortfolioDtoOut>> getMyPortfolios (@RequestHeader AccessToken accessToken, @PathVariable("id") int investor_id)
    {
       return this.portfolioService.getPortfolios(accessToken, investor_id);
    }

    @DeleteMapping("{id}/delete_Portfolio")
    public ResponseEntity<Void>sellPortfolio(@RequestHeader AccessToken accessToken, @PathVariable("id") int investor_id,
                                         @PathVariable("id") int portfolio_id)
    {
       return this.portfolioService.deletePortfolio(accessToken, investor_id, portfolio_id);
    }

    @PostMapping("{investor_id}/new_portfolio/{broker_id}")
    public ResponseEntity<PortfolioDtoOut> createPortfolio(
            @RequestHeader AccessToken accessToken,
            @PathVariable Integer investor_id,
            @PathVariable Integer broker_id,
            @RequestBody PortfolioDtoIn portfolioDtoIn) {
       return this.portfolioService.createPortfolio(accessToken, investor_id, broker_id, portfolioDtoIn);
    }
}

