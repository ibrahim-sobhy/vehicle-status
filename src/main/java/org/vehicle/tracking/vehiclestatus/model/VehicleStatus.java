package org.vehicle.tracking.vehiclestatus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class VehicleStatus {
  private Vehicle vehicle;
  private Owner owner;
}
