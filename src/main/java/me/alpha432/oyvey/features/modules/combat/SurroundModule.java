package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class SurroundModule extends Module {
    public SurroundModule() {
        super("Surround", "Surrounds your feet with Obsidian", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || !mc.player.onGround()) return;

        // The 4 positions around the player's feet
        BlockPos[] positions = {
            mc.player.blockPosition().north(),
            mc.player.blockPosition().south(),
            mc.player.blockPosition().east(),
            mc.player.blockPosition().west()
        };

        int obbySlot = findObsidian();
        if (obbySlot == -1) return;

        int oldSlot = mc.player.getInventory().selected;
        mc.player.getInventory().selected = obbySlot;

        for (BlockPos pos : positions) {
            if (mc.level.getBlockState(pos).isAir()) {
                placeBlock(pos);
            }
        }

        mc.player.getInventory().selected = oldSlot;
    }

    private void placeBlock(BlockPos pos) {
        BlockHitResult bhr = new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, bhr);
        mc.player.swing(InteractionHand.MAIN_HAND);
    }

    private int findObsidian() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).is(Items.OBSIDIAN)) return i;
        }
        return -1;
    }
}
