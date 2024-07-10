package salivo.dev.noauth;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.proxy.config.ProxyConfig;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public interface Configurator {

    static Configurator loadConfig(final Path path, ProxyConfig proxyConfig) throws IOException, ObjectMappingException {
        final Path configPath = loadFiles(path);
        ConfigurationLoader<CommentedConfigurationNode> loader =
                HoconConfigurationLoader.builder().setPath(configPath).build();
        ConfigurationNode rootNode = loader.load(ConfigurationOptions.defaults());


        String RegisterText = rootNode.getNode("register-message").getString("");
        String HowToRegisterText = rootNode.getNode("how-to-reg-message").getString("");
        String BotName = rootNode.getNode("bot-name").getString("");
        String BotUrl = rootNode.getNode("bot-url").getString("");
        String BotHoverText = rootNode.getNode("discord","bot-hover-text").getString("");
        List<String> AllowedCommands = rootNode.getNode("commands","allowed-commands").getList(TypeToken.of(String.class));

        return new Configurator() {
            @Override
            public String register_text(){
                return RegisterText;
            }
            @Override
            public String how_register_text(){
                return HowToRegisterText;
            }
            @Override
            public String BotName(){
                return BotName;
            }
            @Override
            public String BotUrl(){
                return BotUrl;
            }

            @Override
            public List<String> AllowedCommands() {
                return AllowedCommands;
            }

            @Override
            public String BotHoverText(){
                return BotHoverText;
            }
        };
    }
    private static Path loadFiles(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
        final Path configPath = path.resolve("config.conf");
        if (Files.notExists(configPath)) {
            try (var stream = Configurator.class.getClassLoader().getResourceAsStream("config.conf")) {
                Files.copy(Objects.requireNonNull(stream), configPath);
            }
        }
        return configPath;
    }

    String register_text();
    String how_register_text();
    String BotHoverText();
    String BotName();
    String BotUrl();
    List<String> AllowedCommands();
}
