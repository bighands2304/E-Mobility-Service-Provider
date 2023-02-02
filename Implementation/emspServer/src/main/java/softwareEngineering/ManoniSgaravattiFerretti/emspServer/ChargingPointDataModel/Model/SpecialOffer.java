package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SpecialOffer extends Tariff{
    public SpecialOffer(){
        super.setIsSpecialOffer(true);
    }
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
