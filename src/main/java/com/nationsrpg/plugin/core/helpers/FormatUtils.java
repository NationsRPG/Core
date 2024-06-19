package com.nationsrpg.plugin.core.helpers;

import me.lucko.helper.text3.Text;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

public final class FormatUtils {
  private static final Pattern USERNAME_REGEX = Pattern.compile("^\\w{2,16}$");

  private FormatUtils() {}

  //  public static NamedTextColor getColor(@NotNull ChatColor color) {
  //    return NamedTextColor.nearestTo(TextColor.color(color.asBungee().getColor().getRGB()));
  //  }
  //
  //  public static TextDecoration getDecoration(@NotNull ChatColor color) {
  //    if(color.isColor()) {
  //      throw new IllegalArgumentException("ChatColor " + color + " is a color!");
  //    }
  //
  //    return color == ChatColor.MAGIC ? TextDecoration.OBFUSCATED :
  // TextDecoration.valueOf(color.name());
  //  }

  @NotNull
  public static TextComponent fromLegacyText(@NotNull String string) {
    return LegacyComponentSerializer.legacy(ChatColor.COLOR_CHAR)
        .deserialize(Text.colorize(string));
    //    string = Text.colorize(string);
    //    final TextComponent.Builder text = Component.text();
    //
    //    for (String s : string.split("" + ChatColor.COLOR_CHAR)) {
    //      final ChatColor color = ChatColor.getByChar(s.charAt(0));
    //
    //      if(color == null) {
    //        continue;
    //      }
    //
    //      final TextComponent.Builder sub = Component.text();
    //
    //      if(color.isFormat()) {
    //        sub.decorate(getDecoration(color));
    //      } else {
    //        sub.color(getColor(color));
    //      }
    //
    //      sub.content(s.substring(1));
    //      text.append(sub);
    //    }
    //
    //    return text.build();
  }

  @NotNull
  public static String[] colorize(@NotNull String... strings) {
    return Arrays.stream(strings).map(Text::colorize).toArray(String[]::new);
  }

  public static boolean isMinecraftUsername(@NotNull String str) {
    return USERNAME_REGEX.matcher(str).find();
  }

  @NotNull
  public static String getMaterialName(@NotNull Material material) {
    return WordUtils.capitalize(material.name().replace('_', ' ').toLowerCase(Locale.ENGLISH));
  }
}
