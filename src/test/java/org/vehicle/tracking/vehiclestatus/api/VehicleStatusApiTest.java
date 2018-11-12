package org.vehicle.tracking.vehiclestatus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.vehicle.tracking.vehiclestatus.model.Owner;
import org.vehicle.tracking.vehiclestatus.model.Status;
import org.vehicle.tracking.vehiclestatus.model.Vehicle;
import org.vehicle.tracking.vehiclestatus.service.VehicleOwnerService;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class VehicleStatusApiTest {

  @InjectMocks
  private VehicleOwnerApi vehicleOwnerApi;

  private MockMvc mvc;

  @Mock
  private VehicleOwnerService vehicleOwnerService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    mvc = MockMvcBuilders.standaloneSetup(vehicleOwnerApi).build();

  }

  @Test
  public void shouldReturn204IfNoRecordReturned() throws Exception {

    when(vehicleOwnerService.findAll()).thenReturn(empty());

    mvc.perform(get("/api/owner")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());
    verify(vehicleOwnerService, times(1)).findAll();
  }

  @Test
  public void shouldReturn200WithListOfVehicles() throws Exception {

    List<Owner> owners = sampleOfOwners();

    when(vehicleOwnerService.findAll()).thenReturn(Optional.of(owners));

    mvc.perform(get("/api/owner")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*]", hasSize(2)))
        .andExpect(jsonPath("$.[0].name", is("Kalles Grustransporter AB")))
        .andExpect(jsonPath("$.[1].address", is("Balkvägen 12, 222")))
        .andExpect(jsonPath("$.[0].vehicles[*]", hasSize(3)))
        .andExpect(jsonPath("$.[0].vehicles[0].id", is("YS2R4X20005399401")))
        .andExpect(jsonPath("$.[0].vehicles[0].status", is("Connected")));

    verify(vehicleOwnerService, times(1)).findAll();
  }

  @Test
  public void shouldReturn422WhenUpdateNonExistedVehicle() throws Exception {

    final String vehicleId = "YS2R4X20005399401";

    when(vehicleOwnerService.ping(vehicleId)).thenReturn(empty());
    mvc.perform(post("/api/owner/vehicle")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(Vehicle.builder().id(vehicleId).build())))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());

    verify(vehicleOwnerService, times(1)).ping(vehicleId);
  }

  @Test
  public void shouldReturn200WhenUpdateVehicle() throws Exception {

    final String vehicleId = "YS2R4X20005399401";
    final String registrationNumber = "ABC123";

    when(vehicleOwnerService.ping(vehicleId)).thenReturn(
        Optional.of(Vehicle.builder()
            .id(vehicleId)
            .registrationNumber(registrationNumber)
            .status(Status.Connected)
            .build()));

    mvc.perform(post("/api/owner/vehicle")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(Vehicle.builder().id(vehicleId).build())))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(vehicleId)))
        .andExpect(jsonPath("$.registrationNumber", is(registrationNumber)))
        .andExpect(jsonPath("$.status", is("Connected")));

    verify(vehicleOwnerService, times(1)).ping(vehicleId);
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
