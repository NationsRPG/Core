package com.nationsrpg.plugin.core.api.constants;

import com.nationsrpg.plugin.core.helpers.FormatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public interface Messages {
  Component DOUBLE_ARROW_SEPARATOR = text("\u00bb", DARK_GRAY);
  Component ALERT_PREFIX_COMPONENT =
      text()
          .append(text("ALERT", GOLD, BOLD))
          .append(space())
          .append(DOUBLE_ARROW_SEPARATOR)
          .build();
  Component SUCCESS_PREFIX_COMPONENT =
      text()
          .append(text("SUCCESS", GREEN, BOLD))
          .append(space())
          .append(DOUBLE_ARROW_SEPARATOR)
          .build();
  Args1<String> ADDON_GUI_ADD =
      (item) ->
          success(
              text("Successfully added ", GREEN)
                  .append(FormatUtils.fromLegacyText(item))
                  .append(text(" into your inventory.", GREEN)));
  Component ERROR_PREFIX_COMPONENT =
      text()
          .append(text("ERROR", RED, BOLD))
          .append(space())
          .append(DOUBLE_ARROW_SEPARATOR)
          .build();
  Args0 ADDON_GUI_FULL =
      () -> error(text("Failed to add item because your inventory is full!", RED));
  Args0 SERVER_RESTART_TITLE = () -> text("Restarting in...", RED);
  Args1<Integer> SERVER_RESTART_SUBTITLE = (seconds) -> text(seconds + " seconds!", RED);
  Args0 RESTART_REQUEST_CANCELLED = () -> text("Restart request was cancelled.", RED);
  Args0 SERVER_START_KICK = () -> text("The server has reloaded. Please rejoin.", GREEN);
  Args1<String> WELCOME_BETA_JOIN =
      (name) ->
          text()
              .append(text("WELCOME", YELLOW, BOLD))
              .append(space())
              .append(DOUBLE_ARROW_SEPARATOR)
              .append(
                  text(
                      "Hey "
                          + name
                          + "! NationsRPG is currently in beta. Please join our Discord server: https://discord.nationsrpg.com",
                      YELLOW))
              .build();

  static TextComponent alert(ComponentLike component) {
    return text().append(ALERT_PREFIX_COMPONENT).append(space()).append(component).build();
  }

  static TextComponent success(ComponentLike component) {
    return text().append(SUCCESS_PREFIX_COMPONENT).append(space()).append(component).build();
  }

  static TextComponent error(ComponentLike component) {
    return text().append(ERROR_PREFIX_COMPONENT).append(space()).append(component).build();
  }

  interface Args0 {
    Component build();

    default void send(CommandSender sender) {
      sender.sendMessage(build());
    }
  }

  interface Args1<A0> {
    Component build(A0 arg0);

    default void send(CommandSender sender, A0 arg0) {
      sender.sendMessage(build(arg0));
    }
  }
}
