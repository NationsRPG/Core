package com.nationsrpg.plugin.core.api.addon;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.data.DataEntity;
import com.nationsrpg.plugin.core.data.DataItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.blockdata.DataBlock;
import redempt.redlib.blockdata.events.DataBlockDestroyEvent;

import java.util.Collection;

public abstract class AbstractTexturedBlockAddon extends AbstractBlockAddon {
  protected AbstractTexturedBlockAddon(
      @NotNull NationsRPGPlugin plugin,
      @NotNull String id,
      @NotNull String name,
      @NotNull String[] lore,
      @NotNull Material material,
      int customModelData) {
    super(plugin, id, name, lore, material, customModelData);
  }

  @Override
  public final void onPlace(
      @NotNull DataBlock data, @NotNull DataItem item, @NotNull BlockPlaceEvent event) {
    final Location location = event.getBlock().getLocation().clone().add(0.5, 0, 0.5);
    final World world = location.getWorld();
    if (world == null) {
      return;
    }

    final ArmorStand stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
    stand.setVisible(false);
    stand.setGravity(false);
    stand.setMarker(true);
    stand.setCanTick(false);
    stand.setInvulnerable(true);
    stand.setItem(EquipmentSlot.HEAD, item.getItemStack());
    new DataEntity(stand, plugin).set("texture_holder", true);
    onPlaceTextured(data, item, event, stand);
  }

  @Override
  public final void onBreak(@NotNull DataItem item, @NotNull DataBlockDestroyEvent event) {
    final Location location = event.getBlock().getLocation().clone().add(0.5, 0, 0.5);
    final World world = location.getWorld();
    if (world == null) {
      return;
    }

    final Collection<ArmorStand> nearby =
        world.getNearbyEntitiesByType(ArmorStand.class, location, 1, 1, 1);
    if (nearby.isEmpty()) {
      return;
    }

    nearby.stream()
        .map(x -> new DataEntity(x, plugin))
        .filter(x -> x.has("texture_holder"))
        .forEach(
            x -> {
              onBreakTextured(item, event, (ArmorStand) x.getEntity());
              x.getEntity().remove();
            });
  }

  protected abstract void onPlaceTextured(
      @NotNull DataBlock data,
      @NotNull DataItem item,
      @NotNull BlockPlaceEvent event,
      @NotNull ArmorStand armorStand);

  protected abstract void onBreakTextured(
      @NotNull DataItem item, @NotNull DataBlockDestroyEvent event, @NotNull ArmorStand armorStand);
}
