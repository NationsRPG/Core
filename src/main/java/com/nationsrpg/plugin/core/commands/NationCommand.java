package com.nationsrpg.plugin.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import org.bukkit.entity.Player;

@CommandAlias("nation")
public class NationCommand extends BaseCommand {
  @Dependency
  private NationsRPGPlugin plugin;

  @Default
  public void onNationCommand(Player player) {
    // TODO: OPEN NATION GUI
  }
}
