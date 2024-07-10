package salivo.dev.noauth;

import com.velocitypowered.api.proxy.ProxyServer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Path;

public class InitConfig {
    private Configurator config;
    public InitConfig() {
        this.config = null; // Ініціалізуємо config як null при створенні об'єкта
    }
    public void Init(Path path, ProxyServer proxy) throws IOException, ObjectMappingException {
        this.config = Configurator.loadConfig(path, proxy.getConfiguration());
    }
    public Configurator getConfig(){
        return this.config;
    }
}
