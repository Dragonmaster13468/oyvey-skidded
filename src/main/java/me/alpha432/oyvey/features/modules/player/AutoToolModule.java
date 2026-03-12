package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AutoToolModule extends Module {
    public AutoToolModule() {
        super("AutoTool", "Automatically selects the best tool", Category.PLAYER);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        // Only swap if we are actually trying to break a block
        if (mc.player == null || !mc.options.keyAttack.isDown()) return;

        if (mc.hitResult instanceof BlockHitResult bhr) {
            BlockState state = mc.level.getBlockState(bhr.getBlockPos());
            int bestSlot = -1;
            float maxSpeed = 1.0f;

            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.getInventory().getItem(i);
                if (stack.isEmpty()) continue;

                float speed = stack.getDestroySpeed(state);
                if (speed > maxSpeed) {
                    maxSpeed = speed;
                    bestSlot = i;
                }
            }

            if (bestSlot != -1) {
                mc.player.getInventory().selected = bestSlot;
            }
        }
    }
}
