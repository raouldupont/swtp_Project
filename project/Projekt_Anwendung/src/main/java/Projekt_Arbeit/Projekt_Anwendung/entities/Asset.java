package Projekt_Arbeit.Projekt_Anwendung.entities;

import Projekt_Arbeit.Projekt_Anwendung.enums.AssetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "asset")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int asset_id;

    public Asset(String name, AssetType kind) {
        this.name = name;
        this.kind = kind;
    }

    private String name;

    @Enumerated(EnumType.STRING)
    private AssetType kind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id", nullable = false)
    private Broker broker;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Portfolio_Assets> portfolioAssets;
}
