package com.nationsrpg.plugin.core.models.nation;

import com.google.common.reflect.TypeToken;
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
  @Override
  public @NotNull ProcessedModel serialize(@NotNull Nation nation) {
    return new JSONProcessedModel(GsonProvider.standard())
        .append("id", ProcessedModelFieldType.UNIQUE_INDEXED, nation.uuid())
        .append("leader", ProcessedModelFieldType.UNIQUE_INDEXED, nation.leader())
        .append("balance", ProcessedModelFieldType.NORMAL, nation.balance())
        .append("members", ProcessedModelFieldType.NORMAL, nation.members());
  }

  @Override
  public @NotNull Nation deserialize(@NotNull ProcessedModel processedModel) {
    if (processedModel.has("id") && processedModel.has("leader") && processedModel.has("balance") && processedModel.has("members")) {
      final TypeToken<HashSet<NationMember>> setToken = new TypeToken<>() {
      };
      return new Nation(processedModel.get("id", UUID.class).orElseThrow(), processedModel.get("leader", UUID.class).orElseThrow(), processedModel.get("balance", Double.class).orElseThrow(), (Set<NationMember>) processedModel.get("members", setToken.getType()).orElseThrow());
    }

    throw new IllegalArgumentException("Outdated model structure! Please modify database manually.");
  }

  @Override
  public @NotNull Class<Nation> getModelType() {
    return Nation.class;
  }
}
