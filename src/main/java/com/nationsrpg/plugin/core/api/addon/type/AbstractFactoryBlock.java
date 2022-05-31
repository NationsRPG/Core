package com.nationsrpg.plugin.core.api.addon.type;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.api.addon.AbstractTexturedBlockAddon;
import com.nationsrpg.plugin.core.data.DataItem;
import com.nationsrpg.plugin.core.helpers.FormatUtils;
import com.nationsrpg.plugin.core.helpers.HologramUtils;
import com.nationsrpg.plugin.core.helpers.ShapedRecipeBuilder;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.blockdata.DataBlock;
import redempt.redlib.blockdata.events.DataBlockDestroyEvent;

public abstract class AbstractFactoryBlock extends AbstractTexturedBlockAddon {
  @NotNull protected final Material production;

  protected AbstractFactoryBlock(
      @NotNull NationsRPGPlugin plugin,
      @NotNull String id,
      @NotNull String name,
      @NotNull Material production) {
    super(
        plugin,
        "factory_" + id,
        name,
        new String[] {
          "&7Place me to start creating the",
          "&7material: &b" + FormatUtils.getMaterialName(production) + "&7.",
          "&7",
          "&7Factory blocks work even while",
          "&7you aren't on the server.",
          "&7",
          "&7Be sure to place these in",
          "&7a safe location."
        },
        Material.COBBLESTONE,
        1);

    this.production = production;
  }

  @Override
  public @NotNull Recipe getRecipe() {
    return ShapedRecipeBuilder.builder(getId())
        .result(buildItemStack())
        .ingredients('0', Material.REDSTONE, '1', "item_gear", '2', production)
        .shape("010", "121", "010")
        .build();
  }

  @Override
  public void onPlaceTextured(
      @NotNull DataBlock data,
      @NotNull DataItem item,
      @NotNull BlockPlaceEvent event,
      @NotNull ArmorStand armorStand) {
    data.set("production", production.name());
    HologramUtils.createText(
        data.getBlock(), "&7Factory", "&7Production: &b" + FormatUtils.getMaterialName(production));
  }

  @Override
  public void onBreakTextured(
      @NotNull DataItem item,
      @NotNull DataBlockDestroyEvent event,
      @NotNull ArmorStand armorStand) {
    HologramUtils.deleteText(event.getBlock(), 2);
  }

  @Override
  public void onInteract(@NotNull DataBlock data, @NotNull PlayerInteractEvent event) {
    // TODO OPEN GUI
  }
}
