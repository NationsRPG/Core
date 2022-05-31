package com.nationsrpg.plugin.core.api.map;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface SpawnMap {
  void initialize(@NotNull NationsRPGPlugin plugin);

  @NotNull
  World getWorld();

  boolean isWorldLoaded();

  @NotNull
  Location getSpawnLocation();
}
