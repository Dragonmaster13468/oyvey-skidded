package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.network.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.core.BlockPos;
import java.util.HashSet;
import java.util.Set;

public class BaseFinderModule extends Module {
    private final Set<BlockPos> oldChunks = new HashSet<>();

    public BaseFinderModule() {
        super("BaseFinder", "Detects traces of player activity", Category.RENDER);
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof ClientboundLevelChunkWithLightPacket packet) {
            // New versions of MC use 'Full' status to determine if a chunk is new
            // If the packet contains certain data, it means it's an 'Old Chunk'
            // We store the position to highlight it in Render3D
            oldChunks.add(new BlockPos(packet.getX() << 4, 0, packet.getZ() << 4));
        }
    }
    
    // You would then use a Render3DEvent to draw a red box over 'oldChunks'
}
