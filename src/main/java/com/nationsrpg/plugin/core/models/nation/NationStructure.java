package com.nationsrpg.plugin.core.models.nation;

import com.google.common.reflect.TypeToken;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import me.byteful.lib.datastore.api.model.ModelStructure;
import me.byteful.lib.datastore.api.model.ProcessedModel;
import me.byteful.lib.datastore.api.model.ProcessedModelFieldType;
import me.byteful.lib.datastore.api.model.impl.JSONProcessedModel;
import me.lucko.helper.gson.GsonProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class NationStructure implements ModelStructure<Nation> {
  @NotNull private final NationsRPGPlugin plugin;

  public NationStructure(@NotNull NationsRPGPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public @NotNull ProcessedModel serialize(@NotNull Nation nation) {
    return new JSONProcessedModel(GsonProvider.standard())
        .append("id", ProcessedModelFieldType.UNIQUE_INDEXED, nation.uuid())
        .append("leader", ProcessedModelFieldType.UNIQUE_INDEXED, nation.leader())
        .append("balance", ProcessedModelFieldType.NORMAL, nation.balance())
        .append("members", ProcessedModelFieldType.NORMAL, nation.members())
        .append("listed", ProcessedModelFieldType.NORMAL, nation.listed());
  }

  @Override
  public @NotNull Nation deserialize(@NotNull ProcessedModel processedModel) {
    if (processedModel.has("id")
        && processedModel.has("leader")
        && processedModel.has("balance")
        && processedModel.has("members")
        && processedModel.has("listed")) {
      final TypeToken<HashSet<NationMember>> setToken = new TypeToken<>() {};
      return new Nation(
          plugin,
          processedModel.get("id", UUID.class).orElseThrow(),
          processedModel.get("leader", UUID.class).orElseThrow(),
          processedModel.get("balance", Double.class).orElseThrow(),
          (Set<NationMember>) processedModel.get("members", setToken.getType()).orElseThrow(),
          processedModel.get("listed", Boolean.class).orElseThrow());
    }

    throw new IllegalArgumentException(
        "Outdated model structure! Please modify database manually.");
  }

  @Override
  public @NotNull Class<Nation> getModelType() {
    return Nation.class;
  }
}
