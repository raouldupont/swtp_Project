package Projekt_Arbeit.Projekt_Anwendung.dtos;

import Projekt_Arbeit.Projekt_Anwendung.entities.Investor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class InvestorDtoOut
{
    int id;
    String firstname;
    String lastname;
    String username;

    public InvestorDtoOut(Investor investor)
    {
        this.id = investor.getInvestor_id();
        this.firstname = investor.getFirstName();
        this.lastname = investor.getLastname();
        this.username = investor.getUsername();
    }
}
