package salivo.dev.noauth;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.List;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;


public final class AdminCommand implements SimpleCommand {
    NoAuth plugin;
    ProxyServer proxy;
    MiniMessage mm;
    Path path;
    InitConfig config_inner;
    AdminCommand(NoAuth plugin, ProxyServer proxy, MiniMessage mm, InitConfig config_inner, Path path){
        this.plugin = plugin;
        this.proxy = proxy;
        this.mm = mm;
        this.config_inner = config_inner;
        this.path = path;
    }
    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        // Get the arguments after the command alias
        String[] args = invocation.arguments();
        if (args.length > 0) {
            if (Objects.equals(args[0], "help"))
                source.sendMessage(mm.deserialize("Help /na to reload"));
            if (Objects.equals(args[0], "reload")) {
                try {
                    config_inner.Init(path, proxy);
                } catch (IOException | ObjectMappingException e) {
                    throw new RuntimeException(e);
                }
            }
                source.sendMessage(mm.deserialize("reloaded"));

        } else {
            source.sendMessage(mm.deserialize("NoAuth plugin Snapshot bla bla \n /na help to help"));
        }


    }

    /*
     This method allows you to control who can execute the command.
     If the executor does not have the required permission,
     the execution of the command and the control of its autocompletion
     will be sent directly to the server on which the sender is located
     */
    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("noauth.admin");
    }

    // With this method you can control the suggestions to send
    // to the CommandSource according to the arguments
    // it has already written or other requirements you need
    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    // Here you can offer argument suggestions in the same way as the previous method,
    // but asynchronously. It is recommended to use this method instead of the previous one
    // especially in cases where you make a more extensive logic to provide the suggestions
    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.supplyAsync(()-> {
            if (invocation.arguments().length <= 1){
                return List.of("help", "reload");
            } else {
                return List.of();
            }
        });
    }
}