package Projekt_Arbeit.Projekt_Anwendung.controller;

import Projekt_Arbeit.Projekt_Anwendung.dtos.AssetDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.AssetDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.entities.Asset;
import Projekt_Arbeit.Projekt_Anwendung.repositories.AssetRepository;
import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import Projekt_Arbeit.Projekt_Anwendung.services.AssetService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value ="asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @PostMapping("{id}/new_Asset")
    public ResponseEntity<AssetDtoOut> createAsset(@RequestHeader AccessToken accessToken, @PathVariable("id") int broker_id, @RequestBody AssetDtoIn assetDtoIn)
    {
       return this.assetService.createAsset(accessToken, broker_id, assetDtoIn);
    }

    @GetMapping(path = "{id}/All_assets")
    public ResponseEntity<Collection<AssetDtoOut>> getMyAssets (@RequestHeader AccessToken accessToken, @PathVariable("id") int broker_id)
    {
       return this.assetService.getAssets(accessToken, broker_id);
    }

    @DeleteMapping("{broker_id}/delete_Asset/{id}")
    public ResponseEntity<Void> deleteNonAttributedAsset(@RequestHeader AccessToken accessToken,  @PathVariable("broker_id") int broker_id,
                                         @PathVariable("id") int asset_id)
    {
       return this.assetService.deleteAsset(accessToken, broker_id, asset_id);
    }

    @GetMapping("/broker/{brokerId}/available")
    public ResponseEntity<List<Asset>> getAvailableAssetsForBroker(@PathVariable("brokerId") int brokerId) {
       return this.assetService.getAssetsForBroker(brokerId);
    }
}
