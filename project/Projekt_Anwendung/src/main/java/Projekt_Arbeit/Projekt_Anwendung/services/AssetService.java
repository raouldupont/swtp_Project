package Projekt_Arbeit.Projekt_Anwendung.services;

import Projekt_Arbeit.Projekt_Anwendung.Exception.AssetAttributedException;
import Projekt_Arbeit.Projekt_Anwendung.Exception.NotFoundException;
import Projekt_Arbeit.Projekt_Anwendung.dtos.AssetDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.AssetDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.entities.Asset;
import Projekt_Arbeit.Projekt_Anwendung.entities.Broker;
import Projekt_Arbeit.Projekt_Anwendung.entities.Investor;
import Projekt_Arbeit.Projekt_Anwendung.enums.AssetType;
import Projekt_Arbeit.Projekt_Anwendung.repositories.AssetRepository;
import Projekt_Arbeit.Projekt_Anwendung.repositories.BrokerRepository;
import Projekt_Arbeit.Projekt_Anwendung.repositories.PortfolioRepository;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessManager;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    public AssetService(AssetRepository assetRepository, EntityManager entityManager,
                        BrokerRepository brokerRepository,
                        AccessManager accessManager,
                        PortfolioRepository portfolioRepository) {
        this.assetRepository = assetRepository;
        this.entityManager = entityManager;
        this.brokerRepository = brokerRepository;
        this.accessManager = accessManager;
        this.portfolioRepository = portfolioRepository;
    }

    private final AssetRepository assetRepository;
    private final PortfolioRepository portfolioRepository;
    private final EntityManager entityManager;
    private final BrokerRepository brokerRepository;
    private final AccessManager accessManager;
    public ResponseEntity<AssetDtoOut> createAsset(AccessToken accessToken, int broker_id, AssetDtoIn assetDtoIn) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, broker_id);

        try {
            Broker broker = this.brokerRepository.findByBroker_id(broker_id);
            Asset asset = addAsset(broker, assetDtoIn.getKind(), assetDtoIn.getName());

            return ResponseEntity.ok(new AssetDtoOut(asset));
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    public Asset addAsset(Broker broker, AssetType kind, String name) {
        Asset asset = new Asset(name, kind);
        asset.setBroker(broker);
        this.assetRepository.save(asset);

        return asset;
    }
    public void removeAsset(Broker broker, Asset asset) {
        broker.removeAsset(asset);
        mergeAndRefresh(broker);
    }

    public Broker mergeAndRefresh(Broker broker) {
      this.brokerRepository.save(broker);
        return broker;
    }

    public ResponseEntity<Collection<AssetDtoOut>> getAssets(AccessToken accessToken, int broker_id) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, broker_id);
        try {
            Broker broker = this.brokerRepository.findByBroker_id(broker_id);

            return ResponseEntity.ok(
                    broker.getAssets().stream()
                            .map(AssetDtoOut::new)
                            .toList()
            );
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    public ResponseEntity<Void> deleteAsset(AccessToken accessToken, int broker_id, int asset_id) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, broker_id);

        try {
            Broker broker = this.brokerRepository.findByBroker_id(broker_id);
            Optional<Asset> asset = this.assetRepository.findById(asset_id);
            if (asset.isPresent() && broker != null) {
                Optional<Investor> investor = this.portfolioRepository.findInvestorByAssetId(asset_id);
                if (investor.isPresent()) {
                    AssetAttributedException exception = new AssetAttributedException("The asset is attributed to an investor and can't be deleted!");
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, exception.getLocalizedMessage());
                } else {
                    this.assetRepository.deleteById(asset_id);
                    removeAsset(broker, asset.get());
                }
            }
            return ResponseEntity.ok().build();
        } catch (NotFoundException notFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The asset to be deleted was not found");
        }
    }

    public void checkConnexion(AccessToken accessToken) {
        if (!accessManager.brokerhasToken(accessToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Broker not registered");
        }
    }

    public void checkAuthorizations(AccessToken accessToken, int brokerId) {
        if (accessManager.getBroker(accessToken).getBroker_id() != brokerId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Broker has no authorizations");
        }
    }

    public ResponseEntity<List<Asset>> getAssetsForBroker(@PathVariable("brokerId") int brokerId) {
        List<Asset> assets = assetRepository.findByBrokerId(brokerId);
        return ResponseEntity.ok().body(assets);
    }
}
