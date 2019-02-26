package me.wertik.milestones.events;


import me.wertik.milestones.objects.Milestone;
import me.wertik.milestones.objects.MilestoneType;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class MilestoneCollectEvent extends PlayerEvent {

    // Fired after passing through condition checker and reward process

    private Milestone milestone;

    private Entity entity;
    private Block block;
    private String message;

    public MilestoneCollectEvent(Player who, Milestone milestone) {
        super(who);
        this.milestone = milestone;
    }

    public MilestoneCollectEvent(Player who, Milestone milestone, Entity entity) {
        super(who);
        this.milestone = milestone;
        this.entity = entity;
    }

    public MilestoneCollectEvent(Player who, Milestone milestone,  Block block) {
        super(who);
        this.milestone = milestone;
        this.block = block;
    }

    public MilestoneCollectEvent(Player who, Milestone milestone, String message) {
        super(who);
        this.milestone = milestone;
        this.message = message;
    }

    public Entity getEntity() {
        return entity;
    }

    public Block getBlock() {
        return block;
    }

    public String getMessage() {
        return message;
    }

    public MilestoneType getMilestoneType() {
        return  milestone.getConditions().type();
    }

    public Milestone getMilestone() {
        return milestone;
    }

    @Override
    public String getEventName() {
        return "MilestoneCollectEvent";
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
