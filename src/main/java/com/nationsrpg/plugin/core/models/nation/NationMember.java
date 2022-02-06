package com.nationsrpg.plugin.core.models.nation;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public record NationMember(@NotNull UUID uuid, @NotNull MemberRank rank) {
  @NotNull
  public static NationMember create(@NotNull UUID uuid, @NotNull MemberRank rank) {
    return new NationMember(uuid, rank);
  }

  @NotNull
  public static NationMember create(@NotNull UUID uuid) {
    return create(uuid, MemberRank.CITIZEN);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NationMember that = (NationMember) o;
    return uuid.equals(that.uuid) && rank == that.rank;
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, rank);
  }

  @Override
  public String toString() {
    return "NationMember{" +
        "uuid=" + uuid +
        ", rank=" + rank +
        '}';
  }

  public enum MemberRank {
    LEADER("Leader"),
    OFFICER("Officer"),
    CITIZEN("Citizen");

    @NotNull
    private final String name;

    MemberRank(@NotNull String name) {
      this.name = name;
    }

    @NotNull
    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }
  }
}
