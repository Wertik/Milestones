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
    private List<ItemStack> toolTypes;
    private List<String> targetTypes;
    private List<ItemStack> inInventory;
    private List<String> biomes;
    private List<String> regionNames;

    public Condition(String type, List<ItemStack> inInventory, List<ItemStack> toolTypes, List<String> targetTypes, List<String> biomes, List<String> regionNames) {
        this.type = type;
        this.toolTypes = toolTypes;
        this.inInventory = inInventory;
        this.targetTypes = targetTypes;
        this.biomes = biomes;
        this.regionNames = regionNames;
    }

    public String getType() {
        return type;
    }

    public List<ItemStack> getToolTypes() {
        return toolTypes;
    }

    public List<String> getTargetTypes() {
        return targetTypes;
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
