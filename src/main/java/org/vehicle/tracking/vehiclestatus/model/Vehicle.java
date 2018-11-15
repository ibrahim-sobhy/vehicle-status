package org.vehicle.tracking.vehiclestatus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
public class Vehicle {
  @Id
  private String id;
  @Transient
  private String registrationNumber;
  @Transient
  private Status status;
  @Column(name = "owner_id")
  private Long ownerId;
}
