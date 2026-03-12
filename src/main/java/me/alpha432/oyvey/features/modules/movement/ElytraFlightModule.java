package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.phys.Vec3;

public class ElytraFlightModule extends Module {
    public Setting<Double> speed = num("Speed", 1.0, 0.1, 5.0);

    public ElytraFlightModule() {
        super("ElytraFlight", "Makes Elytra flight much easier", Category.MOVEMENT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || !mc.player.isFallFlying()) return;

        // Stop the player from naturally losing altitude
        mc.player.setDeltaMovement(0, 0, 0);

        Vec3 look = mc.player.getLookAngle();
        double forward = 0;
        
        if (mc.options.keyUp.isDown()) forward = speed.getValue();
        if (mc.options.keyDown.isDown()) forward = -speed.getValue();

        mc.player.setDeltaMovement(look.x * forward, look.y * forward, look.z * forward);
    }
}
