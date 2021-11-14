package com.nationsrpg.plugin.core.data;

import com.nationsrpg.plugin.core.helpers.FormatUtils;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

public enum Message {
  SERVER_RESTART_TITLE(FormatUtils.fromLegacyText("&cRestarting in...")),
  SERVER_RESTART_SUBTITLE(FormatUtils.fromLegacyText("&c%s more seconds!")),
  RESTART_REQUEST_CANCELLED(FormatUtils.fromLegacyText("&cRestart request was cancelled."));

  @NotNull private final TextComponent message;

  Message(@NotNull TextComponent message) {
    this.message = message;
  }

  @NotNull
  public TextComponent getMessage() {
    return message;
  }

  @NotNull
  public TextComponent getFormatted(@NotNull Object... replacements) {
    final TextComponent message = getMessage();

    return message.content(String.format(message.content(), replacements));
  }

  @Override
  public String toString() {
    return message.content();
  }
}
