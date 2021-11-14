package com.nationsrpg.plugin.core.models.nation;

import me.byteful.lib.datastore.api.model.Model;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record Nation(@NotNull UUID uuid, @NotNull UUID leader) implements Model {
  @NotNull
  public Optional<Player> getLeaderPlayer() {
    return Players.get(leader);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Nation nation = (Nation) o;
    return uuid.equals(nation.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }

  @Override
  public String toString() {
    return "Nation{" +
        "uuid=" + uuid +
        '}';
  }
}
