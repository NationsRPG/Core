package com.nationsrpg.plugin.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.guis.admin.AdminPanelGUI;
import org.bukkit.entity.Player;

@CommandAlias("admin")
@CommandPermission("nationsrpg.admin")
public class AdminCommand extends BaseCommand {
  @Dependency private NationsRPGPlugin plugin;

  @Default
  @Subcommand("gui")
  public void onAdminCommand(Player player) {
    AdminPanelGUI.open(plugin.getAddonManager(), player);
  }

  @Subcommand("holo")
  public void onHoloCommand(Player player, String... lines) {
    // TODO replace aikar with RedCommands cause its so much easier to use than this ugly garbage
  }
}
