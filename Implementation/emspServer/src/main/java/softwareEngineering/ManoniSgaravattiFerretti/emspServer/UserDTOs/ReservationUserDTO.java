package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDTOs;

import lombok.Data;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.*;

import java.time.LocalDateTime;

@Data
public class ReservationUserDTO {
    public ReservationUserDTO(Reservation reservation){
        if(reservation instanceof EndedReservation){
            type = "ENDED";
        }else if (reservation instanceof ActiveReservation activeReservation) {
            if (activeReservation.getSessionId() == null) {
                type = "RESERVED";
            }else
                type = "ACTIVE";
        }else if(reservation instanceof DeletedReservation){
            type = "DELETED";
        }
    }
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime expiryDate;
    private User user;
    private String tariffId;
    private String cpId;
    private String socketId;

    private Long sessionId;
    private Double batteryPercentage;

    private LocalDateTime deletionTime;

    private LocalDateTime endTime;
    private Double energyAmount;
    private Double price;
    private String type;

}
