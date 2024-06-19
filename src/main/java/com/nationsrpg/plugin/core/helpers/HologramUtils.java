package com.nationsrpg.plugin.core.helpers;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.misc.Hologram;

public final class HologramUtils {
  public static void createText(@NotNull Block block, @NotNull String... lines) {
    Hologram.create(
        block.getLocation().clone().add(0.5, calcY(lines.length), 0.5),
        FormatUtils.colorize(lines));
  }

  public static void deleteText(@NotNull Block block, int lines) {
    final Hologram holo = Hologram.getAt(block.getLocation().clone().add(0.5, calcY(lines), 0.5));
    if (holo != null) {
      holo.clear();
    }
  }

  private static double calcY(int lines) {
    return Math.log(lines * 1.0D) + 0.8D;
  }
}
