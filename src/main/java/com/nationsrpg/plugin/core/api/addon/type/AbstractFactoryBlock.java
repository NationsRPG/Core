package com.nationsrpg.plugin.core.api.addon.type;

import com.nationsrpg.plugin.core.api.addon.AbstractBlockAddon;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFactoryBlock extends AbstractBlockAddon {
  protected AbstractFactoryBlock(
      @NotNull String name,
      @NotNull Material block,
      @NotNull Material production,
      int customModelData) {
    super(
        "factory_" + name,
        name,
        new String[] {"&7This factory block produces " + production.name() + ".", "&7", "&7Rate:"},
        block,
        customModelData);
  }
}
