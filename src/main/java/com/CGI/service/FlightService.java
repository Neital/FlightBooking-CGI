package com.CGI.service;

import com.CGI.model.entity.*;
import com.CGI.model.enums.SeatClass;
import com.CGI.model.enums.SeatFeature;
import com.CGI.model.valueobject.Position;
import com.CGI.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.CGI.dto.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepo flightRepo;
    private final AirportRepo airportRepo;
    private final PlaneRepo planeRepo;
    private final FlightSeatRepo flightSeatRepo;

    @Autowired
    public FlightService(FlightRepo flightRepo, AirportRepo airportRepo, PlaneRepo planeRepo, FlightSeatRepo flightSeatRepo) {
        this.flightRepo = flightRepo;
        this.airportRepo = airportRepo;
        this.planeRepo = planeRepo;
        this.flightSeatRepo = flightSeatRepo;
    }

    @PostConstruct
    public void init() {
        if (flightRepo.count() == 0) {
            //add methods later
            createFlightFromScratch("JFK", "John F. Kennedy", "New York",
                    "USA", "LHR", "London Heathrow", "London",
                    "UK", "2025-04-16 17:30", "2025-04-16 20:00",
                    "Boeing 737", 20, 6, new int[]{90, 75, 50}, new int[]{1, 2, 3, 20}, new int[]{1, 10, 20});
        }
    }

    /**
     * Creates an airport with given parameters.
     * @param code airport code (JFK).
     * @param name airport name (John F. Kennedy).
     * @param city city name (New York).
     * @param country country name (USA).
     * @return new airport.
     */
    public Airport createAirport(String code, String name, String city, String country) {
        Optional<Airport> existingAirport = airportRepo.findByCode(code);
        if (existingAirport.isPresent()) {
            return existingAirport.get(); // Return existing airport if it already exists
        }

        // Create new airport if not found
        Airport airport = new Airport();
        airport.setCode(code);
        airport.setName(name);
        airport.setCity(city);
        airport.setCountry(country);

        airportRepo.save(airport); // Save new airport
        return airport;
    }

    /**
     * Creates a plane with given parameters.
     * @param rowCount vertical size of a plane.
     * @param colCount horizontal size of a plane, expected to be even.
     * @param model plane model name used as a unique identifier.
     * @param seatPrices prices for each type of seatClass.
     * @param legroomRows int array of rows with a LEGROOM feature.
     * @param exitRows int array of rows with a EXIT_ROW feature.
     * @return new plane.
     */

    public Plane createPlane(int rowCount, int colCount, String model, Map<SeatClass, Integer> seatPrices, int[] legroomRows, int[] exitRows) {
        // Create new plane or use existing one based on model
        Optional<Plane> existingPlane = planeRepo.findByModel(model);
        if (existingPlane.isPresent()) {
            Plane plane = existingPlane.get();
            if (!plane.getSeats().isEmpty()) {
                return plane;
            }
        }

        Plane plane = new Plane();
        plane.setModel(model);

        // Create seats for the plane
        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= rowCount; row++) {
            for (int col = 1; col <= colCount; col++) {
                Position position = new Position(row, col);
                Seat seat = new Seat();
                seat.setPosition(position);
                seat.setPlane(plane);
                SeatClass seatClass = determineSeatClass(row);
                seat.setSeatClass(seatClass);
                Set<SeatFeature> seatFeatures = determineSeatFeatures(row, col, colCount, legroomRows, exitRows);
                seat.setFeatures(seatFeatures);

                seats.add(seat);
            }
        }
        plane.setSeats(seats);

        // Set default seat prices based on seatClass
        plane.setSeatPrices(seatPrices);

        planeRepo.save(plane); // Save plane to the database
        return plane;
    }

    /**
     * Utility class for assigning seatClass for each seat.
     * @param row seat row.
     * @return determined seatClass for the seat.
     */

    private SeatClass determineSeatClass(int row) {
        if (row <= 3) { // First 3 rows could be First Class
            return SeatClass.FIRST_CLASS;
        } else if (row <= 8) { // Rows 4 to 8 could be Business Class
            return SeatClass.BUSINESS;
        } else { // Everything else is Economy Class
            return SeatClass.ECONOMY;
        }
    }

    /**
     * Utility class for assigning seatFeature(s) for each seat.
     * @param row seat row.
     * @param col seat column.
     * @param colCount column count on a plane.
     * @param legroomRows which rows should have LEGROOM feature.
     * @param exitRows which rows should have EXIT_ROW feature.
     * @return set of seatFeature(s) for the seat.
     */

    private Set<SeatFeature> determineSeatFeatures(int row, int col, int colCount,
                                                   int[] legroomRows, int[] exitRows) {
        Set<SeatFeature> features = new HashSet<>();
        // Add WINDOW feature for first and last column
        if (col == 1 || col == colCount) {
            features.add(SeatFeature.WINDOW);
        }
        // Add AISLE feature for the middle columns (for even colCount)
        if (col == colCount / 2 || col == colCount / 2 + 1) {
            features.add(SeatFeature.AISLE);
        }
        // Add LEGROOM feature for specified legroom rows
        if (Arrays.stream(legroomRows).anyMatch(legroomRow -> legroomRow == row)) {
            features.add(SeatFeature.LEGROOM);
        }

        // Add EXIT_ROW feature for specified exit rows
        if (Arrays.stream(exitRows).anyMatch(exitRow -> exitRow == row)) {
            features.add(SeatFeature.EXIT_ROW);
        }

        return features;
    }

    /**
     * Utility method for initializing flight seats.
     * @param flight related flight.
     * @param plane related plane.
     * @return list of flightSeats associated with the flight.
     */

    private List<FlightSeat> initializeFlightSeats(Flight flight, Plane plane) {
        List<FlightSeat> flightSeats = new ArrayList<>();
        for (Seat seat : plane.getSeats()) {
            FlightSeat flightSeat = new FlightSeat();
            flightSeat.setFlight(flight);
            flightSeat.setSeat(seat);
            flightSeat.setAvailable(true); // Initially available
            flightSeats.add(flightSeat);
        }
        return flightSeats;
    }

    /**
     * Main method for creating a flight with existing airports and plane information.
     * @param fromAirportCode first airport identificator.
     * @param toAirportCode second airport identificator.
     * @param startDateString start date of a flight.
     * @param arrivalDateString arrival date of a flight.
     * @param planeModel plane identificator.
     */
    public void createFlightWithDetails(String fromAirportCode, String toAirportCode, //from & to Airports
                                        String startDateString, String arrivalDateString, //dates
                                        String planeModel) { //Plane

        // Find Airports
        Airport fromAirport = airportRepo.findByCode(fromAirportCode)
                .orElseThrow(() -> new IllegalArgumentException("Airport with code " + fromAirportCode + " not found"));
        Airport toAirport = airportRepo.findByCode(toAirportCode)
                .orElseThrow(() -> new IllegalArgumentException("Airport with code " + toAirportCode + " not found"));

        // Parse start and arrival date strings into LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateString, formatter);
        LocalDateTime arrivalDate = LocalDateTime.parse(arrivalDateString, formatter);

        // Find existing plane by model, or create a new one
        Plane plane = planeRepo.findByModel(planeModel).orElseThrow(() -> new IllegalArgumentException("Plane with model " + planeModel + " not found"));


        // Create and save Flight
        saveFlight(fromAirport, toAirport, startDate, arrivalDate, plane);
    }

    /**
     * Main method for creating a flight with required airports and plane.
     * @param fromAirportCode first airport identificator.
     * @param fromAirportName first airport name.
     * @param fromAirportCity first airport city.
     * @param fromAirportCountry first airport country.
     * @param toAirportCode second airport identificator.
     * @param toAirportName second airport name.
     * @param toAirportCity second airport city.
     * @param toAirportCountry second airport country.
     * @param startDateString start date of a flight.
     * @param arrivalDateString arrival date of a flight.
     * @param planeModel plane identificator.
     * @param rowCount vertical size of a plane.
     * @param colCount horizontal size of a plane, expected to be even.
     * @param seatPrices prices for each type of seatClass.
     * @param legroomRows int array of rows with a LEGROOM feature.
     * @param exitRows int array of rows with a EXIT_ROW feature.
     */
    public void createFlightFromScratch(String fromAirportCode, String fromAirportName, String fromAirportCity, String fromAirportCountry, //from Airport
                                        String toAirportCode, String toAirportName, String toAirportCity, String toAirportCountry, //to Airport
                                        String startDateString, String arrivalDateString, //dates
                                        String planeModel, int rowCount, int colCount, int[] seatPrices, int[] legroomRows, int[] exitRows) { //Plane

        // Create Airports
        Airport fromAirport = createAirport(fromAirportCode, fromAirportName, fromAirportCity, fromAirportCountry);
        Airport toAirport = createAirport(toAirportCode, toAirportName, toAirportCity, toAirportCountry);

        // Parse start and arrival date strings into LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateString, formatter);
        LocalDateTime arrivalDate = LocalDateTime.parse(arrivalDateString, formatter);

        // Convert seatPrices array into a Map<SeatClass, Integer>
        Map<SeatClass, Integer> seatPriceMap = new HashMap<>();
        SeatClass[] seatClasses = SeatClass.values();
        for (int i = 0; i < Math.min(seatPrices.length, seatClasses.length); i++) {
            seatPriceMap.put(seatClasses[i], seatPrices[i]);
        }

        // Create new Plane
        Plane plane = createPlane(rowCount, colCount, planeModel, seatPriceMap, legroomRows, exitRows);

        // Create and save Flight
        saveFlight(fromAirport, toAirport, startDate, arrivalDate, plane);
    }

    /**
     * Utility method for saving a flight after getting/creating all required data.
     * @param fromAirport first airport class.
     * @param toAirport second airport class.
     * @param startDate formatted start date of a flight.
     * @param arrivalDate formatted arrival date of a flight.
     * @param plane associated plane class.
     */
    private void saveFlight(Airport fromAirport, Airport toAirport, LocalDateTime startDate, LocalDateTime arrivalDate, Plane plane) {
        // Create Flight
        Flight flight = new Flight();
        flight.setFromAirport(fromAirport);
        flight.setToAirport(toAirport);
        flight.setStartDate(startDate);
        flight.setArrivalDate(arrivalDate);
        flight.setPlane(plane);
        flight = flightRepo.save(flight);

        // Initialize seats for the flight
        List<FlightSeat> flightSeats = initializeFlightSeats(flight, plane);

        flightSeatRepo.saveAll(flightSeats); // Save Flight
    }

    public List<FlightDTO> getAllFlights() {
        List<Flight> flights = flightRepo.findAll();
        return flights.stream().map(this::convertToFlightDTO).collect(Collectors.toList());
    }

    public FlightDTO getFlightById(Long id) {
        Flight flight = flightRepo.getReferenceById(id);
        return convertToFlightDTO(flight);
    }

    /////////////////////////////////

    // Converting data to DTO

    /////////////////////////////////

    public FlightDTO convertToFlightDTO(Flight flight) {
        return new FlightDTO(
                flight.getId(),
                flight.getFromAirport().getCode(),
                flight.getToAirport().getCode(),
                flight.getFromAirport().getName(),
                flight.getToAirport().getName(),
                flight.getFromAirport().getCity(),
                flight.getToAirport().getCity(),
                flight.getFromAirport().getCountry(),
                flight.getToAirport().getCountry(),
                flight.getStartDate(),
                flight.getArrivalDate(),
                convertToPlaneDTO(flight.getPlane()),
                flight.getFlightSeats().stream().map(this::convertToFlightSeatDTO).collect(Collectors.toList())


        );
    }

    public PlaneDTO convertToPlaneDTO(Plane plane) {
        return new PlaneDTO(
                plane.getId(),
                plane.getModel(),
                plane.getSeats().stream().map(this::convertToSeatDTO).collect(Collectors.toList()),
                plane.getSeatPrices()
        );
    }

    public SeatDTO convertToSeatDTO(Seat seat) {
        return new SeatDTO(
                seat.getId(),
                seat.getPosition().getSeatRow(),
                seat.getPosition().getSeatColumn(),
                seat.getSeatClass().name(),
                seat.getFeatures().stream().map(SeatFeature::name).collect(Collectors.toList())
        );
    }

    public FlightSeatDTO convertToFlightSeatDTO(FlightSeat flightSeat) {
        return new FlightSeatDTO(
                flightSeat.getId(),
                convertToSeatDTO(flightSeat.getSeat()),
                flightSeat.getFlight().getId(),
                flightSeat.isAvailable()
        );
    }

    public AirportDTO convertToAirportDTO(Airport airport) {
        return new AirportDTO(
                airport.getId(),
                airport.getCode(),
                airport.getName(),
                airport.getCity(),
                airport.getCountry()
        );
    }
}
