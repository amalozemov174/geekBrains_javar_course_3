package server;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatLogger {
    ClientHandler _clientHandler;
    FileInputStream fis;
    FileOutputStream fos;
    File log;

    ChatLogger(ClientHandler clientHandler){

        _clientHandler = clientHandler;
        log = new File("history_" + _clientHandler.getNick() + ".txt");
        if(log.exists()){
            try{
                fis = new FileInputStream(log);
                fos = new FileOutputStream(log, true);
            }
            catch (FileNotFoundException e){
                throw  new RuntimeException("Error of reading file", e);
            }
        }
        else{
            try {
                log.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try{
                fis = new FileInputStream(log);
                fos = new FileOutputStream(log, false);
            }
            catch (FileNotFoundException e){
                throw  new RuntimeException("Error of creation file", e);
            }
        }


    }

    public void write(String message){
        byte[] msg = (message + "\n").getBytes(StandardCharsets.UTF_8);
        try{
            fos.write(msg);

        }
        catch (IOException e){
            throw  new RuntimeException("Error of reading file", e);
        }

    }

    public String read(){
        byte[] buf = new byte[20];
            int count;

            StringBuilder tmp = new StringBuilder();
            try{
                while ((count = fis.read(buf)) > 0) {
                    for (int i = 0; i < count; i++) {
                        tmp.append((char) buf[i]);                    }
                }
            }
            catch (IOException e){
                throw  new RuntimeException("Error of reading file", e);
            }

        String[] mass = tmp.toString().split("\n");
        StringBuilder tmpRes = new StringBuilder();
        String [] res;
        if(mass.length > 100){
            res  = Arrays.copyOfRange(mass, mass.length - 1, mass.length - 101);
        }
        else {
            res  = Arrays.copyOf(mass, mass.length);
        }

        for (String s : res) {
            tmpRes.append(s + "\n");
        };

        return tmpRes.toString();
    }
}
