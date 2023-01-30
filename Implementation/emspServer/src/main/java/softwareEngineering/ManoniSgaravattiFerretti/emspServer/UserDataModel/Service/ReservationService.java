package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository.ReservationRepository;

import java.util.List;
@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    public List<Reservation> getReservationsByUserId(Long userId){return reservationRepository.findAllByUserId(userId);}
    public Reservation getReservationById(Long reservationId){return reservationRepository.findReservationById(reservationId);}
    public Reservation save(Reservation reservation){return reservationRepository.save(reservation);}
    public void delete(Reservation reservation){reservationRepository.delete(reservation);}

}
