package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;

public class JesusModule extends Module {
    public JesusModule() {
        super("Jesus", "Walk on water like it is land", Category.MOVEMENT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null) return;
        if (mc.player.isInWater() || mc.player.isInLava()) {
            // Force the player upward so they stay on the surface
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.11, mc.player.getDeltaMovement().z);
        }
    }
}
