package org.example.zboruri.GUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.zboruri.Domain.Client;
import org.example.zboruri.Domain.Flight;
import org.example.zboruri.Repository.FlightRepository;
import org.example.zboruri.Repository.TicketRepository;
import org.example.zboruri.Service.FlightService;
import org.example.zboruri.Utils.Events.Event;
import org.example.zboruri.Utils.Events.ZborChangeEvent;
import org.example.zboruri.Utils.Obs.Observer;
import org.example.zboruri.Utils.Paging.Page;
import org.example.zboruri.Utils.Paging.Pageable;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable, Observer {
    @FXML
    private Label welcomeLabel;
    @FXML
    private ComboBox<String> fromLocationComboBox;
    @FXML
    private ComboBox<String> toLocationComboBox;
    @FXML
    private DatePicker departureDatePicker;
    @FXML
    private TableView<Flight> flightsTableView;
    @FXML
    private TableColumn<Flight, String> fromColumn;
    @FXML
    private TableColumn<Flight, String> toColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> departureTimeColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> landingTimeColumn;
    @FXML
    private TableColumn<Flight, Integer> availableSeatsColumn;
    @FXML
    private Label pageLabel;
    @FXML
    private Label totalPagesLabel;

    private Client loggedInUser;
    private final FlightService flightService;
    private final FlightRepository flightRepository = new FlightRepository();
    private final TicketRepository ticketRepository = new TicketRepository();
    private int currentPage = 0;
    private static final int PAGE_SIZE = 5;

    public MainController() {
        this.flightService = new FlightService(new FlightRepository(), new TicketRepository());
        this.flightService.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadLocations();
    }

    private void setupTableColumns() {
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        landingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("landingTime"));
        availableSeatsColumn.setCellValueFactory(cellData -> {
            Flight flight = cellData.getValue();
            int availableSeats = flightService.getAvailableSeats(flight);
            return new SimpleIntegerProperty(availableSeats).asObject();
        });
    }

    private void loadLocations() {
        List<String> locations = flightService.getAllLocations();
        fromLocationComboBox.getItems().addAll(locations);
        toLocationComboBox.getItems().addAll(locations);
    }

    public void setLoggedInUser(Client client) {
        this.loggedInUser = client;
        welcomeLabel.setText("Welcome, " + client.getName());
    }

    @FXML
    protected void handleSearch() {
        if (departureDatePicker.getValue() == null ||
                fromLocationComboBox.getValue() == null ||
                toLocationComboBox.getValue() == null) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        LocalDateTime departureDate = departureDatePicker.getValue().atStartOfDay();
        String from = fromLocationComboBox.getValue();
        String to = toLocationComboBox.getValue();

        System.out.println("Searching flights from: " + from + " to: " + to + " on: " + departureDate);

        List<Flight> flights = flightService.findFlightsByDateAndLocations(departureDate, from, to);
        flightsTableView.getItems().setAll(flights);

        currentPage = 0;
        updatePagination();
    }

    @FXML
    protected void handlePurchaseTicket() {
        Flight selectedFlight = flightsTableView.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showAlert("Error", "Please select a flight");
            return;
        }

        try {
            boolean success = flightService.purchaseTicket(loggedInUser.getUsername(), selectedFlight);
            if (success) {
                showAlert("Success", "Ticket purchased successfully");
            } else {
                showAlert("Error", "Failed to purchase ticket");
            }
        } catch (RuntimeException e) {
            showAlert("Error", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void handlePrevious() {
        if (currentPage > 0) {
            currentPage--;
            updatePagination();
        }
    }

    @FXML
    protected void handleNext() {
        Page<Flight> page = flightRepository.findAllOnPage(new Pageable(currentPage + 1, PAGE_SIZE));
        if (!((List<Flight>) page.getElementsOnPage()).isEmpty()) {
            currentPage++;
            updatePagination();
        }
    }

    private void updatePagination() {
        if (departureDatePicker.getValue() == null ||
                fromLocationComboBox.getValue() == null ||
                toLocationComboBox.getValue() == null) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        LocalDateTime departureDate = departureDatePicker.getValue().atStartOfDay();
        String from = fromLocationComboBox.getValue();
        String to = toLocationComboBox.getValue();

        Page<Flight> page = flightRepository.findFilteredFlightsOnPage(departureDate, from, to, new Pageable(currentPage, PAGE_SIZE));
        flightsTableView.getItems().setAll((List<Flight>) page.getElementsOnPage());

        int totalPages = (int) Math.ceil(page.getTotalNumberOfElements() / (double) PAGE_SIZE);
        pageLabel.setText("Page: " + (currentPage + 1));
        totalPagesLabel.setText("of " + totalPages);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void update(Event event) {
        if (event instanceof ZborChangeEvent) {
            updatePagination();
        }
    }


}