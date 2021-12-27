package com.nationsrpg.plugin.core.guis.misc;

import com.nationsrpg.plugin.core.api.addon.AbstractItemStackAddon;
import com.nationsrpg.plugin.core.data.Message;
import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import com.nationsrpg.plugin.core.helpers.MessageUtils;
import com.nationsrpg.plugin.core.managers.AddonManager;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.paginated.PaginatedGuiBuilder;
import me.lucko.helper.text3.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddonGUI {
  //  private AddonGUI(Player player, int lines, String title) {
  //    super(player, lines, title);
  //  }
  //
  //  @Override
  //  public void redraw() {
  //    if (isFirstDraw()) {
  //      AbstractItemAddon gearItem = itemAddons.get(GearItemAddon.class);
  //      final Item gearItemAddon =
  //          ItemStackBuilder.of(gearItem.getMaterial())
  //              .build(() -> {
  //                close();
  //                getPlayer().getInventory().addItem(gearItem.buildItemStack());
  //              });
  //
  //      InventoryUtils.populateBackground(this);
  //
  //      InventoryUtils.fill(1, 1, 1, 1, gearItemAddon, this);
  //    }
  //  }

  public static void open(@NotNull AddonManager addonManager, @NotNull Player openFor) {
    //    final AddonGUI gui = new AddonGUI(player, 1, "&f&lItem Addons");
    //
    //    gui.open();

    PaginatedGuiBuilder.create()
        .title(Text.colorize("&f&lAddons"))
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
                        if (addon instanceof final AbstractItemStackAddon itemStackAddon) {
                          items.add(
                              Item.builder(itemStackAddon.buildItemStack())
                                  .bind(
                                      () -> {
                                        if (InventoryUtils.isBukkitInventoryFull(
                                            player.getInventory())) {
                                          MessageUtils.sendMessage(player, Message.ADDON_GUI_FULL);

                                          return;
                                        }

                                        player.getInventory().addItem(itemStackAddon.buildItemStack());
                                        MessageUtils.sendMessage(
                                            player, Message.ADDON_GUI_ADD, addon.getName());
                                      },
                                      ClickType.RIGHT,
                                      ClickType.LEFT)
                                  .build());
                        }
                      });

              return items;
            })
        .open();
  }
}
