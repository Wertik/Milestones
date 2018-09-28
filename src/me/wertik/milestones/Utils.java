package me.wertik.milestones;

import org.bukkit.inventory.ItemStack;

public class Utils {

    public Utils() {
    }

    // This method.. just.. can't ignore durability while comparing item stacks and have to do this, fvck Spigot tho.
    public boolean compareItemStacks(ItemStack a, ItemStack b) {

        // ah... fml...
        if (a.getType() != b.getType())
            return false;

        if (a.getAmount() != b.getAmount())
            return false;

        if (!a.getEnchantments().equals(b.getEnchantments()))
            return false;

        if (!a.getItemMeta().equals(b.getItemMeta()))
            return false;

        return true;
    }
}
