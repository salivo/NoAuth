package salivo.dev.noauth;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.JDA;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "noauth",
        name = "NoAuth",
        version = "1.0-SNAPSHOT",
        description = "Plugin for no password auth",
        url = "https://github.com/salivo",
        authors = {"salivo"}
)

public class NoAuth {
    @Inject
    private ProxyServer proxy;
    @Inject
    private Logger logger;
    @Inject
    @DataDirectory
    private Path path;
    @Inject
    private EventManager eventManager;
    private final MiniMessage mm;
    private Configurator configuration;
    public NoAuth(){
        this.mm = MiniMessage.miniMessage();
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException, ObjectMappingException {
        // Starting Staff
        proxy.getConsoleCommandSource().sendMessage(
                mm.deserialize("<aqua>Enabling</aqua> <gradient:red:blue>NoAuth</gradient>"));
        // Starting bot

        PlayersInRegister playersInRegister = new PlayersInRegister();

        GenerateCode generateCode_inner = new GenerateCode();

        DiscordBot discordBot = new DiscordBot("SECRET_HERE", generateCode_inner, proxy, playersInRegister);
        JDA jda = discordBot.Start();
        // Load Config
        InitConfig config_inner = new InitConfig();
        config_inner.Init(path,proxy);

        logger.info(config_inner.getConfig().AllowedCommands().toString());

        DataBase.init(logger);


        // Register Event Listener
        proxy.getEventManager().register(this, new PluginListener(proxy, jda, discordBot, playersInRegister, config_inner));


        // Register Commands
        RegisterCommands.registerRegCommand(this, proxy, mm, config_inner, generateCode_inner, playersInRegister);

        // TODO: reload plugin
        // RegisterCommands.registerAdminCommand(this, proxy, mm, config_inner, path);
    }
}
