package com.nationsrpg.plugin.core.api.addon;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.data.DataItem;
import com.nationsrpg.plugin.core.helpers.FormatUtils;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.text3.Text;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractItemStackAddon implements Addon {
  @NotNull protected final NationsRPGPlugin plugin;
  @NotNull private final NamespacedKey id;
  @NotNull private final String name;
  @NotNull private final String[] lore;
  @NotNull private final Material material;
  private final int customModelData;

  protected AbstractItemStackAddon(
      @NotNull NationsRPGPlugin plugin,
      @NotNull String id,
      @NotNull String name,
      @NotNull String[] lore,
      @NotNull Material material,
      int customModelData) {
    this.plugin = plugin;
    this.id = new NamespacedKey(plugin, "item_" + id.toLowerCase().replace(" ", "_"));
    this.name = "&e" + name;
    this.lore = lore;
    this.material = material;
    this.customModelData = customModelData;
  }

  @NotNull
  public abstract Recipe getRecipe();

  @NotNull
  public ItemStack buildItemStack() {
    ItemStack item =
        ItemStackBuilder.of(getMaterial())
            .name(Text.colorize(getName()))
            .lore(FormatUtils.colorize(getLore()))
            .breakable(false)
            .hideAttributes()
            .transformMeta(meta -> meta.setCustomModelData(customModelData))
            .build();

    final DataItem data = new DataItem(item, plugin);
    data.set("id", getId().value());

    return data.getItemStack();
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
  public String[] getLore() {
    return lore;
  }

  @NotNull
  public Material getMaterial() {
    return material;
  }

  public int getCustomModelData() {
    return customModelData;
  }
}
