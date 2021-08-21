package net.tinetwork.tradingcards.tradingcardsplugin.whitelist;

import net.tinetwork.tradingcards.tradingcardsplugin.TradingCards;
import net.tinetwork.tradingcards.api.blacklist.WhitelistMode;
import net.tinetwork.tradingcards.api.blacklist.Blacklist;
import net.tinetwork.tradingcards.tradingcardsplugin.core.SimpleConfigFile;
import net.tinetwork.tradingcards.tradingcardsplugin.core.SimpleConfigurate;
import org.bukkit.World;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

/**
 * @author ketelsb
 */
public class WorldBlacklist extends SimpleConfigurate implements Blacklist<World> {
    private static final String LISTED_WORLDS_NAME = "worlds";
    private static final String WHITELIST_MODE_NAME = "whitelist-mode";
    private final ConfigurationNode worldNode;
    private List<String> listedWorlds;
    private WhitelistMode whitelistMode;


    public WorldBlacklist(TradingCards plugin) throws ConfigurateException {
        super(plugin, "world-blacklist.yml", "lists");
        saveDefaultConfig();

        this.worldNode = rootNode.node(LISTED_WORLDS_NAME);

        loadWorlds();
        setWhitelistMode();
    }

    private void loadWorlds() throws SerializationException {
        listedWorlds = worldNode.getList(String.class);
    }

    private void setWhitelistMode() {
        boolean isWhitelist = rootNode.node(WHITELIST_MODE_NAME).getBoolean();
        if (isWhitelist)
            this.whitelistMode = WhitelistMode.WHITELIST;
        else
            this.whitelistMode = WhitelistMode.BLACKLIST;
    }

    @Override
    public boolean isAllowed(World w) {
        boolean isOnList = listedWorlds.contains(w.getName());

        //If you're not on the blacklist, you're allowed
        if (this.whitelistMode == WhitelistMode.BLACKLIST) {
            return !isOnList;
        }

        //If you're on the whitelist, you're allowed
        if (this.whitelistMode == WhitelistMode.WHITELIST) {
            return isOnList;
        }
        return false;
    }

    @Override
    public void add(World w) {
        final ConfigurationNode worldNode = rootNode.node(LISTED_WORLDS_NAME);
        listedWorlds.add(w.getName());
        try {
            worldNode.set(null);
            worldNode.set(listedWorlds);
            loader.save(worldNode);
        } catch (ConfigurateException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    @Override
    public void remove(World w) {
        final ConfigurationNode worldNode = rootNode.node(LISTED_WORLDS_NAME);
        listedWorlds.remove(w.getName());
        try {
            worldNode.set(null);
            worldNode.set(listedWorlds);
            loader.save(worldNode);
        } catch (ConfigurateException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    @Override
    public WhitelistMode getMode() {
        return whitelistMode;
    }
}
