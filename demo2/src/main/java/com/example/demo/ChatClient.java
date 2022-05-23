package com.example.demo;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ClientController controller;

    private String nick;
    public ChatClient(ClientController controller){
        this.controller = controller;
    }

    public void openConnection() throws IOException{
        socket = new Socket("localhost", 8189);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        new Thread(() ->{
            try{
                waitAuth();
                readMessage();
            }
            finally {
                closeConnection();
            }

        }).start();
    }

    private void waitAuth() {
        while(true){
            try {
                final String msg = in.readUTF();
                if(Command.isCommand(msg)){
                    final Command command = Command.getCommand(msg);
                    final String[] params = command.parse(msg);
                    if(command == Command.AUHOK){
                        final String nick = params[0];
                        controller.addMessage("Successful authorization by nickname " + nick);
                        controller.toggleBoxesVisibility(true);
                        break;
                    }
                    if(command == Command.ERROR){
                        Platform.runLater(() -> controller.showError(params));
                        continue;
                    }
                    if(command == Command.TIMEOUT){
                        Platform.runLater(() -> controller.showError(command,params));
                        closeConnection();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void readMessage() {
        while(true){
            try {
                final String msg = in.readUTF();
                if(Command.isCommand(msg)){
                    final Command command = Command.getCommand(msg);
                    final String[] params = command.parse(msg);
                    if(command == Command.END){
                        controller.toggleBoxesVisibility(false);
                        break;
                    }
                    if(command == Command.ERROR){
                        //controller.showError(params);
                        Platform.runLater(() -> controller.showError(params));
                        continue;
                    }
                    if (command == Command.TIMEOUT){
                        Platform.runLater(() -> controller.showError(params));
                        break;
                    }
                    if(command == Command.CLIENTS){
                        controller.updateClientList(params);
                        continue;
                    }
                }
                controller.addMessage(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void closeConnection() {
        sendMessage("/end");
        try{
            if(in != null){
                in.close();
            }
        }
        catch (IOException e){
            throw  new RuntimeException("Error of closing", e);
        }

        try{
            if(out != null){
                out.close();
            }
        }
        catch (IOException e){
            throw  new RuntimeException("Error of closing", e);
        }

        try{
            if(socket != null){
                socket.close();
            }
        }
        catch (IOException e){
            throw  new RuntimeException("Error of closing", e);
        }
    }


    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick(){
        return this.nick;
    }

    public void sendMessage(Command command, String... params) {
        sendMessage(command.collectMessage(params));
    }
}
