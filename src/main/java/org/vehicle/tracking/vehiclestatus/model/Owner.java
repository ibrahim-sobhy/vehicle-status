package org.vehicle.tracking.vehiclestatus.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
@AllArgsConstructor
@Getter
public class Owner {
  private String name;
  private String address;
  @Singular
  private List<Vehicle> vehicles;
}
