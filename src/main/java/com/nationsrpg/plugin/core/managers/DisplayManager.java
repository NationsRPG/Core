package com.nationsrpg.plugin.core.managers;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.api.constants.Messages;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Item;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static com.nationsrpg.plugin.core.helpers.FormatUtils.getMaterialName;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static redempt.redlib.misc.FormatUtils.color;

public final class DisplayManager {
  public DisplayManager(@NotNull NationsRPGPlugin plugin) {
    Events.subscribe(AsyncChatEvent.class, EventPriority.HIGHEST)
        .handler(
            e -> {
              final LuckPerms lp = LuckPermsProvider.get();

              e.renderer(
                  ChatRenderer.viewerUnaware(
                      (source, sourceDisplayName, message) -> {
                        final User user = lp.getUserManager().getUser(source.getUniqueId());
                        if (user == null) {
                          return translatable("chat.type.text", sourceDisplayName, message);
                        }
                        final Group group = lp.getGroupManager().getGroup(user.getPrimaryGroup());
                        if (group == null) {
                          return translatable("chat.type.text", sourceDisplayName, message);
                        }
                        final String prefix = group.getCachedData().getMetaData().getPrefix();
                        if (prefix == null) {
                          return translatable("chat.type.text", sourceDisplayName, message);
                        }

                        return text()
                            .append(text(color(prefix) + source.getDisplayName()))
                            .append(space())
                            .append(Messages.DOUBLE_ARROW_SEPARATOR)
                            .append(space())
                            .append(message)
                            .build();
                      }));
            })
        .bindWith(plugin);

    Events.subscribe(ItemSpawnEvent.class, EventPriority.LOWEST)
        .handler(
            e -> {
              final Item ent = e.getEntity();
              final ItemStack stack = ent.getItemStack();
              final int amount = stack.getAmount();
              final ItemMeta meta = stack.getItemMeta();
              if (meta == null) {
                return;
              }
              Schedulers.sync()
                  .runLater(
                      () -> {
                        ent.setCustomNameVisible(true);
                        ent.customName(
                            meta.displayName() != null
                                ? meta.displayName().append(text(" (x" + amount + ")"))
                                : text(
                                    getMaterialName(stack.getType()) + " (x" + amount + ")",
                                    WHITE));
                      },
                      1L);
            })
        .bindWith(plugin);

    Events.subscribe(ItemMergeEvent.class, EventPriority.LOWEST)
        .handler(
            e -> {
              final Item target = e.getTarget();
              final ItemStack stack = target.getItemStack();
              final int amount = e.getEntity().getItemStack().getAmount() + stack.getAmount();
              final ItemMeta meta = stack.getItemMeta();
              if (meta == null) {
                return;
              }
              Schedulers.sync()
                  .runLater(
                      () -> {
                        target.setCustomNameVisible(true);
                        target.customName(
                            meta.displayName() != null
                                ? meta.displayName().append(text(" (x" + amount + ")"))
                                : text(
                                    getMaterialName(stack.getType()) + " (x" + amount + ")",
                                    WHITE));
                      },
                      1L);
            })
        .bindWith(plugin);

    if (plugin.getSettings().getNode("beta").getBoolean(true)) {
      Events.subscribe(PlayerJoinEvent.class)
          .handler(
              e ->
                  e.getPlayer()
                      .sendMessage(Messages.WELCOME_BETA_JOIN.build(e.getPlayer().getName())))
          .bindWith(plugin);
    }
  }
}
