package Projekt_Arbeit.Projekt_Anwendung.services;

import Projekt_Arbeit.Projekt_Anwendung.Exception.NotFoundException;
import Projekt_Arbeit.Projekt_Anwendung.dtos.AssetDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.dtos.PortfolioDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.PortfolioDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.entities.*;
import Projekt_Arbeit.Projekt_Anwendung.repositories.*;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessManager;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    private final AccessManager accessManager;
    private final PortfolioRepository portfolioRepository;
    private final InvestorRepository investorRepository;
    private final BrokerRepository brokerRepository;
    private final AssetRepository assetRepository;
    private final PortfolioAssetRepository portfolioAssetRepository;

    @Autowired
    public PortfolioService(AccessManager accessManager, PortfolioRepository portfolioRepository,
                            InvestorRepository investorRepository,
                            BrokerRepository brokerRepository,
                            AssetRepository assetRepository,
                            PortfolioAssetRepository portfolioAssetRepository) {
        this.accessManager = accessManager;
        this.portfolioRepository = portfolioRepository;
        this.investorRepository = investorRepository;
        this.brokerRepository = brokerRepository;
        this.assetRepository = assetRepository;
        this.portfolioAssetRepository = portfolioAssetRepository;
    }

    public void checkConnexion(AccessToken accessToken) {
        if (!accessManager.investorhasToken(accessToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Investor not registered");
        }
    }

    public void checkAuthorizations(AccessToken accessToken, int investorId) {
        if (accessManager.getInvestor(accessToken).getInvestor_id() != investorId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Investor has no authorizations");
        }
    }

    public ResponseEntity<PortfolioDtoOut> createPortfolio(
            AccessToken accessToken,
            Integer investor_id,
            Integer broker_id,
            PortfolioDtoIn portfolioDtoIn) {
        checkConnexion(accessToken);

        if (investor_id == null || broker_id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Investor ID and Broker ID must be provided");
        }

        checkAuthorizations(accessToken, investor_id);

        try {
            // Find investor
            Investor investor = investorRepository.findByInvestorId(investor_id);
            if (investor == null) {
                throw new NotFoundException("Investor not found with ID: " + investor_id);
            }

            // Find broker
            Broker broker = brokerRepository.findByBroker_id(broker_id);
            if (broker == null) {
                throw new NotFoundException("Broker not found with ID: " + broker_id);
            }

            // Create new Portfolio
            Portfolio portfolio = new Portfolio();
            portfolio.setInvestor(investor);
            portfolio.setBroker(broker);
            portfolio.setCreationDate(LocalDateTime.now());

            // Save initial portfolio without assets
            this.portfolioRepository.save(portfolio);

            // Check and add assets
            List<Integer> assetIds = portfolioDtoIn.getAssetIds();
            if (assetIds == null || assetIds.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset IDs must be provided and cannot be null or empty");
            }
            List<Asset> assets = assetRepository.findAllById(assetIds);
            if (assets.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid assets found for the provided IDs");
            }

            Portfolio savedPortfolio =  this.portfolioRepository.findByPortfolio_id(portfolio.getPortfolio_id());
            // Create Portfolio_Assets for each asset and save
            for (Asset asset : assets) {
                Portfolio_Assets portfolioAsset = new Portfolio_Assets(savedPortfolio.getPortfolio_id(), asset.getAsset_id());
                portfolioAsset.setPortfolio(savedPortfolio);
                portfolioAsset.setAsset(asset);
                addToPortfolio(investor, portfolioDtoIn);
                portfolioAssetRepository.save(portfolioAsset);
            }

            // Fetch the portfolio with assets if needed
            Portfolio portfolioWithAssets = portfolioRepository.findByPortfolio_id(savedPortfolio.getPortfolio_id());
            return mapPortfolioToDtoOut(portfolioWithAssets);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create portfolio", ex);
        }
    }


    private ResponseEntity<PortfolioDtoOut> mapPortfolioToDtoOut(Portfolio portfolio) {
        PortfolioDtoOut dtoOut = new PortfolioDtoOut();
        dtoOut.setPortfolioId(portfolio.getPortfolio_id());
        dtoOut.setInvestorId(portfolio.getInvestor().getInvestor_id());
        dtoOut.setBrokerId(portfolio.getBroker().getBroker_id());
        dtoOut.setCreationDate(portfolio.getCreationDate().toString());

        List<AssetDtoOut> assetDtos = portfolio.getPortfolioAssets().stream()
                .map(portfolioAsset -> mapAssetToDtoOut(portfolioAsset.getAsset()))
                .collect(Collectors.toList());
        dtoOut.setAssets(assetDtos);

        return ResponseEntity.ok(dtoOut);
    }

    private AssetDtoOut mapAssetToDtoOut(Asset asset) {
        AssetDtoOut dtoOut = new AssetDtoOut();
        dtoOut.setAssetId(asset.getAsset_id());
        dtoOut.setName(asset.getName());
        // Set other fields as needed
        return dtoOut;
    }

    private Portfolio addToPortfolio(Investor investor, PortfolioDtoIn portfolioDtoIn) {
        Portfolio portfolio = new Portfolio(portfolioDtoIn);
        investor.addPortfolio(portfolio);
        mergeAndRefresh(investor);
        return portfolio;
    }

    public void removePortfolio(Investor investor, Portfolio portfolio) {
        investor.removePortfolio(portfolio);
        mergeAndRefresh(investor);
    }

    public Investor mergeAndRefresh(Investor investor) {
        this.investorRepository.save(investor);
        return investor;
    }

    public ResponseEntity<Void> deletePortfolio(AccessToken accessToken, int investor_id, int portfolio_id) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, investor_id);

        try {
            Investor investor = this.investorRepository.findByInvestorId(investor_id);
            Optional<Portfolio> portfolio = this.portfolioRepository.findById(portfolio_id);

            if (portfolio.isPresent()) {
                Portfolio portfolioTobeDelete = this.portfolioRepository.findByPortfolio_id(portfolio_id);
                this.portfolioRepository.deleteById(portfolio_id);
                removePortfolio(investor, portfolioTobeDelete);
            }
            return ResponseEntity.ok().build();

        } catch (NotFoundException notFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Portfolio to be deleted was not found");
        }
    }

    public ResponseEntity<Collection<PortfolioDtoOut>> getPortfolios(AccessToken accessToken, int investor_id) {
        checkConnexion(accessToken);
        checkAuthorizations(accessToken, investor_id);
        try {
            Investor investor = this.investorRepository.findByInvestorId(investor_id);

            return ResponseEntity.ok(
                    investor.getPortfolios().stream()
                            .map(PortfolioDtoOut::new)
                            .toList()
            );
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }
}
