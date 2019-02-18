package me.wertik.milestones.events;

import me.wertik.milestones.objects.Milestone;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class EntityKillMilestoneCollectEvent extends MilestoneCollectEvent{

    private Entity entity;

    public EntityKillMilestoneCollectEvent(Player who, Milestone milestone, Entity entity) {
        super(who, milestone);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public String getEventName() {
        return super.getEventName();
    }

    @Override
    public HandlerList getHandlers() {
        return super.getHandlers();
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
