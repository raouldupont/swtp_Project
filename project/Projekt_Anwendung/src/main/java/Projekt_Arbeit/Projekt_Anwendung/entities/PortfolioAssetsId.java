package Projekt_Arbeit.Projekt_Anwendung.entities;

import java.io.Serializable;
import java.util.Objects;

public class PortfolioAssetsId implements Serializable {
    private int portfolio_id;
    private int asset_id;

    public PortfolioAssetsId() {
    }

    public PortfolioAssetsId(int portfolio_id, int asset_id) {
        this.portfolio_id = portfolio_id;
        this.asset_id = asset_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioAssetsId that = (PortfolioAssetsId) o;
        return portfolio_id == that.portfolio_id && asset_id == that.asset_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolio_id, asset_id);
    }
}
