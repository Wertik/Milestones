package me.wertik.milestones.events;


import me.wertik.milestones.objects.Milestone;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class MilestoneCollectEvent extends PlayerEvent {

    // Fired after pushing through condition checker and reward process

    private Milestone milestone;
    private Player who;

    public MilestoneCollectEvent(Player who, Milestone milestone) {
        super(who);
        this.who = who;
        this.milestone = milestone;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public Player getWho() {
        return who;
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
