package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ReserveNowDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.ReservationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Page<Reservation> findAll(LocalDateTime dateFrom, LocalDateTime dateTo, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("lastUpdated").descending());
        return reservationRepository.findReservationsByLastUpdatedBetween(dateFrom, dateTo, pageable);
    }

    public Page<Reservation> findAll(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("lastUpdated").descending());
        return reservationRepository.findAll(pageable);
    }

    public void updateReservationStatus(Long reservationInternalId, String status, LocalDateTime timestamp) {
        reservationRepository.updateReservationStatus(status, reservationInternalId, timestamp);
    }

    public void updateSessionEnergyConsumption(Double energyConsumption, Long internalReservationId, LocalDateTime timestamp) {
        reservationRepository.updateReservationEnergyAmount(energyConsumption, internalReservationId, timestamp);
    }

    public long getReservationsCount() {
        return reservationRepository.count();
    }

    public void insertReservation(ReserveNowDTO reserveNowDTO, long reservationId, Socket socket, EmspDetails emspDetails) {
        Reservation reservation = new Reservation();
        reservation.setInternalReservationId(reservationId);
        reservation.setEmspDetails(emspDetails);
        reservation.setReservationIdEmsp(reserveNowDTO.getReservationId());
        reservation.setSocket(socket);
        reservation.setExpiryDate(reserveNowDTO.getExpiryDate());
        reservation.setStatus("RESERVED");
        reservation.setLastUpdated(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    public void insertReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public Optional<Reservation> findReservationByInternalId(Long internalId) {
        return reservationRepository.findReservationByInternalReservationId(internalId);
    }

    public Optional<Reservation> findReservationByEmspId(Long id, EmspDetails emspDetails) {
        return reservationRepository.findReservationByReservationIdEmspAndEmspDetails(id, emspDetails);
    }

    public Optional<Reservation> findReservationBySessionId(Long sessionId) {
        return reservationRepository.findReservationBySessionId(sessionId);
    }

    public Long maxSessionId() {
        long maxSessionId;
        try {
            maxSessionId = reservationRepository.maxSessionId().getUniqueMappedResult().getMaxSessionId();
        } catch (NullPointerException e) {
            maxSessionId = 0;
        }
        return maxSessionId;
    }
}
