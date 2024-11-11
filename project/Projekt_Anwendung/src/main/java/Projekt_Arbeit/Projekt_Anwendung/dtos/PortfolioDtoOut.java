package Projekt_Arbeit.Projekt_Anwendung.dtos;

import Projekt_Arbeit.Projekt_Anwendung.entities.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDtoOut
{
    private int id;
    private int investorId;
    private int brokerId;
    private int portfolioId;
    private List<AssetDtoOut> assets;
    private static final DateTimeFormatter fomatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale( Locale.forLanguageTag("de"));
    private String creationDate;

    public  PortfolioDtoOut(Portfolio portfolio)
    {
        this.creationDate = fomatter.format(portfolio.getCreationDate());
    }
}
