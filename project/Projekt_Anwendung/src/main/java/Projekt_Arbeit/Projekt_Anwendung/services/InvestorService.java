package Projekt_Arbeit.Projekt_Anwendung.services;

import Projekt_Arbeit.Projekt_Anwendung.Exception.NotFoundException;
import Projekt_Arbeit.Projekt_Anwendung.Exception.UsernameExistsException;
import Projekt_Arbeit.Projekt_Anwendung.dtos.*;
import Projekt_Arbeit.Projekt_Anwendung.entities.Investor;
import Projekt_Arbeit.Projekt_Anwendung.repositories.BrokerRepository;
import Projekt_Arbeit.Projekt_Anwendung.repositories.InvestorRepository;
import Projekt_Arbeit.Projekt_Anwendung.repositories.InvestorRepositoryJPA;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessManager;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import Projekt_Arbeit.Projekt_Anwendung.security.PasswordTools;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class InvestorService implements InvestorRepositoryJPA {
    @Autowired
    public InvestorService(InvestorRepository investorRepository,
                           AccessManager accessManager, EntityManager entityManager,
                           BrokerRepository brokerRepository) {
        this.investorRepository = investorRepository;
        this.accessManager = accessManager;
        this.entityManager = entityManager;
        this.brokerRepository = brokerRepository;
    }


    private final InvestorRepository investorRepository;
    BrokerRepository brokerRepository;
    private final AccessManager accessManager;
    private InvestorRepositoryJPA investorRepositoryJPA;

    private final EntityManager entityManager;


    @Override
    public ResponseEntity<LoginDtoOut> createInvestor(InvestorCreateDtoIn investorCreateDtoIn) {
        try {
            Investor investor = createInvestorFromInvestorDTOIn(investorCreateDtoIn);
            Investor investorInDb = this.investorRepository.findByUsername(investor.getUsername());
            if (investorInDb == null) {
                // Persist the new broker
                entityManager.persist(investor);
                entityManager.flush();
                entityManager.refresh(investor);


                AccessToken accessToken = accessManager.createInvestorToken(investor);
                LoginDtoOut returnData = new LoginDtoOut(investor.getInvestor_id(), investor.getUsername(), accessToken);
                return ResponseEntity.ok(returnData);
            } else {
                // Throw exception if the broker already exists
                throw new UsernameExistsException("There is already an registered investor with this username, please try again!");
            }
        } catch (UsernameExistsException exception) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, exception.getLocalizedMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
        }

    }

    public Investor createInvestorFromInvestorDTOIn(InvestorCreateDtoIn investorCreateDtoIn) {
        byte[] passwordSalt = PasswordTools.generateSalt();
        byte[] passwordHash = PasswordTools.generatePasswordHash(investorCreateDtoIn.getPassword(), passwordSalt);
        return new Investor(investorCreateDtoIn.getFirstname(),
                investorCreateDtoIn.getLastname(), investorCreateDtoIn.getUsername(),
                passwordHash, passwordSalt);
    }

    @Override
    public ResponseEntity<InvestorDtoOut> updateInvestor(InvestorUpdateDtoIn investorUpdateDtoIn, AccessToken accessToken, int investorId) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, investorId);

        try {
            return ResponseEntity.ok(
                    new InvestorDtoOut(updateRegisteredInvestor(investorId,
                            investorUpdateDtoIn.getFirstname(),
                            investorUpdateDtoIn.getLastname(),
                            investorUpdateDtoIn.getPassword()))
            );
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    public Investor updateRegisteredInvestor(int id, String vorname, String nachname, String password) {
        Investor investor = entityManager.find(Investor.class, id);
        byte[] pwd_salt = investor.getPasswordSalt();
        byte[] pwd_hash = PasswordTools.generatePasswordHash(password, pwd_salt);

        investor.setFirstName(vorname);
        investor.setLastname(nachname);
        investor.setPasswordSalt(pwd_salt);
        investor.setPasswordHash(pwd_hash);

        this.investorRepository.updateInvestorDetails(id, investor.getFirstName(), investor.getLastname(), investor.getPasswordHash(),
                investor.getPasswordSalt());

        return investor;
    }

    public ResponseEntity<InvestorDtoOut> getInvestorById(AccessToken accessToken, int investorId) {
        checkConnexion(accessToken);

        try {
            return ResponseEntity.ok(new InvestorDtoOut(this.investorRepository.findByInvestorId(investorId)));
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    public ResponseEntity<Void> deleteInvestorById(AccessToken accessToken, int investorId) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, investorId);


        try {
            boolean tokenRemoved = accessManager.removeInvestorTokenAfterDeleteToken(accessToken);
            if (!tokenRemoved) {
                throw new RuntimeException("Failed to remove investor access token");
            }
            deleteInvestor(investorId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }


    public void checkConnexion(AccessToken accessToken) {
        if (!accessManager.investorhasToken(accessToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Investor not registered or invalid access token provided");
        }
    }

    public void checkAuthorizations(AccessToken accessToken, int investorId) {
        if (accessManager.getInvestor(accessToken).getInvestor_id() != investorId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Investor has no authorizations");
        }
    }

    public void deleteInvestor(int investor_id) {
        Investor investor = entityManager.find(Investor.class, investor_id);
        if (investor != null) {
            this.investorRepository.deleteById(investor_id);
        } else {
            throw new NotFoundException("Investor not found");
        }
    }
    @Override
    public Investor mergeAndRefresh(Investor investor)
    {
        entityManager.persist(investor);
        entityManager.flush();
        entityManager.refresh(investor);

        return investor;
    }
}
