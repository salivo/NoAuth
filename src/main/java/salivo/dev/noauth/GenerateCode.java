package salivo.dev.noauth;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GenerateCode {
    private Map<String, String> TempTokens = new HashMap<>();
    public String Generate(String discordName){
        String code;
        do {
            code = generateCode(6);
        } while (TempTokens.containsKey(code));
        this.TempTokens.put(code, discordName);
        return code;
    }
    private static String generateCode(int length) {
        String characters = "0123456789"; //ABCDEFGHIJKLMNOPQRSTUVWXYZ
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }

    public Map getTempTokens(){
        return this.TempTokens;
    }
    public void delTempToken(String discordName){
        this.TempTokens.remove(discordName);
    }
}
