package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangeSocketAvailabilityDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.SocketRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SocketService {
    private final SocketRepository socketRepository;

    @Autowired
    public SocketService(SocketRepository socketRepository) {
        this.socketRepository = socketRepository;
    }

    public Optional<Socket> findSocketByCpIdAndSocketId(String cpId, Integer socketId) {
        return socketRepository.findSocketByCpIdAndSocketId(cpId, socketId);
    }

    public Optional<Socket> findSocketByCpInternalIdAndSocketId(String cpInternalId, Integer socketId) {
        return socketRepository.findSocketByInternalCpIdAndSocketId(cpInternalId, socketId);
    }

    public List<Socket> findCpSocketsByInternalId(String cpId) {
        return socketRepository.findSocketsByInternalCpId(cpId);
    }

    public List<Socket> findCpSocketsById(String cpId) {
        return socketRepository.findSocketsByCpId(cpId);
    }

    public void updateSocketAvailability(ChangeSocketAvailabilityDTO changeSocketAvailabilityDTO, String cpId, Integer socketId) {
        socketRepository.updateSocketAvailability(changeSocketAvailabilityDTO, cpId, socketId);
    }

    public void updateSocketStatus(String cpInternalId, Integer socketId, String status) {
        socketRepository.updateSocketStatus(cpInternalId, socketId, status);
    }
}
