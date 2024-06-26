package com.nationsrpg.plugin.core.addons.item.materials;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.api.addon.AbstractItemAddon;
import com.nationsrpg.plugin.core.data.DataItem;
import com.nationsrpg.plugin.core.helpers.ShapedRecipeBuilder;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

public class GearAddon extends AbstractItemAddon {
  public GearAddon(@NotNull NationsRPGPlugin plugin) {
    super(
        plugin,
        "gear",
        "Gear",
        new String[] {"&7This item is used in", "&7various crafting recipes."},
        Material.STICK,
        1);
  }

  @Override
  public @NotNull Recipe getRecipe() {
    return ShapedRecipeBuilder.builder(getId())
        .result(buildItemStack())
        .shape(" 0 ", "010", " 0 ")
        .ingredients(
            '0', Material.IRON_NUGGET,
            '1', Material.STICK)
        .build();
  }

  @Override
  public void onRightClick(@NotNull DataItem data, @NotNull PlayerInteractEvent event) {}

  @Override
  public void onLeftClick(@NotNull DataItem data, @NotNull PlayerInteractEvent event) {}
}
