package com.nationsrpg.plugin.core.api.addon;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractItemAddon extends AbstractBasicAddon {
  protected AbstractItemAddon(
      @NotNull String id,
      @NotNull String name,
      @NotNull String[] lore,
      @NotNull Material material,
      int customModelData) {
    super(id, name, lore, material, customModelData);
  }

  public abstract void onRightClick(@NotNull NBTItem nbt, @NotNull PlayerInteractEvent event);

  public abstract void onLeftClick(@NotNull NBTItem nbt, @NotNull PlayerInteractEvent event);
}
