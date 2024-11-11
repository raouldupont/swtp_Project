package Projekt_Arbeit.Projekt_Anwendung.dtos;

import Projekt_Arbeit.Projekt_Anwendung.entities.Asset;
import Projekt_Arbeit.Projekt_Anwendung.enums.AssetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssetDtoOut
{
    private int assetId;
    private String name;
    private String kind;



    public AssetDtoOut(Asset asset) {
        this.assetId = asset.getAsset_id();
        this.name = asset.getName();
        this.kind = asset.getKind().name();
    }
}
