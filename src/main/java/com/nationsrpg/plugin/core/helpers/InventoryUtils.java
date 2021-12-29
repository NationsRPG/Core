package com.nationsrpg.plugin.core.helpers;

import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.Slot;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public final class InventoryUtils {
  private static final Item
      BACKGROUND_ITEM =
      ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name("").lore("").hideAttributes().breakable(false).build(() -> {
      }),
      CLOSE_ITEM =
          ItemStackBuilder.of(Material.BARRIER)
              .name("&f&cClose")
              .lore("&7Click me to close this GUI.")
              .build(() -> {
              });

  public static void populateBackground(@NotNull Gui gui) {
    gui.fillWith(BACKGROUND_ITEM);
  }

  public static void populateTools(@NotNull Gui gui, @Range(from = 1, to = 6) int row) {
    setSlot(gui, 4, row, CLOSE_ITEM);
  }

  public static Slot getSlot(@NotNull Gui gui, @Range(from = 1, to = 9) int x, @Range(from = 1, to = 6) int y) {
    return gui.getSlot((x - 1) + ((y - 1) * 9));
  }

  public static void setSlot(@NotNull Gui gui, @Range(from = 1, to = 9) int x, @Range(from = 1, to = 6) int y, @NotNull Item item) {
    getSlot(gui, x, y).applyFromItem(item);
  }

  public static void fill(@NotNull Gui gui, @Range(from = 1, to = 9) int x1, @Range(from = 1, to = 6) int y1, @Range(from = 1, to = 9) int x2, @Range(from = 1, to = 6) int y2, @NotNull Item item) {
    for (int x = x1; x <= x2; x++) {
      for (int y = y1; y <= y2; y++) {
        setSlot(gui, x, y, item);
      }
    }
  }

  public static boolean isBukkitInventoryFull(@NotNull Inventory inventory) {
    return inventory.firstEmpty() == -1;
  }
}
