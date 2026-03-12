package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class HoleSnapModule extends Module {
    public HoleSnapModule() {
        super("HoleSnap", "Automatically pulls you into the nearest hole", Category.MOVEMENT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || !mc.player.isAlive()) return;

        BlockPos hole = findNearestHole();
        if (hole != null) {
            Vec3 center = new Vec3(hole.getX() + 0.5, mc.player.getY(), hole.getZ() + 0.5);
            
            // Move the player toward the center
            double diffX = center.x - mc.player.getX();
            double diffZ = center.z - mc.player.getZ();
            
            mc.player.setDeltaMovement(diffX * 0.5, mc.player.getDeltaMovement().y, diffZ * 0.5);
        }
    }

    private BlockPos findNearestHole() {
        BlockPos playerPos = mc.player.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(playerPos.offset(-3, -1, -3), playerPos.offset(3, 1, 3))) {
            if (isHole(pos)) return pos.immutable();
        }
        return null;
    }

    private boolean isHole(BlockPos pos) {
        return mc.level.isEmptyBlock(pos) && mc.level.isEmptyBlock(pos.above()) && 
               !mc.level.isEmptyBlock(pos.below()); // Simplified check
    }
}
