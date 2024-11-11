package Projekt_Arbeit.Projekt_Anwendung.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PortfolioDtoIn
{
    private int investorId;
    private int brokerId;
    private List<Integer> assetIds;

}
