package com.nationsrpg.plugin.core;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import com.nationsrpg.plugin.core.api.constants.Messages;
import com.nationsrpg.plugin.core.api.map.SpawnMap;
import com.nationsrpg.plugin.core.commands.AdminCommand;
import com.nationsrpg.plugin.core.commands.NationCommand;
import com.nationsrpg.plugin.core.managers.AddonManager;
import com.nationsrpg.plugin.core.managers.DisplayManager;
import com.nationsrpg.plugin.core.managers.NationManager;
import com.nationsrpg.plugin.core.managers.UserManager;
import com.nationsrpg.plugin.core.models.nation.NationStructure;
import com.nationsrpg.plugin.core.models.user.UserStructure;
import me.byteful.lib.datastore.api.ModelManager;
import me.byteful.lib.datastore.api.data.DataStore;
import me.byteful.lib.datastore.mysql.MySQLDataStore;
import me.byteful.lib.datastore.sqlite.SQLiteDataStore;
import me.lucko.helper.config.ConfigurationNode;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.utils.Players;

import java.lang.reflect.InvocationTargetException;

import static com.nationsrpg.plugin.core.helpers.Log.info;

public final class NationsRPGPlugin extends ExtendedJavaPlugin {
  private static NationsRPGPlugin instance;

  private DataStore dataStore;
  private ConfigurationNode settings;
  private SpawnMap spawn;
  private AddonManager addonManager;
  private BukkitCommandManager commandManager;
  private NationManager nationManager;
  private UserManager userManager;
  private DisplayManager displayManager;

  public static NationsRPGPlugin getInstance() {
    return instance;
  }

  @Override
  protected void enable() {
    Players.forEach(p -> p.kick(Messages.SERVER_START_KICK.build()));
    instance = this;

    settings = loadConfigNode("config.yml");
    info("Loaded configuration...");

    loadDataStore();
    info("Connected to data storage servers...");

    nationManager = new NationManager(this);
    userManager = new UserManager(this);
    info("Initialized user and nation managers...");

    displayManager = new DisplayManager(this);
    info("Initialized chat manager...");

    loadSpawnMap();
    info("Loaded spawn map...");

    addonManager = new AddonManager(this);
    addonManager.registerAll(this);
    info("Loaded addons...");

    commandManager = new PaperCommandManager(this);
    commandManager.enableUnstableAPI("brigadier");
    commandManager.enableUnstableAPI("help");
    registerCommands();
    info("Registered commands...");

    info("Successfully started NationsRPG Core Plugin. All core services are now available.");
  }

  @Override
  protected void disable() {
    if (dataStore != null) {
      try {
        dataStore.close();
        info("Disconnected from data storage servers...");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (commandManager != null) {
      commandManager.unregisterCommands();
      info("Unregistered commands...");
    }

    if (addonManager != null) {
      addonManager.getBlockDataManager().saveAndClose();
      info("Closed BlockDataManager...");
    }

    info("Successfully stopped NationsRPG Core Plugin. All core services are no longer available.");

    instance = null;
  }

  private void loadDataStore() {
    final boolean beta = settings.getNode("beta").getBoolean(true);

    if (beta) {
      dataStore = new SQLiteDataStore(getDataFolder().toPath().resolve("data.db"));
    } else {
      final ConfigurationNode mysql = settings.getNode("mysql");
      dataStore =
          new MySQLDataStore(
              mysql.getNode("host").getString("localhost"),
              mysql.getNode("port").getInt(3306),
              mysql.getNode("user").getString("root"),
              mysql.getNode("pass").getString("password"),
              mysql.getNode("database").getString("NationsRPG"));
    }
    bind(dataStore);

    ModelManager.registerModelStructure(new NationStructure(this));
    ModelManager.registerModelStructure(new UserStructure(this));
  }

  private void registerCommands() {
    commandManager.registerCommand(new NationCommand());
    commandManager.registerCommand(new AdminCommand());
  }

  private void loadSpawnMap() {
    final String mapClass =
        settings.getNode("map").getString("com.nationsrpg.plugin.core.maps.ClassicSpawnMap");

    try {
      spawn = Class.forName(mapClass).asSubclass(SpawnMap.class).getConstructor().newInstance();
      spawn.initialize(this);
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException
        | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public DataStore getDataStore() {
    return dataStore;
  }

  public ConfigurationNode getSettings() {
    return settings;
  }

  public SpawnMap getSpawn() {
    return spawn;
  }

  public AddonManager getAddonManager() {
    return addonManager;
  }

  public BukkitCommandManager getCommandManager() {
    return commandManager;
  }

  public NationManager getNationManager() {
    return nationManager;
  }

  public UserManager getUserManager() {
    return userManager;
  }

  public DisplayManager getChatManager() {
    return displayManager;
  }
}
