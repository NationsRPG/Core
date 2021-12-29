package com.nationsrpg.plugin.core.managers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.models.user.User;
import me.byteful.lib.datastore.api.model.impl.JSONModelId;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class UserManager {
  @NotNull
  private static final LoadingCache<UUID, Optional<User>> cache = CacheBuilder.newBuilder()
      .maximumSize(100)
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .build(new CacheLoader<>() {
        @Override
        public @NotNull Optional<User> load(@NotNull UUID key) {
          return NationsRPGPlugin.getInstance().getDataStore().get(User.class, JSONModelId.of("id", key.toString()));
        }
      });

  @NotNull
  public Optional<User> getUser(@NotNull UUID uuid) {
    try {
      return cache.get(uuid);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  @NotNull
  public Optional<User> getUser(@NotNull Player player) {
    return getUser(player.getUniqueId());
  }
}
