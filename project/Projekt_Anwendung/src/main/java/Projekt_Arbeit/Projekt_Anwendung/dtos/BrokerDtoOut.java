package Projekt_Arbeit.Projekt_Anwendung.dtos;

import Projekt_Arbeit.Projekt_Anwendung.entities.Broker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BrokerDtoOut
{
    int id;
    String company;
    String username;

    public BrokerDtoOut(Broker broker)
    {
        this.id = broker.getBroker_id();
        this.company = broker.getUsername();
        this.username = broker.getUsername();
    }
}
