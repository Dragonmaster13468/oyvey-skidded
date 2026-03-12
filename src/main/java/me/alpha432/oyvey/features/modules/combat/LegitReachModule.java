package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public class LegitReachModule extends Module {
    public Setting<Float> expand = num("Expand", 0.1f, 0.0f, 1.0f);

    public LegitReachModule() {
        super("LegitReach", "Expands entity hitboxes slightly", Category.COMBAT);
    }

    /**
     * This method would be called in your MixinEntity or MixinLevel 
     * where the game checks for target intersections.
     */
    public AABB getExpandedBox(Entity entity) {
        return entity.getBoundingBox().inflate(expand.getValue());
    }
}
