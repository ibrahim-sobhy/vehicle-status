package org.vehicle.tracking.vehiclestatus.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vehicle.tracking.vehiclestatus.model.Owner;
import org.vehicle.tracking.vehiclestatus.model.Vehicle;
import org.vehicle.tracking.vehiclestatus.repo.VehicleOwnerRepository;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;

@Service
public class VehicleOwnerService {

  @Autowired
  VehicleOwnerRepository vehicleOwnerRepository;

  @Autowired
  VehicleService vehicleService;

  public List<Owner> findAll() {
    List<Owner> owners = vehicleOwnerRepository.findAll();
    if(!owners.isEmpty()) {

      List<Vehicle> vehiclesInfo = vehicleService.findAll();


      owners.stream()
          .flatMap(owner -> owner.getVehicles().stream())
          .forEach(vehicle -> {
            vehiclesInfo.stream()
                .filter(actualVehicle -> actualVehicle.getId().equalsIgnoreCase(vehicle.getId()))
                .findAny()
                .map(targetVehicle -> {
                  vehicle.setRegistrationNumber(targetVehicle.getRegistrationNumber());
                  vehicle.setStatus(targetVehicle.getStatus());
                  return targetVehicle;
                })
                .orElse(null);

          });

    }
    return owners;
  }

  public Optional<Vehicle> ping(String vehicleId) {
    if(StringUtils.isBlank(vehicleId)) throw new IllegalArgumentException("vehicleId is missing");
    return vehicleService.ping(vehicleId);
  }
}
