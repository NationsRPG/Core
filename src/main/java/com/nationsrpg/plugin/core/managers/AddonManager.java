package com.nationsrpg.plugin.core.managers;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.api.addon.AbstractBlockAddon;
import com.nationsrpg.plugin.core.api.addon.AbstractItemAddon;
import com.nationsrpg.plugin.core.api.addon.AbstractItemStackAddon;
import com.nationsrpg.plugin.core.api.addon.Addon;
import com.nationsrpg.plugin.core.data.DataItem;
import com.nationsrpg.plugin.core.helpers.Log;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.RedLib;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;
import redempt.redlib.blockdata.events.DataBlockDestroyEvent;
import redempt.redlib.blockdata.events.DataBlockMoveEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class AddonManager {
  @NotNull
  private static final String TEXTURE_PACK_URL = "https://drive.google.com/uc?export=download&id=131tUmDFr4339XBEzk2fedj9e_bXXZAQV",
                              TEXTURE_PACK_HASH = "fce206dfefc324c06591a72489aab1dab4264152".toLowerCase();

  @NotNull private final Map<String, Addon> addons = new HashMap<>();
  @NotNull private final BlockDataManager blockDataManager;

  public AddonManager(@NotNull NationsRPGPlugin plugin) {
    this.blockDataManager = BlockDataManager.createPDC(plugin, true, true);
    Schedulers.async()
        .runRepeating(blockDataManager::save, 0L, 20L)
        .bindWith(plugin);
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

              if (!data.has("id") || !addons.containsKey(data.get("id", String.class))) {
                return;
              }
              final Addon addon = addons.get(data.get("id", String.class));

              if (!(addon instanceof AbstractItemAddon)) {
                return;
              }
              if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                ((AbstractItemAddon) addon).onRightClick(data, e);
              } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                ((AbstractItemAddon) addon).onLeftClick(data, e);
              }
              e.setUseInteractedBlock(Event.Result.DENY);
              e.setUseItemInHand(Event.Result.DENY);
            })
        .bindWith(plugin);

    Events.subscribe(PlayerInteractEvent.class, EventPriority.HIGH)
        .filter(
            e ->
                e.useItemInHand() != Event.Result.DENY
                    && e.hasBlock()
                    && e.getClickedBlock() != null
                    && e.getAction() != Action.PHYSICAL
                    && !e.getAction().name().contains("LEFT"))
        .handler(
            e -> {
              final Block block = e.getClickedBlock();
              final DataBlock data = blockDataManager.getDataBlock(block, false);

              if (data == null || data.getString("id") == null) {
                return;
              }
              if(!addons.containsKey(data.getString("id"))) {
                data.clear();

                return;
              }
              final Addon addon = addons.get(data.getString("id"));

              if (addon instanceof AbstractBlockAddon) {
                ((AbstractBlockAddon) addon).onInteract(data, e);
                e.setUseInteractedBlock(Event.Result.DENY);
                e.setUseItemInHand(Event.Result.DENY);
              }
            })
        .bindWith(plugin);

    Events.subscribe(BlockPlaceEvent.class, EventPriority.HIGHEST)
        .filter(e -> !e.isCancelled())
        .handler(
            e -> {
              final Block block = e.getBlockPlaced();
              final ItemStack item = e.getItemInHand();
              final DataItem dataItem = new DataItem(item, plugin);

              if (!dataItem.has("id") || !addons.containsKey(dataItem.get("id", String.class))) {
                return;
              }
              final DataBlock data = blockDataManager.getDataBlock(block, true);
              if (!data.getData().isEmpty()) {
                data.clear();
              }
              data.set("id", Objects.requireNonNull(dataItem.get("id", String.class)));
              final Addon addon = addons.get(dataItem.get("id", String.class));

              if (addon instanceof AbstractBlockAddon) {
                ((AbstractBlockAddon) addon).onPlace(data, dataItem, e);
              }
            })
        .bindWith(plugin);

    Events.subscribe(DataBlockDestroyEvent.class, EventPriority.HIGHEST)
        .filter(e -> !e.isCancelled())
        .handler(
            e -> {
              final DataBlock data = e.getDataBlock();

              if (data == null || data.getData().isEmpty() || data.getString("id") == null) {
                return;
              }

              if (!addons.containsKey(data.getString("id"))) {
                data.clear();

                return;
              }
              final Addon addon = addons.get(data.getString("id"));

              if (!(addon instanceof AbstractBlockAddon blockAddon)) {
                return;
              }
              final DataItem item = new DataItem(blockAddon.buildItemStack(), plugin);
              blockAddon.onBreak(item, e);
              data.clear();
              if(e.getParent() instanceof final BlockBreakEvent ev) {
                ev.setDropItems(false);
              }
              e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), item.getItemStack());
            })
        .bindWith(plugin);

    Events.subscribe(DataBlockMoveEvent.class, EventPriority.LOWEST)
        .handler(DataBlockMoveEvent::cancelParent)
        .bindWith(plugin);

    Events.subscribe(PlayerJoinEvent.class, EventPriority.LOWEST)
      .handler(e -> e.getPlayer().setResourcePack(TEXTURE_PACK_URL, TEXTURE_PACK_HASH, true))
      .bindWith(plugin);
  }

  public void registerAll(@NotNull NationsRPGPlugin plugin) {
    List<Class<? extends Addon>> list = RedLib.getExtendingClasses(plugin, Addon.class);
    for (Class<?> clazz : list) {
      try {
        Constructor<?> constructor = clazz.getDeclaredConstructor(NationsRPGPlugin.class);
        constructor.setAccessible(true);
        Addon addon = (Addon) constructor.newInstance(plugin);
        addons.put(addon.getId().value(), addon);
      } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new IllegalStateException("Class " + clazz.getName() + " does not have a valid constructor or could not be loaded", e);
      }
    }
    for (Addon addon : addons.values()) {
      if(addon instanceof AbstractItemStackAddon itemAddon) {
        if (!Bukkit.addRecipe(itemAddon.getRecipe())) {
          Log.warn("Failed to add recipe: " + addon.getId());
        }
      }
    }
  }

  public BlockDataManager getBlockDataManager() {
    return blockDataManager;
  }

  @NotNull
  public Map<String, Addon> getAddons() {
    return addons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddonManager that = (AddonManager) o;
    return addons.equals(that.addons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addons);
  }

  @Override
  public String toString() {
    return "AddonManager{" + "addonMap=" + addons + '}';
  }
}
