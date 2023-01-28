package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Data
public class Tariff {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String tariffId;
    @Id
    private ChargingPoint chargingPoint;
    private String socketType;
    private Double price;
    private Integer stepSize;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isSpecialOffer=false;
}
