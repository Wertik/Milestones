package me.wertik.milestones.objects;

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
    private List<String> toolTypes;
    private List<String> targetTypes;
    private List<String> inInventory;

    public Condition(String type, List<String> inInventory, List<String> toolTypes, List<String> targetTypes) {
        this.type = type;
        this.toolTypes = toolTypes;
        this.inInventory = inInventory;
        this.targetTypes = targetTypes;
    }

    public String getType() {
        return type;
    }

    public List<String> getToolTypes() {
        return toolTypes;
    }

    public List<String> getTargetTypes() {
        return targetTypes;
    }

    public List<String> getInInventory() {
        return inInventory;
    }
}
