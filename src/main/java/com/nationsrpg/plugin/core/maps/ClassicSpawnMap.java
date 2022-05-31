package com.nationsrpg.plugin.core.maps;

import com.google.common.base.Preconditions;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.api.map.SpawnMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class ClassicSpawnMap implements SpawnMap, Listener {
  @Override
  public void initialize(@NotNull NationsRPGPlugin plugin) {
    if (!isWorldLoaded()) {
      Bukkit.createWorld(WorldCreator.name("spawn"));
    }

    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerSpawn(PlayerSpawnLocationEvent e) {
    e.setSpawnLocation(getSpawnLocation());
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent e) {
    e.setRespawnLocation(getSpawnLocation());
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
    return new Location(getWorld(), 10000.5, 101.1, 10000.5, 0, 0);
  }
}
