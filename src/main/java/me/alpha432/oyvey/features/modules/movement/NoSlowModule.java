package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;

public class NoSlowModule extends Module {
    public NoSlowModule() {
        super("NoSlow", "Prevents items from slowing you down", Category.MOVEMENT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null) return;
        if (mc.player.isUsingItem() && !mc.player.isPassenger()) {
            // Modern versions handle this via movement input manipulation
            mc.player.input.leftImpulse *= 5.0f;
            mc.player.input.forwardImpulse *= 5.0f;
        }
    }
}
