package com.nationsrpg.plugin.core.guis.misc;

import com.nationsrpg.plugin.core.addons.item.GearItemAddon;
import com.nationsrpg.plugin.core.api.addon.AbstractItemAddon;
import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddonGUI extends Gui {

  private static Map<Class<?>, AbstractItemAddon> itemAddons = new HashMap<>();
  static {
    itemAddons.put(GearItemAddon.class, new GearItemAddon());
  }

  private AddonGUI(Player player, int lines, String title) {
    super(player, lines, title);
  }

  @Override
  public void redraw() {

    if (isFirstDraw()) {

      AbstractItemAddon gearItem = itemAddons.get(GearItemAddon.class);
      final Item gearItemAddon =
          ItemStackBuilder.of(gearItem.getMaterial())
              .build(() -> {
                close();
                getPlayer().getInventory().addItem(gearItem.buildItemStack());
              });

      InventoryUtils.populateBackground(this);

      InventoryUtils.fill(1, 1, 1, 1, gearItemAddon, this);
    }
  }

  public static void open(@NotNull Player player) {
    final AddonGUI gui = new AddonGUI(player, 1, "&f&lItem Addons");

    gui.open();
  }
}
