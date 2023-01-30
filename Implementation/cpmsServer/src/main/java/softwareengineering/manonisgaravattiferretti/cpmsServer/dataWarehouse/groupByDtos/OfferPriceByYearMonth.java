package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos;

import lombok.Data;

@Data
public class OfferPriceByYearMonth {
    private String dsoId;
    private String cpId;
    private String year;
    private String month;
    private double price;
}
