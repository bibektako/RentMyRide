package com.bibek.webbackend.Repository;

import com.bibek.webbackend.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {

    List<Vehicle> findAllByVehicleId(int vehicleId);

    Vehicle getVehicleByVehicleId(int vehicleId);

    List<Vehicle> findAll();

    void deleteAllByVehicleId(int vehicleId);
}
