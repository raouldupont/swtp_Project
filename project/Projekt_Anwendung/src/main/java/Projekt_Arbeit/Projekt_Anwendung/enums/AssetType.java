package Projekt_Arbeit.Projekt_Anwendung.enums;

import java.util.Optional;

public enum AssetType {
    Share,
    Bond;

    public static AssetType parse(String type) {
        return Optional.of(AssetType.valueOf(type.toUpperCase()))
                .orElse(AssetType.Bond); // or another default or undefined value
    }

}
