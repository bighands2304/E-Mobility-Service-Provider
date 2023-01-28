package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

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
    private Double minDuration;
    private Double maxDuration;
    private String daysOfTheWeek;
}
