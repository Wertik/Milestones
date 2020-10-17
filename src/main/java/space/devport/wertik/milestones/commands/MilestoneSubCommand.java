package space.devport.wertik.milestones.commands;

import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.wertik.milestones.MilestonesPlugin;

public abstract class MilestoneSubCommand extends SubCommand {

    protected final MilestonesPlugin plugin;

    public MilestoneSubCommand(MilestonesPlugin plugin, String name) {
        super(name);
        setPermissions();
        this.plugin = plugin;
    }

    @Override
    public abstract @Nullable String getDefaultUsage();

    @Override
    public abstract @Nullable String getDefaultDescription();

    @Override
    public abstract @Nullable ArgumentRange getRange();
}
