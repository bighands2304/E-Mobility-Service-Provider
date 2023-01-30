package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos;

import lombok.Data;

@Data
public class OfferPriceByYear {
    private String dsoId;
    private String cpId;
    private String year;
    private double price;
}
