package org.vehicle.tracking.vehiclestatus.service;

import org.apache.commons.configuration.FileConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.vehicle.tracking.vehiclestatus.model.Owner;
import org.vehicle.tracking.vehiclestatus.model.Status;
import org.vehicle.tracking.vehiclestatus.model.Vehicle;
import org.vehicle.tracking.vehiclestatus.repo.VehicleOwnerRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class VehicleOwnerServiceTest {

  @InjectMocks
  private VehicleOwnerService vehicleOwnerService;
  @Mock
  private VehicleOwnerRepository vehicleOwnerRepository;
  @Mock
  private VehicleService vehicleService;

  @Test
  public void shouldReturnEmptyIfNoOwners() {

    when(vehicleOwnerRepository.findAll()).thenReturn(emptyList());

    assertThat(vehicleOwnerService.findAll()).isEmpty();

    verify(vehicleOwnerRepository, times(1)).findAll();
  }

  @Test
  public void shouldReturnFullListOfOwners() {
    List<Owner> owners = sampleOfOwners();

    when(vehicleOwnerRepository.findAll()).thenReturn(owners);

    assertThat(vehicleOwnerService.findAll()).isEqualTo(owners);

    verify(vehicleOwnerRepository, times(1)).findAll();
  }

  @Test
  public void shouldThrowErrorIfVehicleIdIsAbsent() {
    final String vehicleId = null;

    assertThatThrownBy(() -> vehicleOwnerService.ping(vehicleId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("vehicleId is missing");

    verify(vehicleOwnerRepository, never()).findAll();
  }

  @Test
  public void shouldReturnEmptyIfVehicleIsNotExist() {
    final String vehicleId = "ERT444";

    when(vehicleService.ping(vehicleId)).thenReturn(empty());

    assertThat(vehicleOwnerService.ping(vehicleId))
        .isEmpty();

    verify(vehicleService, times(1)).ping(vehicleId);
  }

  @Test
  public void shouldReturnThePingedVehicle() {
    final String vehicleId = "ERT444";
    final Vehicle vehicle = Vehicle.builder()
        .id(vehicleId)
        .registrationNumber("1235")
        .status(Status.Connected)
        .build();

    when(vehicleService.ping(vehicleId)).thenReturn(Optional.of(vehicle));

    assertThat(vehicleOwnerService.ping(vehicleId))
        .isNotEmpty()
        .contains(vehicle);

    verify(vehicleService, times(1)).ping(vehicleId);
  }


  private List<Owner> sampleOfOwners() {
    return asList(
        Owner.builder()
            .name("Kalles Grustransporter AB")
            .address("Cementvägen 8,")
            .vehicle(Vehicle.builder()
                .id("YS2R4X20005399401")
                .registrationNumber("ABC123")
                .status(Status.Connected)
                .build())
            .vehicle(Vehicle.builder()
                .id("VLUR4X20009093588")
                .registrationNumber("DEF456")
                .status(Status.Disconnected)
                .build())
            .vehicle(Vehicle.builder()
                .id("VLUR4X20009048066")
                .registrationNumber("GHI789")
                .status(Status.Connected)
                .build())
            .build(),
        Owner.builder()
            .name("Johans Bulk AB")
            .address("Balkvägen 12, 222")
            .vehicle(Vehicle.builder()
                .id("YS2R4X20005388011")
                .registrationNumber("JKL012")
                .status(Status.Connected)
                .build())
            .vehicle(Vehicle.builder()
                .id("YS2R4X20005387949")
                .registrationNumber("MNO345")
                .status(Status.Disconnected)
                .build())
            .build()
    );
  }

}
