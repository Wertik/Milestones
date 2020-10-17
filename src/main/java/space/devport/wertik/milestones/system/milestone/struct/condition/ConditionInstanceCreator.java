package space.devport.wertik.milestones.system.milestone.struct.condition;

import org.jetbrains.annotations.NotNull;

public interface ConditionInstanceCreator {
    @NotNull AbstractCondition createInstance();
}
