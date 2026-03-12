
package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class InventoryTotemModule extends Module {
    public Setting<Double> health = num("Health", 14.0, 0.0, 36.0);
    public Setting<Boolean> strict = b("Strict", true);

    public InventoryTotemModule() {
        super("AutoTotemInv", "Opens inventory packet-side to swap totems", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.level == null) return;

        // Don't run if we already have a totem
        if (mc.player.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING) return;

        float totalHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (totalHealth <= health.getValue()) {
            int totemSlot = findTotemSlot();
            if (totemSlot != -1) {
                swapTotem(totemSlot);
            }
        }
    }

    private int findTotemSlot() {
        // Search the inventory (excluding offhand/armor)
        for (int i = 0; i < 45; i++) {
            if (mc.player.getInventory().getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                // Convert inventory index to Container Slot ID
                if (i < 9) return i + 36; // Hotbar
                return i;
            }
        }
        return -1;
    }

    private void swapTotem(int slot) {
        // 1. Tell the server we are "interacting" with our inventory
        // Note: We don't use mc.setScreen() because that would lock the user's mouse/camera
        if (strict.getValue()) {
            // Some servers require a player action or a specific window packet
            mc.player.connection.send(new ServerboundPlayerActionPacket(
                ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));
        }

        // 2. Perform the swap (Pick up totem -> Click offhand -> Put back old item)
        // Slot 45 is always the Offhand
        click(slot);
        click(45);
        click(slot);

        // 3. Close the container server-side to finish the transaction
        if (strict.getValue()) {
            mc.player.connection.send(new ServerboundContainerClosePacket(mc.player.containerMenu.containerId));
        }
    }

    private void click(int slot) {
        mc.gameMode.handleInventoryMouseClick(
            mc.player.containerMenu.containerId, 
            slot, 
            0, 
            ClickType.PICKUP, 
            mc.player
        );
    }
}
