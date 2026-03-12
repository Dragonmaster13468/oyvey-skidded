package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;

public class NoClipModule extends Module {
    public NoClipModule() {
        super("NoClip", "Allows you to walk through walls", Category.MOVEMENT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null) return;

        // Disable collision client-side
        mc.player.noPhysics = true;

        // Note: For this to work on servers, you often need to 
        // send tiny movement packets that "nudge" you into the block.
        if (mc.options.keyUp.isDown()) {
            // Simple logic: remove the Y velocity to hover while phasing
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0, mc.player.getDeltaMovement().z);
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.noPhysics = false;
    }
}
