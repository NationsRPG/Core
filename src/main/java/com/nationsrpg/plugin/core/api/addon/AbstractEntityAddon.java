package com.nationsrpg.plugin.core.api.addon;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.data.DataEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEntityAddon implements Addon {
  @NotNull
  private final NationsRPGPlugin plugin;
  @NotNull
  private final NamespacedKey id;
  @NotNull
  private final EntityType type;

  protected AbstractEntityAddon(
      @NotNull NationsRPGPlugin plugin,
      @NotNull String id,
      @NotNull EntityType type) {
    this.plugin = plugin;
    this.id = new NamespacedKey(plugin, "addon_" + id);
    this.type = type;
  }

  public abstract void applyCustomEntity(@NotNull DataEntity data);

  @NotNull
  public Entity spawn(@NotNull Location location) {
    final World world = location.getWorld();
    if (!location.isWorldLoaded() || world == null) {
      throw new IllegalArgumentException("World for location (" + location + ") is not valid!");
    }

    Entity entity = world.spawnEntity(location, type);
    final DataEntity data = new DataEntity(entity, plugin);
    applyCustomEntity(data);
    data.set("custom", true);
    data.set("id", getId().value());
    entity = data.getEntity();

    return entity;
  }

  @NotNull
  public EntityType getType() {
    return type;
  }

  @Override
  public @NotNull NamespacedKey getId() {
    return id;
  }
}
