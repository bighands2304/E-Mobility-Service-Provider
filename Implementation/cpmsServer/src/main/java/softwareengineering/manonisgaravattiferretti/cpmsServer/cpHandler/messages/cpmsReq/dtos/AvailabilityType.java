package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos;

public enum AvailabilityType {
    INOPERATIVE, OPERATIVE;

    public static AvailabilityType getFromBoolean(boolean available) {
        return (available) ? OPERATIVE : INOPERATIVE;
    }
}
