package com.nationsrpg.plugin.core.data;

import me.lucko.helper.gson.GsonProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

public class DataItem {
  @NotNull private final ItemStack itemStack;
  @NotNull private final JavaPlugin plugin;

  public DataItem(final @NotNull ItemStack itemStack, final @NotNull JavaPlugin plugin) {
    this.itemStack = itemStack;
    this.plugin = plugin;
  }

  private PersistentDataContainer getPDC() {
    if (itemStack.getItemMeta() == null) {
      throw new IllegalArgumentException(
          "Item " + itemStack + " is not valid and does not have a PDC!");
    }

    return itemStack.getItemMeta().getPersistentDataContainer();
  }

  private void applyPDC(Consumer<PersistentDataContainer> consumer) {
    final ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) {
      throw new IllegalArgumentException(
          "Item " + itemStack + " is not valid and does not have a PDC!");
    }

    consumer.accept(meta.getPersistentDataContainer());
    itemStack.setItemMeta(meta);
  }

  @NotNull
  public ItemStack getItemStack() {
    return itemStack;
  }

  public void set(@NotNull String key, @NotNull Object value) {
    applyPDC(
        pdc ->
            pdc.set(
                new NamespacedKey(plugin, key),
                PersistentDataType.STRING,
                GsonProvider.standard().toJson(value)));
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
