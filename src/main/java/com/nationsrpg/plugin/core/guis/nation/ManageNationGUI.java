package com.nationsrpg.plugin.core.guis.nation;

import me.lucko.helper.menu.Gui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ManageNationGUI extends Gui {
  private ManageNationGUI(Player player, int lines, String title) {
    super(player, lines, title);
  }

  public static void open(@NotNull Player player) {
    new ManageNationGUI(player, 4, "&f&lCreate or join a nation.").open();
  }

  @Override
  public void redraw() {

  }
}
