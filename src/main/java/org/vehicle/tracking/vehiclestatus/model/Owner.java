package org.vehicle.tracking.vehiclestatus.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Owner {
  @Id
  private Long id;
  private String name;
  private String address;
  @OneToMany()
  @JoinColumn(name="owner_id")
  @Singular
  private List<Vehicle> vehicles;
}
