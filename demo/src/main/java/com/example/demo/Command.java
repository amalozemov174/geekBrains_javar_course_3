package com.example.demo;

import java.util.HashMap;
import java.util.Map;

public enum Command {
    AUTH("/auth"){
        @Override
        public String[] parse(String commandText) {
            final String[] split = commandText.split(COMMAND_DELIMETER);
            return new String[]{split[1], split[2]};
        }
    },
    AUHOK("/authok") {
        @Override
        public String[] parse(String commandText) {
            return new String[]{commandText.split(COMMAND_DELIMETER)[1]};
        }
    },
    PRIVATE_MESSAGE("/w") {
        @Override
        public String[] parse(String commandText) {
            final String[] split = commandText.split(COMMAND_DELIMETER, 3);
            final String nick = split[1];
            final String msg = split[2];
            return new String[]{nick, msg};
        }
    },
    END("/end") {
        @Override
        public String[] parse(String commandText) {
            return new String[0];
        }
    },
    ERROR("/error") {
        @Override
        public String[] parse(String commandText) {
            final String errorMsg = commandText.split(COMMAND_DELIMETER, 2)[1];
            return new String[]{errorMsg};
        }
    },
    CLIENTS("/clients") {
        @Override
        public String[] parse(String commandText) {
            final String[] split = commandText.split(COMMAND_DELIMETER);
            final String[] nicks = new String[split.length - 1];
            for(int i = 1; i < split.length; i++){
                nicks[i - 1] = split[i];
            }
            return nicks;
        }
    },
    TIMEOUT("/timeout") {
        @Override
        public String[] parse(String commandText) {
            final String errorMsg = commandText.split(COMMAND_DELIMETER, 2)[1];
            return new String[]{errorMsg};
        }
    },
    CHANGENICK("/changenick") {
        @Override
        public String[] parse(String commandText) {
            final String[] split = commandText.split(COMMAND_DELIMETER);
            final String[] comand = new String[split.length - 1];
            for(int i = 1; i < split.length; i++){
                comand[i - 1] = split[i];
            }
            return comand;
        }
    };

    private static final Map<String,Command> map = new HashMap<>(){{
        put("/auth", Command.AUTH);
        put("/authok", Command.AUHOK);
        put("/w", Command.PRIVATE_MESSAGE);
        put("/end", Command.END);
        put("/error", Command.ERROR);
        put("/clients", Command.CLIENTS);
        put("/timeout", Command.TIMEOUT);
        put("/changenick", Command.CHANGENICK);
    }};

    private String command;
    private String[] params = new String[0];

    static final String COMMAND_DELIMETER = "\\s+";
    Command(String command){
        this.command = command;
    }

    public static boolean isCommand(String message){
        return message.startsWith("/");
    }

    public String[] getParams(){
        return params;
    }

    public String getCommand(){
        return command;
    }

    public static Command getCommand(String message){
        message = message.trim();
        if(!isCommand(message)){
            throw new RuntimeException("'" + message + "' is not command");
        }
        final int index = message.indexOf(" ");
        final String cmd = index > 0 ? message.substring(0, index) : message;

        return map.get(cmd);
    }

    public abstract String[] parse(String commandText);

    public String collectMessage(String[] params) {
        final  String command = this.getCommand();
        final String join = String.join(" ", params);
        return command + (params == null ? "" : " " + join);

    }

    public String collectMessage(String nick, String message) {
        return this.getCommand()+ " " + nick + message;

    }
}

