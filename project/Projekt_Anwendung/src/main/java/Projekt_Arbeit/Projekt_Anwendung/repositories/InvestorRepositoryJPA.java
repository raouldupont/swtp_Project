package Projekt_Arbeit.Projekt_Anwendung.repositories;

import Projekt_Arbeit.Projekt_Anwendung.dtos.InvestorCreateDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.InvestorDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.dtos.InvestorUpdateDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.LoginDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.entities.Investor;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface InvestorRepositoryJPA {
    @Transactional
    ResponseEntity<LoginDtoOut> createInvestor(InvestorCreateDtoIn investorCreateDtoIn);
     ResponseEntity<InvestorDtoOut> updateInvestor(InvestorUpdateDtoIn investorUpdateDtoIn, AccessToken accessToken, int investorId);

     Investor createInvestorFromInvestorDTOIn(InvestorCreateDtoIn investorCreateDtoIn);
     Investor mergeAndRefresh(Investor investor);
}
