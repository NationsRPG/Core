package com.nationsrpg.plugin.core.guis.admin;

import com.nationsrpg.plugin.core.api.constants.Skulls;
import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ManagePlayerGUI extends Gui {
  private static final MenuScheme LAYOUT =
      new MenuScheme()
          .mask("000000000")
          .mask("000010000")
          .mask("000000000")
          .mask("000000000")
          .mask("000000000")
          .mask("000000000");

  private final Player target;

  public ManagePlayerGUI(Player player, int lines, String title, Player target) {
    super(player, lines, title);
    this.target = target;
  }

  public static void open(Player player, Player target) {
    new ManagePlayerGUI(player, 6, "&fManaging " + target.getName(), target).open();
  }

  @Override
  public void redraw() {
    final List<Item> items =
        Arrays.asList(
            ItemStackBuilder.of(Skulls.PLAYER.build(target))
                .name("&e" + target.getName())
                .build(() -> {}));

    InventoryUtils.populateBackground(this);
    final MenuPopulator populator = LAYOUT.newPopulator(this);
    items.forEach(populator::acceptIfSpace);
    InventoryUtils.populateTools(this, 6);
  }
}
