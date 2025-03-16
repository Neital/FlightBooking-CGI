package com.CGI.service;

import com.CGI.model.entity.Airport;
import com.CGI.model.entity.Flight;
import com.CGI.model.entity.Plane;
import com.CGI.model.entity.Seat;
import com.CGI.model.enums.SeatClass;
import com.CGI.model.enums.SeatFeature;
import com.CGI.model.valueobject.Position;
import com.CGI.repository.AirportRepo;
import com.CGI.repository.FlightRepo;
import com.CGI.repository.PlaneRepo;
import com.CGI.repository.SeatRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FlightService {

    @Autowired
    private final FlightRepo flightRepo;
    @Autowired
    private final AirportRepo airportRepo;
    @Autowired
    private final PlaneRepo planeRepo;

    private final SeatService seatService;

    public FlightService(FlightRepo flightRepo, AirportRepo airportRepo, PlaneRepo planeRepo, SeatService seatService) {
        this.flightRepo = flightRepo;
        this.airportRepo = airportRepo;
        this.planeRepo = planeRepo;
        this.seatService = seatService;
    }

    @PostConstruct
    public void init() {
        if (flightRepo.count() == 0) {
            //add methods later
            createFlightFromScratch("JFK", "John F. Kennedy", "New York",
                    "USA", "LHR", "London Heathrow", "London",
                    "UK", "2025-04-16 17:30", "2025-04-16 20:00",
                    "Boeing 737", 20, 6, new int[]{15, 20, 30}, new int[]{1, 2, 3, 20}, new int[]{1, 10, 20});
        }
    }

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

    public Plane createPlane(int rowCount, int colCount, String model, Map<SeatClass, Integer> seatPrices, int[] legroomRows, int[] exitRows) {
        // Create new plane or use existing one based on model
        Optional<Plane> existingPlane = planeRepo.findByModel(model);
        if (existingPlane.isPresent()) {
            return existingPlane.get();
        }

        Plane plane = new Plane();
        plane.setModel(model);

        // Create seats for the plane
        Map<Position, Seat> seats = new HashMap<>();
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

                seats.put(position, seat);
            }
        }
        plane.setSeats(seats);

        // Set default seat prices based on seatClass
        plane.setSeatPrices(seatPrices);

        planeRepo.save(plane); // Save plane to the database
        return plane;
    }

    private SeatClass determineSeatClass(int row) {
        if (row <= 3) { // First 3 rows could be First Class
            return SeatClass.FIRST_CLASS;
        } else if (row <= 8) { // Rows 4 to 8 could be Business Class
            return SeatClass.BUSINESS;
        } else { // Everything else is Economy Class
            return SeatClass.ECONOMY;
        }
    }

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

    private List<Seat> initializeSeatsForFlight(Plane plane, Flight flight) {
        List<Seat> seats = new ArrayList<>();
        for (Seat seat : plane.getSeats().values()) {
            Seat flightSeat = new Seat();
            //flightSeat.setAvailable(true); // All seats are initially available
            flightSeat.setPosition(seat.getPosition());
            flightSeat.setPlane(plane);
            flightSeat.setSeatClass(seat.getSeatClass());
            flightSeat.setFlight(flight); // Link to the current flight
            seats.add(flightSeat);
        }
        return seats;
    }

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


        // Create Flight
        Flight flight = new Flight();
        flight.setFromAirport(fromAirport);
        flight.setToAirport(toAirport);
        flight.setStartDate(startDate);
        flight.setArrivalDate(arrivalDate);
        flight.setPlane(plane);

        // Initialize seats for the flight
        List<Seat> seats = initializeSeatsForFlight(plane, flight);
        flight.setSeats(seats); // Set the seats for the flight

        flightRepo.save(flight); // Save Flight
    }

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
        for (int i = 0; i < seatPrices.length; i++) {
            seatPriceMap.put(SeatClass.values()[i], seatPrices[i]);
        }

        // Create new Plane
        Plane plane = createPlane(rowCount, colCount, planeModel, seatPriceMap, legroomRows, exitRows);

        // Create Flight
        Flight flight = new Flight();
        flight.setFromAirport(fromAirport);
        flight.setToAirport(toAirport);
        flight.setStartDate(startDate);
        flight.setArrivalDate(arrivalDate);
        flight.setPlane(plane);

        // Initialize seats for the flight
        List<Seat> seats = initializeSeatsForFlight(plane, flight);
        flight.setSeats(seats); // Set the seats for the flight

        flightRepo.save(flight); // Save Flight
    }

    public void bookSeatOnFlight(Long flightId, Position position) {
        // Optionally validate that the flight exists and is not past
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found"));

        seatService.bookSeatOnFlight(flightId, position); // Delegate the seat booking logic to SeatService
    }

    public List<Flight> getAllFlight() {
        return flightRepo.findAll();
    }

    public Flight getFlightById(String id) {
        return flightRepo.getReferenceById(Long.valueOf(id));
    }
}
