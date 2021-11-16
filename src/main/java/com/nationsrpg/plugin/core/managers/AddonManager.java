package com.nationsrpg.plugin.core.managers;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.addons.item.GearItemAddon;
import com.nationsrpg.plugin.core.api.addon.AbstractBlockAddon;
import com.nationsrpg.plugin.core.api.addon.AbstractItemAddon;
import com.nationsrpg.plugin.core.api.addon.Addon;
import com.nationsrpg.plugin.core.data.DataBlock;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.lucko.helper.Events;
import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class AddonManager {
  @NotNull private final Map<String, Addon> addonMap = new HashMap<>();

  public AddonManager(@NotNull NationsRPGPlugin plugin) {
    register(new GearItemAddon());

    Events.subscribe(PlayerInteractEvent.class, EventPriority.HIGHEST)
        .filter(
            e ->
                /*!e.isCancelled()
                &&*/ e.hasItem()
                    && e.getItem() != null
                    && e.getAction() != Action.PHYSICAL)
        .handler(
            e -> {
              final ItemStack item = e.getItem();
              final Action action = e.getAction();
              final NBTItem nbt = new NBTItem(Objects.requireNonNull(item));

              if (nbt.hasKey("id") && addonMap.containsKey(nbt.getString("id"))) {
                final Addon addon = addonMap.get(nbt.getString("id"));

                if (addon instanceof AbstractItemAddon) {
                  if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    ((AbstractItemAddon) addon).onRightClick(nbt, e);
                  } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                    ((AbstractItemAddon) addon).onLeftClick(nbt, e);
                  }
                }
              }
            })
        .bindWith(plugin);

    Events.subscribe(PlayerInteractEvent.class, EventPriority.HIGHEST)
        .filter(e -> /*!e.isCancelled() && */ e.hasBlock() && e.getClickedBlock() != null)
        .handler(
            e -> {
              final Block block = e.getClickedBlock();
              final DataBlock data = new DataBlock(Objects.requireNonNull(block), plugin);

              if (data.has("id") && addonMap.containsKey(data.get("id", String.class))) {
                final Addon addon = addonMap.get(data.get("id", String.class));

                if (addon instanceof AbstractBlockAddon) {
                  ((AbstractBlockAddon) addon).onInteract(data, e);
                }
              }
            })
        .bindWith(plugin);

    Events.subscribe(BlockPlaceEvent.class, EventPriority.HIGHEST)
        .filter(e -> !e.isCancelled())
        .handler(
            e -> {
              final Block block = e.getBlockPlaced();
              final ItemStack item = e.getItemInHand();
              final DataBlock data = new DataBlock(block, plugin);
              final NBTItem nbt = new NBTItem(item);

              if (!data.isEmpty()) {
                data.clear();
              }

              if (nbt.hasKey("id") && addonMap.containsKey(nbt.getString("id"))) {
                data.set("id", nbt.getString("id"));
                final Addon addon = addonMap.get(nbt.getString("id"));

                if (addon instanceof AbstractBlockAddon) {
                  ((AbstractBlockAddon) addon).onPlace(data, nbt, e);
                }
              }
            })
        .bindWith(plugin);

    Events.subscribe(BlockBreakEvent.class, EventPriority.HIGHEST)
        .filter(e -> !e.isCancelled())
        .handler(
            e -> {
              final Block block = e.getBlock();
              final DataBlock data = new DataBlock(block, plugin);

              if (data.isEmpty() || !data.has("id")) {
                return;
              }

              if (addonMap.containsKey(data.get("id", String.class))) {
                final Addon addon = addonMap.get(data.get("id", String.class));

                if (addon instanceof AbstractBlockAddon) {
                  ((AbstractBlockAddon) addon).onBreak(data, e);
                }
              }
            })
        .bindWith(plugin);
  }

  public void register(@NotNull Addon... addons) {
    for (Addon addon : addons) {
      addonMap.put(addon.getId().value(), addon);
    }
  }

  @NotNull
  public Map<String, Addon> getAddons() {
    return addonMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddonManager that = (AddonManager) o;
    return addonMap.equals(that.addonMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addonMap);
  }

  @Override
  public String toString() {
    return "AddonManager{" + "addonMap=" + addonMap + '}';
  }
}
