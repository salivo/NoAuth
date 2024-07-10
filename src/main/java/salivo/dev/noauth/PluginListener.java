package salivo.dev.noauth;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.JDA;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;


public class PluginListener {
    private final ProxyServer proxy;
    private final JDA jda;
    private final DiscordBot discordBot;
    PlayersInRegister playersInRegister;
    InitConfig config;
    public PluginListener(ProxyServer proxy, JDA jda, DiscordBot discordBot, PlayersInRegister playersInRegister, InitConfig config) {
        this.proxy = proxy;
        this.jda = jda;
        this.discordBot=discordBot;
        this.playersInRegister = playersInRegister;
        this.config = config;
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        String playerName = event.getPlayer().getUsername();
        if (DataBase.getDiscordID(playerName).equals("-1")) {
            proxy.getPlayer(playerName).ifPresent(player -> player.sendMessage(Component.text("register with command /reg!")));
            playersInRegister.addPlayerToReg(playerName);
        }
        else if (DataBase.getDiscordID(playerName).equals("-2")) {
            proxy.getPlayer(playerName).ifPresent(player -> player.sendMessage(Component.text("internal Error! please contact with administration")));
            event.getPlayer().disconnect(Component.text("Internal error! try later"));
        } else {
            proxy.getPlayer(playerName).ifPresent(player -> player.sendMessage(Component.text(DataBase.getLastJoin(playerName))));
            discordBot.MessageUser(jda,DataBase.getDiscordID(playerName),"new join detected! please confirm or decline with commands: /accept /decline");
            playersInRegister.addPlayerToLog(playerName);
        }
    }
    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event){
        String[] command = event.getCommand().split(" ");
        if(event.getCommandSource() instanceof Player) {
            Player player = (Player) event.getCommandSource();
            if (playersInRegister.isPlayerOnLogin(player.getUsername()) || playersInRegister.isPlayerOnRegister(player.getUsername())) {
                if (!config.getConfig().AllowedCommands().contains(command[0])) {
                    event.getCommandSource().sendMessage(Component.text("u can't execute it"));
                    event.setResult(CommandExecuteEvent.CommandResult.denied());
                }
            }
        }
    }
    @Subscribe
    public void onPlayerChat(PlayerChatEvent event){
        if(event.getPlayer() != null) {
            if (playersInRegister.isPlayerOnLogin(event.getPlayer().getUsername()) || playersInRegister.isPlayerOnRegister(event.getPlayer().getUsername())) {
                event.getPlayer().sendMessage(Component.text("u need to register first"));
                event.setResult(PlayerChatEvent.ChatResult.denied());
            }
        }
    }

}