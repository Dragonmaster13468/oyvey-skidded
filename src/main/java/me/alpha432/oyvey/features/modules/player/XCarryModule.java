package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.impl.network.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;

public class XCarryModule extends Module {
    public XCarryModule() {
        super("XCarry", "Keep items in your crafting slots", Category.PLAYER);
    }

    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        // Intercept the packet that tells the server we closed our inventory
        if (event.getPacket() instanceof ServerboundContainerClosePacket packet) {
            // Window ID 0 is the player's main inventory/crafting grid
            if (packet.getContainerId() == 0) {
                event.cancel();
            }
        }
    }
}
