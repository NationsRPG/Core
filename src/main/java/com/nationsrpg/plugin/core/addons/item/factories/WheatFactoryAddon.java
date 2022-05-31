package com.nationsrpg.plugin.core.addons.item.factories;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.api.addon.type.AbstractFactoryBlock;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class WheatFactoryAddon extends AbstractFactoryBlock {
  protected WheatFactoryAddon(@NotNull NationsRPGPlugin plugin) {
    super(plugin, "wheat", "Wheat Factory", Material.WHEAT);
  }
}
