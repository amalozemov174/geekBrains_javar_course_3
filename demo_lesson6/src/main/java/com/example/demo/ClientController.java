package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.Normalizer;

public class ClientController {

    private ChatClient client;
    @FXML
    public ListView<String> clientList;
    @FXML
    private HBox reloginBox;
    @FXML
    private HBox loginBox;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button authButton;
    @FXML
    private HBox messageBox;
    @FXML
    private TextArea messageArea;
    @FXML
    private TextField textField;
    @FXML
    private Button sendButton;
    @FXML
    private TextField newNickField;

    private String login;

    public ClientController(){
        this.client = new ChatClient(this);
        try {
            client.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authButtonClick(MouseEvent mouseEvent) {
        login = loginField.getText();
        client.sendMessage(Command.AUTH, loginField.getText(), passwordField.getText());
    }

    public void sendButtonClick(MouseEvent mouseEvent) {
        final String text = textField.getText();
        if(text.trim().isEmpty()){
            return;
        }
        client.sendMessage(text);
        textField.clear();
        textField.requestFocus();
    }

    public void addMessage(String message) {
        messageArea.appendText(message + "\n");
    }

    public void toggleBoxesVisibility(boolean isSuccess) {
        loginBox.setVisible(!isSuccess);
        messageBox.setVisible(isSuccess);
    }

    public void showError(String[] error) {
       final Alert alert = new Alert(Alert.AlertType.ERROR, error[0], new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
       alert.setTitle("Error!");
       alert.showAndWait();
    }

    public void showError(Command command, String[] error) {
        if(command == Command.TIMEOUT){
            final Alert alert = new Alert(Alert.AlertType.ERROR, error[0], new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
            alert.setTitle("Error!");
            alert.showAndWait();
            loginBox.setVisible(false);
            reloginBox.setVisible(true);
        }


    }

    public void selectClient(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2){
            final String message = textField.getText();
            final String nick = clientList.getSelectionModel().getSelectedItem();
            textField.setText(Command.PRIVATE_MESSAGE.collectMessage(nick, message));
            textField.requestFocus();
            textField.selectEnd();
        }
    }

    public void updateClientList(String[] params) {
        clientList.getItems().clear();
        clientList.getItems().addAll(params);
    }

    public void reLoginButtonClick(MouseEvent mouseEvent) {
        this.client = new ChatClient(this);
        try {
            client.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginBox.setVisible(true);
        reloginBox.setVisible(false);
    }

    public void changeNickButtonClick(MouseEvent mouseEvent) {
        if(!newNickField.getText().isEmpty()){
            client.sendMessage(Command.CHANGENICK, newNickField.getText(), login);
            newNickField.clear();
        }
    }


}