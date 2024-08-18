package com.bibek.webbackend.Service;

import com.bibek.webbackend.Entity.Vehicle;
import com.bibek.webbackend.Dto.VehicleDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VehicleService {

    VehicleDto getVehicleById(int id);

    List<Vehicle> viewAllVehicles();

    String saveVehicle(VehicleDto vehicleDto);

    VehicleDto updateVehicle(VehicleDto vehicleDto);

    String updateVehicleWithoutImage(VehicleDto vehicleDto);

    void deleteVehicleById(int vehicleId);

}
