package com.nationsrpg.plugin.core.data;

import me.lucko.helper.gson.GsonProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class DataItem {
  @NotNull private final ItemStack itemStack;
  @NotNull private final JavaPlugin plugin;

  public DataItem(final @NotNull ItemStack itemStack, final @NotNull JavaPlugin plugin) {
    this.itemStack = itemStack;
    this.plugin = plugin;
  }

  private PersistentDataContainer getPDC() {
    if (itemStack.getItemMeta() == null || !itemStack.hasItemMeta()) {
      throw new IllegalArgumentException(
          "Entity " + itemStack + " is not valid and does not have a PDC!");
    }

    return itemStack.getItemMeta().getPersistentDataContainer();
  }

  @NotNull
  public ItemStack getItemStack() {
    return itemStack;
  }

  public void set(@NotNull String key, @NotNull Object value) {
    getPDC()
        .set(
            new NamespacedKey(plugin, key),
            PersistentDataType.STRING,
            GsonProvider.standard().toJson(value));
  }

  @Nullable
  public <T> T get(@NotNull String key, @NotNull Class<T> type) {
    return GsonProvider.standard()
        .fromJson(getPDC().get(new NamespacedKey(plugin, key), PersistentDataType.STRING), type);
  }

  @Nullable
  public <T> T getOrDefault(@NotNull String key, @NotNull T t, @NotNull Class<T> type) {
    return has(key) ? get(key, type) : t;
  }

  public boolean has(@NotNull String key) {
    return getPDC().has(new NamespacedKey(plugin, key), PersistentDataType.STRING);
  }

  @NotNull
  public Set<NamespacedKey> getKeys() {
    return getPDC().getKeys();
  }

  public void remove(@NotNull String key) {
    getPDC().remove(new NamespacedKey(plugin, key));
  }

  public boolean isEmpty() {
    return getPDC().isEmpty();
  }
}
