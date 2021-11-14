package com.nationsrpg.plugin.core.maps;

import com.google.common.base.Preconditions;
import com.nationsrpg.plugin.core.api.map.SpawnMap;
import org.bukkit.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ClassicSpawnMap implements SpawnMap {
  @Override
  public void initialize() {
    if (!isWorldLoaded()) {
      Bukkit.createWorld(
          WorldCreator.name("spawn")
              .environment(World.Environment.NORMAL)
              .type(WorldType.NORMAL)
              .generateStructures(false));
    }
  }

  @Override
  public @NotNull World getWorld() {
    return Preconditions.checkNotNull(Bukkit.getWorld("spawn"));
  }

  @Override
  public boolean isWorldLoaded() {
    final File version = new File(getWorld().getWorldFolder(), "core-version.txt");

    try {
      return Bukkit.getWorld("spawn") != null
          && version.exists()
          && Files.readString(version.toPath(), StandardCharsets.UTF_8).equals("1.0");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Override
  public @NotNull Location getSpawnLocation() {
    return new Location(getWorld(), 0, 0, 0, 0, 0);
  }
}
