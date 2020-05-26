package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.HospitalTM;
import util.UserTM;
import validation.FormValidation;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


public class ManageUserController {
    public TextField txtPasswordShow;
    public PasswordField txtPasswordHide;
    public ComboBox<String> cmbUserRole;
    public ComboBox<String> cmbUserLocation;
    public TableView<UserTM> tblUsers;
    public Pane pne1;
    public AnchorPane root;
    public Button btnNewUser;
    public Button btnHome;
    public TextField txtName;
    public TextField txtContactNumber;
    public TextField txtEmail;
    public TextField txtUserName;
    public TextField txtSearchUser;
    public Button btnSave;
    public TextField txtUserId;
    public ImageView imghidePassword;
    public ImageView imgShowPassword;
    public Button btnDelete;
    public Label lblErrorUserId;
    public Label lblErrorName;
    public Label lblErrorUserContactNumber;
    public Label lblErrorUserEmail;
    public Label lblErrorUserName;
    public Label lblErrorPassword;
    public Label lblErrorUserRole;
    public Label lblErrorUserLocation;
    private ArrayList<UserTM> userDB = new ArrayList<>();

    public void initialize(){
        //data mapping to the user table
        tblUsers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("username"));
        tblUsers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblUsers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("userRole"));
        //load data to user table
        addDataToUserTable();
        //add user roles to combo box
        cmbUserRole.getItems().addAll("Admin","P.S.T.F Member","Hospital IT","Quarantine Center IT");
        cmbUserLocation.setVisible(false);
        txtUserId.setDisable(true);
        txtName.setDisable(true);
        txtUserName.setDisable(true);
        txtPasswordHide.setDisable(true);
        txtPasswordShow.setDisable(true);
        cmbUserRole.setDisable(true);
        cmbUserLocation.setDisable(true);
        txtContactNumber.setDisable(true);
        txtEmail.setDisable(true);
        btnSave.setDisable(true);

        //select user role from combo box
        cmbUserRole.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null){
                    lblErrorUserLocation.setText("");
                    return;
                }
                //when select role - hospital or QCenter
                if (newValue.contains("Hospital IT")||newValue.contains("Quarantine Center IT")){
                    cmbUserLocation.setVisible(true);
                    if (newValue.contains("Hospital IT")) {
                        cmbUserLocation.setPromptText("Select Hospital");
                        cmbUserLocation.getSelectionModel().clearSelection();
                        cmbUserLocation.getSelectionModel().select(null);
                        cmbUserLocation.getItems().clear();
                        loadAllHospital();
                    }else if (newValue.contains("Quarantine Center IT")) {
                        cmbUserLocation.setPromptText("Select Quarantine Center");
                        cmbUserLocation.getSelectionModel().clearSelection();
                        cmbUserLocation.getSelectionModel().clearSelection();
                        cmbUserLocation.getSelectionModel().select(null);
                        cmbUserLocation.getItems().clear();
                        loadAllQuarantineCenters();
                    }
                }else{
                        cmbUserLocation.setVisible(false);
                    errorLabelClear();
//                        clearAll();
                }
            }
        });
        //select a user from user table
        tblUsers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserTM>() {
            @Override
            public void changed(ObservableValue<? extends UserTM> observable, UserTM oldValue, UserTM newValue) {
                UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
                errorLabelClear();
                if(selectedUser == null){
//                    cmbUserRole.getSelectionModel().clearSelection();
                    return;
                }
                if(newValue.getUserRole().equals("Hospital IT") || newValue.getUserRole().equals("Quarantine Center IT")){
                    txtUserId.setText(selectedUser.getUserId());
                    txtName.setText(selectedUser.getName());
                    txtContactNumber.setText(selectedUser.getContactNumber());
                    txtEmail.setText(selectedUser.getEmail());
                    txtUserName.setText(selectedUser.getUsername());
                    txtPasswordHide.setText(selectedUser.getPassword());
                    cmbUserRole.getSelectionModel().select(selectedUser.getUserRole());
                    cmbUserLocation.getSelectionModel().select(selectedUser.getLocation());
                    cmbUserLocation.setVisible(true);

                }else{
                    txtUserId.setText(selectedUser.getUserId());
                    txtName.setText(selectedUser.getName());
                    txtContactNumber.setText(selectedUser.getContactNumber());
                    txtEmail.setText(selectedUser.getEmail());
                    txtUserName.setText(selectedUser.getUsername());
                    txtPasswordHide.setText(selectedUser.getPassword());
                    cmbUserRole.getSelectionModel().select(newValue.getUserRole());
                }
                btnSave.setDisable(false);
                txtUserId.setDisable(false);
                txtName.setDisable(false);
                txtUserName.setDisable(false);
                txtPasswordHide.setDisable(false);
                txtPasswordShow.setDisable(false);
                txtContactNumber.setDisable(false);
                txtEmail.setDisable(false);
                cmbUserRole.setDisable(false);
                cmbUserLocation.setDisable(false);
                btnSave.setText("Update");
            }
        });
        //Bind the textField and passwordField text values bidirectionally.
        txtPasswordShow.textProperty().bindBidirectional(txtPasswordHide.textProperty());
        //generate password
        txtUserName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                txtPasswordHide.setText(passwordGenerator(8));
            }
        });

        //search User
        txtSearchUser.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                errorLabelClear();
                ObservableList<UserTM>searchUser = tblUsers.getItems();
                String lowerCaseFilter = newValue.toLowerCase();
                searchUser.clear();
                for(UserTM user : userDB){
                    if(user.getName().toLowerCase().contains(lowerCaseFilter) ||
                            user.getUsername().toLowerCase().contains(lowerCaseFilter)||
                            user.getUserRole().toLowerCase().contains(lowerCaseFilter)){
                        searchUser.add(user);
                    }
                }
            }
        });
