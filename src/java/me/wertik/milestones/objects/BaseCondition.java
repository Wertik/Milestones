package me.wertik.milestones.objects;

import me.wertik.milestones.Main;
import me.wertik.milestones.Utils;
import me.wertik.milestones.handlers.StorageHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BaseCondition {

    private StorageHandler storageHandler;

    // Items that ALL have to be in the inventory
    private List<ItemStack> inInventory;
    private List<String> inInventoryList;

    // List of possible biome types
    private List<String> biomeTypes;

    // List of possible regions
    private List<String> regionNames;

    // List oif possible world names
    private List<String> worldNames;

    // ItemStacks possible to have in hand
    private List<ItemStack> inHandItems;
    private List<String> inHandItemsList;

    // Permissions
    private List<String> permissions;

    // Hmm...
    public BaseCondition(List<String> inInventoryList, List<String> biomeTypes, List<String> regionNames, List<String> worldNames, List<String> inHandItemsList, List<String> permissions) {

        storageHandler = Main.getInstance().getStorageHandler();

        this.biomeTypes = Utils.checkStringList(biomeTypes);

        this.regionNames = Utils.checkStringList(regionNames);

        this.worldNames = Utils.checkStringList(worldNames);

        this.permissions = Utils.checkStringList(permissions);

        if (inInventoryList != null) {
            this.inInventoryList = inInventoryList;

            // Parse stuff for item placeholders
            inInventory = storageHandler.parseForItemPlaceholders(inInventoryList);
        } else {
            this.inInventoryList = new ArrayList<>();
            inInventory = new ArrayList<>();
        }

        if (inHandItemsList != null) {
            this.inHandItemsList = inHandItemsList;

            inHandItems = storageHandler.parseForItemPlaceholders(inHandItemsList);
        } else {
            this.inHandItemsList = new ArrayList<>();
            inHandItems = new ArrayList<>();
        }
    }

    public List<ItemStack> getInInventory() {
        return inInventory;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getInInventoryList() {
        return inInventoryList;
    }

    public List<String> getInHandItemsList() {
        return inHandItemsList;
    }

    public List<String> getBiomeTypes() {
        return biomeTypes;
    }

    public List<String> getRegionNames() {
        return regionNames;
    }

    public List<String> getWorldNames() {
        return worldNames;
    }

    public List<ItemStack> getInHandItems() {
        return inHandItems;
    }

    public boolean check(Player player) {

        // Worlds
        if (!worldNames.isEmpty())
            if (!worldNames.contains(player.getWorld().getName()))
                return false;

        // Biomes
        if (!biomeTypes.isEmpty())
            if (!biomeTypes.contains(player.getLocation().getBlock().getBiome().toString()))
                return false;

        // Tools (in hand items)
        if (!inHandItems.isEmpty()) {
            int i = 0;
            for (ItemStack item : inHandItems) {

                if (item.getAmount() == -1)
                    if (item.getType().equals(player.getInventory().getItemInMainHand().getType()))
                        break;

                if (Utils.compareItemStacks(item, player.getInventory().getItemInMainHand()))
                    break;

                i++;
                if (i == inHandItems.size())
                    return false;
            }
        }

        // Regions
        if (!regionNames.isEmpty()) {
            List<String> regionSet = Main.getInstance().getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegionsIDs(Main.getInstance().getWorldGuard().wrapPlayer(player).getPosition());

            int i = 0;
            for (String region : regionSet) {
                if (regionNames.contains(region))
                    break;

                i++;
                if (i == regionSet.size())
                    return false;
            }
        }

        // inInventory
        if (!inInventory.isEmpty()) {
            List<ItemStack> contents = Utils.listToArray(player.getInventory().getContents());

            for (ItemStack item : inInventory) {
                if (!contents.contains(item))
                    return false;
            }
        }

        // Permissions
        if (Main.getInstance().getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            if (!permissions.isEmpty())
                for (String permission : permissions) {
                    if (!player.hasPermission(permission))
                        return false;
                }
        }

        return true;
    }
}
