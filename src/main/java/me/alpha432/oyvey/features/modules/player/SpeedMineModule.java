package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.impl.network.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;

public class SpeedMineModule extends Module {
    public SpeedMineModule() {
        super("SpeedMine", "Breaks blocks faster using packets", Category.PLAYER);
    }

    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof ServerboundPlayerActionPacket packet) {
            // If we start breaking a block
            if (packet.getAction() == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                BlockPos pos = packet.getPos();
                Direction side = packet.getDirection();

                // Send a packet claiming we finished immediately after starting
                mc.player.connection.send(new ServerboundPlayerActionPacket(
                    ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, pos, side));
                
                mc.player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}