//        errorLabelClear();
    }
    public void btnHome_OnAction(ActionEvent actionEvent)throws IOException {
        Scene dashboard = new Scene(FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(dashboard);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }

    public void btnNewUser_OnAction(ActionEvent actionEvent) {
        errorLabelClear();
        tblUsers.getSelectionModel().clearSelection();
        cmbUserRole.getSelectionModel().clearSelection();
        cmbUserLocation.setVisible(false);
        txtName.clear();
        txtContactNumber.clear();
        txtEmail.clear();
        txtUserName.clear();
        txtPasswordHide.clear();
        txtPasswordShow.clear();
        txtSearchUser.clear();
        enableTxt();
        btnSave.setText("Save");
        btnSave.setDisable(false);
        //create id
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1");
            ResultSet rst = pstm.executeQuery();
            if(!rst.next()){
                txtUserId.setText("U001");
            }else{
                String lastRowId = rst.getString(1);
                String subString = lastRowId.substring(1,4);
                int userId = Integer.parseInt(subString)+1;
                if(userId < 10){
                    txtUserId.setText("U00" + userId);
                }else if(userId < 100){
                    txtUserId.setText("U0" + userId);
                }else{
                    txtUserId.setText("U" + userId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtName.requestFocus();
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        //VALIDATION
        boolean validId = FormValidation.validId(txtUserId, lblErrorUserId, "* Invalid Id or press +NewUser button");
        boolean validName = FormValidation.validText(txtName, lblErrorName, "* Invalid Text");
        boolean validContactNumber = FormValidation.validContactNumber(txtContactNumber, lblErrorUserContactNumber, "* Invalid contact number xxx-xxxxxxx");
        boolean validEmail = FormValidation.validEmail(txtEmail, lblErrorUserEmail, "* Invalid Email");
        boolean validUsername = FormValidation.validUsername(txtUserName, lblErrorUserName, "Invalid Username");
        boolean validShowPassword = FormValidation.validPassword(txtPasswordShow, lblErrorPassword, "Invalid password");
        boolean validHidePassword = FormValidation.validPassword(txtPasswordHide, lblErrorPassword, "Invalid password");
        boolean validcomboUserRole = FormValidation.comBoxValidation(cmbUserRole, lblErrorUserRole, "* Please select the user Role");

//        boolean validComboUserLocation = FormValidation.comBoxValidation(cmbUserLocation, lblErrorUserLocation, "* Please select the user Location");

        if (
                validId && validName && validContactNumber && validEmail && validUsername && validShowPassword && validHidePassword &&
                validcomboUserRole
        ) {
            //save data
            if (btnSave.getText().equals("Save")) {
                //CHECK USERNAME DUPLICATION
                try {
                    PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("SELECT user_name FROM User WHERE user_name=?");
                    pstm.setObject(1, txtUserName.getText());
                    ResultSet resultSet = pstm.executeQuery();
                    if (resultSet.next()) {
                        new Alert(Alert.AlertType.ERROR, "the username is already exists.Please try another", ButtonType.OK).showAndWait();
                        txtUserName.requestFocus();
                    } else {
                        //save user data in DB
                        //save admin & pstf member data
                        if (cmbUserRole.getSelectionModel().getSelectedItem().equals("Admin") || cmbUserRole.getSelectionModel().getSelectedItem().equals("P.S.T.F Member")) {
                            addAdminPSTFDataToDB();
                            clearAll();
                        } else if (cmbUserRole.getValue().equals("Hospital IT") || cmbUserRole.getValue().equals("Quarantine Center IT")) {
/*                         boolean validComboUserLocation = FormValidation.comBoxValidation(cmbUserLocation, lblErrorUserLocation, "* Please select the user Location");
                            if (validComboUserLocation) {*/
                                addHospitalQuarantineITDataToDB();
                                clearAll();
                            }
                        }
//                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                tblUsers.refresh();
                cmbUserLocation.setVisible(false);
                cmbUserRole.getSelectionModel().clearSelection();
                btnNewUser.requestFocus();
            } else {
                //update data
                lblErrorUserLocation.setText("");
                cmbUserLocation.setVisible(false);
                if (tblUsers.getSelectionModel().getSelectedItem().getUsername().equals(txtUserName.getText())) {
                    if (cmbUserRole.getValue().equals("Admin") || cmbUserRole.getValue().equals("P.S.T.F Member")) {
                        updateAdminPSTFDbData();
                        clearAll();
                    } else {
                        updateDbData();
                        clearAll();
                    }
                } else {
                    try {
                        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement
                                ("SELECT user_name FROM user WHERE user_name=?");
                        pstm.setObject(1, txtUserName.getText());
                        ResultSet rst = pstm.executeQuery();
                        if (rst.next()) {
                            new Alert(Alert.AlertType.ERROR, "the username is already exists.Please try another", ButtonType.OK).showAndWait();
                            txtUserName.selectAll();
                            txtUserName.requestFocus();
                        } else {
                            if (cmbUserRole.getValue().equals("Hospital IT") || cmbUserRole.getValue().equals("Quarantine Center IT")) {
                                updateDbData();
                                clearAll();
                            } else {
                                updateDbData();
                                clearAll();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            addDataToUserTable();
        }
//        clearForm();
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        errorLabelClear();
        UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if(selectedUser == null){
            new Alert(Alert.AlertType.WARNING,"Select an user to delete",ButtonType.OK).showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Are you sure that you want to delete the selected user",ButtonType.YES,ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult()==ButtonType.YES){
            if (tblUsers.getSelectionModel().getSelectedItem().getUserRole().equals("Admin")||tblUsers.getSelectionModel().getSelectedItem().getUserRole().equals("P.S.T.F Member")){
                //delete row
                deleteUser();
            }else {
                //delete row
                deleteUser();
                //update hospital or qc it person
                if (tblUsers.getSelectionModel().getSelectedItem().getUserRole().equals("Hospital IT")){
                    deleteItPersonHospital();
                    cmbUserLocation.setVisible(false);
                }else {
                    deleteItPersonQuarantineCenter();
                    cmbUserLocation.setVisible(false);
                }
            }
        }
        addDataToUserTable();
        clearAll();
        btnSave.setText("Save");
        btnNewUser.requestFocus();
    }


    //METHOD - ADD ADMIN USER DATA TO DATABASE
    private void addAdminPSTFDataToDB(){
        lblErrorUserLocation.setText("");
        String userId = txtUserId.getText();
        String name = txtName.getText();
        String username = txtUserName.getText();
        String password = txtPasswordShow.getText();
        Object userRole = cmbUserRole.getValue();
        String contactNumber = txtContactNumber.getText();
        String email = txtEmail.getText();
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT into" +
                    " user(user_id,`name`,user_name,password,user_role, contact_number," +
                    "email) " +
                    "VALUES (?,?,?,?,?,?,?)");
            pstm.setObject(1,userId);
            pstm.setObject(2,name);
            pstm.setObject(3,username);
            pstm.setObject(4,password);
            pstm.setObject(5,userRole);
            pstm.setObject(6,contactNumber);
            pstm.setObject(7,email);
            int affectedRow = pstm.executeUpdate();
            if (affectedRow>0){
                new Alert(Alert.AlertType.INFORMATION, "Successfully Added", ButtonType.OK).showAndWait();
                addDataToUserTable();
            }else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong...please try again", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - ADD Hospital or Quarantine IT USER DATA TO DATABASE
    private void addHospitalQuarantineITDataToDB(){
        tblUsers.getSelectionModel().clearSelection();
        String userId = txtUserId.getText();
        String name = txtName.getText();
        String username = txtUserName.getText();
        String password = txtPasswordShow.getText();
        Object userRole = cmbUserRole.getValue();
        String contactNumber = txtContactNumber.getText();
        String email = txtEmail.getText();
        Object location = cmbUserLocation.getValue();
        String selectedHospital = cmbUserLocation.getSelectionModel().getSelectedItem();
        String selectedHospitalId = selectedHospital.substring(0,4);
//validation
        boolean validComboUserLocation = FormValidation.comBoxValidation(cmbUserLocation, lblErrorUserLocation, "* Please select the user Location");
        if (validComboUserLocation) {

            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT into" +
                        " user(user_id,`name`,user_name,password,user_role, contact_number," +
                        "email,location) " +
                        "VALUES (?,?,?,?,?,?,?,?)");
                pstm.setObject(1, userId);
                pstm.setObject(2, name);
                pstm.setObject(3, username);
                pstm.setObject(4, password);
                pstm.setObject(5, userRole);
                pstm.setObject(6, contactNumber);
                pstm.setObject(7, email);
                pstm.setObject(8, location);
                int affectedRow = pstm.executeUpdate();
                if (affectedRow > 0) {
                    addDataToUserTable();
                    //add IT person to hospital dataBase
                    if (cmbUserRole.getValue().equals("Hospital IT")) {
                        addItPersonHospital();
                    } else if (cmbUserRole.getValue().equals("Quarantine Center IT")) {
                        addItPersonQuarantineCenter();
                    }
                    new Alert(Alert.AlertType.INFORMATION, "User has been added Successfully ", ButtonType.OK).showAndWait();

                } else {
                    new Alert(Alert.AlertType.INFORMATION, "user failed to add", ButtonType.OK).showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //METHOD - ADD USER DATA TO TABLE
    private void addDataToUserTable() {
        try {
            ObservableList<UserTM> users = tblUsers.getItems();
            users.clear();
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM user");
            ResultSet rst = preparedStatement.executeQuery();
            while (rst.next()){
                String userid = rst.getString(1);
                String name = rst.getString(2);
                String username = rst.getString(3);
                String password = rst.getString(4);
                String userRole = rst.getString(5);
                String contactNumber = rst.getString(6);
                String email = rst.getString(7);
                String location = rst.getString(8);

                UserTM user = new UserTM(userid,name,username,password,userRole,contactNumber,
                        email,location);
                tblUsers.getItems().add(user);
                userDB.add(user);
                tblUsers.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - UPDATE USER DATA
    private void updateDbData() {

        UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        String userId = txtUserId.getText();
        String name = txtName.getText();
        String username = txtUserName.getText();
        String password = txtPasswordShow.getText();
        //hide password
        String useRole = cmbUserRole.getSelectionModel().getSelectedItem();
        String contactNumber = txtContactNumber.getText();
        String email = txtEmail.getText();
        String location = cmbUserLocation.getSelectionModel().getSelectedItem();
        //validation
        boolean validComboUserLocation = FormValidation.comBoxValidation(cmbUserLocation, lblErrorUserLocation, "* Please select the user Location");
        if (validComboUserLocation) {
            String sql = "UPDATE user SET user_id=?,name=?,user_name=?,password=?," +
                    "user_role=?,contact_number=?,email=?," +
                    "location=? WHERE user_id=?";

            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
                pstm.setObject(1, userId);
                pstm.setObject(2, name);
                pstm.setObject(3, username);
                pstm.setObject(4, password);
                pstm.setObject(5, useRole);
                pstm.setObject(6, contactNumber);
                pstm.setObject(7, email);
                pstm.setObject(8, location);
                pstm.setObject(9, selectedUser.getUserId());
                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    if (cmbUserRole.getValue().equals("Hospital IT")) {
                        addItPersonHospital();
                    } else if (cmbUserRole.getValue().equals("Quarantine Center IT")) {
                        addItPersonQuarantineCenter();
                    }
                    new Alert(Alert.AlertType.INFORMATION, "Updated Successfully", ButtonType.OK).showAndWait();
                    tblUsers.refresh();
                    tblUsers.getSelectionModel().clearSelection();
                    btnSave.setText("Save");
                    btnSave.setDisable(true);
                    btnNewUser.requestFocus();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong...please try again", ButtonType.OK).showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //METHOD - UPDATE USER DATA
    private void updateAdminPSTFDbData() {
        UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        String userId = txtUserId.getText();
        String name = txtName.getText();
        String username = txtUserName.getText();
        String password = txtPasswordShow.getText();
        //hide password
        String useRole = cmbUserRole.getSelectionModel().getSelectedItem();
        String contactNumber = txtContactNumber.getText();
        String email = txtEmail.getText();
//        String location = cmbUserLocation.getSelectionModel().getSelectedItem();

        String sql = "UPDATE user SET user_id=?,name=?,user_name=?,password=?," +
                "user_role=?,contact_number=?,email=?," +
                "location=? WHERE user_id=?";

        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setObject(1, userId);
            pstm.setObject(2, name);
            pstm.setObject(3, username);
            pstm.setObject(4, password);
            pstm.setObject(5, useRole);
            pstm.setObject(6, contactNumber);
            pstm.setObject(7, email);
            pstm.setObject(8, "");
            pstm.setObject(9, selectedUser.getUserId());
            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully", ButtonType.OK).showAndWait();
                tblUsers.refresh();
                tblUsers.getSelectionModel().clearSelection();
                btnSave.setText("Save");
                btnSave.setDisable(true);
                btnNewUser.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong...please try again", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    //METHOD - load hospitals to comboBox
    private void loadAllHospital() {
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM hospital WHERE it_person=?");
            pstm.setObject(1,"not reserved");
            ResultSet rst = pstm.executeQuery();
            cmbUserLocation.getItems().clear();
            while (rst.next()){
                String hospitalId = rst.getString(1);
                String hospitalName = rst.getString(2);

                cmbUserLocation.getItems().add(hospitalId+  "-" +hospitalName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - load Quarantine Centers to comboBox
    private void loadAllQuarantineCenters() {
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM quarantine_center WHERE it_person=?");
            pstm.setObject(1,"not reserved");
            ResultSet rst = pstm.executeQuery();
            cmbUserLocation.getItems().clear();
            while (rst.next()){
                String quarantineCenterId = rst.getString(1);
                String quarantineCenterName = rst.getString(2);

                cmbUserLocation.getItems().add(quarantineCenterId+  "-" +quarantineCenterName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - generate random password
    private String passwordGenerator(int passwordLength) {
        String charLowercase = "abcdefghijklmnopqrstuvwxyz";
        String charUppercase = charLowercase.toUpperCase();
        String specialCharacters = "!@#$%&*()_+-=[]?";
        String numbers = "0123456789";
        String passwordCharBase = charLowercase + charUppercase + specialCharacters + numbers;
//        Random random = new Random();
        SecureRandom random = new SecureRandom();
        char[] password = new char[passwordLength];

        password[0] = charLowercase.charAt(random.nextInt(charLowercase.length()));
        password[1] = charUppercase.charAt(random.nextInt(charUppercase.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < passwordLength; i++) {
            password[i] = passwordCharBase.charAt(random.nextInt(passwordCharBase.length()));
        }
        return password.toString();
    }
    //METHOD - TO CLEAR TEXT
    private void clearAll() {
        cmbUserRole.getSelectionModel().clearSelection();
        cmbUserLocation.getSelectionModel().clearSelection();
        tblUsers.getSelectionModel().clearSelection();
        txtUserId.clear();
        txtName.clear();
        txtContactNumber.clear();
        txtEmail.clear();
        txtUserName.clear();
        txtPasswordHide.clear();
        txtPasswordShow.clear();
        txtSearchUser.clear();
    }
    //METHOD - to enable textboxes
    private void enableTxt() {
        txtUserId.setDisable(false);
        txtName.setDisable(false);
        txtUserName.setDisable(false);
        txtPasswordHide.setDisable(false);
        txtPasswordShow.setDisable(false);
        cmbUserRole.setDisable(false);
        cmbUserLocation.setDisable(false);
        txtContactNumber.setDisable(false);
        txtEmail.setDisable(false);
        btnSave.setDisable(false);
    }
    //METHOD - add it person to hospitalDB
    private void addItPersonHospital(){
        try {
            String userId = txtUserId.getText();
            String selectedHospital = cmbUserLocation.getSelectionModel().getSelectedItem();
            String selectedHospitalId = selectedHospital.substring(0,4);
            PreparedStatement pstm2 = DBConnection.getInstance().getConnection().prepareStatement
                    ("UPDATE hospital SET hospital.it_person=? WHERE hospital_id=?");
            pstm2.setObject(1, userId);
            pstm2.setObject(2, selectedHospitalId);
            pstm2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - add it person to QuarantineDB
    private void addItPersonQuarantineCenter(){
        try {
            String userId = txtUserId.getText();
            String selectedQuarantineCenter = cmbUserLocation.getSelectionModel().getSelectedItem();
            String selectedQuarantineCenterId = selectedQuarantineCenter.substring(0,4);
            PreparedStatement pstm2 = DBConnection.getInstance().getConnection().prepareStatement
                    ("UPDATE quarantine_center SET quarantine_center.it_person=? WHERE quarantine_center_id=?");
            pstm2.setObject(1, userId);
            pstm2.setObject(2, selectedQuarantineCenterId);
            pstm2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - delete it person to hospitalDB
    private void deleteItPersonHospital(){
        try {
            String selectedLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
            String subsStringLocationId = selectedLocation.substring(0,4);
            PreparedStatement pstm2 = DBConnection.getInstance().getConnection().prepareStatement
                    ("UPDATE hospital SET hospital.it_person=? WHERE hospital_id=?");
            pstm2.setObject(1,"not reserved");
            pstm2.setObject(2,subsStringLocationId);
            pstm2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - Delete it person to QuarantineDB
    private void deleteItPersonQuarantineCenter(){
        try {
            String selectedLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
            String subsStringLocationId = selectedLocation.substring(0,4);
            PreparedStatement pstm2 = DBConnection.getInstance().getConnection().prepareStatement
                    ("UPDATE quarantine_center SET quarantine_center.it_person=? WHERE quarantine_center_id=?");
            pstm2.setObject(1,"not reserved");
            pstm2.setObject(2,subsStringLocationId);
            pstm2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //hide password
    public void imgHidePAssword_OnAction(MouseEvent mouseEvent) {
        imghidePassword.setVisible(false);
        txtPasswordHide.setVisible(true);
        txtPasswordShow.setVisible(false);
        imgShowPassword.setVisible(true);
        txtPasswordHide.requestFocus();
    }
    //show password
    public void imgShowPassword_OnAction(MouseEvent mouseEvent) {
        txtPasswordHide.setVisible(false);
        imghidePassword.setVisible(true);
        imgShowPassword.setVisible(false);
        txtPasswordShow.setVisible(true);

        txtPasswordShow.requestFocus();
    }
    //METHOD - Delete user
    private void deleteUser(){
        try {
            UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement
                    ("DELETE FROM User WHERE user_id=?");
            preparedStatement.setObject(1,selectedUser.getUserId());
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully",ButtonType.OK).showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //method Error message clear
    private void errorLabelClear(){
        lblErrorUserId.setText("");
        lblErrorName.setText("");
        lblErrorUserContactNumber.setText("");
        lblErrorUserEmail.setText("");
        lblErrorUserName.setText("");
        lblErrorPassword.setText("");
        lblErrorUserRole.setText("");
        lblErrorUserLocation.setText("");
    }

}
