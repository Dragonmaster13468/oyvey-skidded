package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Inventory;

public class AutoTotemModule extends Module {
    public Setting<Double> health = num("Health", 14.0, 0.0, 36.0);
    public Setting<Boolean> checkGap = b("CheckGap", true);

    public AutoTotemModule() {
        super("AutoTotem", "Automatically moves a totem to your offhand", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.screen != null) return;

        // 1. Check if we already have a totem in the offhand
        if (mc.player.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING) return;

        // 2. Condition: Only equip if health is low (or always if health setting is high)
        float totalHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (totalHealth > health.getValue()) return;

        // 3. Find a totem in the inventory
        int totemSlot = findTotemSlot();

        if (totemSlot != -1) {
            moveTotemToOffhand(totemSlot);
        }
    }

    private int findTotemSlot() {
        // Search the main inventory (0-35) and hotbar (36-44)
        for (int i = 0; i < 45; i++) {
            if (mc.player.getInventory().getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                // Adjusting index for window click: 
                // Inventory slots in packet are slightly different than player.getInventory
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    private void moveTotemToOffhand(int slot) {
        // Slot 45 is the constant ID for the Offhand slot in Minecraft's container system
        int offhandSlot = 45;

        // Pick up the totem
        mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, slot, 0, ClickType.PICKUP, mc.player);
        // Place it in the offhand
        mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, offhandSlot, 0, ClickType.PICKUP, mc.player);
        // Put whatever was in the offhand back into the original slot
        mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, slot, 0, ClickType.PICKUP, mc.player);
    }
}
