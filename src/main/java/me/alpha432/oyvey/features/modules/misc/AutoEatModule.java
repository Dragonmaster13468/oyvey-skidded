package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class AutoEatModule extends Module {
    public Setting<Integer> hunger = num("HungerThreshold", 14, 1, 20);

    private int lastSlot = -1;
    private boolean isEating = false;

    public AutoEatModule() {
        super("AutoEat", "Automatically eats food when hungry", Category.MISC);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null) return;

        if (isEating) {
            if (!mc.player.isUsingItem()) {
                if (lastSlot != -1) mc.player.getInventory().selected = lastSlot;
                lastSlot = -1;
                isEating = false;
                mc.options.keyUse.setDown(false);
            }
            return;
        }

        if (mc.player.getFoodData().getFoodLevel() <= hunger.getValue()) {
            int foodSlot = findFoodSlot();
            if (foodSlot != -1) {
                lastSlot = mc.player.getInventory().selected;
                mc.player.getInventory().selected = foodSlot;
                mc.options.keyUse.setDown(true);
                mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
                isEating = true;
            }
        }
    }

    private int findFoodSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem().isEdible()) return i;
        }
        return -1;
    }
}
