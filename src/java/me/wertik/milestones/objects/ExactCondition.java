package me.wertik.milestones.objects;

import me.wertik.milestones.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExactCondition {

    private MilestoneType milestoneType;

    // Basic conditions, in all of them.
    private BaseCondition baseCondition;

    // The former list of targets
    private List<String> targetTypes;

    // Block types possible to mine
    // BLOCK_BREAK
    private List<String> blockTypes;

    // Entity types possible to kill
    // ENTITY_KILL
    private List<EntityType> entityTypes;

    // Messages to trigger the milestone
    // PLAYER_CHAT
    private List<String> messages;

    public ExactCondition(MilestoneType milestoneType, BaseCondition baseCondition, List<String> targetTypes) {
        this.blockTypes = new ArrayList<>();
        this.entityTypes = new ArrayList<>();
        this.targetTypes = targetTypes;

        if (milestoneType.equals(MilestoneType.ENTITY_KILL))
            targetTypes.forEach(entityType -> entityTypes.add(EntityType.valueOf(entityType)));
        else if (milestoneType.equals(MilestoneType.BLOCK_BREAK))
            targetTypes.forEach(blockType -> blockTypes.add(Utils.toBTypeString(blockType)));
        else if (milestoneType.equals(milestoneType.PLAYER_CHAT))
            messages = targetTypes;

        this.baseCondition = baseCondition;
        this.milestoneType = milestoneType;
    }

    public ExactCondition(MilestoneType milestoneType, BaseCondition baseCondition) {
        this.blockTypes = new ArrayList<>();
        this.entityTypes = new ArrayList<>();
        this.milestoneType = milestoneType;
        this.baseCondition = baseCondition;
    }

    public List<String> getTargetTypes() {
        return targetTypes;
    }

    public boolean check(Player player) {
        // Base conditions
        if (!baseCondition.check(player))
            return false;

        return true;
    }

    public boolean check(Player player, Entity entity) {
        // Entity
        if (!entityTypes.isEmpty())
            if (!entityTypes.contains(entity.getType()))
                return false;

        // Base conditions
        if (!baseCondition.check(player))
            return false;

        return true;
    }

    public boolean check(Player player, Block block) {

        // Block
        if (!blockTypes.isEmpty())
            if (!blockTypes.contains(Utils.toBTypeString(block.getType(), block.getData())))
                return false;

        // Base conditions
        if (!baseCondition.check(player))
            return false;

        return true;
    }

    public boolean check(Player player, String message) {

        // Block
        if (!messages.isEmpty())
            if (!messages.contains(message))
                return false;

        // Base conditions
        if (!baseCondition.check(player))
            return false;

        return true;
    }

    public BaseCondition baseCondition() {
        return baseCondition;
    }

    public MilestoneType type() {
        return milestoneType;
    }
}
