package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageLoginController {
    public AnchorPane root;
    public TextField txtUsername;
    public Label lblUsernameError;
    public TextField txtPasswordShow;
    public Label lblPasswordError;
    public Button btnLogin;
    public Label lblError;
    public static String loginUsername;
    public static String loginUserId;
    public static String loginUserRole;
    public ImageView imghidePassword;
    public ImageView imgShowPassword;
    public PasswordField txtPasswordHide;

    public void initialize(){
        txtPasswordShow.textProperty().bindBidirectional(txtPasswordHide.textProperty());
    }

    public void btnLogin_OnAction(ActionEvent actionEvent) {

        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM user WHERE user_name=? AND password=?");
                pstm.setObject(1,txtUsername.getText());
                pstm.setObject(2,txtPasswordShow.getText());
                ResultSet rst = pstm.executeQuery();
                if(rst.next()) {
                    String userId = rst.getString(1);
                    String userRole = rst.getString(5);
//                    System.out.println(userRole);
                   String username = rst.getString(3);
                    loginUsername = username;
                    loginUserId=userId;
                    loginUserRole = userRole;
                        try {
                            Scene dashboard = new Scene(FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml")));
                            Stage primaryStage = (Stage) (root.getScene().getWindow());
                            primaryStage.setScene(dashboard);
                            primaryStage.centerOnScreen();
                            primaryStage.sizeToScene();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }else {
                    lblError.setText("Incorrect Username or Password...try again");
                    txtUsername.requestFocus();
                }
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
}
