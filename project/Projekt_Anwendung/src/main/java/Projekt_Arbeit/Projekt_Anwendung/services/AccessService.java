package Projekt_Arbeit.Projekt_Anwendung.services;

import Projekt_Arbeit.Projekt_Anwendung.Exception.NotFoundException;
import Projekt_Arbeit.Projekt_Anwendung.Exception.UsernameExistsException;
import Projekt_Arbeit.Projekt_Anwendung.dtos.LoginDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.LoginDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.entities.Broker;
import Projekt_Arbeit.Projekt_Anwendung.entities.Investor;
import Projekt_Arbeit.Projekt_Anwendung.repositories.BrokerRepository;
import Projekt_Arbeit.Projekt_Anwendung.repositories.InvestorRepository;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessManager;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import Projekt_Arbeit.Projekt_Anwendung.security.PasswordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AccessService {

    private final AccessManager accessManager;
    private final InvestorRepository investorRepository;

    private final BrokerRepository brokerRepository;

@Autowired
    public AccessService(AccessManager accessManager,
                         InvestorRepository investorRepository,
                         BrokerRepository brokerRepository) {
        this.accessManager = accessManager;
        this.brokerRepository = brokerRepository;
        this.investorRepository = investorRepository;
    }

    public ResponseEntity<LoginDtoOut> methodForInvestorLogin(LoginDtoIn loginDtoIn)
    {
        String username = loginDtoIn.getUsername();
        String password = loginDtoIn.getPassword();

        Investor investor = this.investorRepository.findByUsername(username);

        if (PasswordTools.checkPassword(password, investor.getPasswordSalt(), investor.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AccessToken accessToken = accessManager.createInvestorToken(investor);
        LoginDtoOut returnData = new LoginDtoOut(investor.getInvestor_id(), investor.getUsername(), accessToken);

        return  ResponseEntity.ok(returnData);
    }

    public ResponseEntity<LoginDtoOut> methodForBrokerLogin(LoginDtoIn loginDtoIn)
    {
        String username = loginDtoIn.getUsername();
        String password = loginDtoIn.getPassword();

        Broker broker = this.brokerRepository.findByUsername(username);

        if (!PasswordTools.checkPassword(password, broker.getPasswordSalt(), broker.getPasswordHash())) {
            AccessToken accessToken = accessManager.createBrokerToken(broker);
            LoginDtoOut returnData = new LoginDtoOut(broker.getBroker_id(), broker.getUsername(), accessToken);

            return  ResponseEntity.ok(returnData);
        }
        else {
            throw new NotFoundException("username or password false, please try again!");
        }

    }
    public ResponseEntity<Boolean> logoutInvestor(AccessToken accessToken) {
        return ResponseEntity.ok(accessManager.removeInvestorTokenAfterDelete(accessToken));
    }
    public ResponseEntity<Boolean> logoutBroker(AccessToken accessToken) {
        return ResponseEntity.ok(accessManager.removeBrokerTokenAfterDelete(accessToken));
    }
}
