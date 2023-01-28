package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangeSocketAvailabilityDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.IncludeBatteryDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.DSOManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.EnergyMixManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleDsoSelectionOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleEnergyMixOptimizerEvent;

import java.util.Optional;

@RestController("/api/CPO/chargingPoints")
public class ChargingPointsManager {
    private final ChargingPointService chargingPointService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EnergyMixManager energyMixManager;
    private final DSOManager dsoManager;

    @Autowired
    public ChargingPointsManager(ChargingPointService chargingPointService,
                                 ApplicationEventPublisher applicationEventPublisher,
                                 EnergyMixManager energyMixManager, DSOManager dsoManager) {
        this.chargingPointService = chargingPointService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.energyMixManager = energyMixManager;
        this.dsoManager = dsoManager;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<ChargingPoint>> getAllCps(@AuthenticationPrincipal CPO cpo,
                                                             @RequestParam(defaultValue = "0") int offset,
                                                             @RequestParam(defaultValue = "100") int limit) {
        return new ResponseEntity<>(chargingPointService.getChargingPointsOfCpo(cpo.getCpoCode(), offset, limit), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargingPoint> getCPById(@AuthenticationPrincipal CPO cpo, @PathVariable String id) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charging point not found");
        }
        return new ResponseEntity<>(chargingPointOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChargingPoint(@AuthenticationPrincipal CPO cpo, @PathVariable String id) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charging point not found");
        }
        chargingPointService.deleteChargingPoint(id);
        return new ResponseEntity<>(chargingPointOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/{cpId}/sockets")
    public ResponseEntity<Socket> getSockets(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId) {
        return ResponseEntity.ok(null); //TODO
    }

    @GetMapping("/{cpId}/sockets/{socketId}")
    public ResponseEntity<Socket> getSocketInfo(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId,
                                                @PathVariable String socketId) {
        return ResponseEntity.ok(null); //TODO
    }

    @GetMapping("/{cpId}/tariffs")
    public ResponseEntity<Socket> getTariffs(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId) {
        return ResponseEntity.ok(null); //TODO
    }

    @GetMapping("/{cpId}/tariffs/{tariffId}")
    public ResponseEntity<Socket> getTariffs(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId,
                                             @PathVariable String tariffId) {
        return ResponseEntity.ok(null); //TODO
    }

    @PostMapping()
    public void addChargingPoint() {
        //Todo
    }

    @PostMapping("/{id}/optimizer/{type}")
    public ResponseEntity<?> toggleOptimizer(@PathVariable String id, @PathVariable String type,
                                             @RequestParam boolean automatic) {
        switch (type) {
            case "dsoSelection" -> {
                ToggleDsoSelectionOptimizerEvent event = new ToggleDsoSelectionOptimizerEvent(this, automatic, id);
                applicationEventPublisher.publishEvent(event);
            }
            case "energyMix" -> {
                ToggleEnergyMixOptimizerEvent event = new ToggleEnergyMixOptimizerEvent(this, id, automatic);
                applicationEventPublisher.publishEvent(event);
            }
            case "price" -> {
                // todo
            }
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the optimizer type is not recognized");
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/sockets/{socketId}")
    public ResponseEntity<?> updateSocketAvailability(@PathVariable String id, @PathVariable Integer socketId,
                                                      @RequestBody @Valid ChangeSocketAvailabilityDTO socketAvailabilityDTO,
                                                      @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointOfCpoById(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "socket not found");
        }
        // todo: send to socket status
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/energySources/battery/{batteryId}")
    public ResponseEntity<?> includeBattery(@PathVariable String id, @PathVariable Integer batteryId,
                                            @RequestBody @Valid IncludeBatteryDTO includeBatteryDTO,
                                            @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointOfCpoById(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "battery not found");
        }
        if (!energyMixManager.includeBattery(includeBatteryDTO, id, batteryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameters not correct");
        }
        return ResponseEntity.ok().build();
    }
}
