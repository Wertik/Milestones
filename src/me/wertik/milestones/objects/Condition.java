package me.wertik.milestones.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Condition {

    /*     ****  /*
     *     *  *   *
     *     *  *   *
     *-*/  /**/   /*-*/

    /*
     * Planned conditions:
     * Biome, Block under the player, Region (WG)
     *
     * */

    private String type;
    private List<ItemStack> inHand;
    private List<String> targets;
    private List<ItemStack> inInventory;
    private List<String> biomes;
    private List<String> regionNames;

    public Condition(String type, List<ItemStack> inInventory, List<ItemStack> inHand, List<String> targets, List<String> biomes, List<String> regionNames) {
        this.type = type;
        this.inHand = inHand;
        this.inInventory = inInventory;
        this.targets = targets;
        this.biomes = biomes;
        this.regionNames = regionNames;
    }

    public String getType() {
        return type;
    }

    public List<ItemStack> getInHand() {
        return inHand;
    }

    public List<String> getTargets() {
        return targets;
    }

    public List<ItemStack> getInInventory() {
        return inInventory;
    }

    public List<String> getBiomes() {
        return biomes;
    }

    public List<String> getRegionNames() {
        return regionNames;
    }
}
