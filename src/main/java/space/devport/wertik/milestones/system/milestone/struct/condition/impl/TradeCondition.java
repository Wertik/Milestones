package space.devport.wertik.milestones.system.milestone.struct.condition.impl;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Recipe;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.Utils;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TradeCondition extends AbstractCondition {

    private final Set<Villager.Profession> allowedProfessions = new HashSet<>();

    private final Set<Material> allowedResults = new HashSet<>();

    @Override
    public boolean onCheck(Player player, ActionContext context) {
        Villager.Profession profession = context.get(Villager.Profession.class);
        Recipe recipe = context.get(Recipe.class);

        if (profession == null || !allowedProfessions.isEmpty() && !allowedProfessions.contains(profession))
            return false;

        if (recipe == null || !allowedResults.isEmpty() && !allowedResults.contains(recipe.getResult().getType()))
            return false;

        return true;
    }

    @Override
    public void onLoad(Configuration configuration, ConfigurationSection section) {
        List<String> professionNames = section.getStringList("professions");
        for (String professionName : professionNames) {
            Villager.Profession profession = Utils.parseEnum(professionName, Villager.Profession.class);
            if (profession != null)
                this.allowedProfessions.add(profession);
        }

        List<String> materialNames = section.getStringList("results");
        for (String materialName : materialNames) {
            Material material = Material.matchMaterial(materialName);
            if (material != null)
                this.allowedResults.add(material);
        }
    }
}
