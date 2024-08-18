package com.bibek.webbackend.Controller;

import com.bibek.webbackend.Dto.VehicleDto;
import com.bibek.webbackend.Entity.Vehicle;
import com.bibek.webbackend.Service.FileService;
import com.bibek.webbackend.Service.VehicleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;
    
    @Value("${project.image}")
    private String path;

    @Autowired
    private FileService fileService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/save-vehicle")
    public String saveVehicle(@ModelAttribute VehicleDto vehicleDto){
        try {
            vehicleService.saveVehicle(vehicleDto);
            return "Vehicle saved";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    @PostMapping("/update-vehicle")
    public String updateVehicle(@ModelAttribute VehicleDto vehicleDto){
        try {
            vehicleService.updateVehicle(vehicleDto);
            return "Vehicle updated";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    @PostMapping("/update-vehicle-without-image")
    public String updateVehicleWithoutImage(@RequestBody VehicleDto vehicleDto){
        try {
            vehicleService.updateVehicleWithoutImage(vehicleDto);
            return "Vehicle updated";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    @GetMapping("/find-all-vehicles")
    public List<Vehicle> viewAllVehicles(){
           return(vehicleService.viewAllVehicles());
    }

    @DeleteMapping("/delete-vehicle/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable("vehicleId") int vehicleId) {
        try {
            vehicleService.deleteVehicleById(vehicleId);
            return ResponseEntity.ok("Vehicle deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete vehicle: " + e.getMessage());
        }
    }

    @GetMapping("/total-vehicle-records")
    public ResponseEntity<Object> getTotalVehicleRecords() {
        List<Vehicle> vehicles = vehicleService.viewAllVehicles();
        int totalRecords = vehicles.size();
        Map<String, Object> response = new HashMap<>();
        response.put("totalRecords", totalRecords);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/image/upload/{vehicleId}")
    public ResponseEntity<VehicleDto> uploadPostImage(@RequestParam("image") MultipartFile image,
                                                      @PathVariable int vehicleId) throws IOException {
        VehicleDto vehicleDto = this.vehicleService.getVehicleById(vehicleId);

        String fileName = this.fileService.uploadImage(path, image);
        vehicleDto.setVehicleImageString(fileName);
        System.out.println("image = " + fileName
        );

        VehicleDto updatedVehicle = this.vehicleService.updateVehicle(vehicleDto);
        return new ResponseEntity<>(updatedVehicle, HttpStatus.CREATED);
    }


    @GetMapping(value="/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable String imageName,
                              HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
