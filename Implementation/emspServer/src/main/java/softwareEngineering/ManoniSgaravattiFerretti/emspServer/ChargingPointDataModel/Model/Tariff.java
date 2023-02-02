package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;

import jakarta.persistence.GeneratedValue;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Document
public class Tariff {
    @Id
    private String id;
    @Indexed(unique=true)
    private String tariffId;
    private String socketType;
    private Double price;
    private Integer stepSize;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isSpecialOffer;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double minKWh;
    private Double maxKWh;
    private Double minCurrent;
    private Double maxCurrent;
    private Integer minDuration;
    private Integer maxDuration;
    private List<String> daysOfTheWeek;
}
