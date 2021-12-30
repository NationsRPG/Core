package com.nationsrpg.plugin.core.models.user;

import org.jetbrains.annotations.NotNull;

public record UserSettings(@NotNull Boolean messagesEnabled) {
  @NotNull
  public static UserSettings defaultSettings() {
    return new UserSettings(true);
  }
}