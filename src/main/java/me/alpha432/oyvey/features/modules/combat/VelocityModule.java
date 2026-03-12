package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.network.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;

public class VelocityModule extends Module {
    public Setting<Double> horizontal = num("Horizontal", 0.0, 0.0, 100.0);
    public Setting<Double> vertical = num("Vertical", 0.0, 0.0, 100.0);
    public Setting<Boolean> explosions = b("Explosions", true);

    public VelocityModule() {
        super("Velocity", "Prevents you from taking knockback", Category.MOVEMENT);
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null) return;

        // 1. Handle standard knockback (from players, mobs, arrows)
        if (event.getPacket() instanceof ClientboundSetEntityMotionPacket packet) {
            // Check if the packet is intended for us
            if (packet.getId() == mc.player.getId()) {
                if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                    event.cancel(); // 0% Velocity: Just delete the packet
                } else {
                    // Custom Velocity: Modify the multipliers
                    // Note: Minecraft stores velocity in packets as integers (scaled)
                    // You would need an Accessor Mixin here to modify the private fields
                    // packet.xa *= (horizontal.getValue() / 100.0);
                    // packet.ya *= (vertical.getValue() / 100.0);
                    // packet.za *= (horizontal.getValue() / 100.0);
                }
            }
        }

        // 2. Handle explosion knockback (Crystals, TNT, Anchors)
        if (explosions.getValue() && event.getPacket() instanceof ClientboundExplodePacket packet) {
            if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                // Again, we cancel or modify the knockback vectors of the explosion
                // event.cancel(); 
            }
        }
    }
}
