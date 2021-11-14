package com.nationsrpg.plugin.core.helpers;

import com.nationsrpg.plugin.core.data.Message;
import me.lucko.helper.Schedulers;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicInteger;

public final class ServerUtils {
  public static void startServerRestart(int seconds) {
    AtomicInteger count = new AtomicInteger(seconds);

    MessageUtils.broadcastTitle(
        Message.SERVER_RESTART_TITLE.getMessage(),
        Message.SERVER_RESTART_SUBTITLE.getFormatted(seconds));

    Schedulers.sync()
        .runRepeating(
            task -> {
              if (count.get() <= 5) {
                MessageUtils.broadcastTitle(
                    Message.SERVER_RESTART_TITLE.getMessage(),
                    Message.SERVER_RESTART_SUBTITLE.getFormatted(count.get()));
              }

              if (count.get() <= 0) {
                Bukkit.spigot().restart();
                task.close();

                return;
              }

              count.decrementAndGet();
            },
            0L,
            20L);
  }
}
