package com.nationsrpg.plugin.core.managers;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.addons.item.GearAddon;
import com.nationsrpg.plugin.core.api.addon.AbstractBlockAddon;
import com.nationsrpg.plugin.core.api.addon.AbstractItemAddon;
import com.nationsrpg.plugin.core.api.addon.Addon;
import com.nationsrpg.plugin.core.data.DataBlock;
import com.nationsrpg.plugin.core.data.DataItem;
import me.lucko.helper.Events;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
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
    register(new GearAddon(plugin));

    Events.subscribe(PlayerInteractEvent.class, EventPriority.HIGHEST)
        .filter(
            e ->
                e.useItemInHand() != Event.Result.DENY
                    && e.hasItem()
                    && e.getItem() != null
                    && e.getAction() != Action.PHYSICAL)
        .handler(
            e -> {
              final ItemStack item = e.getItem();
              final Action action = e.getAction();
              final DataItem data = new DataItem(Objects.requireNonNull(item), plugin);

              if (data.has("id") && addonMap.containsKey(data.get("id", String.class))) {
                final Addon addon = addonMap.get(data.get("id", String.class));

                if (addon instanceof AbstractItemAddon) {
                  if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    ((AbstractItemAddon) addon).onRightClick(data, e);
                  } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                    ((AbstractItemAddon) addon).onLeftClick(data, e);
                  }
                  e.setUseInteractedBlock(Event.Result.DENY);
                  e.setUseItemInHand(Event.Result.DENY);
                }
              }
            })
        .bindWith(plugin);

    Events.subscribe(PlayerInteractEvent.class, EventPriority.HIGH)
        .filter(
            e ->
                e.useItemInHand() != Event.Result.DENY
                    && e.hasBlock()
                    && e.getClickedBlock() != null)
        .handler(
            e -> {
              final Block block = e.getClickedBlock();
              final DataBlock data = new DataBlock(Objects.requireNonNull(block), plugin);

              if (data.has("id") && addonMap.containsKey(data.get("id", String.class))) {
                final Addon addon = addonMap.get(data.get("id", String.class));

                if (addon instanceof AbstractBlockAddon) {
                  ((AbstractBlockAddon) addon).onInteract(data, e);
                  e.setUseInteractedBlock(Event.Result.DENY);
                  e.setUseItemInHand(Event.Result.DENY);
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
              final DataItem dataItem = new DataItem(item, plugin);

              if (!data.isEmpty()) {
                data.clear();
              }

              if (dataItem.has("id") && addonMap.containsKey(dataItem.get("id", String.class))) {
                data.set("id", Objects.requireNonNull(dataItem.get("id", String.class)));
                final Addon addon = addonMap.get(dataItem.get("id", String.class));

                if (addon instanceof AbstractBlockAddon) {
                  ((AbstractBlockAddon) addon).onPlace(data, dataItem, e);
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
