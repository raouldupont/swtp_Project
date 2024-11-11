package Projekt_Arbeit.Projekt_Anwendung.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "investor")
public class Investor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int investor_id;
    private String username;

    public Investor(String username, String firstName, String lastname, byte[] passwordHash, byte[] passwordSalt) {
        this.username = username;
        this.firstName = firstName;
        this.lastname = lastname;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

    private String firstName;
    private String lastname;

    @Column(name = "password_hash", nullable = false, columnDefinition = "BINARY(20)")
    private byte[] passwordHash;

    @Column(name = "password_salt", nullable = false, columnDefinition = "BINARY(32)")
    private byte[] passwordSalt;

    @OneToMany(mappedBy = "investor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();

    @Override
    public boolean equals(Object o)
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Investor investor = (Investor) o;
        return investor_id == investor.investor_id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(investor_id);
    }

    @Override
    public String toString()
    {
        return "Investor{" +
                "firstname='" + firstName + '\'' +
                "lastname='" + lastname + '\'' +
                ", username ='" + username + '\'' +
                '}';
    }

    public void addPortfolio(Portfolio portfolio)
    {
        portfolios.add(portfolio);
    }
    public  void removePortfolio(Portfolio portfolio){portfolios.remove(portfolio);}
}
