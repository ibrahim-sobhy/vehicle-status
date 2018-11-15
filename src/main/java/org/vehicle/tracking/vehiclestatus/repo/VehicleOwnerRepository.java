package org.vehicle.tracking.vehiclestatus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.vehicle.tracking.vehiclestatus.model.Owner;

import java.util.List;

//@Service
//public class VehicleOwnerRepository {
//  public List<Owner> findAll() {
//    throw new UnsupportedOperationException();
//  }
//}
@Repository
public interface VehicleOwnerRepository extends JpaRepository<Owner, String> {

}
