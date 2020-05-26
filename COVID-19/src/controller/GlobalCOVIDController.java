package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import validation.FormValidation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;

import static controller.ManageLoginController.loginUserId;
import static controller.ManageLoginController.loginUserRole;

public class GlobalCOVIDController {
    public AnchorPane root;
    public Label lblUpdated_Date;
    public Label lblConfirmed_Cases;
    public Label lblRecovered_cases;
    public Label lbl_Deaths;
    public DatePicker txtDate;
    public TextField txtConfirmed_cases;
    public TextField txtRecovered_cases;
    public TextField txtDeaths;
    public Label lblErrorDate;
    public Label lblErrorConfirmedCases;
    public Label lblErrorRecoveredCases;
    public Label lblErrorDeaths;
    public Button btnUpdate;

    public void initialize(){
        //date from 2019.11.01 upto today
        LocalDate minDate = LocalDate.of(2019,11,1);
        LocalDate maxDate = LocalDate.now();
        txtDate.setDayCellFactory(d->
                new DateCell(){
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }
                });
        if(loginUserRole.equals("Hospital IT")|| loginUserRole.equals("Quarantine Center IT")){
            txtDate.setDisable(true);
            txtConfirmed_cases.setDisable(true);
            txtRecovered_cases.setDisable(true);
            txtDeaths.setDisable(true);
            btnUpdate.setDisable(true);
        }
        //load latest update data to labels
        loadAllEnteredRecords();
    }

    public void btnUpdate_OnAction(ActionEvent actionEvent) {
        boolean validDate = FormValidation.dateValidation(txtDate, lblErrorDate, "Invalid Date");
        boolean validConfirmedCases = FormValidation.validNumber(txtConfirmed_cases, lblErrorConfirmedCases, "Invalid Number");
        boolean validRecoveredCases = FormValidation.validNumber(txtRecovered_cases, lblErrorRecoveredCases, "Invalid Number");
        boolean validDeathCases = FormValidation.validNumber(txtDeaths, lblErrorDeaths, "Invalid Number");

        if (validDate && validConfirmedCases && validRecoveredCases && validDeathCases) {
            updateCOVID();
            loadAllEnteredRecords();
            txtDate.getEditor().clear();
            txtConfirmed_cases.clear();
            txtRecovered_cases.clear();
        }
    }

    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
        Scene dashboard = new Scene(FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(dashboard);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }
    //METHOD - update data in database
    private void updateCOVID(){
        //update data to db
        try {
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement
                    ("INSERT INTO global_covid_update(updated_date,confirmed_cases," +
                            "recovered_cases,dead_cases,user_id) VALUES(?,?,?,?,?) ");

            pstm.setObject(1,txtDate.getValue());
            pstm.setObject(2,txtConfirmed_cases.getText());
            pstm.setObject(3,txtRecovered_cases.getText());
            pstm.setObject(4,txtDeaths.getText());
            pstm.setObject(5,loginUserId);

            int affectedRows = pstm.executeUpdate();
            if(affectedRows > 0){
                new Alert(Alert.AlertType.INFORMATION,"Successfully updated").showAndWait();
            }else {
                new Alert(Alert.AlertType.ERROR, "Please try again", ButtonType.OK).show();
                txtDate.requestFocus();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - load data to interface
    private void loadAllEnteredRecords() {
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM global_covid_update ORDER BY updated_date DESC LIMIT 1");
            ResultSet rst = pstm.executeQuery();
            if(rst.next()){
                lblUpdated_Date.setText(String.valueOf(rst.getDate(2)));

                //load count cases
                sumOfCases();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //METHOD - calculate total cases(sum)
    private void sumOfCases() {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT SUM(confirmed_cases),SUM(recovered_cases),SUM(dead_cases) FROM global_covid_update");
            ResultSet rst = preparedStatement.executeQuery();
            if(rst.next()){
                lblConfirmed_Cases.setText(rst.getInt(1)+"");
                lblRecovered_cases.setText(rst.getInt(2)+"");
                lbl_Deaths.setText(rst.getInt(3)+"");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private int numberingFormat(int number){
        NumberFormat numberInstance = NumberFormat.getInstance();
        numberInstance.setGroupingUsed(true);
//        int x = 125456;
        System.out.println(numberInstance.format(number));
/*        String formattedText = numberInstance.format(txtConfirmed_cases.getText());
        lblConfirmed_Cases.setText(formattedText);*/
        return number;
    }
}
