package Projekt_Arbeit.Projekt_Anwendung.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portfolio_assets")
@IdClass(PortfolioAssetsId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Portfolio_Assets {
    @Id
    @Column(name = "portfolio_id")
    private int portfolio_id;

    @Id
    @Column(name = "asset_id")
    private int asset_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("portfolio_id")
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("asset_id")
    @JoinColumn(name = "asset_id")
    private Asset asset;

    public Portfolio_Assets(int portfolioId, int assetId) {
        this.portfolio_id = portfolioId;
        this.asset_id = assetId;
    }
}
