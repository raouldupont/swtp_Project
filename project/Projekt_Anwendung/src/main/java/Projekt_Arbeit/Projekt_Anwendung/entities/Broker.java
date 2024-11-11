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
@Table(name = "broker")
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int broker_id;

    private String username;

    private String company;

    public Broker(String company, String username, byte[] passwordHash, byte[] passwordSalt) {
        this.username = username;
        this.company = company;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

    @Column(name = "password_hash", nullable = false, columnDefinition = "BINARY(20)")
    private byte[] passwordHash;

    @Column(name = "password_salt", nullable = false, columnDefinition = "BINARY(32)")
    private byte[] passwordSalt;

    @OneToMany(mappedBy = "broker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asset> assets = new ArrayList<>();



    @Override
    public int hashCode()
    {
        return Objects.hash(broker_id);
    }

    @Override
    public String toString()
    {
        return "Broker{" +
                "username='" + username + '\'' +
                "username='" + company + '\'' +
                '}';
    }

    public void addAsset(Asset asset)
    {
        assets.add(asset);
    }

    public void removeAsset(Asset asset){assets.remove(asset);}
}


