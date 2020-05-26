package controller;

import com.sun.org.omg.CORBA.Initializer;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.HospitalTM;
import util.QuarantineCenterTM;
import validation.FormValidation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ManageQuarantineCenterController {
    public ComboBox<String> cmbDistricts;
    public ListView<QuarantineCenterTM> lstCenter;
    public TextField txtSearchCenter;
    public AnchorPane root;
    public TextField txtQuarantineCenterId;
    public TextField txtQuarantineCenterName;
    public TextField txtQuarantineCenterCity;
    public TextField txtQuarantineCenterHead;
    public TextField txtQuarantineCenterHeadContact;
    public TextField txtQuarantineCenterContact1;
    public TextField txtQuarantineCenterContact2;
    public TextField txtQuarantineCenterCapacity;
    public TextField txtQuarantineCenterEmail;
    public Button btnSave;
    public Button btnDelete;
    public Button btnAddNew;
    public Label lblErrorCenterContact2;
    public Label lblErrorCenterEmail;
    public Label lblErrorCenterContact1;
    public Label lblErrorCenterHeadContact;
    public Label lblErrorCenterHeadName;
    public Label lblErrorCenterCapacity;
    public Label lblErrorCenterCity;
    public Label lblErrorCenterName;
    public Label lblErrorCenterDistrict;
    public Label lblErrorCenterId;
    private ArrayList<QuarantineCenterTM> quarantineCentersDB = new ArrayList<>();


    public  void initialize(){
        cmbDistricts.getItems().addAll("Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo", "Galle", "Gampaha", "Hambantota", "Jaffna", "Kalutara",
                "Kandy", "Kegalle", "Killinochchi", "Kurunegala", "Mannar", "Matale", "Matara", "Moneragala", "Mullaitivu", "Nuwara Eliya",
                "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee", "Vavuniya");

        //load hospitals to Listview
        addcentersToListView();
        //set disable
        btnDelete.setDisable(true);
        btnSave.setDisable(true);
        //selection of hospital
        lstCenter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QuarantineCenterTM>() {
            @Override
            public void changed(ObservableValue<? extends QuarantineCenterTM> observable, QuarantineCenterTM oldValue, QuarantineCenterTM newValue) {
                errorLabelInvisible();
                int selectedIndex = lstCenter.getSelectionModel().getSelectedIndex();
                if (selectedIndex == -1){
                    txtQuarantineCenterId.clear();
                    txtQuarantineCenterName.clear();
                    txtQuarantineCenterCity.clear();
                    cmbDistricts.getSelectionModel().clearSelection();
                    lstCenter.getSelectionModel().clearSelection();
                    txtQuarantineCenterCapacity.clear();
                    txtQuarantineCenterHead.clear();
                    txtQuarantineCenterHeadContact.clear();
                    txtQuarantineCenterContact1.clear();
                    txtQuarantineCenterContact2.clear();
                    txtQuarantineCenterEmail.clear();
                    btnSave.setText("Save");
                    btnDelete.setDisable(true);
                    return;
                }
                QuarantineCenterTM selectedCenter = lstCenter.getSelectionModel().getSelectedItem();
                txtQuarantineCenterId.setText(selectedCenter.getQuarantineCenterId());
                txtQuarantineCenterName.setText(selectedCenter.getQuarantineCenterName());
                txtQuarantineCenterCity.setText(selectedCenter.getCity());
                cmbDistricts.setValue(selectedCenter.getDistrict());
                txtQuarantineCenterCapacity.setText(selectedCenter.getCapacity());
                txtQuarantineCenterHead.setText(selectedCenter.getHeadName());
                txtQuarantineCenterHeadContact.setText(selectedCenter.getHeadContactNumber());
                txtQuarantineCenterContact1.setText(selectedCenter.getQuarantineCenterContactNumber1());
                txtQuarantineCenterContact2.setText(selectedCenter.getQuarantineCenterContactNumber2());
                txtQuarantineCenterEmail.setText(selectedCenter.getQuarantineCenterEmail());
                btnSave.setDisable(false);
                btnSave.setText("Update");
                btnDelete.setDisable(false);
            }
        });
        //search hospitals
        txtSearchCenter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                errorLabelInvisible();
                ObservableList<QuarantineCenterTM> searchCenter = lstCenter.getItems();
                String lowerCaseFilter = newValue.toLowerCase();
                lstCenter.getItems().clear();
                for (QuarantineCenterTM center: quarantineCentersDB) {
                    if (center.getQuarantineCenterName().toLowerCase().contains(lowerCaseFilter)){
                        searchCenter.add(center);
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

    public void btnNewCenter_OnAction(ActionEvent actionEvent) {
        errorLabelInvisible();
        lstCenter.getSelectionModel().clearSelection();
        //create new ID
        ObservableList<QuarantineCenterTM> selectedCenter = lstCenter.getItems();
        if (selectedCenter.size() == 0){
            txtQuarantineCenterId.setText("C001");
        }else {
            QuarantineCenterTM lastCenter = selectedCenter.get(selectedCenter.size() - 1);
            String lastCenterId = lastCenter.getQuarantineCenterId();
            int value = Integer.parseInt(lastCenterId.substring(1, 4));
            int last = value + 1;
            if (last > 0 && last <= 9) {
                txtQuarantineCenterId.setText("C00" + last);
            } else if (last >= 10 && last <= 99) {
                txtQuarantineCenterId.setText("C0" + last);
            } else {
                txtQuarantineCenterId.setText("C" + last);
            }
        }

        txtQuarantineCenterName.requestFocus();
        txtQuarantineCenterCity.clear();
        cmbDistricts.getSelectionModel().clearSelection();
        txtQuarantineCenterCapacity.clear();
        txtQuarantineCenterHead.clear();
        txtQuarantineCenterHeadContact.clear();
        txtQuarantineCenterContact1.clear();
        txtQuarantineCenterContact2.clear();
        txtQuarantineCenterEmail.clear();
        btnSave.setDisable(false);
        btnSave.setText("Save");
    }

    public void btnSaveQC_OnAction(ActionEvent actionEvent) {
        Boolean centerIdValidation = FormValidation.validId(txtQuarantineCenterId,lblErrorCenterId,"* empty Id, Press new hospital button ");
        Boolean centerNameValidation = FormValidation.validText(txtQuarantineCenterName,lblErrorCenterName,"* Invalid text");
        Boolean cityValidation = FormValidation.validText(txtQuarantineCenterCity,lblErrorCenterCity,"* Invalid text");
        Boolean centerHeadNameValidation = FormValidation.validText(txtQuarantineCenterHead,lblErrorCenterHeadName,"* Invalid text");

        Boolean centerCapacityValidation = FormValidation.validNumber(txtQuarantineCenterCapacity,lblErrorCenterCapacity,"* Invalid number");
        Boolean centerDistrictValidation = FormValidation.comBoxValidation(cmbDistricts,lblErrorCenterDistrict,"* Select a District");

        Boolean centerContact1Validation = FormValidation.validContactNumber(txtQuarantineCenterContact1,lblErrorCenterContact1,"* Invalid contact number xxx-xxxxxxx");
        Boolean centerContact2Validation = FormValidation.validContactNumber(txtQuarantineCenterContact2,lblErrorCenterContact2,"* Invalid contact number xxx-xxxxxxx");
        Boolean centerHeadContactValidation = FormValidation.validContactNumber(txtQuarantineCenterHeadContact,lblErrorCenterHeadContact,"* Invalid contact number xxx-xxxxxxx");
        Boolean centerEmailValidation = FormValidation.validEmail(txtQuarantineCenterEmail,lblErrorCenterEmail,"* Invalid Email address abcd@example.com");
        if (
                centerIdValidation && centerNameValidation && cityValidation && centerHeadNameValidation &&
                        centerCapacityValidation && centerDistrictValidation &&
                        centerContact2Validation && centerContact1Validation && centerHeadContactValidation &&
                        centerEmailValidation
        ) {
            if (btnSave.getText().equals("Save")) {
                addcentersToDB();
//clear data in txt fields
                btnAddNew.requestFocus();
                txtQuarantineCenterName.clear();
                txtQuarantineCenterId.clear();
                txtQuarantineCenterCity.clear();
                cmbDistricts.getSelectionModel().clearSelection();
                txtQuarantineCenterCapacity.clear();
                txtQuarantineCenterHead.clear();
                txtQuarantineCenterHeadContact.clear();
                txtQuarantineCenterContact1.clear();
                txtQuarantineCenterContact2.clear();
                txtQuarantineCenterEmail.clear();
                btnSave.setDisable(true);
                btnDelete.setDisable(true);
            } else {
                updateDbData();
            }
            addcentersToListView();

        }
    }

    public void btnDeleteQC_OnAction(ActionEvent actionEvent) {
        errorLabelInvisible();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the selected Center ?", ButtonType.NO, ButtonType.YES);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            deleteCenterFromDB();
            addcentersToListView();
        }
        btnSave.setText("Save");
        btnSave.setDisable(true);
        btnAddNew.requestFocus();
    }

    private void addcentersToDB(){
        String QuarantineCenterId = txtQuarantineCenterId.getText();
        String QuarantineCenterName = txtQuarantineCenterName.getText();
        String city = txtQuarantineCenterCity.getText();
        Object district = cmbDistricts.getValue();
        String capacity = txtQuarantineCenterCapacity.getText();
        String headName = txtQuarantineCenterHead.getText();
        String headContactNumber = txtQuarantineCenterHeadContact.getText();
        String QuarantineCenterContact1 = txtQuarantineCenterContact1.getText();
        String QuarantineCenterContact2 = txtQuarantineCenterContact2.getText();
        String QuarantineCenterEmail = txtQuarantineCenterEmail.getText();
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT into" +
                    " quarantine_center(quarantine_center_id,quarantine_center_name,city,district,head_name, head_contact_number," +
                    "quarantine_center_contactNo1,quarantine_center_contactNo2,capacity, quarantine_center_email) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)");
            pstm.setObject(1,QuarantineCenterId);
            pstm.setObject(2,QuarantineCenterName);
            pstm.setObject(3,city);
            pstm.setObject(4,district);
            pstm.setObject(5,headName);
            pstm.setObject(6,headContactNumber);
            pstm.setObject(7,QuarantineCenterContact1);
            pstm.setObject(8,QuarantineCenterContact2);
            pstm.setObject(9,capacity);
            pstm.setObject(10,QuarantineCenterEmail);
            int affectedRow = pstm.executeUpdate();
            if (affectedRow>0){
                new Alert(Alert.AlertType.INFORMATION, "Successfully Added", ButtonType.OK).showAndWait();
                addcentersToListView();
            }else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong...please try again", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    private void addcentersToListView() {
        try {
            ObservableList<QuarantineCenterTM> quarantineCenters = lstCenter.getItems();
            quarantineCenters.clear();
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM quarantine_center");
            ResultSet rst = preparedStatement.executeQuery();
            while (rst.next()){
                String quarantineCenterId = rst.getString(1);
                String quarantineCenterName = rst.getString(2);
                String city = rst.getString(3);
                String district = rst.getString(4);
                String headName = rst.getString(5);
                String headContactNumber = rst.getString(6);
                String quarantineCenterContactNo1 = rst.getString(7);
                String quarantineCenterContactNo2 = rst.getString(8);
                String capacity = rst.getString(9);
                String quarantineCenterEmail = rst.getString(10);

                QuarantineCenterTM quarantineCenter = new QuarantineCenterTM(quarantineCenterId,quarantineCenterName,city,district,headName,
                        headContactNumber,quarantineCenterContactNo1,quarantineCenterContactNo2,capacity,quarantineCenterEmail);
                lstCenter.getItems().add(quarantineCenter);
                quarantineCentersDB.add(quarantineCenter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteCenterFromDB(){
        QuarantineCenterTM selectedCenter = lstCenter.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement
                    ("DELETE FROM quarantine_center WHERE quarantine_center_id=?");
            preparedStatement.setObject(1,selectedCenter.getQuarantineCenterId());
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully",ButtonType.OK).showAndWait();
                lstCenter.refresh();
                addcentersToListView();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateDbData() {
        QuarantineCenterTM selectedCenter = lstCenter.getSelectionModel().getSelectedItem();
        String QuarantineCenterId = txtQuarantineCenterId.getText();
        String QuarantineCenterName = txtQuarantineCenterName.getText();
        String city = txtQuarantineCenterCity.getText();
        Object district = cmbDistricts.getValue();
        String headName = txtQuarantineCenterHead.getText();
        String headContactNumber = txtQuarantineCenterHeadContact.getText();
        String quarantineCenterContact1 = txtQuarantineCenterContact1.getText();
        String quarantineCenterContact2 = txtQuarantineCenterContact2.getText();
        String capacity = txtQuarantineCenterCapacity.getText();
        String quarantineCenterEmail = txtQuarantineCenterEmail.getText();
        String sql = "UPDATE quarantine_center SET quarantine_center_id=?,quarantine_center_name=?,city=?,district=?," +
                "head_name=?,head_contact_number=?,quarantine_center_contactNo1=?," +
                "quarantine_center_contactNo2=?,capacity=?,quarantine_center_email=? WHERE quarantine_center_id=?";

        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setObject(1, QuarantineCenterId);
            pstm.setObject(2, QuarantineCenterName);
            pstm.setObject(3, city);
            pstm.setObject(4, district);
            pstm.setObject(5, headName);
            pstm.setObject(6, headContactNumber);
            pstm.setObject(7, quarantineCenterContact1);
            pstm.setObject(8, quarantineCenterContact2);
            pstm.setObject(9, capacity);
            pstm.setObject(10, quarantineCenterEmail);
            pstm.setObject(11, selectedCenter.getQuarantineCenterId());
            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully", ButtonType.OK).showAndWait();
                lstCenter.refresh();
                lstCenter.getSelectionModel().clearSelection();
                btnSave.setText("Save");
                btnSave.setDisable(true);
                btnDelete.setDisable(true);
                btnAddNew.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong...please try again", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    private void errorLabelInvisible(){
        lblErrorCenterId.setText("");
        lblErrorCenterName.setText("");
        lblErrorCenterCity.setText("");
        lblErrorCenterDistrict.setText("");
        lblErrorCenterCapacity.setText("");
        lblErrorCenterHeadName.setText("");
        lblErrorCenterContact1.setText("");
        lblErrorCenterContact2.setText("");
        lblErrorCenterHeadContact.setText("");
        lblErrorCenterEmail.setText("");

    }
}
