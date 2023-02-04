package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import com.google.gson.*;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.LocationsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.ChargingPointDTO;
import org.springframework.beans.BeanUtils;


import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
public class StationsResearchManager {
    private Mapper mapper;
    @Autowired
    ChargingPointService cpService;
    @Autowired
    LocationsSender locations;
    @Autowired
    TariffService tariffService;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
            ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime()).create();

    @GetMapping("/getCPsInRange/{latitude}/{longitude}/{range}")
    public ResponseEntity<?> getCpsInRange(@PathVariable Double latitude, @PathVariable Double longitude, @PathVariable Double range){
        //Search for CPs in a certain range
        List<ChargingPoint> cps= cpService.getCPsInRange(latitude, latitude+range, longitude, longitude+range);

        List<JsonElement> jsonCPs = new ArrayList<>();
        for (ChargingPoint cp: cps) {
            JsonElement jsonCP = gson.toJsonTree(cp);
            List<Tariff> tariffs = new ArrayList<>();
            for (String tariffId: cp.getTariffsId()) {
                tariffs.add(tariffService.getTariffById(tariffId));
            }
            jsonCP.getAsJsonObject().addProperty("tariffs", gson.toJson(tariffs));
            jsonCPs.add(jsonCP);
        }

        //Return the response with the list of CPs found
        return ResponseEntity.ok(gson.toJson(jsonCPs));
    }

    @GetMapping("/getCP/{cpId}")
    public ResponseEntity<?> getCp(@PathVariable String cpId){
        //Collect the payload
        ChargingPoint cp = cpService.getCPById(cpId);

        //Fetch the status of the cp from its cpms
        locations.getCp(cp);

        JsonElement jsonCP = gson.toJsonTree(cp);
        List<Tariff> tariffs = new ArrayList<>();
        for (String tariffId: cp.getTariffsId()) {
            tariffs.add(tariffService.getTariffById(tariffId));
        }
        jsonCP.getAsJsonObject().addProperty("tariffs", gson.toJson(tariffs));

        //Return last information
        return ResponseEntity.ok(gson.toJson(jsonCP));
    }

    @GetMapping("/getTariff/{tariffId}")
    public ResponseEntity<?> getTariff(@PathVariable String tariffId){
        //Return the tariff by its id
        return ResponseEntity.ok(tariffService.getTariffById(tariffId));
    }
 }
