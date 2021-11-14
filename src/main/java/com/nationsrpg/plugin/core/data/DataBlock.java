package com.nationsrpg.plugin.core.data;

import me.lucko.helper.gson.GsonProvider;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class DataBlock implements PersistentDataContainer {
  @NotNull private final PersistentDataContainer pdc;
  @NotNull private final Chunk chunk;
  @NotNull private final NamespacedKey key;
  @NotNull private final JavaPlugin plugin;

  public DataBlock(final @NotNull Block block, final @NotNull JavaPlugin plugin) {
    this.chunk = block.getChunk();
    this.key = new NamespacedKey(plugin, getKey(block));
    this.pdc = getPersistentDataContainer();
    this.plugin = plugin;
  }

  @NotNull
  private static String getKey(@NotNull Block block) {
    final int x = block.getX() & 0x000F;
    final int y = block.getY() & 0x00FF;
    final int z = block.getZ() & 0x000F;
    return String.format("x%dy%dz%d", x, y, z);
  }

  public void clear() {
    pdc.getKeys().forEach(pdc::remove);
    save();
  }

  @NotNull
  private PersistentDataContainer getPersistentDataContainer() {
    final PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
    final PersistentDataContainer blockPDC;
    if (chunkPDC.has(key, PersistentDataType.TAG_CONTAINER)) {
      blockPDC = chunkPDC.get(key, PersistentDataType.TAG_CONTAINER);
      assert blockPDC != null;
      return blockPDC;
    }
    blockPDC = chunkPDC.getAdapterContext().newPersistentDataContainer();
    chunkPDC.set(key, PersistentDataType.TAG_CONTAINER, blockPDC);
    return blockPDC;
  }

  private void save() {
    if (pdc.isEmpty()) {
      chunk.getPersistentDataContainer().remove(key);
    } else {
      chunk.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, pdc);
    }
  }

  public void set(@NotNull String key, @NotNull Object value) {
    set(
        new NamespacedKey(plugin, key),
        PersistentDataType.STRING,
        GsonProvider.standard().toJson(value));
  }

  @Nullable
  public <T> T get(@NotNull String key, @NotNull Class<T> type) {
    return GsonProvider.standard()
        .fromJson(get(new NamespacedKey(plugin, key), PersistentDataType.STRING), type);
  }

  @Nullable
  public <T> T getOrDefault(@NotNull String key, @NotNull T t, @NotNull Class<T> type) {
    return has(key) ? get(key, type) : t;
  }

  public boolean has(@NotNull String key) {
    return has(new NamespacedKey(plugin, key), PersistentDataType.STRING);
  }

  @Override
  public <T, Z> void set(
      final @NotNull NamespacedKey namespacedKey,
      final @NotNull PersistentDataType<T, Z> persistentDataType,
      final @NotNull Z z) {
    pdc.set(namespacedKey, persistentDataType, z);
    save();
  }

  @Override
  public <T, Z> boolean has(
      final @NotNull NamespacedKey namespacedKey,
      final @NotNull PersistentDataType<T, Z> persistentDataType) {
    return pdc.has(namespacedKey, persistentDataType);
  }

  @Nullable
  @Override
  public <T, Z> Z get(
      final @NotNull NamespacedKey namespacedKey,
      final @NotNull PersistentDataType<T, Z> persistentDataType) {
    return pdc.get(namespacedKey, persistentDataType);
  }

  @NotNull
  @Override
  public <T, Z> Z getOrDefault(
      final @NotNull NamespacedKey namespacedKey,
      final @NotNull PersistentDataType<T, Z> persistentDataType,
      final @NotNull Z z) {
    return pdc.getOrDefault(namespacedKey, persistentDataType, z);
  }

  @NotNull
  @Override
  public Set<NamespacedKey> getKeys() {
    return pdc.getKeys();
  }

  @Override
  public void remove(final @NotNull NamespacedKey namespacedKey) {
    pdc.remove(namespacedKey);
    save();
  }

  @Override
  public boolean isEmpty() {
    return pdc.isEmpty();
  }

  @NotNull
  @Override
  public PersistentDataAdapterContext getAdapterContext() {
    return pdc.getAdapterContext();
  }
}
