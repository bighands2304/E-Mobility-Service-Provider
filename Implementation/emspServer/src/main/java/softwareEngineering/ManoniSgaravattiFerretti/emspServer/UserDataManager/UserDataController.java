package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.UserVehicle;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Vehicle;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserVehicleService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.VehicleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserDataController {

    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    UserVehicleService userVehicleService;
    @Autowired
    ReservationService reservationService;

    @PostMapping("/addVehicle")
    public ResponseEntity<?> addVehicle(@RequestBody Map<String, String> payload){
        User user;
        try {
            //Get the user by his id
            user = userService.findById(Long.parseLong(payload.get("userId")));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
        //Values for the simulation of the VIN API
        String[] socketTypes = {"MEDIUM","FAST","RAPID"};
        String[] models = {"Tesla Model Y", "Citroen AMI", "BMW i3", "Hyundai Ioniq Electric", "Volkswagen ID.4", "Subaru Solterra", "MINI Cooper Electric"};

        //Search if the vehicle already exist
        Vehicle vehicle = vehicleService.getVehicleByVin(payload.get("vin"));
        if (vehicle==null) {
            //Create a new vehicle if it doesn't exist
            vehicle = new Vehicle();
            //Set required fields
            vehicle.setVINCode(payload.get("vin"));
            //Randomly select a SocketType simulating the VIN API
            vehicle.setSocketType(socketTypes[new Random().nextInt(socketTypes.length)]);

            //Set non required fields
            //Randomly select a Model simulating the VIN API
            vehicle.setModel(models[new Random().nextInt(socketTypes.length)]);
            vehicle.setBatteryPercentage(new Random().nextInt(100));
            vehicle.setKmRange(new Random().nextInt(600));

            try {
                //Save the vehicle in the DB
                vehicleService.saveVehicle(vehicle);
            } catch (Exception e) {
                ResponseEntity.badRequest().body(e.toString());
            }
        }

        //Create the connection between the user and vehicle we found at the beginning
        UserVehicle uv = new UserVehicle();
        uv.setUser(user);
        uv.setVehicle(vehicle);
        //If the vehicle is the favourite find the old favourite one and replace with the new one
        if(Boolean.parseBoolean(payload.get("favourite"))){
            uv.setFavourite(true);
            UserVehicle oldFavourite = userVehicleService.findFavouriteOfUser(user.getId());
            if(oldFavourite!=null) {
                oldFavourite.setFavourite(false);
                userVehicleService.saveUserVehicle(oldFavourite);
            }
        }

        //Save the connection between user and vehicle
        try{
            userVehicleService.saveUserVehicle(uv);
        }catch (Exception e){
            //Return the error message
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        //Return the connection in the response
        return ResponseEntity.ok(uv);
    }

    @DeleteMapping("/deleteVehicle/{vin}")
    public ResponseEntity<?> deleteVehicle(@PathVariable String vin){
        vehicleService.deleteVehicleByVin(vin);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/setFavouriteVehicle")
    public ResponseEntity<?> setFavouriteVehicle(@RequestBody Map<String, String> payload){
        //Find user by id
        User user = userService.findById(Long.parseLong(payload.get("userId")));

        //Find the new favourite vehicle and set it as favourite
        UserVehicle newFavourite = userVehicleService.findVehcileByIds(user.getId(), Long.parseLong(payload.get("vin")));
        newFavourite.setFavourite(true);

        //Find the actual favourite vehicle and set it as non-favourite
        UserVehicle oldFavourite = userVehicleService.findFavouriteOfUser(user.getId());
        if(oldFavourite!=null) {
            oldFavourite.setFavourite(false);

            //Save the changes
            userVehicleService.saveUserVehicle(oldFavourite);
        }
        userVehicleService.saveUserVehicle(newFavourite);

        //Return the new favourite vehicle in the response
        return ResponseEntity.ok(newFavourite);
    }

    @GetMapping("/getUserVehicles/{userId}")
    public ResponseEntity<?> getUserVehicles(@PathVariable String userId){
        //Collect the vehicle user connection by the id of the user
        List<UserVehicle> uv = userVehicleService.getUserVehicles(Long.parseLong(userId));
        //Get just the vehicles from the list
        //List<Vehicle> vehicles = uv.stream().map(UserVehicle::getVehicle).collect(Collectors.toList());
        //Return the list of vehicles in the response
        return ResponseEntity.ok(uv);
    }

    @GetMapping("/getReservationsOfUser/{userId}")
    public ResponseEntity<?> getReservations(@PathVariable String userId){
        //Collect all the reservations(Active, Ended, and Deleted)
        List<Reservation> reservations = reservationService.getReservationsByUserId(Long.parseLong(userId));
        //Return the reservations in the response
        return ResponseEntity.ok(reservations);
    }
}
