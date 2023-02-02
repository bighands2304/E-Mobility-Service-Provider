package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.ChargingPointDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.SocketDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Socket;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointOperatorService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;

@RestController
@RequestMapping("/ocpi/emsp/locations")
public class LocationsReceiver {
    @Autowired
    ChargingPointService cpService;
    @Autowired
    ChargingPointOperatorService cpoService;
    @PutMapping("/{cp_id}")
    public ResponseEntity<?> putCP(@PathVariable String cp_id, @RequestBody ChargingPointDTO cp, @RequestHeader("Authorization") String auth){
        ChargingPoint newCp = new ChargingPoint();
        newCp.setTariffsId(cp.getTariffIds());
        newCp.setName(cp.getName());
        newCp.setAddress(cp.getAddress());
        newCp.setCpo(cpoService.searchCPOByToken(auth));
        newCp.setCpId(cp_id);
        newCp.setLatitude(cp.getLatitude());
        newCp.setLongitude(cp.getLongitude());
        newCp.setLastUpdate(cp.getLastUpdated());

        for (SocketDTO s : cp.getSockets()) {
            Socket newSocket = new Socket();
            newSocket.setSocketId(s.getSocketId().toString());
            newSocket.setType(s.getSocketType());
            newSocket.setStatus(s.getStatus());
            newSocket.setLastUpdate(s.getLastUpdate());
            newSocket.setAvailability(s.getAvailability());
            newCp.addSocket(newSocket);
        }
        cpService.save(newCp);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cp_id}/{socket_id}")
    public ResponseEntity<?> putSocket(@PathVariable String cp_id,@PathVariable String socket_id, @RequestBody SocketDTO socket) {
        ChargingPoint chargingPoint = cpService.getCPById(cp_id);

        Socket newSocket = new Socket();
        newSocket.setSocketId(socket_id);
        newSocket.setType(socket.getSocketType());
        newSocket.setStatus(socket.getStatus());
        newSocket.setLastUpdate(socket.getLastUpdate());
        newSocket.setAvailability(socket.getAvailability());

        chargingPoint.addSocket(newSocket);

        cpService.save(chargingPoint);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cp_id}")
    public ResponseEntity<?> patchCP(@PathVariable String cp_id, @RequestBody ChargingPointDTO updatedCp){
        ChargingPoint cp = cpService.getCPById(cp_id);
        cp.setTariffsId(updatedCp.getTariffIds());
        cp.setName(updatedCp.getName());
        cp.setAddress(updatedCp.getAddress());
        cp.setLatitude(updatedCp.getLatitude());
        cp.setLongitude(updatedCp.getLongitude());
        cp.setLastUpdate(updatedCp.getLastUpdated());

        cpService.save(cp);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cp_id}/{socket_id}")
    public ResponseEntity<?> patchSocket(@PathVariable String cp_id, @PathVariable String socket_id, @RequestBody SocketDTO updatedSocket){
        ChargingPoint chargingPoint = cpService.getCPById(cp_id);
        Socket socket = chargingPoint.getSockets().stream().filter(socket1 -> socket1.getSocketId().equals(socket_id)).toList().get(0);
        chargingPoint.removeSocket(socket);

        socket.setType(updatedSocket.getSocketType());
        socket.setStatus(updatedSocket.getStatus());
        socket.setLastUpdate(updatedSocket.getLastUpdate());
        socket.setAvailability(updatedSocket.getAvailability());

        chargingPoint.addSocket(socket);
        cpService.save(chargingPoint);

        return ResponseEntity.noContent().build();
    }
}
