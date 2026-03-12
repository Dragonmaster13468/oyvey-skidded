package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SpeedModifier extends Module {

    private final Setting<Float> speed = num("Speed", 0.15f, 0.1f, 0.5f);

    private double prev;

    public SpeedModifier() {
        super("SpeedModifier", "Adjusts player movement speed attribute", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            prev = 0.1;
            return;
        }

        prev = mc.player.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;

        mc.player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(prev);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        mc.player.getAttribute(Attributes.MOVEMENT_SPEED)
                .setBaseValue(speed.getValue());
    }
}
