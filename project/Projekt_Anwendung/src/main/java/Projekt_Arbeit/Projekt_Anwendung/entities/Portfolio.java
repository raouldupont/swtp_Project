package Projekt_Arbeit.Projekt_Anwendung.entities;

import Projekt_Arbeit.Projekt_Anwendung.converter.LocalDateTimeConverter;
import Projekt_Arbeit.Projekt_Anwendung.dtos.PortfolioDtoIn;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "portfolio")
public class Portfolio {
    @Id
    @Column(name = "portfolio_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int portfolio_id;

    @ManyToOne
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    @ManyToOne
    @JoinColumn(name = "broker_id", nullable = false)
    private Broker broker;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio_Assets> portfolioAssets = new ArrayList<>();

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime creationDate;

    public Portfolio(PortfolioDtoIn portfolioDtoIn) {
        // initialize from DTO if needed
    }
}
