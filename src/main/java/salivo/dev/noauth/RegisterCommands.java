package salivo.dev.noauth;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.nio.file.Path;

public class RegisterCommands {
    public static void registerRegCommand(NoAuth plugin, ProxyServer proxy, MiniMessage mm, InitConfig config_inner, GenerateCode generateCode_inner, PlayersInRegister playersInRegister){
        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta RegistercommandMeta = commandManager.metaBuilder("reg")
                .aliases("register", "r")
                .plugin(plugin)
                .build();
        commandManager.register(RegistercommandMeta, new RegCommand(proxy, mm, config_inner, generateCode_inner, playersInRegister));

    }
    public static void registerAdminCommand(NoAuth plugin, ProxyServer proxy, MiniMessage mm, InitConfig config_inner, Path path){
        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta RegistercommandMeta = commandManager.metaBuilder("noauth")
                .aliases("noauth:na", "na")
                .plugin(plugin)
                .build();
        commandManager.register(RegistercommandMeta, new AdminCommand(plugin, proxy, mm, config_inner, path));

    }
}
