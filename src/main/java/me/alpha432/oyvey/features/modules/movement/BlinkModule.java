
package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.impl.network.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import java.util.ArrayList;
import java.util.List;

public class BlinkModule extends Module {
    // List to store packets while "blinking"
    private final List<ServerboundMovePlayerPacket> packetList = new ArrayList<>();

    public BlinkModule() {
        super("Blink", "Suspends your movement packets", Category.PLAYER);
    }

    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof ServerboundMovePlayerPacket packet) {
            // Cancel the packet so the server doesn't see us move
            event.cancel();
            // Store it for later
            packetList.add(packet);
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        // When disabled, dump all stored packets to the server at once
        for (ServerboundMovePlayerPacket packet : packetList) {
            mc.player.connection.send(packet);
        }
        packetList.clear();
    }
}
