package softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs;

import lombok.Data;

import java.nio.DoubleBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class TariffDTO {
    private String tariffId;
    private String cpId;
    private String socketType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Integer stepSize;

    //special offer fields
    private LocalTime startTime;
    private LocalTime endTime;
    private Double minKWh;
    private Double maxKWh;
    private Double minCurrent;
    private Double maxCurrent;
    private Integer minDuration;
    private Integer maxDuration;
    private List<String> daysOfTheWeek;

    public Boolean isSpecialOffer(){
        if (startTime==null && endTime==null && minKWh==null && maxKWh==null && minCurrent==null && maxCurrent==null && minDuration==null && maxDuration==null && daysOfTheWeek==null){
            return false;
        }else {
            return true;
        }
    }
}