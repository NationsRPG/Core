package com.nationsrpg.plugin.core.guis.misc;

import com.nationsrpg.plugin.core.api.addon.AbstractItemStackAddon;
import com.nationsrpg.plugin.core.api.constants.Messages;
import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import com.nationsrpg.plugin.core.managers.AddonManager;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.paginated.PaginatedGuiBuilder;
import me.lucko.helper.text3.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.nationsrpg.plugin.core.api.constants.PaginatedSettings.*;

public class AddonGUI {
  public static void open(@NotNull AddonManager addonManager, @NotNull Player openFor) {
    PaginatedGuiBuilder.create()
        .title(Text.colorize("&fAddons"))
        .lines(LINES)
        .scheme(LAYOUT)
        .itemSlots(ITEM_INDEXES)
        .nextPageSlot(NEXT_ITEM_SLOT)
        .previousPageSlot(PREVIOUS_ITEM_SLOT)
        .nextPageItem(NEXT_PAGE_ITEM)
        .previousPageItem(PREVIOUS_PAGE_ITEM)
        .build(
            openFor,
            (gui) -> {
              final Player player = gui.getPlayer();
              final List<Item> items = new ArrayList<>();

              addonManager
                  .getAddons()
                  .values()
                  .forEach(
                      addon -> {
                        if (!(addon instanceof final AbstractItemStackAddon itemStackAddon)) {
                          return;
                        }

                        items.add(
                            Item.builder(itemStackAddon.buildItemStack())
                                .bind(
                                    () -> {
                                      if (InventoryUtils.isBukkitInventoryFull(player.getInventory())) {
                                        Messages.ADDON_GUI_FULL.send(player);
                                        gui.close();

                                        return;
                                      }

                                      player.getInventory().addItem(itemStackAddon.buildItemStack());
                                      Messages.ADDON_GUI_ADD.send(player, addon.getName());
                                    },
                                    ClickType.RIGHT,
                                    ClickType.LEFT)
                                .build());
                      });

              return items;
            })
        .open();
  }
}
