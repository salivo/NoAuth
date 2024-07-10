package salivo.dev.noauth;


import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.kyori.adventure.text.Component;

import java.util.Collections;

public class DiscordBot extends ListenerAdapter {
    private final GenerateCode generateCode_inner;
    private final String token;
    ProxyServer proxy;
    PlayersInRegister playersInRegister;
    public DiscordBot(String token, GenerateCode generateCode_inner, ProxyServer proxy, PlayersInRegister playersInRegister) {
        this.token = token;
        this.generateCode_inner = generateCode_inner;
        this.proxy = proxy;
        this.playersInRegister = playersInRegister;
    }
    public JDA Start()
    {
        JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .addEventListeners(this)
                .setActivity(Activity.playing("Minekampf"))
                .build();

        // Sets the global command list to the provided commands (removing all others)
        jda.updateCommands().addCommands(
                Commands.slash("get-code", "code for register"),
                Commands.slash("accept", "accept login"),
                Commands.slash("decline", "decline login")
        ).queue();
        return jda;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // make sure we handle the right command
        String nickname = DataBase.getNickname(event.getUser().getId());
        if (event.getName().equals("get-code")) {
            if (nickname.equals("-1")) {
                if (generateCode_inner.getTempTokens().containsValue(event.getUser().getId()))
                    event.reply("You Already GetCode").setEphemeral(true).queue();
                else {
                    String code = generateCode_inner.Generate(event.getUser().getId());
                    event.reply("Your Code:").setEphemeral(true) // reply or acknowledge
                            .flatMap(v ->
                                    event.getHook().editOriginalFormat("Your Code: `%s` \nNow you can type `/reg %s` on server", code, code) // then edit original
                            ).queue();
                }
            } else {
                event.reply("You Already Registered!").setEphemeral(true).queue();
            }
        }
        if (event.getName().equals("accept")) {
            if (nickname.equals("-1")) {
                event.reply("You are not registered. please register with /get-code").setEphemeral(true).queue();
            } else if (proxy.getPlayer(nickname).isEmpty()) {
                event.reply("You are not on server").setEphemeral(true).queue();
            } else {
                proxy.getPlayer(DataBase.getNickname(event.getUser().getId())).ifPresent(player -> player.sendMessage(Component.text("You are accepted")));
                event.reply("successfully accepted!").setEphemeral(true).queue();
                playersInRegister.removePlayerFromLog(nickname);
                // connect
                proxy.getServer("game").ifPresent(registeredServer -> proxy.getPlayer(nickname)
                        .ifPresent(player ->
                                player.createConnectionRequest(registeredServer).connect())
                );
            }
        }
        if (event.getName().equals("decline")) {
            if (nickname.equals("-1")) {
          # Compiled class file
*.class

# Log file
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files #
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# virtual machine crash logs, see http://www.java.com/en/download/help/error_hotspot.xml
hs_err_pid*
replay_pid*      event.reply("You are not registered. please register with /get-code").setEphemeral(true).queue();
            } else if (proxy.getPlayer(nickname).isEmpty()) {
                event.reply("You are not on server").setEphemeral(true).queue();
            } else {
                proxy.getPlayer(nickname).ifPresent(player -> player.disconnect(Component.text("You are kicked for Discord Reason")));
                playersInRegister.removePlayerFromLog(nickname);
                event.reply("successfully kicked from server!").setEphemeral(true).queue();
            }
        }
    }
    public void MessageUser(JDA jda, String discord_id, String message){
        User user = jda.retrieveUserById(discord_id).complete();

        PrivateChannel privateChannel = user.openPrivateChannel().complete();

        privateChannel.sendMessage(message).queue();
    }
}
