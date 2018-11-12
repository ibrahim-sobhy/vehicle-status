package org.vehicle.tracking.vehiclestatus.api;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.unprocessableEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vehicle.tracking.vehiclestatus.model.Owner;
import org.vehicle.tracking.vehiclestatus.model.Vehicle;
import org.vehicle.tracking.vehiclestatus.service.VehicleOwnerService;

@RestController
@RequestMapping("/api/owner")
public class VehicleOwnerApi {


  @Autowired
  private VehicleOwnerService vehicleStatusService;

  /**
   * Find all available owners with their vehicles.
   *
   * @return a list of owners.
   */
  @GetMapping
  public ResponseEntity<List<Owner>> findAll() {
    return vehicleStatusService.findAll()
        .map(ResponseEntity::ok)
        .orElse(noContent().build());
  }

  /**
   * Ping a vehicle to connect to system.
   *
   * @param vehicle the vehicle to connect.
   * @return the connected vehicle.
   */
  @PostMapping("/vehicle")
  public ResponseEntity<Vehicle> pingVehicle(@RequestBody Vehicle vehicle) {
    return vehicleStatusService.ping(vehicle.getId())
        .map(ResponseEntity::ok)
        .orElse(unprocessableEntity().build());
  }
}
