package com.nationsrpg.plugin.core.models.nation;

import me.byteful.lib.datastore.api.model.ModelStructure;
import me.byteful.lib.datastore.api.model.ProcessedModel;
import me.byteful.lib.datastore.api.model.ProcessedModelFieldType;
import me.byteful.lib.datastore.api.model.impl.JSONProcessedModel;
import me.lucko.helper.gson.GsonProvider;
import org.jetbrains.annotations.NotNull;

public class NationStructure implements ModelStructure<Nation> {
  @Override
  public @NotNull ProcessedModel serialize(@NotNull Nation nation) {
    return new JSONProcessedModel(GsonProvider.standard())
        .append("id", ProcessedModelFieldType.UNIQUE_INDEXED, nation.uuid())
        .append("leader", ProcessedModelFieldType.UNIQUE_INDEXED, nation.leader());
  }

  @Override
  public @NotNull Nation deserialize(@NotNull ProcessedModel processedModel) {
    if (processedModel instanceof JSONProcessedModel) {
      return GsonProvider.standard()
          .fromJson(((JSONProcessedModel) processedModel).toJSON(), Nation.class);
    }

    throw new IllegalStateException("Bad data store.");
  }

  @Override
  public @NotNull Class<Nation> getModelType() {
    return Nation.class;
  }
}
