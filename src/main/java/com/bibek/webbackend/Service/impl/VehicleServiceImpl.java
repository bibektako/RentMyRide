package com.bibek.webbackend.Service.impl;

import com.bibek.webbackend.Entity.Vehicle;
import com.bibek.webbackend.Repository.VehicleRepository;
import com.bibek.webbackend.Service.FileService;
import com.bibek.webbackend.Service.VehicleService;
import com.bibek.webbackend.Dto.VehicleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Value("${project.image}")
    private String uploadPath;

    @Autowired
    public FileService fileService;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public VehicleDto getVehicleById(int id) {
        Vehicle vehicle = this.vehicleRepository.findById(id).orElseThrow(()->new RuntimeException("Vehicle not found"));
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setVehicleId(vehicle.getVehicleId());
        vehicleDto.setVehicleName(vehicle.getVehicleName());
        vehicleDto.setVehicleType(vehicle.getVehicleType());
        vehicleDto.setVehicleNumber(vehicle.getVehicleNumber());
        vehicleDto.setNumberOfSeats(vehicle.getNumberOfSeats());
        vehicleDto.setPricePerHour(vehicle.getPricePerHour());
        vehicleDto.setVehicleImageString(vehicle.getVehicleImage());

        return vehicleDto;
    }

    @Override
    public List<Vehicle> viewAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public String saveVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleName(vehicleDto.getVehicleName());
        vehicle.setVehicleType(vehicleDto.getVehicleType());
        String fileName = UUID.randomUUID().toString() + "_" + vehicleDto.getVehicleImage().getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        try {
            // Ensure the upload directory exists
            Path uploadPathDir = Paths.get(uploadPath);
            if (!Files.exists(uploadPathDir)) {
                Files.createDirectories(uploadPathDir);
            }
            System.out.println("Saving image to path: " + filePath.toAbsolutePath());
            Files.copy(vehicleDto.getVehicleImage().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vehicle.setVehicleImage(fileName);
        vehicle.setNumberOfSeats(vehicleDto.getNumberOfSeats());
        vehicle.setVehicleNumber(vehicleDto.getVehicleNumber());
        vehicle.setPricePerHour(vehicleDto.getPricePerHour());
        vehicleRepository.save(vehicle);
        return "vehicle saved";
    }

    @Override
    public VehicleDto updateVehicle(VehicleDto vehicleDto) {
        Vehicle existingVehicle = vehicleRepository.findById(vehicleDto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existingVehicle.setVehicleName(vehicleDto.getVehicleName());
        existingVehicle.setVehicleType(vehicleDto.getVehicleType());
        existingVehicle.setNumberOfSeats(vehicleDto.getNumberOfSeats());
        existingVehicle.setVehicleNumber(vehicleDto.getVehicleNumber());
        existingVehicle.setPricePerHour(vehicleDto.getPricePerHour());

        // Handle image update if present
        if (vehicleDto.getVehicleImage() != null && !vehicleDto.getVehicleImage().isEmpty()) {
            try {
                String fileName = fileService.uploadImage(uploadPath, vehicleDto.getVehicleImage());
                existingVehicle.setVehicleImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update image", e);
            }
        }

        vehicleRepository.save(existingVehicle);

        // Convert Vehicle back to VehicleDto and return
        VehicleDto updatedVehicleDto = new VehicleDto();
        updatedVehicleDto.setVehicleId(existingVehicle.getVehicleId());
        updatedVehicleDto.setVehicleName(existingVehicle.getVehicleName());
        updatedVehicleDto.setVehicleType(existingVehicle.getVehicleType());
        updatedVehicleDto.setVehicleNumber(existingVehicle.getVehicleNumber());
        updatedVehicleDto.setNumberOfSeats(existingVehicle.getNumberOfSeats());
        updatedVehicleDto.setPricePerHour(existingVehicle.getPricePerHour());
        updatedVehicleDto.setVehicleImageString(existingVehicle.getVehicleImage());

        return updatedVehicleDto;
    }
    @Override
    public String updateVehicleWithoutImage(VehicleDto vehicleDto) {
        Vehicle existingVehicle = vehicleRepository.getVehicleByVehicleId(vehicleDto.getVehicleId());
        existingVehicle.setVehicleName(vehicleDto.getVehicleName());
        existingVehicle.setVehicleType(vehicleDto.getVehicleType());
        existingVehicle.setNumberOfSeats(vehicleDto.getNumberOfSeats());
        existingVehicle.setVehicleNumber(vehicleDto.getVehicleNumber());
        vehicleRepository.save(existingVehicle);
        return "vehicle updated";
    }

    @Override
    public void deleteVehicleById(int vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }
}
