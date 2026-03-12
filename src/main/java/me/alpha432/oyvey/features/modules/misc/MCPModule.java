package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

public class MCPModule extends Module {
    private boolean clicked = false;

    public MCPModule() {
        super("MCP", "Middle Click Pearl", Category.MISC);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null) return;

        // Check if middle mouse is pressed (GLFW_MOUSE_BUTTON_3)
        if (GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS) {
            if (!clicked) {
                int pearlSlot = findPearlSlot();
                if (pearlSlot != -1) {
                    int oldSlot = mc.player.getInventory().selected;
                    
                    // Instant swap, throw, and swap back
                    mc.player.getInventory().selected = pearlSlot;
                    mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
                    mc.player.getInventory().selected = oldSlot;
                }
                clicked = true;
            }
        } else {
            clicked = false;
        }
    }

    private int findPearlSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).is(Items.ENDER_PEARL)) return i;
        }
        return -1;
    }
}
