package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class AnchorMacro extends Module {
    public Setting<Boolean> autoSwitch = b("AutoSwitch", true);
    public Setting<Boolean> packet = b("Packet", String.valueOf(true));

    public AnchorMacro() {
        super("AnchorMacro", "Fastest manual anchor placement and detonation", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.level == null) return;
        if (mc.level.dimensionType().respawnAnchorWorks()) return; // Safety check

        if (mc.hitResult instanceof BlockHitResult bhr) {
            BlockPos pos = bhr.getBlockPos();
            var state = mc.level.getBlockState(pos);

            // Logic: Handle the block currently being looked at
            if (state.is(Blocks.RESPAWN_ANCHOR)) {
                int charges = state.getValue(RespawnAnchorBlock.CHARGE);

                if (charges == 0) {
                    // Step 1: Charge
                    performAction(findItemSlot(Items.GLOWSTONE), bhr);
                } else {
                    // Step 2: Detonate (Click with anything that isn't glowstone)
                    performAction(findItemSlot(Items.RESPAWN_ANCHOR), bhr);
                }
            } else if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.BEDROCK)) {
                // Step 0: Place
                performAction(findItemSlot(Items.RESPAWN_ANCHOR), bhr);
            }
        }
    }

    private void performAction(int slot, BlockHitResult bhr) {
        if (slot == -1) return;

        int oldSlot = mc.player.getInventory().selected;
        
        // Instant Switch
        mc.player.getInventory().selected = slot;
        
        // Use Item
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, bhr);
        mc.player.swing(InteractionHand.MAIN_HAND);

        // Switch Back
        if (autoSwitch.getValue()) {
            mc.player.getInventory().selected = oldSlot;
        }
    }

    private int findItemSlot(net.minecraft.world.item.Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }
}
