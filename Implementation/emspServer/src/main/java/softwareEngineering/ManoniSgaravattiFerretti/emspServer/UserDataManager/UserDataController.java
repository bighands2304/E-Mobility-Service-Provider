package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataManager;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.AuthManager.TokenManager;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.UserVehicle;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Vehicle;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserVehicleService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.VehicleService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserDataController {

    @Autowired
    TokenManager tokenManager;

    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    UserVehicleService userVehicleService;
    @PostMapping("/addVehicle")
    public ResponseEntity<?> addVehicle(@RequestBody Map<String, String> payload){
        User user;
        try {
            user = userService.findById(Long.parseLong(payload.get("userId")));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setVINCode(payload.get("vin"));
        vehicle.setSocketType(payload.get("socketType"));

        vehicle.setModel(payload.get("model"));

        try {
            vehicleService.saveVehicle(vehicle);
        }catch (Exception e) {
            ResponseEntity.badRequest().body(e.toString());
        }

        UserVehicle uv = new UserVehicle();
        uv.setUser(user);
        uv.setVehicle(vehicle);
        uv.setFavourite(Boolean.parseBoolean(payload.get("favourite")));
        //TODO remove eventual old favourite
        try{
            userVehicleService.saveUserVehicle(uv);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
        return ResponseEntity.ok(uv);
    }

    @GetMapping("/getUserVehicles")
    public ResponseEntity<?> getUserVehicles(@RequestBody Map<String, String> payload){
        List<UserVehicle> uv = userVehicleService.getUserVehicles(Long.parseLong(payload.get("userId")));
        List<Vehicle> vehicles = uv.stream().map(u -> u.getVehicle()).collect(Collectors.toList());
        return ResponseEntity.ok(vehicles);
    }
}
