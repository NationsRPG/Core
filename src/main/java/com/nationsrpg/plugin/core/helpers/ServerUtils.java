package com.nationsrpg.plugin.core.helpers;

import com.nationsrpg.plugin.core.api.constants.Messages;
import me.lucko.helper.Schedulers;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicInteger;

public final class ServerUtils {
  private ServerUtils() {}

  public static void startServerRestart(int seconds) {
    AtomicInteger count = new AtomicInteger(seconds);

    MessageUtils.broadcastTitle(
        Messages.SERVER_RESTART_TITLE.build(), Messages.SERVER_RESTART_SUBTITLE.build(seconds));

    Schedulers.sync()
        .runRepeating(
            task -> {
              if (count.get() <= 5) {
                MessageUtils.broadcastTitle(
                    Messages.SERVER_RESTART_TITLE.build(),
                    Messages.SERVER_RESTART_SUBTITLE.build(count.get()));
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
