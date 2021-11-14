package com.nationsrpg.plugin.core.addons.item;

import com.nationsrpg.plugin.core.api.addon.AbstractItemAddon;
import com.nationsrpg.plugin.core.api.addon.CraftableAddon;
import com.nationsrpg.plugin.core.helpers.ShapedRecipeBuilder;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

public class GearItemAddon extends AbstractItemAddon implements CraftableAddon {
  public GearItemAddon() {
    super("gear", "&f&lGear", new String[0], Material.STICK, 1);
  }

  @Override
  public @NotNull Recipe getRecipe() {
    return ShapedRecipeBuilder.of(getId())
        .result(buildItemStack())
        .shape(" 0 ", "010", " 0 ")
        .ingredients(
            '0', Material.IRON_NUGGET,
            '1', Material.STICK)
        .build();
  }

  @Override
  public void onRightClick(@NotNull NBTItem nbt, @NotNull PlayerInteractEvent event) {}

  @Override
  public void onLeftClick(@NotNull NBTItem nbt, @NotNull PlayerInteractEvent event) {}
}
