package salivo.dev.noauth;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.List;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;



public final class RegCommand implements SimpleCommand {
    ProxyServer proxy;
    Configurator config;
    MiniMessage mm;
    GenerateCode generateCode_inner;
    PlayersInRegister playersInRegister;
    RegCommand(ProxyServer proxy, MiniMessage mm, InitConfig config_inner, GenerateCode generateCode_inner, PlayersInRegister playersInRegister){
        this.proxy = proxy;
        this.config = config_inner.getConfig();
        this.mm = mm;
        this.generateCode_inner=generateCode_inner;
        this.playersInRegister = playersInRegister;
    }
    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        if(invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();
            if (DataBase.getDiscordID(player.getUsername()).equals("-1")) {
                String[] args = invocation.arguments();
                if (args.length == 0) {
                    String clickMessage = String.format(
                            "<hover:show_text:'%s'><blue><u><click:open_url:'%s'>%s</click></u></blue><reset>",
                            config.BotHoverText(), config.BotUrl(), config.BotName()
                    );
                    source.sendMessage(mm.deserialize(String.format(config.how_register_text(), clickMessage)));
                } else {
                    if (this.generateCode_inner.getTempTokens().containsKey(args[0])) {
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = dateFormat.format(currentDate);

                        DataBase.addNewUser(
                                player.getUsername(),
                                this.generateCode_inner.getTempTokens().get(args[0]).toString(),
                                formattedDate, player.getRemoteAddress().getAddress().toString()
                        );
                        playersInRegister.removePlayerFromReg(player.getUsername());
                        if (this.proxy.getServer("game").isPresent()) {
                            player.createConnectionRequest(this.proxy.getServer("game").get()).connect();
                            source.sendMessage(mm.deserialize("Successfully registered!"));
                        }

                    } else {
                        source.sendMessage(mm.deserialize("Wrong Code!"));
                    }
                }
            }
            else {
                if (playersInRegister.isPlayerOnLogin(player.getUsername()))
                    source.sendMessage(Component.text("You already registered! Execute /accept in discord"));
                else
                    source.sendMessage(Component.text("You already registered!"));
            }
        }
        else {
            source.sendMessage(mm.deserialize("Only player can execute it!"));
        }
        // Get the arguments after the command alias


    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.supplyAsync(()-> {
            if (invocation.arguments().length <= 1){
                return List.of("[Code]");
            } else {
                return List.of();
            }
        });
    }
}
