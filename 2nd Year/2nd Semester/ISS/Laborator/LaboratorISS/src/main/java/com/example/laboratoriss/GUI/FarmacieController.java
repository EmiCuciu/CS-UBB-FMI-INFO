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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FarmacieController implements Observer<Comanda> {

    private static final Logger logger = LogManager.getLogger();

    // Service references
    private IMedicamentService medicamentService;
    private User currentUser;

    // Static context holder for services
    public static class ApplicationContext {
        private static IComandaService comandaService;

        public static IComandaService getComandaService() {
            return comandaService;
        }

        public static void setComandaService(IComandaService comandaService) {
            ApplicationContext.comandaService = comandaService;
        }
    }

    // Tab 1 - Orders
    @FXML
    private TableView<ComandaItemViewModel> TabelComenziTableView;
    @FXML
    private TableColumn<ComandaItemViewModel, Integer> ComandaIDTableColumn;
    @FXML
    private TableColumn<ComandaItemViewModel, String> SectieTableColumn;
    @FXML
    private TableColumn<ComandaItemViewModel, String> MedicamentTableColumn;
    @FXML
    private TableColumn<ComandaItemViewModel, Integer> CantitateTableColumn;

    @FXML
    private TableView<ComandaItemViewModel> TabelComenziTableView_Onorate;
    @FXML
    private TableColumn<ComandaItemViewModel, Integer> ComandaIDTableColumn_Onorate;
    @FXML
    private TableColumn<ComandaItemViewModel, String> SectieTableColumn_Onorate;
    @FXML
    private TableColumn<ComandaItemViewModel, String> MedicamentTableColumn_Onorate;
    @FXML
    private TableColumn<ComandaItemViewModel, Integer> CantitateTableColumn_Onorate;

    @FXML
    private Button OnoreazaComandaButton;
    @FXML
    private Button RefuzaComandaButton;
    @FXML
    private Button StergeComandaButton;

    // Tab 2 - Medications
    @FXML
    private TableView<Medicament> TabelMedicamenteTableView;
    @FXML
    private TableColumn<Medicament, String> MedicamenteInDepozitTableColumn;
    @FXML
    private TableColumn<Medicament, Float> PretInDepozitTableColumn;
    @FXML
    private TableColumn<Medicament, String> DescriereInDepozitTableColumn;

    @FXML
    private TextField numeMedicamentTextField;
    @FXML
    private TextField pretMedicamentTextField;
    @FXML
    private TextArea descriereMedicamentTextField;

    @FXML
    private Button AdaugaMedicamentButton;
    @FXML
    private Button StergeMedicamentDepozitButton;
    @FXML
    private Button UpdateMedicamtDepozitButton;

    // Observable lists for table views
    private ObservableList<ComandaItemViewModel> pendingOrdersModel = FXCollections.observableArrayList();
    private ObservableList<ComandaItemViewModel> fulfilledOrdersModel = FXCollections.observableArrayList();
    private ObservableList<Medicament> medicamentsModel = FXCollections.observableArrayList();

    public void setServices(IMedicamentService medicamentService, User user) {
        this.medicamentService = medicamentService;
        this.currentUser = user;

        // Register as observer for command changes
        ApplicationContext.getComandaService().addObserver(this);

        initializeTableViews();
        loadData();
    }

    @FXML
    public void initialize() {
        // Set up button handlers
        AdaugaMedicamentButton.setOnAction(event -> handleAddMedicament());
        StergeMedicamentDepozitButton.setOnAction(event -> handleDeleteMedicament());
        UpdateMedicamtDepozitButton.setOnAction(event -> handleUpdateMedicament());

        OnoreazaComandaButton.setOnAction(event -> handleFulfillOrder());
        RefuzaComandaButton.setOnAction(event -> handleRejectOrder());
        StergeComandaButton.setOnAction(event -> handleDeleteFulfilledOrder());
    }

    private void initializeTableViews() {
        // Initialize Pending Orders Table
        ComandaIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("comandaId"));
        SectieTableColumn.setCellValueFactory(new PropertyValueFactory<>("sectie"));
        MedicamentTableColumn.setCellValueFactory(new PropertyValueFactory<>("medicamentNume"));
        CantitateTableColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        TabelComenziTableView.setItems(pendingOrdersModel);

        // Initialize Fulfilled Orders Table
        ComandaIDTableColumn_Onorate.setCellValueFactory(new PropertyValueFactory<>("comandaId"));
        SectieTableColumn_Onorate.setCellValueFactory(new PropertyValueFactory<>("sectie"));
        MedicamentTableColumn_Onorate.setCellValueFactory(new PropertyValueFactory<>("medicamentNume"));
        CantitateTableColumn_Onorate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        TabelComenziTableView_Onorate.setItems(fulfilledOrdersModel);

        // Initialize Medications Table
        MedicamenteInDepozitTableColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        PretInDepozitTableColumn.setCellValueFactory(new PropertyValueFactory<>("pret"));
        DescriereInDepozitTableColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        TabelMedicamenteTableView.setItems(medicamentsModel);
    }

    private void loadData() {
        loadMedications();
        loadOrders();
    }

    private void loadMedications() {
        medicamentsModel.clear();
        medicamentsModel.addAll(StreamSupport
                .stream(medicamentService.getAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    private void loadOrders() {
        pendingOrdersModel.clear();
        fulfilledOrdersModel.clear();

        // Get orders by status
        List<Comanda> pendingOrders = ApplicationContext.getComandaService().getByStatus(Status.IN_ASTEPTARE);
        List<Comanda> fulfilledOrders = ApplicationContext.getComandaService().getByStatus(Status.ONORATA);

        // Process pending orders
        for (Comanda comanda : pendingOrders) {
            for (ComandaItem item : comanda.getComandaItems()) {
                ComandaItemViewModel viewModel = new ComandaItemViewModel(
                        comanda.getId(),
                        comanda.getUser().getSectie(),
                        item.getMedicament().getNume(),
                        item.getCantitate(),
                        comanda
                );
                pendingOrdersModel.add(viewModel);
            }
        }

        // Process fulfilled orders
        for (Comanda comanda : fulfilledOrders) {
            for (ComandaItem item : comanda.getComandaItems()) {
                ComandaItemViewModel viewModel = new ComandaItemViewModel(
                        comanda.getId(),
                        comanda.getUser().getSectie(),
                        item.getMedicament().getNume(),
                        item.getCantitate(),
                        comanda
                );
                fulfilledOrdersModel.add(viewModel);
            }
        }
    }

    private void handleAddMedicament() {
        try {
            String nume = numeMedicamentTextField.getText().trim();
            String pretText = pretMedicamentTextField.getText().trim();
            String descriere = descriereMedicamentTextField.getText().trim();

            if (nume.isEmpty()) {
                showAlert("Eroare", "Numele medicamentului este obligatoriu.");
                return;
            }

            if (pretText.isEmpty()) {
                showAlert("Eroare", "Prețul medicamentului este obligatoriu.");
                return;
            }

            float pret;
            try {
                pret = Float.parseFloat(pretText);
                if (pret <= 0) {
                    showAlert("Eroare", "Prețul trebuie să fie un număr pozitiv.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Eroare", "Prețul trebuie să fie un număr valid.");
                return;
            }

            Medicament medicament = new Medicament(null, nume, pret, descriere);
            medicamentService.save(medicament);

            // Clear fields
            numeMedicamentTextField.clear();
            pretMedicamentTextField.clear();
            descriereMedicamentTextField.clear();

            showAlert("Succes", "Medicamentul a fost adăugat cu succes.");
            loadMedications();

        } catch (Exception e) {
            logger.error("Error adding medicament", e);
            showAlert("Eroare", "A apărut o eroare la adăugarea medicamentului: " + e.getMessage());
        }
    }

    private void handleUpdateMedicament() {
        Medicament selectedMedicament = TabelMedicamenteTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert("Atenție", "Selectați un medicament pentru actualizare.");
            return;
        }

        try {
            String nume = numeMedicamentTextField.getText().trim();
            String pretText = pretMedicamentTextField.getText().trim();
            String descriere = descriereMedicamentTextField.getText().trim();

            if (nume.isEmpty()) {
                showAlert("Eroare", "Numele medicamentului este obligatoriu.");
                return;
            }

            if (pretText.isEmpty()) {
                showAlert("Eroare", "Prețul medicamentului este obligatoriu.");
                return;
            }

            float pret;
            try {
                pret = Float.parseFloat(pretText);
                if (pret <= 0) {
                    showAlert("Eroare", "Prețul trebuie să fie un număr pozitiv.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Eroare", "Prețul trebuie să fie un număr valid.");
                return;
            }

            selectedMedicament.setNume(nume);
            selectedMedicament.setPret(pret);
            selectedMedicament.setDescriere(descriere);

            medicamentService.update(selectedMedicament);

            // Clear fields
            numeMedicamentTextField.clear();
            pretMedicamentTextField.clear();
            descriereMedicamentTextField.clear();

            showAlert("Succes", "Medicamentul a fost actualizat cu succes.");
            loadMedications();

        } catch (Exception e) {
            logger.error("Error updating medicament", e);
            showAlert("Eroare", "A apărut o eroare la actualizarea medicamentului: " + e.getMessage());
        }
    }

    private void handleDeleteMedicament() {
        Medicament selectedMedicament = TabelMedicamenteTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert("Atenție", "Selectați un medicament pentru ștergere.");
            return;
        }

        try {
            medicamentService.delete(selectedMedicament.getId());
            showAlert("Succes", "Medicamentul a fost șters cu succes.");
            loadMedications();
        } catch (Exception e) {
            logger.error("Error deleting medicament", e);
            showAlert("Eroare", "A apărut o eroare la ștergerea medicamentului: " + e.getMessage());
        }
    }

    private void handleFulfillOrder() {
        ComandaItemViewModel selectedItem = TabelComenziTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Atenție", "Selectați o comandă pentru onorare.");
            return;
        }

        try {
            Comanda comanda = selectedItem.getComanda();
            comanda.setStatus(Status.ONORATA);
            ApplicationContext.getComandaService().update(comanda);

            showAlert("Succes", "Comanda a fost onorată cu succes.");
        } catch (Exception e) {
            logger.error("Error fulfilling order", e);
            showAlert("Eroare", "A apărut o eroare la onorarea comenzii: " + e.getMessage());
        }
    }

    private void handleRejectOrder() {
        ComandaItemViewModel selectedItem = TabelComenziTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Atenție", "Selectați o comandă pentru respingere.");
            return;
        }

        try {
            Comanda comanda = selectedItem.getComanda();
            comanda.setStatus(Status.REFUZATA);
            ApplicationContext.getComandaService().update(comanda);

            showAlert("Succes", "Comanda a fost respinsă.");
            loadOrders();
        } catch (Exception e) {
            logger.error("Error rejecting order", e);
            showAlert("Eroare", "A apărut o eroare la respingerea comenzii: " + e.getMessage());
        }
    }

    private void handleDeleteFulfilledOrder() {
        ComandaItemViewModel selectedItem = TabelComenziTableView_Onorate.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Atenție", "Selectați o comandă pentru ștergere.");
            return;
        }

        try {
            ApplicationContext.getComandaService().delete(selectedItem.getComandaId());
            showAlert("Succes", "Comanda a fost ștearsă cu succes.");
            loadOrders();
        } catch (Exception e) {
            logger.error("Error deleting order", e);
            showAlert("Eroare", "A apărut o eroare la ștergerea comenzii: " + e.getMessage());
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
            loadOrders();
        }
    }

    // View model class for order items
    public static class ComandaItemViewModel {
        private final Integer comandaId;
        private final String sectie;
        private final String medicamentNume;
        private final Integer cantitate;
        private final Comanda comanda;

        public ComandaItemViewModel(Integer comandaId, String sectie, String medicamentNume, Integer cantitate, Comanda comanda) {
            this.comandaId = comandaId;
            this.sectie = sectie;
            this.medicamentNume = medicamentNume;
            this.cantitate = cantitate;
            this.comanda = comanda;
        }

        public Integer getComandaId() {
            return comandaId;
        }

        public String getSectie() {
            return sectie;
        }

        public String getMedicamentNume() {
            return medicamentNume;
        }

        public Integer getCantitate() {
            return cantitate;
        }

        public Comanda getComanda() {
            return comanda;
        }
    }
}