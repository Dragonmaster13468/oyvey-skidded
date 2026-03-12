package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.network.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class CriticalsModule extends Module {
    public Setting<String> mode = enumSetting("Mode", "Packet", "Bypass", "Jump");

    public CriticalsModule() {
        super("Criticals", "Deals 50% more damage per hit", Category.COMBAT);
    }

    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof ServerboundInteractPacket packet && mc.player.onGround()) {
            // Simplified check: only crit if we are attacking
            if (mode.getValue().equals("Jump")) {
                mc.player.jumpFromGround();
            } else {
                doPacketCrit();
            }
        }
    }

    private void doPacketCrit() {
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        if (mode.getValue().equals("Packet")) {
            mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(x, y + 0.0625, z, false));
            mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(x, y, z, false));
        } else if (mode.getValue().equals("Bypass")) {
            // A more complex offset to confuse anti-cheats
            mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(x, y + 0.11, z, false));
            mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(x, y + 0.1100013579, z, false));
            mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(x, y + 0.0000013579, z, false));
        }
    }
}
