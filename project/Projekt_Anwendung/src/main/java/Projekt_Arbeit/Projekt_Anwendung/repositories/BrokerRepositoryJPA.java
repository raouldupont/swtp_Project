package Projekt_Arbeit.Projekt_Anwendung.repositories;

import Projekt_Arbeit.Projekt_Anwendung.dtos.BrokerCreateDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.entities.Broker;
import org.springframework.transaction.annotation.Transactional;

public interface BrokerRepositoryJPA {
    Broker createBroker(String firstname, String lastname,
                        String username, String password);
    @Transactional
    void createBroker(BrokerCreateDtoIn brokerCreateDtoIn);
}
