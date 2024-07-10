package salivo.dev.noauth;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;

import javax.annotation.Nullable;
import java.time.Duration;

public class InfoSender {
    private final static InfoSender instance = new InfoSender();
    public static InfoSender getInstance(){
        return instance;
    }

    private @Nullable BossBar activeBar;


    public void sendTitle(Audience target) {
        final Component mainTitle = Component.text("This is the main title", NamedTextColor.WHITE, TextDecoration.BOLD);
        final Component subtitle = Component.text("This is the subtitle", NamedTextColor.GRAY);
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3000), Duration.ofMillis(1000));
        final Title title = Title.title(mainTitle,subtitle,times);
        target.showTitle(title);
    }
    public void sendBossbar(Audience target, String text, float progress) {
        final Component name = Component.text(text);
        this.activeBar =  BossBar.bossBar(name, progress, BossBar.Color.RED, BossBar.Overlay.PROGRESS);
        target.showBossBar(this.activeBar);
    }
}
