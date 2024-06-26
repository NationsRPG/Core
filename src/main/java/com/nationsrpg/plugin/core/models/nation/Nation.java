package com.nationsrpg.plugin.core.models.nation;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import me.byteful.lib.datastore.api.data.StoredGroup;
import me.byteful.lib.datastore.api.model.Model;
import me.byteful.lib.datastore.api.model.impl.JSONModelId;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@StoredGroup("nations")
public record Nation(@NotNull NationsRPGPlugin plugin, @NotNull UUID uuid, @NotNull UUID leader, @NotNull Double balance,
                     @NotNull Set<NationMember> members, @NotNull Boolean listed) implements Model {
  @NotNull
  public static Nation create(@NotNull NationsRPGPlugin plugin, @NotNull UUID leader) {
    final UUID uuid = UUID.randomUUID();
    final Nation nation = new Nation(plugin, uuid, leader, 0.0D, new HashSet<>(Collections.singleton(NationMember.create(leader, NationMember.MemberRank.LEADER))), false);
    nation.update();

    return nation;
  }

  @NotNull
  public Optional<Player> getLeaderPlayer() {
    return Players.get(leader);
  }

  public void update() {
    plugin.getDataStore().set(JSONModelId.of("id", uuid.toString()), this);
  }

  public void delete() {
    plugin.getDataStore().delete(Nation.class, JSONModelId.of("id", uuid.toString()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Nation nation = (Nation) o;
    return uuid.equals(nation.uuid) && leader.equals(nation.leader) && balance.equals(nation.balance) && members.equals(nation.members) && listed.equals(nation.listed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, leader, balance, members, listed);
  }

  @Override
  public String toString() {
    return "Nation{" +
        "uuid=" + uuid +
        ", leader=" + leader +
        ", balance=" + balance +
        ", members=" + members +
        ", listed=" + listed +
        '}';
  }
}
