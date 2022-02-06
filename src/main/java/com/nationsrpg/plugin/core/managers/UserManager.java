package com.nationsrpg.plugin.core.managers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.models.user.User;
import me.byteful.lib.datastore.api.model.impl.JSONModelId;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class UserManager {
  @NotNull
  private static final LoadingCache<UUID, Optional<User>> cache =
      CacheBuilder.newBuilder()
          .maximumSize(100)
          .expireAfterWrite(1, TimeUnit.MINUTES)
          .build(
              new CacheLoader<>() {
                @Override
                public @NotNull Optional<User> load(@NotNull UUID key) {
                  return NationsRPGPlugin.getInstance()
                      .getDataStore()
                      .get(User.class, JSONModelId.of("id", key.toString()));
                }
              });

  public UserManager(@NotNull NationsRPGPlugin plugin) {
    Players.forEach(
        p ->
            createOrLoadAsync(
                plugin, p.getUniqueId())); // Make sure plugin captures any possible online players.
    // Reloads should not be done, but fail-safes are put in
    // place.

    Events.subscribe(PlayerJoinEvent.class, EventPriority.LOWEST)
        .handler(e -> createOrLoadAsync(plugin, e.getPlayer().getUniqueId()))
        .bindWith(plugin);
  }

  private void createOrLoadAsync(@NotNull NationsRPGPlugin plugin, @NotNull UUID uuid) {
    Schedulers.async()
        .run(
            () -> {
              if (getUser(uuid).isEmpty()) {
                cache.put(uuid, Optional.of(User.create(plugin, uuid)));
              }
            });
  }

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
