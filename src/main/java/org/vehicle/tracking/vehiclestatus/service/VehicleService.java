package org.vehicle.tracking.vehiclestatus.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.vehicle.tracking.vehiclestatus.model.Vehicle;

import java.util.List;
import java.util.Optional;

@FeignClient("vehicle-api")
public interface VehicleService {

  @PostMapping("/api/vehicle")
  Optional<Vehicle> ping(String vehicleId);

  @GetMapping("/api/vehicle")
  List<Vehicle> findAll();
}
