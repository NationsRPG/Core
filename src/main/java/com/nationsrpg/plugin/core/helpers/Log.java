package com.nationsrpg.plugin.core.helpers;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import org.jetbrains.annotations.NotNull;

public final class Log {
  private Log() {}

  public static void info(@NotNull String str) {
    NationsRPGPlugin.getInstance().getLogger().info(str);
  }

  public static void warn(@NotNull String str) {
    NationsRPGPlugin.getInstance().getLogger().warning(str);
  }

  public static void error(@NotNull String str) {
    NationsRPGPlugin.getInstance().getLogger().severe(str);
  }
}
