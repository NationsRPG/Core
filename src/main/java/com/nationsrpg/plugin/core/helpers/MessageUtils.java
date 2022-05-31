package com.nationsrpg.plugin.core.helpers;

import me.lucko.helper.utils.Players;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MessageUtils {
  private MessageUtils() {}

  public static void sendTitle(
      @NotNull Player player, @NotNull Component main, @NotNull Component sub) {
    player.showTitle(Title.title(main, sub));
  }

  public static void broadcastTitle(@NotNull Component main, @NotNull Component sub) {
    Players.forEach(p -> sendTitle(p, main, sub));
  }
}
