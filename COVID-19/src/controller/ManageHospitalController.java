package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.HospitalTM;
import validation.FormValidation;

import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

public class ManageHospitalController {
    public ListView<HospitalTM> lstHospitals;
    public TextField txtSearch;
    public ComboBox<String> cmbDistricts;
    public AnchorPane root;
    public TextField txtHospitalId;
    public TextField txtHospitalName;
    public TextField txtHospitalCity;
    public TextField txtDirectorName;
    public TextField txtDirectorContactNumber;
    public TextField txtHospitalContact1;
    public TextField txtHospitalContact2;
    public TextField txtHospitalFaxNumber;
    public TextField txtHospitalEmail;
    public TextField txtCapacity;
    public Button btnSave;
    public Button btnDelete;
    public Button btnAddNew;
    public Label lblErrorHospitalName;
    public Label lblErrorHospitalID;
    public Label lblErrorCity;
    public Label lblErrorCapacity;
    public Label lblErrorDirectorName;
    public Label lblDirectorContact;
    public Label lblErrorHospitalContact1;
    public Label lblErrorHospitalFax;
    public Label lblErrorHospitalEmail;
    public Label lblErrorHospitalContact2;
    public Label lblErrorDistrict;
    private ArrayList<HospitalTM> hospitalDB = new ArrayList<>();

