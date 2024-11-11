package Projekt_Arbeit.Projekt_Anwendung.services;

import Projekt_Arbeit.Projekt_Anwendung.Exception.NotFoundException;
import Projekt_Arbeit.Projekt_Anwendung.Exception.UsernameExistsException;
import Projekt_Arbeit.Projekt_Anwendung.dtos.*;
import Projekt_Arbeit.Projekt_Anwendung.entities.Broker;
import Projekt_Arbeit.Projekt_Anwendung.repositories.AssetRepository;
import Projekt_Arbeit.Projekt_Anwendung.repositories.BrokerRepository;
import Projekt_Arbeit.Projekt_Anwendung.repositories.BrokerRepositoryJPA;
import Projekt_Arbeit.Projekt_Anwendung.repositories.PortfolioRepository;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessManager;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import Projekt_Arbeit.Projekt_Anwendung.security.PasswordTools;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class BrokerService {
    @Autowired
    public BrokerService(BrokerRepository brokerRepository,
                         AccessManager accessManager,
                         EntityManager entityManager,
                         AssetRepository assetRepository,
                         PortfolioRepository portfolioRepository) {
        this.brokerRepository = brokerRepository;
        this.accessManager = accessManager;
        this.entityManager = entityManager;
        this.assetRepository = assetRepository;
        this.portfolioRepository = portfolioRepository;
    }

    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;

    private final BrokerRepository brokerRepository;

    private final AccessManager accessManager;

    private BrokerRepositoryJPA brokerRepositoryJPA;


    private final EntityManager entityManager;

    @Transactional
    public ResponseEntity<LoginDtoOut> createBroker(BrokerCreateDtoIn brokerCreateDtoIn) {
        try {
            // Create the Broker entity once
            Broker broker = createBrokerFromBrokerDTO(brokerCreateDtoIn);

            // Check if the broker already exists in the database
            Broker brokerInDb = this.brokerRepository.findByUsername(broker.getUsername());

            if (brokerInDb == null) {
                // Persist the new broker
                entityManager.persist(broker);
                entityManager.flush();
                entityManager.refresh(broker);

                // Create the access token using the persisted broker entity
                AccessToken accessToken = accessManager.createBrokerToken(broker);
                LoginDtoOut returnData = new LoginDtoOut(broker.getBroker_id(), broker.getUsername(), accessToken);
                return ResponseEntity.ok(returnData);
            } else {
                // Throw exception if the broker already exists
                throw new UsernameExistsException("There is already a registered Broker with this username, please try again!");
            }
        } catch (UsernameExistsException exception) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, exception.getLocalizedMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
        }
    }

    public Broker createBrokerFromBrokerDTO(BrokerCreateDtoIn brokerCreateDtoIn) {
        byte[] passwordSalt = PasswordTools.generateSalt();
        byte[] passwordHash = PasswordTools.generatePasswordHash(brokerCreateDtoIn.getPassword(), passwordSalt);
        return new Broker(brokerCreateDtoIn.getCompany(), brokerCreateDtoIn.getUsername(),
                passwordHash, passwordSalt);
    }


    public void checkConnexion(AccessToken accessToken) {
        if (!accessManager.brokerhasToken(accessToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Broker not registered");
        }
    }

    public void checkAuthorizations(AccessToken accessToken, int brokerId) {
        if (accessManager.getBroker(accessToken).getBroker_id() != brokerId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Broker has no authorizations");
        }
    }

    public ResponseEntity<BrokerDtoOut> getBrokerById(AccessToken accessToken, int brokerId) {
        checkConnexion(accessToken);

        try {
            return ResponseEntity.ok(new BrokerDtoOut(this.brokerRepository.findByBroker_id(brokerId)));
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    public ResponseEntity<BrokerDtoOut> updateBroker(BrokerCreateDtoIn brokerCreateDtoIn, AccessToken accessToken, int brokerId) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, brokerId);

        try {
            return ResponseEntity.ok(
                    new BrokerDtoOut(updateRegisteredBroker(brokerId, brokerCreateDtoIn.getCompany(),
                            brokerCreateDtoIn.getUsername(),
                            brokerCreateDtoIn.getPassword()))
            );

        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    private Broker updateRegisteredBroker(int brokerId, String company, String password, String username) {
        Broker broker = entityManager.find(Broker.class, brokerId);
        byte[] pwd_salt = broker.getPasswordSalt();
        byte[] pwd_hash = PasswordTools.generatePasswordHash(password, pwd_salt);

        broker.setCompany(company);
        broker.setUsername(username);
        broker.setPasswordHash(pwd_hash);
        broker.setPasswordSalt(pwd_salt);

       this.brokerRepository.updateBrokerDetails(brokerId, broker.getUsername(), broker.getCompany(), broker.getPasswordHash(), broker.getPasswordSalt());

        return broker;
    }

    public ResponseEntity<Void> deleteBrokerById(AccessToken accessToken, int brokerId) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, brokerId);

        try {
            accessManager.removeBrokerTokenAfterDelete(accessToken);
            deleteBroker(brokerId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    public void deleteBroker(int broker_id) {
        Broker broker = entityManager.find(Broker.class, broker_id);
        this.brokerRepository.deleteById(broker_id);
    }

    public ResponseEntity<Collection<BrokerDtoOut>> getAllBrokers(AccessToken accessToken) {

        return ResponseEntity.ok(this.brokerRepository.getAll().stream()
                .map(BrokerDtoOut::new).toList());
    }

}
