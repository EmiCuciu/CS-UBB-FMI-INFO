package com.example.laboratoriss.GUI;

import com.example.laboratoriss.Domain.*;
import com.example.laboratoriss.Service.IComandaService;
import com.example.laboratoriss.Service.IMedicamentService;
import com.example.laboratoriss.Utils.Observer.ObservableEvent;
import com.example.laboratoriss.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmacieController implements Observer<Comanda> {
    // Comenzi tab
    @FXML
    private TableView<ComandaTableItem> TabelComenziTableView;
    @FXML
    private TableColumn<ComandaTableItem, String> SectieTableColumn;
    @FXML
    private TableColumn<ComandaTableItem, String> MedicamentTableColumn;
    @FXML
    private TableColumn<ComandaTableItem, Integer> CantitateTableColumn;
    @FXML
    private TableColumn<ComandaTableItem, Integer> ComandaIDTableColumn;

    @FXML
    private TableView<ComandaTableItem> TabelComenziTableView_Onorate;
    @FXML
    private TableColumn<ComandaTableItem, String> SectieTableColumn_Onorate;
    @FXML
    private TableColumn<ComandaTableItem, String> MedicamentTableColumn_Onorate;
    @FXML
    private TableColumn<ComandaTableItem, Integer> CantitateTableColumn_Onorate;
    @FXML
    private TableColumn<ComandaTableItem, Integer> ComandaIDTableColumn_Onorate;

    @FXML
    private Button OnoreazaComandaButton;
    @FXML
    private Button RefuzaComandaButton;
    @FXML
    private Button StergeComandaButton;

    // Depozit tab
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

    private IMedicamentService medicamentService;
    private IComandaService comandaService;

    private final ObservableList<ComandaTableItem> comenziModel = FXCollections.observableArrayList();
    private final ObservableList<ComandaTableItem> comenziOnorateModel = FXCollections.observableArrayList();
    private final ObservableList<Medicament> medicamenteModel = FXCollections.observableArrayList();

    // Map to keep track of which ComandaTableItem belongs to which Comanda
    private final Map<ComandaTableItem, Comanda> comandaItemMap = new HashMap<>();

    public void setServices(IMedicamentService medicamentService, User currentUser) {
        this.medicamentService = medicamentService;

        // Inject ComandaService from the application context
        // This would typically be done in a more formal way with dependency injection
        this.comandaService = ApplicationContext.getComandaService();

        // Register as observer
        comandaService.addObserver(this);
        medicamentService.addObserver(new MedicamentObserver());

        loadData();
    }

    @FXML
    public void initialize() {
        // Configure cell factories
        ComandaIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("comandaId"));
        SectieTableColumn.setCellValueFactory(new PropertyValueFactory<>("sectie"));
        MedicamentTableColumn.setCellValueFactory(new PropertyValueFactory<>("medicamentNume"));
        CantitateTableColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));

        ComandaIDTableColumn_Onorate.setCellValueFactory(new PropertyValueFactory<>("comandaId"));
        SectieTableColumn_Onorate.setCellValueFactory(new PropertyValueFactory<>("sectie"));
        MedicamentTableColumn_Onorate.setCellValueFactory(new PropertyValueFactory<>("medicamentNume"));
        CantitateTableColumn_Onorate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));

        MedicamenteInDepozitTableColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        PretInDepozitTableColumn.setCellValueFactory(new PropertyValueFactory<>("pret"));
        DescriereInDepozitTableColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));

        // Set up button handlers
        OnoreazaComandaButton.setOnAction(event -> onoreazaComanda());
        RefuzaComandaButton.setOnAction(event -> refuzaComanda());
        StergeComandaButton.setOnAction(event -> stergeComanda());

        AdaugaMedicamentButton.setOnAction(event -> adaugaMedicament());
        StergeMedicamentDepozitButton.setOnAction(event -> stergeMedicament());
        UpdateMedicamtDepozitButton.setOnAction(event -> updateMedicament());

        // Set model data to tables
        TabelComenziTableView.setItems(comenziModel);
        TabelComenziTableView_Onorate.setItems(comenziOnorateModel);
        TabelMedicamenteTableView.setItems(medicamenteModel);
    }

    private void loadData() {
        // Load medications
        loadMedicamente();

        // Load orders
        loadComenzi();
    }

    private void loadMedicamente() {
        medicamenteModel.clear();
        medicamentService.getAll().forEach(medicamenteModel::add);
    }

    private void loadComenzi() {
        comenziModel.clear();
        comenziOnorateModel.clear();
        comandaItemMap.clear();

        // Get orders with status IN_ASTEPTARE
        List<Comanda> comenziInAsteptare = comandaService.getByStatus(Status.IN_ASTEPTARE);
        processComenzi(comenziInAsteptare, comenziModel);

        // Get orders with status ONORATA
        List<Comanda> comenziOnorate = comandaService.getByStatus(Status.ONORATA);
        processComenzi(comenziOnorate, comenziOnorateModel);
    }

    private void processComenzi(List<Comanda> comenzi, ObservableList<ComandaTableItem> model) {
        for (Comanda comanda : comenzi) {
            String sectie = comanda.getUser().getSectie();
            Integer comandaID = comanda.getId();

            for (ComandaItem item : comanda.getComandaItems()) {
                ComandaTableItem tableItem = new ComandaTableItem(comandaID, sectie, item.getMedicament().getNume(), item.getCantitate(), comanda.getData().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                model.add(tableItem);
                comandaItemMap.put(tableItem, comanda);
            }
        }
    }

    private void onoreazaComanda() {
        ComandaTableItem selectedItem = TabelComenziTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Eroare", "Selectați o comandă pentru a o onora.");
            return;
        }

        Comanda comanda = comandaItemMap.get(selectedItem);
        if (comanda != null) {
            comandaService.updateStatus(comanda.getId(), Status.ONORATA);
            showAlert("Succes", "Comanda a fost onorată cu succes.");
        }
    }

    private void refuzaComanda() {
        ComandaTableItem selectedItem = TabelComenziTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Eroare", "Selectați o comandă pentru a o refuza.");
            return;
        }

        Comanda comanda = comandaItemMap.get(selectedItem);
        if (comanda != null) {
            comandaService.updateStatus(comanda.getId(), Status.REFUZATA);
            showAlert("Succes", "Comanda a fost refuzată.");
        }
    }

    private void stergeComanda() {
        ComandaTableItem selectedItem = TabelComenziTableView_Onorate.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Eroare", "Selectați o comandă pentru a o șterge.");
            return;
        }

        Comanda comanda = comandaItemMap.get(selectedItem);
        if (comanda != null) {
            comandaService.delete(comanda.getId());
            showAlert("Succes", "Comanda a fost ștearsă.");
        }
    }

    private void adaugaMedicament() {
        String nume = numeMedicamentTextField.getText().trim();
        String pretText = pretMedicamentTextField.getText().trim();
        String descriere = descriereMedicamentTextField.getText().trim();

        if (nume.isEmpty() || pretText.isEmpty()) {
            showAlert("Eroare", "Numele și prețul sunt obligatorii.");
            return;
        }

        try {
            float pret = Float.parseFloat(pretText);
            Medicament medicament = new Medicament(null, nume, pret, descriere);
            medicamentService.save(medicament);

            clearMedicamentFields();
            showAlert("Succes", "Medicamentul a fost adăugat.");
        } catch (NumberFormatException e) {
            showAlert("Eroare", "Prețul trebuie să fie un număr valid.");
        }
    }

    private void stergeMedicament() {
        Medicament selectedMedicament = TabelMedicamenteTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert("Eroare", "Selectați un medicament pentru a-l șterge.");
            return;
        }

        medicamentService.delete(selectedMedicament.getId());
        showAlert("Succes", "Medicamentul a fost șters.");
    }

    private void updateMedicament() {
        Medicament selectedMedicament = TabelMedicamenteTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert("Eroare", "Selectați un medicament pentru a-l actualiza.");
            return;
        }

        String nume = numeMedicamentTextField.getText().trim();
        String pretText = pretMedicamentTextField.getText().trim();
        String descriere = descriereMedicamentTextField.getText().trim();

        if (nume.isEmpty() || pretText.isEmpty()) {
            showAlert("Eroare", "Numele și prețul sunt obligatorii.");
            return;
        }

        try {
            float pret = Float.parseFloat(pretText);
            selectedMedicament.setNume(nume);
            selectedMedicament.setPret(pret);
            selectedMedicament.setDescriere(descriere);

            medicamentService.update(selectedMedicament);
            clearMedicamentFields();
            showAlert("Succes", "Medicamentul a fost actualizat.");
        } catch (NumberFormatException e) {
            showAlert("Eroare", "Prețul trebuie să fie un număr valid.");
        }
    }

    private void clearMedicamentFields() {
        numeMedicamentTextField.clear();
        pretMedicamentTextField.clear();
        descriereMedicamentTextField.clear();
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
        // Reload orders when any change occurs
        loadComenzi();
    }

    private class MedicamentObserver implements Observer<Medicament> {
        @Override
        public void update(ObservableEvent<Medicament> event) {
            loadMedicamente();
        }
    }

    // Helper class to represent table items
    public static class ComandaTableItem {
        private final Integer comandaId;
        private final String sectie;
        private final String medicamentNume;
        private final int cantitate;
        private final String data;

        public ComandaTableItem(Integer id, String sectie, String medicamentNume, int cantitate, String data) {
            this.comandaId = id;
            this.sectie = sectie;
            this.medicamentNume = medicamentNume;
            this.cantitate = cantitate;
            this.data = data;
        }

        public int getComandaId(){
            return comandaId;
        }

        public String getSectie() {
            return sectie;
        }

        public String getMedicamentNume() {
            return medicamentNume;
        }

        public int getCantitate() {
            return cantitate;
        }

        public String getData() {
            return data;
        }
    }

    /**
     * Clasa ajuta la injectarea serviciului de comenzi in controllerul de farmacie.
     */
    public static class ApplicationContext {
        private static IComandaService comandaService;

        public static void setComandaService(IComandaService service) {
            comandaService = service;
        }

        public static IComandaService getComandaService() {
            return comandaService;
        }
    }
}