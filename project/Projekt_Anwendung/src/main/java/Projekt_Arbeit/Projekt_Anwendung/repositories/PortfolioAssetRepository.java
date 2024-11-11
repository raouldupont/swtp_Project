package Projekt_Arbeit.Projekt_Anwendung.repositories;

import Projekt_Arbeit.Projekt_Anwendung.entities.Portfolio_Assets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioAssetRepository extends JpaRepository<Portfolio_Assets, Integer> {
}
