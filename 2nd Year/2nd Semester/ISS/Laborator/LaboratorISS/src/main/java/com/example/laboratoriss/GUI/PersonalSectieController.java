package com.example.laboratoriss.GUI;

import com.example.laboratoriss.Domain.*;
import com.example.laboratoriss.Service.IComandaService;
import com.example.laboratoriss.Service.IMedicamentService;
import com.example.laboratoriss.Utils.Observer.ChangeEventType;
import com.example.laboratoriss.Utils.Observer.ObservableEvent;
import com.example.laboratoriss.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonalSectieController implements Observer<Comanda> {
    private static final Logger logger = LogManager.getLogger();

    private IMedicamentService medicamentService;
    private IComandaService comandaService;
    private User currentUser;

    // Tab 1 - New Order
    @FXML
    private TableView<Medicament> medicamenteTableView;
    @FXML
    private TableColumn<Medicament, String> numeColumn;
    @FXML
    private TableColumn<Medicament, Float> pretColumn;
    @FXML
    private TableColumn<Medicament, String> descriereColumn;

    @FXML
    private Spinner<Integer> cantitateSpinner;
    @FXML
    private Button adaugaInCosButton;

    @FXML
    private TableView<ComandaItemViewModel> cosTableView;
    @FXML
    private TableColumn<ComandaItemViewModel, String> cosNumeColumn;
    @FXML
    private TableColumn<ComandaItemViewModel, Integer> cosCantitateColumn;
    @FXML
    private TableColumn<ComandaItemViewModel, Float> cosTotalColumn;
    @FXML
    private Button stergeSelectieCosButton;
    @FXML
    private Button trimiteComandaButton;

    // Tab 2 - My Orders
    @FXML
    private TableView<ComandaViewModel> comenziTableView;
    @FXML
    private TableColumn<ComandaViewModel, Integer> comandaIdColumn;
    @FXML
    private TableColumn<ComandaViewModel, String> dataColumn;
    @FXML
    private TableColumn<ComandaViewModel, String> statusColumn;
    @FXML
    private TableColumn<ComandaViewModel, String> medicamenteColumn;
    @FXML
    private Button anuleazaComandaButton;

    private ObservableList<Medicament> medicamentModel = FXCollections.observableArrayList();
    private ObservableList<ComandaItemViewModel> cosModel = FXCollections.observableArrayList();
    private ObservableList<ComandaViewModel> comenziModel = FXCollections.observableArrayList();

    public void setServices(IMedicamentService medicamentService, IComandaService comandaService, User currentUser) {
        this.medicamentService = medicamentService;
        this.comandaService = comandaService;
        this.currentUser = currentUser;

        comandaService.addObserver(this);

        initializeTableViews();
        loadData();
    }

    @FXML
    public void initialize() {
        // Initialize spinner
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        cantitateSpinner.setValueFactory(valueFactory);

        // Set up button handlers
        adaugaInCosButton.setOnAction(event -> handleAddToCart());
        stergeSelectieCosButton.setOnAction(event -> handleRemoveFromCart());
        trimiteComandaButton.setOnAction(event -> handleSubmitOrder());
        anuleazaComandaButton.setOnAction(event -> handleCancelOrder());
    }

    private void initializeTableViews() {
        // Medicamente table
        numeColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        pretColumn.setCellValueFactory(new PropertyValueFactory<>("pret"));
        descriereColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        medicamenteTableView.setItems(medicamentModel);

        // Cos table
        cosNumeColumn.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
        cosCantitateColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        cosTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        cosTableView.setItems(cosModel);

        // Comenzi table
        comandaIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataFormatted"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        medicamenteColumn.setCellValueFactory(new PropertyValueFactory<>("medicamenteString"));
        comenziTableView.setItems(comenziModel);
    }

    private void loadData() {
        // Load medications
        medicamentModel.clear();
        medicamentService.getAll().forEach(medicamentModel::add);

        // Load user's orders
        loadOrdersData();
    }

    private void loadOrdersData() {
        comenziModel.clear();
        List<Comanda> comenzi = comandaService.getByUser(currentUser);

        for (Comanda comanda : comenzi) {
            ComandaViewModel viewModel = new ComandaViewModel(comanda);
            comenziModel.add(viewModel);
        }
    }

    private void handleAddToCart() {
        Medicament selectedMedicament = medicamenteTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert("Atenție", "Selectați un medicament din listă.");
            return;
        }

        int cantitate = cantitateSpinner.getValue();

        // Check if medicament is already in cart
        for (ComandaItemViewModel item : cosModel) {
            if (item.getMedicament().getId().equals(selectedMedicament.getId())) {
                item.setCantitate(item.getCantitate() + cantitate);
                item.setTotal(item.getCantitate() * selectedMedicament.getPret());
                cosTableView.refresh();
                return;
            }
        }

        // Add new item to cart
        ComandaItemViewModel newItem = new ComandaItemViewModel(selectedMedicament, cantitate);
        cosModel.add(newItem);
    }

    private void handleRemoveFromCart() {
        ComandaItemViewModel selectedItem = cosTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Atenție", "Selectați un medicament din coș pentru a-l șterge.");
            return;
        }

        cosModel.remove(selectedItem);
    }

    private void handleSubmitOrder() {
        if (cosModel.isEmpty()) {
            showAlert("Atenție", "Coșul este gol. Adăugați medicamente în coș înainte de a trimite comanda.");
            return;
        }

        List<ComandaItem> items = new ArrayList<>();
        for (ComandaItemViewModel viewModel : cosModel) {
            items.add(new ComandaItem(null, viewModel.getMedicament(), viewModel.getCantitate()));
        }

        Comanda comanda = new Comanda(null, items, Status.IN_ASTEPTARE, currentUser, LocalDateTime.now());
        comandaService.save(comanda);

        showAlert("Succes", "Comanda a fost trimisă cu succes!");
        cosModel.clear();
    }

    private void handleCancelOrder() {
        ComandaViewModel selectedOrder = comenziTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Atenție", "Selectați o comandă pentru anulare.");
            return;
        }

        if (selectedOrder.getStatus().equals(Status.IN_ASTEPTARE.toString())) {
            comandaService.delete(selectedOrder.getId());
            showAlert("Succes", "Comanda a fost anulată.");
        } else {
            showAlert("Eroare", "Doar comenzile în așteptare pot fi anulate.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void update(ObservableEvent<Comanda> event) {
        if (event.getType() == ChangeEventType.ADD ||
                event.getType() == ChangeEventType.UPDATE ||
                event.getType() == ChangeEventType.DELETE) {
            loadOrdersData();
        }
    }

    // ViewModel classes
    public static class ComandaItemViewModel {
        private Medicament medicament;
        private int cantitate;
        private float total;

        public ComandaItemViewModel(Medicament medicament, int cantitate) {
            this.medicament = medicament;
            this.cantitate = cantitate;
            this.total = medicament.getPret() * cantitate;
        }

        public Medicament getMedicament() {
            return medicament;
        }

        public String getNumeMedicament() {
            return medicament.getNume();
        }

        public int getCantitate() {
            return cantitate;
        }

        public void setCantitate(int cantitate) {
            this.cantitate = cantitate;
        }

        public float getTotal() {
            return total;
        }

        public void setTotal(float total) {
            this.total = total;
        }
    }

    public static class ComandaViewModel {
        private final Integer id;
        private final LocalDateTime data;
        private final Status status;
        private final List<ComandaItem> items;

        public ComandaViewModel(Comanda comanda) {
            this.id = comanda.getId();
            this.data = comanda.getData();
            this.status = comanda.getStatus();
            this.items = comanda.getComandaItems();
        }

        public Integer getId() {
            return id;
        }

        public String getDataFormatted() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return data.format(formatter);
        }

        public String getStatus() {
            return status.toString();
        }

        public String getMedicamenteString() {
            return items.stream()
                    .map(item -> item.getMedicament().getNume() + " (x" + item.getCantitate() + ")")
                    .collect(Collectors.joining(", "));
        }
    }
}