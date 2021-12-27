package com.nationsrpg.plugin.core.maps;

import com.google.common.base.Preconditions;
import com.nationsrpg.plugin.core.api.map.SpawnMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.jetbrains.annotations.NotNull;

public class ClassicSpawnMap implements SpawnMap {
  @Override
  public void initialize() {
    if (!isWorldLoaded()) {
      Bukkit.createWorld(WorldCreator.name("spawn"));
    }
  }

  @Override
  public @NotNull World getWorld() {
    return Preconditions.checkNotNull(Bukkit.getWorld("spawn"));
  }

  @Override
  public boolean isWorldLoaded() {
    return Bukkit.getWorld("spawn") != null;
  }

  @Override
  public @NotNull Location getSpawnLocation() {
    return new Location(getWorld(), 0, 0, 0, 0, 0);
  }
}
