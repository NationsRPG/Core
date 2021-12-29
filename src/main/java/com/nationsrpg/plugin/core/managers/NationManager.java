package com.nationsrpg.plugin.core.managers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.models.nation.Nation;
import me.byteful.lib.datastore.api.model.impl.JSONModelId;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class NationManager {
  @NotNull
  private static final LoadingCache<UUID, Optional<Nation>> cacheById = CacheBuilder.newBuilder()
      .maximumSize(50)
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .build(new CacheLoader<>() {
        @Override
        public @NotNull Optional<Nation> load(@NotNull UUID key) {
          return NationsRPGPlugin.getInstance().getDataStore().get(Nation.class, JSONModelId.of("id", key.toString()));
        }
      });

  @NotNull
  private static final LoadingCache<UUID, Optional<Nation>> cacheByLeader = CacheBuilder.newBuilder()
      .maximumSize(50)
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .build(new CacheLoader<>() {
        @Override
        public @NotNull Optional<Nation> load(@NotNull UUID key) {
          return NationsRPGPlugin.getInstance().getDataStore().get(Nation.class, JSONModelId.of("leader", key.toString()));
        }
      });

  @NotNull
  public Optional<Nation> getNation(@NotNull UUID uuid) {
    try {
      return cacheById.get(uuid);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  @NotNull
  public Optional<Nation> getNationFromLeader(@NotNull UUID leader) {
    try {
      return cacheByLeader.get(leader);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }
}
