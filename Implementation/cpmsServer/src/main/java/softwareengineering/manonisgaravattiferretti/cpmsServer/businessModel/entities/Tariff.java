package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class Tariff {
    @Id
    private String tariffId;
    private String socketType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Integer stepSize;
}
