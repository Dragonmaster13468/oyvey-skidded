
package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;

public class AnchorAuraModule extends Module {
    public Setting<Double> range = num("Range", 5.0, 1.0, 6.0);
    public Setting<Boolean> rotate = b("Rotate", true);

    public AnchorAuraModule() {
        super("AnchorAura", "Automatically explodes Respawn Anchors", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.level == null) return;
        
        // Safety: Do not anchor in the Nether (unless you want to set your spawn!)
        if (mc.level.dimensionType().respawnAnchorWorks()) return;

        Player target = getTarget();
        if (target == null) return;

        BlockPos pos = getAnchorPos(target);
        if (pos == null) return;

        // Logic: Place -> Charge -> Explode
        if (mc.level.getBlockState(pos).is(Blocks.RESPAWN_ANCHOR)) {
            int charges = mc.level.getBlockState(pos).getValue(RespawnAnchorBlock.CHARGE);
            
            if (charges == 0) {
                // Charge with Glowstone
                if (mc.player.getMainHandItem().is(Items.GLOWSTONE)) {
                    clickBlock(pos);
                }
            } else {
                // Explode (click with anything else)
                clickBlock(pos);
            }
        } else {
            // Place Anchor
            if (mc.player.getMainHandItem().is(Items.RESPAWN_ANCHOR)) {
                clickBlock(pos.below()); // Clicks the top of the block below to place
            }
        }
    }

    private void clickBlock(BlockPos pos) {
        BlockHitResult bhr = new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, bhr);
        mc.player.swing(InteractionHand.MAIN_HAND);
    }

    private BlockPos getAnchorPos(Player target) {
        // Simple logic: check blocks around target feet/head
        return BlockPos.betweenClosedStream(target.blockPosition().offset(-1, -1, -1), target.blockPosition().offset(1, 1, 1))
                .filter(p -> mc.level.getBlockState(p).is(Blocks.RESPAWN_ANCHOR) || mc.level.isEmptyBlock(p))
                .min(Comparator.comparingDouble(p -> mc.player.distanceToSqr(p.getX(), p.getY(), p.getZ())))
                .orElse(null);
    }

    private Player getTarget() {
        return mc.level.players().stream()
                .filter(p -> p != mc.player && p.isAlive())
                .filter(p -> mc.player.distanceTo(p) <= range.getValue())
                .min(Comparator.comparingDouble(p -> mc.player.distanceTo(p)))
                .orElse(null);
    }
}
