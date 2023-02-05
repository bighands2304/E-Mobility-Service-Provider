package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils;

import org.springframework.beans.BeanUtils;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.*;

import java.time.LocalDateTime;
import java.util.List;

public class EntityFromDTOConverter {
    public static CPO cpoFromRegistrationDTO(CPORegistrationDTO cpoRegistrationDTO) {
        CPO cpo = new CPO();
        BeanUtils.copyProperties(cpoRegistrationDTO, cpo);
        return cpo;
    }

    public static EmspDetails emspDetailsFromCredentialsDTO(CredentialsDTO credentialsDTO) {
        EmspDetails emspDetails = new EmspDetails();
        emspDetails.setEmspToken(credentialsDTO.getEmspToken());
        emspDetails.setUrl(credentialsDTO.getEmspUrl());
        emspDetails.setCpoToken(credentialsDTO.getCpmsToken());
        return emspDetails;
    }

    public static EmspSocketDTO emspSocketDTOFromSocket(Socket socket) {
        EmspSocketDTO emspSocketDTO = new EmspSocketDTO();
        BeanUtils.copyProperties(socket, emspSocketDTO);
        return emspSocketDTO;
    }

    public static EmspChargingPointDTOWithId emspChargingPointDTOWithIdFromChargingPoint(ChargingPoint chargingPoint) {
        EmspChargingPointDTOWithId emspChargingPointDTO = new EmspChargingPointDTOWithId();
        emspChargingPointDTO.setCpId(chargingPoint.getCpId());
        return (EmspChargingPointDTOWithId) setCpDTOProperties(chargingPoint, emspChargingPointDTO);
    }

    public static EmspChargingPointDTO emspChargingPointDTOFromChargingPoint(ChargingPoint chargingPoint) {
        EmspChargingPointDTO emspChargingPointDTO = new EmspChargingPointDTO();
        return setCpDTOProperties(chargingPoint, emspChargingPointDTO);
    }

    private static EmspChargingPointDTO setCpDTOProperties(ChargingPoint chargingPoint, EmspChargingPointDTO emspChargingPointDTO) {
        emspChargingPointDTO.setName(chargingPoint.getName());
        emspChargingPointDTO.setAddress(chargingPoint.getAddress());
        emspChargingPointDTO.setLatitude(chargingPoint.getLatitude());
        emspChargingPointDTO.setLongitude(chargingPoint.getLongitude());
        emspChargingPointDTO.setLastUpdated(chargingPoint.getLastUpdated());
        emspChargingPointDTO.setTariffIds(chargingPoint.getTariffs().stream().map(Tariff::getTariffId).toList());
        emspChargingPointDTO.setSockets(chargingPoint.getSockets().stream()
                .map(EntityFromDTOConverter::emspSocketDTOFromSocket).toList());
        return emspChargingPointDTO;
    }

    public static ChargingSessionDTO chargingSessionDTOFromReservation(Reservation reservation) {
        ChargingSessionDTO chargingSessionDTO = new ChargingSessionDTO();
        BeanUtils.copyProperties(reservation, chargingSessionDTO);
        chargingSessionDTO.setReservationId(reservation.getReservationIdEmsp());
        chargingSessionDTO.setChargingPointId(reservation.getSocket().getCpId());
        chargingSessionDTO.setKwh(reservation.getEnergyAmount());
        chargingSessionDTO.setSocketId(reservation.getSocket().getSocketId());
        chargingSessionDTO.setStartDateTime(reservation.getStartTime());
        chargingSessionDTO.setEndDateTime(reservation.getEndTime());
        return chargingSessionDTO;
    }

    public static Tariff fromAddTariffDTOToTariff(AddTariffDTO addTariffDTO, String id) {
        Tariff tariff = new Tariff();
        BeanUtils.copyProperties(addTariffDTO, tariff);
        tariff.setTariffId(id);
        return tariff;
    }

    public static SpecialOffer fromAddTariffDTOToSpecialOffer(AddTariffDTO addTariffDTO, String id) {
        SpecialOffer tariff = new SpecialOffer();
        BeanUtils.copyProperties(addTariffDTO, tariff);
        tariff.setTariffId(id);
        return tariff;
    }

    public static ChargingPoint fromAddCpDTOToCp(AddChargingPointDTO addChargingPointDTO, String cpoCode) {
        ChargingPoint chargingPoint = new ChargingPoint();
        BeanUtils.copyProperties(addChargingPointDTO, chargingPoint);
        List<Socket> sockets = addChargingPointDTO.getSockets().stream()
                .map(socketDto -> {
                    Socket socket = new Socket();
                    BeanUtils.copyProperties(socketDto, socket);
                    socket.setAvailability("AVAILABLE");
                    socket.setStatus("AVAILABLE");
                    socket.setLastUpdated(LocalDateTime.now());
                    socket.setCpoCode(cpoCode);
                    socket.setCpId(addChargingPointDTO.getCpId());
                    socket.setChargingProfiles(List.of());
                    return socket;
                }).toList();
        chargingPoint.setSockets(sockets);
        chargingPoint.setCpoCode(cpoCode);
        chargingPoint.setLastUpdated(LocalDateTime.now());
        chargingPoint.setToggleDSOSelectionOptimizer(true);
        chargingPoint.setToggleEnergyMixOptimizer(true);
        chargingPoint.setToggleDSOSelectionOptimizer(true);
        return chargingPoint;
    }

    public static DSOOfferDTO fromOfferToDto(DSOOffer dsoOffer) {
        DSOOfferDTO dsoOfferDTO = new DSOOfferDTO();
        BeanUtils.copyProperties(dsoOffer, dsoOfferDTO);
        dsoOfferDTO.setOfferId(dsoOffer.getId());
        return dsoOfferDTO;
    }
}
