package com.nationsrpg.plugin.core.api.addon;

import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

public interface CraftableAddon extends Addon {
  @NotNull
  Recipe getRecipe();
}
