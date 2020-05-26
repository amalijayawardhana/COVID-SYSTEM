package validation;

import javafx.scene.control.*;

import java.time.LocalDate;


public class FormValidation {
    //id
    public static boolean validId(TextField text, Label errorLabel, String errorMessage) {
        boolean validity = true;
        String errorText = null;

        if (text.getText().trim().isEmpty() || !(text.getText().matches("^[HhCcUu]{1}[0-9]{3}"))) {
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }

    //number
    public static boolean validNumber(TextField text, Label errorLabel, String errorMessage) {
        boolean validity = true;
        String errorText = null;

        if (text.getText().trim().isEmpty() || !(text.getText().matches("^[0-9]+"))) {
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }

    //contactNumber
    public static boolean validContactNumber(TextField text, Label errorLabel, String errorMessage) {
        boolean validity = true;
        String errorText = null;
        if (text.getText().trim().isEmpty() || !text.getText().matches("^[0-9]{3}[-][0-9]{7}")) {
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }

    //Text
    public static boolean validText(TextField text, Label errorLabel, String errorMessage) {
        boolean validity = true;
        String errorText = null;

        if (text.getText().trim().isEmpty() || !text.getText().matches("^[a-zA-Z]+([\\s.a-zA-Z])*")) {
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }

    //email
    public static boolean validEmail(TextField text, Label errorLabel, String errorMessage) {
        boolean validity = true;
        String errorText = null;

        if (text.getText().trim().isEmpty() || !text.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }

    //combo box
    public static boolean comBoxValidation(ComboBox comboValue, Label errorLabel, String errorMessage) {
        boolean validity = true;
        String errorText = null;

        if (comboValue.getSelectionModel().isEmpty()) {
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }

    //username
    public static boolean validUsername(TextField text, Label errorLabel, String errorMessage) {
        boolean validity = true;
        String errorText=null;

        if (text.getText().trim().isEmpty() || !text.getText().matches("^[a-zA-Z0-9\\\\._@^\\\\-]{5,}$")) {
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }

    //password
    public static boolean validPassword(TextField text, Label errorLabel, String errorMessage){
        boolean validity = true;
        String errorText = null;

        if (text.getText().trim().isEmpty() || !(text.getText().length()>=8)) {
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }

    //Date
    public static boolean dateValidation(DatePicker date, Label errorLabel, String errorMessage){
        boolean validity = true;
        String errorText = null;
/*        LocalDate minDate = LocalDate.of(2019,11,1);
        LocalDate maxDate = LocalDate.now();
        date.setDayCellFactory(d->
                new DateCell(){
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }
                });*/

        if (date.getValue()==null){
            validity = false;
            errorText = errorMessage;
        }
        errorLabel.setText(errorText);
        return validity;
    }
}
