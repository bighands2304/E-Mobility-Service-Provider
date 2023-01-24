package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import jakarta.persistence.GeneratedValue;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class Tariff {
    @Id
    @GeneratedValue(generator = "uuid")
    private String tariffId;
    private String socketType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Integer stepSize;
}
