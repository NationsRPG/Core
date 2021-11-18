package com.nationsrpg.plugin.core.api.addon;

import com.nationsrpg.plugin.core.helpers.FormatUtils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.text3.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractBasicAddon implements Addon {
  @NotNull private final NamespacedKey id;
  @NotNull private final String name;
  @NotNull private final String[] lore;
  @NotNull private final Material material;
  private final int customModelData;

  protected AbstractBasicAddon(
      @NotNull String id,
      @NotNull String name,
      @NotNull String[] lore,
      @NotNull Material material,
      int customModelData) {
    this.id =
        new NamespacedKey(
            Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("NationsRPG")),
            "addon_" + id);
    this.name = name;
    this.lore = lore;
    this.material = material;
    this.customModelData = customModelData;
  }

  @Override
  @NotNull
  public ItemStack buildItemStack() {
    final NBTItem nbt =
        new NBTItem(
            ItemStackBuilder.of(getMaterial())
                .name(Text.colorize(getName()))
                .lore(FormatUtils.colorize(getLore()))
                .data(customModelData)
                .breakable(false)
                .flag(ItemFlag.HIDE_ATTRIBUTES)
                .build());
    nbt.setString("id", getId().value());

    return nbt.getItem();
  }

  @Override
  @NotNull
  public NamespacedKey getId() {
    return id;
  }

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  @NotNull
  @Override
  public String[] getLore() {
    return lore;
  }

  @NotNull
  @Override
  public Material getMaterial() {
    return material;
  }

  @Override
  public int getCustomModelData() {
    return customModelData;
  }
}
