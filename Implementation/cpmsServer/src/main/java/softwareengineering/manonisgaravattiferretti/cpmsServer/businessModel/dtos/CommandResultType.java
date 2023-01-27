package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.CommandResult;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.ReservationStatus;

public enum CommandResultType {
    ACCEPTED, REJECTED, TIMEOUT, UNKNOWN_RESERVATION, NOT_SUPPORTED, FAILED;

    public static CommandResultType getFromCpCommandResult(CommandResult commandResult) {
        return switch (commandResult) {
            case UNKNOWN -> UNKNOWN_RESERVATION;
            case REJECTED -> REJECTED;
            case SCHEDULED, ACCEPTED -> ACCEPTED;
            case NOT_SUPPORTED -> NOT_SUPPORTED;
        };
    }

    public static CommandResultType getFromReservationStatus(ReservationStatus reservationStatus) {
        return switch (reservationStatus) {
            case ACCEPTED -> ACCEPTED;
            case FAULTED, OCCUPIED -> FAILED;
            case REJECTED, UNAVAILABLE -> REJECTED;
        };
    }
}
