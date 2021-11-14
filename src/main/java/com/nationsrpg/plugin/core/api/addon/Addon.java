package com.nationsrpg.plugin.core.api.addon;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface Addon {
  @NotNull
  NamespacedKey getId();

  @NotNull
  String getName();

  @NotNull
  String[] getLore();

  @NotNull
  Material getMaterial();

  int getCustomModelData();
}
