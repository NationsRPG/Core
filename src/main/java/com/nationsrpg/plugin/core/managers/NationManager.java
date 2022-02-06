package com.nationsrpg.plugin.core.managers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.models.nation.Nation;
import me.byteful.lib.datastore.api.model.impl.JSONModelId;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class NationManager {
  @NotNull
  private static final LoadingCache<UUID, Optional<Nation>> cacheById =
      CacheBuilder.newBuilder()
          .maximumSize(50)
          .expireAfterWrite(1, TimeUnit.MINUTES)
          .build(
              new CacheLoader<>() {
                @Override
                public @NotNull Optional<Nation> load(@NotNull UUID key) {
                  return NationsRPGPlugin.getInstance()
                      .getDataStore()
                      .get(Nation.class, JSONModelId.of("id", key.toString()));
                }
              });

  // I commented out all the stuff related to 'cacheByLeader' because I doubt we'd use it.

  //  @NotNull
  //  private static final LoadingCache<UUID, Optional<Nation>> cacheByLeader =
  // CacheBuilder.newBuilder()
  //      .maximumSize(50)
  //      .expireAfterWrite(1, TimeUnit.MINUTES)
  //      .build(new CacheLoader<>() {
  //        @Override
  //        public @NotNull Optional<Nation> load(@NotNull UUID key) {
  //          return NationsRPGPlugin.getInstance().getDataStore().get(Nation.class,
  // JSONModelId.of("leader", key.toString()));
  //        }
  //      });

  public NationManager(@NotNull NationsRPGPlugin plugin) {
    //    Players.forEach(p -> loadAsync(p.getUniqueId())); // Make sure plugin captures any
    // possible online players. Reloads should not be done, but fail-safes are put in place.
    //
    Events.subscribe(PlayerJoinEvent.class, EventPriority.LOW)
        .handler(
            e ->
                plugin
                    .getUserManager()
                    .getUser(e.getPlayer())
                    .ifPresent(
                        user -> {
                          if (user.nationUUID() == null) {
                            return;
                          }

                          Schedulers.async().run(() -> getNation(user.nationUUID()).orElseThrow());
                        }))
        .bindWith(plugin);
  }

  @NotNull
  public Optional<Nation> getNation(@NotNull UUID uuid) {
    try {
      return cacheById.get(uuid);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  //  @NotNull
  //  public Optional<Nation> getNationFromLeader(@NotNull UUID leader) {
  //    try {
  //      return cacheByLeader.get(leader);
  //    } catch (ExecutionException e) {
  //      e.printStackTrace();
  //    }
  //
  //    return Optional.empty();
  //  }
}