    public  void initialize() {
        //add districts to combo box
        cmbDistricts.getItems().addAll("Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo", "Galle", "Gampaha", "Hambantota", "Jaffna", "Kalutara",
                "Kandy", "Kegalle", "Killinochchi", "Kurunegala", "Mannar", "Matale", "Matara", "Moneragala", "Mullaitivu", "Nuwara Eliya",
                "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee", "Vavuniya");
       //load hospitals to Listview
        addDataToListView();
        //set disable
        btnDelete.setDisable(true);
        btnSave.setDisable(true);
        errorLabelClear();
        //selection of hospital
        lstHospitals.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HospitalTM>() {
            @Override
            public void changed(ObservableValue<? extends HospitalTM> observable, HospitalTM oldValue, HospitalTM newValue) {
                int selectedIndex = lstHospitals.getSelectionModel().getSelectedIndex();
                System.out.println(newValue);
                if (newValue == null){
                    txtHospitalId.clear();
                    txtHospitalName.clear();
                    txtHospitalCity.clear();
                    cmbDistricts.getSelectionModel().clearSelection();
                    lstHospitals.getSelectionModel().clearSelection();
                    txtCapacity.clear();
                    txtDirectorName.clear();
                    txtDirectorContactNumber.clear();
                    txtHospitalContact1.clear();
                    txtHospitalContact2.clear();
                    txtHospitalFaxNumber.clear();
                    txtHospitalEmail.clear();
                    btnSave.setText("Save");
                    btnDelete.setDisable(true);
                    return;
                }
                HospitalTM selectedHospital = lstHospitals.getSelectionModel().getSelectedItem();
                txtHospitalId.setText(selectedHospital.getHospitalId());
                txtHospitalName.setText(selectedHospital.getHospitalName());
                txtHospitalCity.setText(selectedHospital.getCity());
                cmbDistricts.setValue(selectedHospital.getDistrict());
                txtCapacity.setText(selectedHospital.getCapacity());
                txtDirectorName.setText(selectedHospital.getDirectorName());
                txtDirectorContactNumber.setText(selectedHospital.getDirectorContactNumber());
                txtHospitalContact1.setText(selectedHospital.getHospitalContactNumber1());
                txtHospitalContact2.setText(selectedHospital.getHospitalContactNumber2());
                txtHospitalFaxNumber.setText(selectedHospital.getHospitalFaxNumber());
                txtHospitalEmail.setText(selectedHospital.getHospitalEmail());
                btnSave.setDisable(false);
                btnSave.setText("Update");
                btnDelete.setDisable(false);
            }
        });
        //search hospitals
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<HospitalTM> searchHospitals = lstHospitals.getItems();
                String lowerCaseFilter = newValue.toLowerCase();
                searchHospitals.clear();
                for (HospitalTM hospital: hospitalDB) {
                    if (hospital.getHospitalName().toLowerCase().contains(lowerCaseFilter)){
                        searchHospitals.add(hospital);
                    }
                }
            }
        });
    }


    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
        Scene dashboard = new Scene(FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(dashboard);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }

    public void btnNewHospital_OnAction(ActionEvent actionEvent) {
        errorLabelClear();
        lstHospitals.getSelectionModel().clearSelection();
        //create new ID
        ObservableList<HospitalTM> selectedHospital = lstHospitals.getItems();
        if (selectedHospital.size() == 0){
            txtHospitalId.setText("H001");
        }else {
            HospitalTM lastHospital = selectedHospital.get(selectedHospital.size() - 1);
            String lastHospitalId = lastHospital.getHospitalId();
            int value = Integer.parseInt(lastHospitalId.substring(1, 4));
            int last = value + 1;
            if (last > 0 && last <= 9) {
                txtHospitalId.setText("H00" + last);
            } else if (last >= 10 && last <= 99) {
                txtHospitalId.setText("H0" + last);
            } else {
                txtHospitalId.setText("H" + last);
            }
        }
        txtHospitalName.clear();
        txtHospitalCity.clear();
        cmbDistricts.getSelectionModel().clearSelection();
        txtCapacity.clear();
        txtDirectorName.clear();
        txtDirectorContactNumber.clear();
        txtHospitalContact1.clear();
        txtHospitalContact2.clear();
        txtHospitalFaxNumber.clear();
        txtHospitalEmail.clear();
        txtHospitalName.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");

    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        //validation
/*        if(txtDirectorContactNumber.getText().trim().isEmpty() || txtHospitalContact1.getText().trim().isEmpty() ||
                txtHospitalContact2.getText().trim().isEmpty() || txtHospitalFaxNumber.getText().trim().isEmpty() || !txtDirectorContactNumber.getText().matches("\\d{3}-\\d{7}")
                || !txtHospitalContact1.getText().matches("\\d{3}-\\d{7}") || !txtHospitalContact2.getText().matches("\\d{3}-\\d{7}")
                || !txtHospitalFaxNumber.getText().matches("\\d{3}-\\d{7}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Contact No.Please input a valid phone number", ButtonType.OK).showAndWait();
            return;
        }
        if(txtHospitalEmail.getText().trim().isEmpty() || !txtHospitalEmail.getText().matches
                ("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
            new Alert(Alert.AlertType.ERROR, "Invalid Email.Please Enter Valid Email Address", ButtonType.OK).showAndWait();
            return;
        }*/
        Boolean hospitalIdValidation = FormValidation.validId(txtHospitalId,lblErrorHospitalID,"* Invalid Id or Press new hospital button ");
        Boolean hospitalNameValidation = FormValidation.validText(txtHospitalName,lblErrorHospitalName,"* Invalid text");
        Boolean cityValidation = FormValidation.validText(txtHospitalCity,lblErrorCity,"* Invalid text");
        Boolean directorNameValidation = FormValidation.validText(txtDirectorName,lblErrorDirectorName,"* Invalid text");

        Boolean hospitalCapacityValidation = FormValidation.validNumber(txtCapacity,lblErrorCapacity,"* Invalid number");
        Boolean hospitalDistrictValidation = FormValidation.comBoxValidation(cmbDistricts,lblErrorDistrict,"* Select a District");

        Boolean hospitalContact1Validation = FormValidation.validContactNumber(txtHospitalContact1,lblErrorHospitalContact1,"* Invalid contact number xxx-xxxxxxx");
        Boolean hospitalContact2Validation = FormValidation.validContactNumber(txtHospitalContact2,lblErrorHospitalContact2,"* Invalid contact number xxx-xxxxxxx");
        Boolean directorContactValidation = FormValidation.validContactNumber(txtDirectorContactNumber,lblDirectorContact,"* Invalid contact number xxx-xxxxxxx");
        Boolean hospitalfaxValidation = FormValidation.validContactNumber(txtHospitalFaxNumber,lblErrorHospitalFax,"* Invalid fax number xxx-xxxxxxx");
        Boolean hospitalEmailValidation = FormValidation.validEmail(txtHospitalEmail,lblErrorHospitalEmail,"* Invalid Email address abcd@example.com");
        if (
                hospitalIdValidation && hospitalNameValidation && cityValidation && directorNameValidation &&
                        hospitalCapacityValidation && hospitalDistrictValidation &&
                        hospitalContact2Validation && hospitalContact1Validation && directorContactValidation && hospitalfaxValidation &&
                        hospitalEmailValidation
        ) {
            if(btnSave.getText().equals("Save")) {

                addDataToDB();
//clear data in txt fields
                textClear();
            } else {
                updateDbData();
            }
            addDataToListView();
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        errorLabelClear();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the selected Hospital ?", ButtonType.NO, ButtonType.YES);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            deleteDataFromDB();
            addDataToListView();
        }
        btnSave.setText("Save");
        btnSave.setDisable(true);
        btnAddNew.requestFocus();

    }
    private void deleteDataFromDB(){
        HospitalTM selectedHospital = lstHospitals.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement
                    ("DELETE FROM hospital WHERE hospital_id=?");
            preparedStatement.setObject(1,selectedHospital.getHospitalId());
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully",ButtonType.OK).showAndWait();
                lstHospitals.refresh();
                addDataToListView();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addDataToDB(){
        String hospitalId = txtHospitalId.getText();
        String hospitalName = txtHospitalName.getText();
        String city = txtHospitalCity.getText();
        Object district = cmbDistricts.getValue();
        String capacity = txtCapacity.getText();
        String directorName = txtDirectorName.getText();
        String directorContactNumber = txtDirectorContactNumber.getText();
        String hospitalContact1 = txtHospitalContact1.getText();
        String hospitalContact2 = txtHospitalContact2.getText();
        String hospitalFaxNumber = txtHospitalFaxNumber.getText();
        String hospitalEmail = txtHospitalEmail.getText();
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT into" +
                    " hospital(hospital_id,hospital_name,city,district,capacity,director_name, director_contact_number," +
                    "hospital_contactNo1,hospital_contactNo2, hospital_fax_number, hospital_email) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            pstm.setObject(1,hospitalId);
            pstm.setObject(2,hospitalName);
            pstm.setObject(3,city);
            pstm.setObject(4,district);
            pstm.setObject(5,capacity);
            pstm.setObject(6,directorName);
            pstm.setObject(7,directorContactNumber);
            pstm.setObject(8,hospitalContact1);
            pstm.setObject(9,hospitalContact2);
            pstm.setObject(10,hospitalFaxNumber);
            pstm.setObject(11,hospitalEmail);
            int affectedRow = pstm.executeUpdate();
            if (affectedRow>0){
                new Alert(Alert.AlertType.INFORMATION, "Successfully Added", ButtonType.OK).showAndWait();
                addDataToListView();
            }else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong...please try again", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    private void updateDbData() {
        HospitalTM selectedHospital = lstHospitals.getSelectionModel().getSelectedItem();
        String hospitalId = txtHospitalId.getText();
        String hospitalName = txtHospitalName.getText();
        String city = txtHospitalCity.getText();
        Object district = cmbDistricts.getValue();
        String capacity = txtCapacity.getText();
        String directorName = txtDirectorName.getText();
        String directorContactNumber = txtDirectorContactNumber.getText();
        String hospitalContact1 = txtHospitalContact1.getText();
        String hospitalContact2 = txtHospitalContact2.getText();
        String hospitalFaxNumber = txtHospitalFaxNumber.getText();
        String hospitalEmail = txtHospitalEmail.getText();
        String sql = "UPDATE hospital SET hospital_id=?,hospital_name=?,city=?,district=?," +
                "capacity=?,director_name=?,director_contact_number=?,hospital_contactNo1=?," +
                "hospital_contactNo2=?,hospital_fax_number=?,hospital_email=? WHERE hospital_id=?";

        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setObject(1, hospitalId);
            pstm.setObject(2, hospitalName);
            pstm.setObject(3, city);
            pstm.setObject(4, district);
            pstm.setObject(5, capacity);
            pstm.setObject(6, directorName);
            pstm.setObject(7, directorContactNumber);
            pstm.setObject(8, hospitalContact1);
            pstm.setObject(9, hospitalContact2);
            pstm.setObject(10, hospitalFaxNumber);
            pstm.setObject(11, hospitalEmail);
            pstm.setObject(12, selectedHospital.getHospitalId());
            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully", ButtonType.OK).showAndWait();
                lstHospitals.refresh();
                lstHospitals.getSelectionModel().clearSelection();
                btnSave.setText("Save");
                btnSave.setDisable(true);
                btnAddNew.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong...please try again", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    private void textClear(){
        txtHospitalId.clear();
        txtHospitalName.clear();
        txtHospitalCity.clear();
        txtCapacity.clear();
        txtDirectorName.clear();
        txtDirectorContactNumber.clear();
        txtHospitalContact1.clear();
        txtHospitalContact2.clear();
        txtHospitalFaxNumber.clear();
        txtHospitalEmail.clear();
        cmbDistricts.getSelectionModel().clearSelection();
    }
    private void addDataToListView() {
        try {
            ObservableList<HospitalTM> hospitals = lstHospitals.getItems();
            hospitals.clear();
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM Hospital");
            ResultSet rst = preparedStatement.executeQuery();
            while (rst.next()){
                String hospitalId = rst.getString(1);
                String hospitalName = rst.getString(2);
                String city = rst.getString(3);
                String district = rst.getString(4);
                String capacity = rst.getString(5);
                String directorName = rst.getString(6);
                String directorContactNo = rst.getString(7);
                String hospitalContact1 = rst.getString(8);
                String hospitalContact2 = rst.getString(9);
                String hospitalFax = rst.getString(10);
                String hospitalEmail = rst.getString(11);

                HospitalTM hospitalDetail = new HospitalTM(hospitalId,hospitalName,city,district,capacity,directorName,
                        directorContactNo,hospitalContact1,hospitalContact2,hospitalFax,hospitalEmail);
//                ObservableList<HospitalTM> hospital = FXCollections.observableList(hospitalDB);
                lstHospitals.getItems().add(hospitalDetail);
//                lstHospitals.setItems(hospital);
                hospitalDB.add(hospitalDetail);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void txtSearch_OnAction(KeyEvent keyEvent) {
    }
    public void lstHospital_OnAction(MouseEvent mouseEvent) {
    }

    private void errorLabelClear(){
        lblErrorHospitalID.setText("");
        lblErrorHospitalName.setText("");
        lblErrorCity.setText("");
        lblErrorDistrict.setText("");
        lblErrorCapacity.setText("");
        lblErrorDirectorName.setText("");
        lblDirectorContact.setText("");
        lblErrorHospitalContact1.setText("");
        lblErrorHospitalContact2.setText("");
        lblErrorHospitalFax.setText("");
        lblErrorHospitalEmail.setText("");

    }
}
