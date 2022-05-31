package com.nationsrpg.plugin.core.helpers;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.Slot;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.UUID;

public final class InventoryUtils {
  private static final Item
      BACKGROUND_ITEM =
          ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
              .name("")
              .lore("")
              .hideAttributes()
              .breakable(false)
              .build(() -> {}),
      CLOSE_ITEM =
          ItemStackBuilder.of(Material.BARRIER)
              .name("&f&cClose")
              .lore("&7Click me to close this GUI.")
              .buildConsumer((e) -> e.getWhoClicked().closeInventory());

  private InventoryUtils() {}

  public static void populateBackground(@NotNull Gui gui) {
    gui.fillWith(BACKGROUND_ITEM);
  }

  public static void populateTools(@NotNull Gui gui, @Range(from = 1, to = 6) int row) {
    setSlot(gui, 5, row, CLOSE_ITEM);
  }

  public static Slot getSlot(
      @NotNull Gui gui, @Range(from = 1, to = 9) int x, @Range(from = 1, to = 6) int y) {
    return gui.getSlot((x - 1) + ((y - 1) * 9));
  }

  public static void setSlot(
      @NotNull Gui gui,
      @Range(from = 1, to = 9) int x,
      @Range(from = 1, to = 6) int y,
      @NotNull Item item) {
    getSlot(gui, x, y).applyFromItem(item);
  }

  public static void fill(
      @NotNull Gui gui,
      @Range(from = 1, to = 9) int x1,
      @Range(from = 1, to = 6) int y1,
      @Range(from = 1, to = 9) int x2,
      @Range(from = 1, to = 6) int y2,
      @NotNull Item item) {
    for (int x = x1; x <= x2; x++) {
      for (int y = y1; y <= y2; y++) {
        setSlot(gui, x, y, item);
      }
    }
  }

  public static boolean isBukkitInventoryFull(@NotNull Inventory inventory) {
    return inventory.firstEmpty() == -1;
  }

  @NotNull
  public static ItemStack skull(@NotNull String identifier) {
    final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
    final SkullMeta meta = (SkullMeta) item.getItemMeta();
    if (FormatUtils.isMinecraftUsername(identifier)) {
      Players.getOffline(identifier).ifPresent(meta::setOwningPlayer);
    } else {
      final PlayerProfile profile = Bukkit.createProfileExact(UUID.randomUUID(), null);
      profile.clearProperties();
      profile.setProperty(new ProfileProperty("textures", identifier));

      meta.setPlayerProfile(profile);
    }
    item.setItemMeta(meta);

    return item;
  }
}
