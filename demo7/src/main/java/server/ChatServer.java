package server;

import com.example.demo.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ChatServer {
    private final Map<String,ClientHandler> clients;
    private final List<String> niks;
    private AuthService authService;

    private ExecutorService executorService;

    public ChatServer() {
        this.clients = new HashMap<>();
        this.niks = new ArrayList<>();
        executorService = Executors.newCachedThreadPool();
        //authService = new InMemoryAuthService();
        authService = new DbAuthService();
        authService.start();

    }

    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(8189);) {
            while(true){
                System.out.println("server waiting connection from client...");
                final Socket socket = serverSocket.accept();
                System.out.println("Client connected...");
                new ClientHandler(socket, this, authService, executorService);
            }
        } catch (IOException e) {
            throw new RuntimeException("Server error", e);
        }
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);
    }

    public void broadcast(String message) {
        for(ClientHandler client : clients.values()){
            client.sendMessage(message);
        }
    }

    public void broadcast(ClientHandler sender, String message) {
        for(ClientHandler client : clients.values()){
            client.sendMessage(sender.getNick() + ": " + message);
        }
    }

    public void unicast(ClientHandler sender, String nick, String message){
//        for(ClientHandler client : clients.values()){
//            if(client.getNick().equals(nick))
//                client.sendMessage(message);
//        }
        final ClientHandler receiver = clients.get(nick);
        if(receiver != null){
            receiver.sendMessage("from " + sender.getNick() + ": " + message);
            sender.sendMessage("participant " + nick + ": " + message);
        } else {
            sender.sendMessage(Command.ERROR, "Participant with nick" + nick + " not in chat");
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.put(clientHandler.getNick(), clientHandler);
        niks.add(clientHandler.getNick());
        broadcastClientList();
    }

    public void unsubscrube(ClientHandler clientHandler) {
        clients.remove(clientHandler.getNick());
        broadcastClientList();
    }

    private void broadcastClientList() {
      final String nicks = clients.values().stream()
                .map(client -> client.getNick())
                .collect(Collectors.joining(" "));

      broadcast(Command.CLIENTS, nicks);
    }

    private void broadcast(Command command, String nicks) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(command, nicks);
        }
    }


    public void setNickName(ClientHandler sender, String nick, String login) {
        if(!isNickBusy(nick)){
            String tmpNick = sender.getNick();
            authService.setNickName(nick, login);
            niks.remove(sender.getNick());
            niks.add(nick);

            String tmp = "";
            for(String nck : niks){
                tmp = tmp + nck + " ";
            }

            broadcast(Command.CLIENTS, tmp.trim());
        }
        else {
            sender.sendMessage(Command.ERROR, "the nick" + nick + " is used by another user");
        }


    }
}
