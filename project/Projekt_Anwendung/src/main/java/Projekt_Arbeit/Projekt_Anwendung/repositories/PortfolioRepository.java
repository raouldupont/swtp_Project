package Projekt_Arbeit.Projekt_Anwendung.repositories;

import Projekt_Arbeit.Projekt_Anwendung.entities.Investor;
import Projekt_Arbeit.Projekt_Anwendung.entities.Portfolio;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

    @Query("select p.portfolio.investor from Portfolio_Assets p where p.asset_id = :assetId")
    Optional<Investor> findInvestorByAssetId(@Param("assetId") int assetId);
    @Query("select b from Portfolio b where b.portfolio_id = ?1")
    Portfolio findByPortfolio_id (int portfolio_id);


}
