package server;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DbAuthService implements AuthService {

    private List<UserData> users;
    private Connection connection;

    public class UserData{
        private String login;
        private String password;
        private String nick;

        public UserData(String login, String password, String nick){
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }
    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        for (UserData user : users){
            if(user.login.equals(login) && user.password.equals(password)){
                return user.nick;
            }
        }
        return null;
    }

    @Override
    public void start() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:chat.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        users = new ArrayList<>();
        loadUsers();
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setNickName(String nick, String login) {
        try {
            final PreparedStatement statement = connection.prepareStatement("UPDATE clients SET nick = ? WHERE login = ?;");
            statement.setString(1,nick);
            statement.setString(2,login);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadUsers(){
       try {
           final PreparedStatement statement = connection.prepareStatement("SELECT * FROM clients;");
           final ResultSet rs = statement.executeQuery();
           while(rs.next()){
               String login = rs.getString("login");
               String password = rs.getString("password");
               String nick = rs.getString("nick");
               users.add(new UserData(login,password,nick));
           }
       }
       catch (SQLException e){
           throw new RuntimeException(e);
       }

    }

}
