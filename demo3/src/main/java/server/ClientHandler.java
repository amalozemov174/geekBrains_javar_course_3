package server;

import com.example.demo.ChatClient;
import com.example.demo.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    public Socket socket;
    public ChatServer chatServer;
    public String nick;
    public final DataInputStream in;
    public final DataOutputStream out;
    public AuthService authService;

    public ChatLogger chatLogger;

    private boolean authOk = false;

    public ClientHandler(Socket socket, ChatServer chatServer, AuthService authService) {
        try{
            this.authService = authService;
            this.socket = socket;
            this.chatServer = chatServer;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

//            new Thread(() -> {
//                try{
//                    authenticate();
//                    readMessage();
//
//                    closeConnection();
//
//                }
//                finally {
//                    closeConnection();
//                }
//
//            }).start();

            Thread current = new Thread(() -> {
                try{
                    authenticate();
                    readMessage();
                }
                finally {
                    closeConnection();
                }

            });

            Thread timer = new Thread(() -> {
                try {
                        Thread.sleep(120 * 1000);
                        if(!authOk){
                            sendMessage(Command.TIMEOUT, "Time to connection is out...");
                            //current.stop();
                            current.interrupt();

                            throw  new RuntimeException("Time to connection is out...");
                        }
                }
                catch (InterruptedException e){
                    throw  new RuntimeException("Error creation client connection...", e);
                }


            });

            current.start();
            timer.start();

        }
        catch (Exception e){
            throw  new RuntimeException("Error creation client connection...", e);
        }

    }

    private void authenticate() {
        while (true){
            try {
                final String message = in.readUTF();
                if(Command.isCommand(message)){
                    final Command command = Command.getCommand(message);
                    final String[] params = command.parse(message);
                    if(command == Command.AUTH){
                        final String login = params[0];
                        final String password = params[1];
                        final String nick = authService.getNickByLoginAndPassword(login, password);
                        if (nick != null){
                            if (chatServer.isNickBusy(nick)){
                                sendMessage(Command.ERROR,"User has authorized already");
                                continue;
                            }
                            sendMessage(Command.AUHOK, nick);
                            chatLogger = new ChatLogger(this);
                            this.nick = nick;
                            chatServer.broadcast("User " + nick + " join the chat");
                            chatLogger.write("User " + nick + " join the chat");
                            chatServer.subscribe(this);
                            authOk = true;
                            chatServer.unicast(this, nick, chatLogger.read());
                            break;
                        }
                        else {
                            authOk = false;
                            sendMessage(Command.ERROR, "Incorrdct login and password");
                        }

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Command command, String... params) {
        sendMessage(command.collectMessage(params));
    }

    private void readMessage(){
        while (true){
            try {
                final String msg = in.readUTF();
                if(Command.isCommand(msg) && Command.getCommand(msg) == Command.END){
                    break;
                }
                if(Command.isCommand(msg) && Command.getCommand(msg) == Command.PRIVATE_MESSAGE){
                    Command command = Command.getCommand(msg);
                    final String[] params = command.parse(msg);
                    chatServer.unicast(this, params[0], params[1]);
                    chatLogger.write(params[0] + params[1]);
                    continue;
                }
                if(Command.isCommand(msg) && Command.getCommand(msg) == Command.CHANGENICK){
                    Command command = Command.getCommand(msg);
                    final String[] params = command.parse(msg);
                    chatServer.setNickName(this, params[0], params[1]);
                    continue;
                }
                else {
                    System.out.println("Get message: " + msg);
                    chatLogger.write(msg);
                    chatServer.broadcast(this, msg);
                    //chatServer.broadcast( msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void closeConnection(){
        sendMessage(Command.END);
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
                chatServer.unsubscrube(this);
                socket.close();
            }
        }
        catch (IOException e){
            throw  new RuntimeException("Error of closing", e);
        }
    }

    public void sendMessage(String message){
        try {
            System.out.println("Sending the message " + message);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return this.nick;
    }
}
