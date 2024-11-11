package Projekt_Arbeit.Projekt_Anwendung.repositories;

import Projekt_Arbeit.Projekt_Anwendung.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Integer>
{
    @Query("select b from Asset b where b.asset_id = ?1")
    Asset findByAsset_id(int asset_id);

    @Query("SELECT a FROM Asset a WHERE a.broker.broker_id = :brokerId")
    List<Asset> findByBrokerId(@Param("brokerId") int brokerId);
}
