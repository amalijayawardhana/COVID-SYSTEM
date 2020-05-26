package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import static controller.ManageLoginController.loginUserRole;
import static controller.ManageLoginController.loginUsername;

public class DashboardController {
    public AnchorPane root;
    public Label lblLoggedUser;

    public  void initialize() {
        lblLoggedUser.setText(loginUsername);
    }

    public void btnGlobalCOVID_OnAction(ActionEvent actionEvent) throws IOException {
            Scene globalCOVIDScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/GlobalCOVID.fxml")));
            Stage primaryStage = (Stage) (root.getScene().getWindow());
            primaryStage.setScene(globalCOVIDScene);
            primaryStage.centerOnScreen();
            primaryStage.sizeToScene();


    }

    public void btnManageHospital_OnAction(ActionEvent actionEvent) throws IOException {
        if(loginUserRole.equals("Admin")) {
            Scene manageHospital = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ManageHospital.fxml")));
            Stage primaryStage = (Stage) (root.getScene().getWindow());
            primaryStage.setScene(manageHospital);
            primaryStage.centerOnScreen();
            primaryStage.sizeToScene();
        }else {
            new Alert(Alert.AlertType.INFORMATION, "You may not have access to login to this. Please contact admin", ButtonType.OK).showAndWait();
        }
    }

    public void btnQuarantineCenters_OnAction(ActionEvent actionEvent) throws IOException {
        if(loginUserRole.equals("Admin")) {
        Scene manageQuarantineCenter = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ManageQuarantineCenter.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(manageQuarantineCenter);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
        }else {
            new Alert(Alert.AlertType.INFORMATION, "You may not have access to login to this. Please contact admin", ButtonType.OK).showAndWait();
        }
    }

    public void btnManageUsers_OnAction(ActionEvent actionEvent) throws IOException {
        if(loginUserRole.equals("Admin")) {

            Scene manageUser = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ManageUser.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(manageUser);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }else {
        new Alert(Alert.AlertType.INFORMATION, "You may not have access to login to this. Please contact admin", ButtonType.OK).showAndWait();
    }
    }

    public void btnLogout_OnAction(ActionEvent actionEvent) throws IOException {
        Scene login = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ManageLogin.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(login);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }
}
