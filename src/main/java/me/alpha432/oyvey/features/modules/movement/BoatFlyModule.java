package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;

public class BoatFlyModule extends Module {
    public Setting<Double> speed = num("Speed", 3.0, 0.1, 10.0);
    public Setting<Double> verticalSpeed = num("Vertical", 2.0, 0.1, 5.0);

    public BoatFlyModule() {
        super("BoatFly", "Allows you to fly while riding a boat", Category.MOVEMENT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || !(mc.player.getVehicle() instanceof Boat boat)) return;

        // Reset gravity
        boat.setNoGravity(true);
        Vec3 velocity = new Vec3(0, 0, 0);

        // Horizontal Movement
        double yaw = Math.toRadians(mc.player.getYRot());
        if (mc.options.keyUp.isDown()) {
            velocity = new Vec3(-Math.sin(yaw) * speed.getValue(), velocity.y, Math.cos(yaw) * speed.getValue());
        }

        // Vertical Movement
        double y = 0;
        if (mc.options.keyJump.isDown()) y = verticalSpeed.getValue();
        if (mc.options.keyShift.isDown()) y = -verticalSpeed.getValue();

        boat.setDeltaMovement(velocity.x, y, velocity.z);
    }

    @Override
    public void onDisable() {
        if (mc.player != null && mc.player.getVehicle() instanceof Boat boat) {
            boat.setNoGravity(false);
        }
    }
}
