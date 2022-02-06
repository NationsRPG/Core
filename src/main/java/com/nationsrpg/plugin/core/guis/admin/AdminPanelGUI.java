package com.nationsrpg.plugin.core.guis.admin;

import com.nationsrpg.plugin.core.data.Message;
import com.nationsrpg.plugin.core.guis.misc.AddonGUI;
import com.nationsrpg.plugin.core.guis.misc.ConfirmGUI;
import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import com.nationsrpg.plugin.core.helpers.MessageUtils;
import com.nationsrpg.plugin.core.helpers.ServerUtils;
import com.nationsrpg.plugin.core.managers.AddonManager;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class AdminPanelGUI extends Gui {
  private static final MenuScheme LAYOUT =
      new MenuScheme().mask("000000000").mask("001010100").mask("000000000").mask("000000000");

  @NotNull private final AddonManager addonManager;

  private AdminPanelGUI(
      @NotNull AddonManager addonManager, Player player, int lines, String title) {
    super(player, lines, title);

    this.addonManager = addonManager;
  }

  public static void open(@NotNull AddonManager addonManager, @NotNull Player player) {
    new AdminPanelGUI(addonManager, player, 4, "&f&lManagement Panel").open();
  }

  @Override
  public void redraw() {
    if (isFirstDraw()) {

      final List<Item> items =
          Arrays.asList(

              // Addon GUI
              ItemStackBuilder.of(Material.ANVIL)
                  .build(
                      () -> {
                        close();
                        AddonGUI.open(addonManager, getPlayer());
                      }),

              // Server Restart
              ItemStackBuilder.of(Material.RED_WOOL)
                  .build(
                      () -> {
                        close();

                        ConfirmGUI.open(
                            getPlayer(),
                            (result) -> {
                              if (result) {
                                ServerUtils.startServerRestart(30);
                              } else {
                                MessageUtils.sendMessage(
                                    getPlayer(), Message.RESTART_REQUEST_CANCELLED);
                              }
                            });
                      }));

      InventoryUtils.populateBackground(this);

      final MenuPopulator populator = LAYOUT.newPopulator(this);
      items.forEach(populator::acceptIfSpace);

      InventoryUtils.populateTools(this, 4);
    }
  }
}
