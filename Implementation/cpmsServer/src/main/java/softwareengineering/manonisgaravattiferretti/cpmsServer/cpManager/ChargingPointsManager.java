package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.OcppConnectionTrigger;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events.TogglePriceOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiLocationsSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.DSOManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.EnergyMixManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleDsoSelectionOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleEnergyMixOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SocketAvailabilityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class ChargingPointsManager {
    private final ChargingPointService chargingPointService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EnergyMixManager energyMixManager;
    private final DSOManager dsoManager;
    private final DSOOfferService dsoOfferService;
    private final PriceManager priceManager;
    private final OcppConnectionTrigger ocppConnectionTrigger;
    private final OcpiLocationsSender ocpiLocationsSender;
    private final EmspDetailsService emspDetailsService;
    private final SocketService socketService;
    private final Logger logger = LoggerFactory.getLogger(ChargingPointsManager.class);

    @Autowired
    public ChargingPointsManager(ChargingPointService chargingPointService,
                                 ApplicationEventPublisher applicationEventPublisher,
                                 EnergyMixManager energyMixManager, DSOManager dsoManager,
                                 DSOOfferService dsoOfferService, PriceManager priceManager,
                                 OcppConnectionTrigger ocppConnectionTrigger,
                                 OcpiLocationsSender ocpiLocationsSender,
                                 EmspDetailsService emspDetailsService, SocketService socketService) {
        this.chargingPointService = chargingPointService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.energyMixManager = energyMixManager;
        this.dsoManager = dsoManager;
        this.dsoOfferService = dsoOfferService;
        this.priceManager = priceManager;
        this.ocppConnectionTrigger = ocppConnectionTrigger;
        this.ocpiLocationsSender = ocpiLocationsSender;
        this.emspDetailsService = emspDetailsService;
        this.socketService = socketService;
    }

    @GetMapping("/api/CPO/chargingPoints")
    public ResponseEntity<Iterable<ChargingPoint>> getAllCps(@AuthenticationPrincipal CPO cpo,
                                                             @RequestParam(defaultValue = "0") int offset,
                                                             @RequestParam(defaultValue = "100") int limit) {
        return new ResponseEntity<>(chargingPointService.getChargingPointsOfCpo(cpo.getCpoCode(), offset, limit), HttpStatus.OK);
    }

    @GetMapping("/api/CPO/chargingPoints/{id}")
    public ResponseEntity<ChargingPoint> getCPById(@AuthenticationPrincipal CPO cpo, @PathVariable String id) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charging point not found");
        }
        return new ResponseEntity<>(chargingPointOptional.get(), HttpStatus.OK);
    }

    @PostMapping("/api/CPO/chargingPoints")
    public ResponseEntity<?> addChargingPoint(@RequestBody AddChargingPointDTO addChargingPointDTO, @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService
                .findChargingPointByExternalId(addChargingPointDTO.getCpId());
        if (chargingPointOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charging point with that id already exists");
        }
        ChargingPoint chargingPoint = EntityFromDTOConverter.fromAddCpDTOToCp(addChargingPointDTO, cpo.getCpoCode());
        ResponseEntity<Void> response = ocppConnectionTrigger.triggerConnection(chargingPoint);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Wasn't able to connect to the charging point");
        }
        List<Socket> socketsWithId = new ArrayList<>();
        for (Socket socket: chargingPoint.getSockets()) {
            socketsWithId.add(socketService.save(socket));
        }
        chargingPoint.setSockets(socketsWithId);
        chargingPointService.addChargingPoint(chargingPoint);
        List<EmspDetails> emspDetailsList = emspDetailsService.findAll();
        for (EmspDetails emspDetails: emspDetailsList) {
            ocpiLocationsSender.putChargingPoint(emspDetails, chargingPoint.getCpId(),
                    EntityFromDTOConverter.emspChargingPointDTOFromChargingPoint(chargingPoint), true);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/CPO/chargingPoints/{id}")
    public ResponseEntity<?> deleteChargingPoint(@AuthenticationPrincipal CPO cpo, @PathVariable String id) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found");
        }
        for (Socket socket: chargingPointOptional.get().getSockets()) {
            socketService.removeSocket(socket.getId());
        }
        chargingPointService.deleteChargingPoint(id);
        return new ResponseEntity<>(chargingPointOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/api/CPO/chargingPoints/{cpId}/sockets")
    public ResponseEntity<Iterable<Socket>> getSockets(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(cpId, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found");
        }
        return ResponseEntity.ok(chargingPointOptional.get().getSockets());
    }

    @GetMapping("/api/CPO/chargingPoints/{cpId}/sockets/{socketId}")
    public ResponseEntity<Socket> getSocketInfo(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId,
                                                @PathVariable Integer socketId) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(cpId, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found");
        }
        Optional<Socket> socketOptional = chargingPointOptional.get().getSockets().stream()
                .filter(socket -> Objects.equals(socket.getSocketId(), socketId)).findFirst();
        if (socketOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Socket not found");
        }
        return ResponseEntity.ok(socketOptional.get());
    }

    @GetMapping("/api/CPO/chargingPoints/{cpId}/tariffs")
    public ResponseEntity<Iterable<Tariff>> getTariffs(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(cpId, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found");
        }
        return ResponseEntity.ok(chargingPointOptional.get().getTariffs());
    }

    @GetMapping("/api/CPO/chargingPoints/{id}/tariffs/{tariffId}")
    public ResponseEntity<Tariff> getTariff(@AuthenticationPrincipal CPO cpo, @PathVariable String id,
                                            @PathVariable String tariffId) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found");
        }
        Optional<Tariff> tariffOptional = chargingPointOptional.get().getTariffs().stream()
                .filter(tariff -> tariff.getTariffId().equals(tariffId)).findFirst();
        if (tariffOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found");
        }
        return ResponseEntity.ok(tariffOptional.get());
    }

    @PostMapping("/api/CPO/chargingPoints/{id}/tariffs")
    public ResponseEntity<Tariff> addNewTariff(@PathVariable String id, @AuthenticationPrincipal CPO cpo,
                                               @RequestBody AddTariffDTO addTariffDTO) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found");
        }
        return new ResponseEntity<>(priceManager.addTariff(addTariffDTO, id), HttpStatus.CREATED);
    }

    @PutMapping("/api/CPO/chargingPoints/{id}/tariffs/{tariffId}")
    public ResponseEntity<?> putTariff(@PathVariable String id, @PathVariable String tariffId,
                                       @RequestBody AddTariffDTO addTariffDTO, @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found");
        }
        Tariff tariff;
        try {
            tariff = priceManager.putTariff(addTariffDTO, id, tariffId);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "wrong parameters format");
        }
        return new ResponseEntity<>(tariff, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/CPO/chargingPoints/{id}/tariffs/{tariffId}")
    public ResponseEntity<?> deleteTariff(@PathVariable String id, @PathVariable String tariffId,
                                          @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found");
        }
        priceManager.removeTariff(id, tariffId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/CPO/chargingPoints/{id}/optimizer/{type}")
    public ResponseEntity<?> toggleOptimizer(@PathVariable String id, @PathVariable String type,
                                             @RequestParam boolean automaticMode) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id);
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "charging point not found");
        }
        switch (type) {
            case "dsoSelection" -> {
                ToggleDsoSelectionOptimizerEvent event = new ToggleDsoSelectionOptimizerEvent(this, automaticMode, id);
                applicationEventPublisher.publishEvent(event);
                chargingPointService.updateToggleOptimizer(id, "DSOSelection", automaticMode);
            }
            case "energyMix" -> {
                ToggleEnergyMixOptimizerEvent event = new ToggleEnergyMixOptimizerEvent(this, id, automaticMode);
                applicationEventPublisher.publishEvent(event);
                chargingPointService.updateToggleOptimizer(id, "EnergyMix", automaticMode);
            }
            case "price" -> {
                TogglePriceOptimizerEvent event = new TogglePriceOptimizerEvent(this, automaticMode, id);
                applicationEventPublisher.publishEvent(event);
                chargingPointService.updateToggleOptimizer(id, "Price", automaticMode);
            }
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the optimizer type is not recognized");
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/CPO/chargingPoints/{id}/sockets/{socketId}")
    public ResponseEntity<?> updateSocketAvailability(@PathVariable String id, @PathVariable Integer socketId,
                                                      @RequestBody @Valid ChangeSocketAvailabilityDTO socketAvailabilityDTO,
                                                      @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "socket not found");
        }
        SocketAvailabilityEvent socketAvailabilityEvent = new SocketAvailabilityEvent(this,
                socketAvailabilityDTO, chargingPointOptional.get().getCpId(), socketId);
        applicationEventPublisher.publishEvent(socketAvailabilityEvent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/api/CPO/chargingPoints/{id}/energySources/battery/{batteryId}")
    public ResponseEntity<?> includeBattery(@PathVariable String id, @PathVariable Integer batteryId,
                                            @RequestBody @Valid IncludeBatteryDTO includeBatteryDTO,
                                            @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "battery not found");
        }
        if (!energyMixManager.includeBattery(includeBatteryDTO, id, batteryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameters not correct");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/api/CPO/chargingPoints/{id}/energySources/battery/{batteryId}/availability")
    public ResponseEntity<?> changeBatteryAvailability(@PathVariable String id, @PathVariable Integer batteryId,
                                                       @RequestParam boolean available, @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "battery not found");
        }
        if (!energyMixManager.changeBatteryAvailability(id, batteryId, available)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameters not correct");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/CPO/chargingPoints/{id}/dso/offers")
    public ResponseEntity<Iterable<DSOOfferDTO>> getChargingPointDsoOffers(@PathVariable String id, @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "charging point not found");
        }
        List<DSOOfferDTO> dsoOffers = dsoOfferService.findOffersOfCp(chargingPointOptional.get().getId())
                .stream().map(EntityFromDTOConverter::fromOfferToDto).toList();
        return new ResponseEntity<>(dsoOffers, HttpStatus.OK);
    }

    @PatchMapping("/api/CPO/chargingPoints/{id}/dso/offers/{offerId}")
    public ResponseEntity<?> changeDsoProvider(@PathVariable String id, @PathVariable String offerId,
                                               @RequestBody OfferTimeSlot offerTimeSlot, @AuthenticationPrincipal CPO cpo) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "charging point not found");
        }
        if(!dsoManager.changeDsoProviderManual(id, chargingPointOptional.get().getCpId(), offerId, offerTimeSlot)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameters not correct");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
