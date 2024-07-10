package salivo.dev.noauth;

import java.util.ArrayList;

public class PlayersInRegister {
    ArrayList<String> playersOnRegister = new ArrayList<String>();
    ArrayList<String> playersOnLogin = new ArrayList<String>();
    PlayersInRegister(){
    }
    void addPlayerToReg(String name){
        playersOnRegister.add(name);
    }
    void addPlayerToLog(String name){
        playersOnLogin.add(name);
    }
    void removePlayerFromReg(String name){
        playersOnRegister.remove(name);
    }
    void removePlayerFromLog(String name){
        playersOnLogin.remove(name);
    }
    boolean isPlayerOnRegister(String name){
        return playersOnRegister.contains(name);
    }
    boolean isPlayerOnLogin(String name){
        return playersOnLogin.contains(name);
    }
}
