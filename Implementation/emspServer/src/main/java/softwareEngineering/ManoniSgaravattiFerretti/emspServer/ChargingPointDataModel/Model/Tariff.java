package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
public class Tariff {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Indexed(unique=true)
    private String tariffId;
    private String socketType;
    private Double price;
    private Integer stepSize;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isSpecialOffer=false;
}
