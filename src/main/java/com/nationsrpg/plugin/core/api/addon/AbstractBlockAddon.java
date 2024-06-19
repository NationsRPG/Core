package com.nationsrpg.plugin.core.api.addon;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.data.DataItem;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.blockdata.DataBlock;
import redempt.redlib.blockdata.events.DataBlockDestroyEvent;

public abstract class AbstractBlockAddon extends AbstractItemStackAddon {
  protected AbstractBlockAddon(
      @NotNull NationsRPGPlugin plugin,
      @NotNull String id,
      @NotNull String name,
      @NotNull String[] lore,
      @NotNull Material material,
      int customModelData) {
    super(plugin, id, name, lore, material, customModelData);
  }

  public abstract void onPlace(
      @NotNull DataBlock data, @NotNull DataItem item, @NotNull BlockPlaceEvent event);

  public abstract void onBreak(@NotNull DataItem item, @NotNull DataBlockDestroyEvent event);

  public abstract void onInteract(@NotNull DataBlock data, @NotNull PlayerInteractEvent event);
}
