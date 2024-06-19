package com.nationsrpg.plugin.core.guis.admin;

import com.nationsrpg.plugin.core.api.constants.Messages;
import com.nationsrpg.plugin.core.guis.misc.AddonGUI;
import com.nationsrpg.plugin.core.guis.misc.ConfirmGUI;
import com.nationsrpg.plugin.core.guis.misc.PlayersGUI;
import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import com.nationsrpg.plugin.core.helpers.ServerUtils;
import com.nationsrpg.plugin.core.managers.AddonManager;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
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
    new AdminPanelGUI(addonManager, player, 4, "&fManagement Panel").open();
  }

  @Override
  public void redraw() {
    if (isFirstDraw()) {

      final ItemStackBuilder restartItem =
          ItemStackBuilder.of(Material.RED_WOOL)
              .name("&c&lRestart the server.")
              .lore("&7Left click me to", "&7restart the server in", "&730 seconds.")
              .enchant(Enchantment.PROTECTION_ENVIRONMENTAL)
              .flag(ItemFlag.HIDE_ENCHANTS);
      final List<Item> items =
          Arrays.asList(

              // Addon GUI
              ItemStackBuilder.of(Material.ANVIL)
                  .name("&f&lView addons.")
                  .lore(
                      "&7Left click me to",
                      "&7open a page of addons",
                      "&7that you can click to",
                      "&7receive in your inventory.")
                  .build(
                      () -> {
                        close();
                        AddonGUI.open(addonManager, getPlayer());
                      }),

              // Server Restart
              restartItem.build(
                  () -> {
                    close();

                    ConfirmGUI.open(
                        getPlayer(),
                        restartItem.build(),
                        (result) -> {
                          if (result) {
                            ServerUtils.startServerRestart(30);
                          } else {
                            Messages.RESTART_REQUEST_CANCELLED.send(getPlayer());
                          }
                        });
                  }),

              // Manage player
              ItemStackBuilder.of(Material.PLAYER_HEAD)
                  .name("&f&lManage a player.")
                  .lore(
                      "&7Left click me to",
                      "&7open a page of players",
                      "&7that you can click to",
                      "&7manage.")
                  .build(
                      () -> {
                        close();

                        PlayersGUI.open(
                            getPlayer(),
                            p -> {
                              ManagePlayerGUI.open(getPlayer(), p);
                            });
                      }));

      InventoryUtils.populateBackground(this);

      final MenuPopulator populator = LAYOUT.newPopulator(this);
      items.forEach(populator::acceptIfSpace);

      InventoryUtils.populateTools(this, 4);
    }
  }
}
