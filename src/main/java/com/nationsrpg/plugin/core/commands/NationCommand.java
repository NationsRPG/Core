package com.nationsrpg.plugin.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.guis.nation.CreateJoinNationGUI;
import com.nationsrpg.plugin.core.guis.nation.ManageNationGUI;
import org.bukkit.entity.Player;

@CommandAlias("nation")
public class NationCommand extends BaseCommand {
  @Dependency private NationsRPGPlugin plugin;

  @Default
  public void onNationCommand(Player player) {
    plugin
        .getUserManager()
        .getUser(player)
        .ifPresent(
            user -> {
              if (user.nationUUID() != null) {
                ManageNationGUI.open(player);
              } else {
                CreateJoinNationGUI.open(player);
              }
            });
  }
}
