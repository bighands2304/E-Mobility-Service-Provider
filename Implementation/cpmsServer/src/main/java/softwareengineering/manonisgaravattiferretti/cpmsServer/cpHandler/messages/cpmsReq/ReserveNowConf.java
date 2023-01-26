package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.ReservationStatus;

@Data
public class ReserveNowConf {
    private ReservationStatus reservationStatus;
}
