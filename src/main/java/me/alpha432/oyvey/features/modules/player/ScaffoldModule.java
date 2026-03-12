package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ScaffoldModule extends Module {
    public ScaffoldModule() {
        super("Scaffold", "Automatically places blocks under you", Category.PLAYER);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null) return;

        // Position exactly 1 block below the player's feet
        BlockPos pos = new BlockPos(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ());

        if (mc.level.getBlockState(pos).isAir()) {
            int blockSlot = findBlockSlot();
            if (blockSlot != -1) {
                int oldSlot = mc.player.getInventory().selected;
                mc.player.getInventory().selected = blockSlot;

                // Simple placement logic (Face UP from the block below)
                BlockHitResult bhr = new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false);
                mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, bhr);
                mc.player.swing(InteractionHand.MAIN_HAND);

                mc.player.getInventory().selected = oldSlot;
            }
        }
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).getItem() instanceof net.minecraft.world.item.BlockItem) return i;
        }
        return -1;
    }
}
