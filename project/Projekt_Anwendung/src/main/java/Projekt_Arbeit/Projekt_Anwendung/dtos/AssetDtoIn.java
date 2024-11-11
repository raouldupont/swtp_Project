package Projekt_Arbeit.Projekt_Anwendung.dtos;

import Projekt_Arbeit.Projekt_Anwendung.enums.AssetType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetDtoIn
{
    private int assetId;
    private String name;
    private AssetType kind;
}
