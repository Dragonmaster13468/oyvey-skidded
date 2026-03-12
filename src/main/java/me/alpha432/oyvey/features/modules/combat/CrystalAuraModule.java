package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.EndCrystalItem;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Blocks;

import java.util.Comparator;

public class CrystalAuraModule extends Module {
    public Setting<Double> range = num("Range", 5.0, 1.0, 6.0);
    public Setting<Double> wallRange = num("WallRange", 3.0, 1.0, 6.0);
    public Setting<Double> minDamage = num("MinDamage", 4.0, 0.0, 20.0);
    public Setting<Double> maxSelfDamage = num("MaxSelf", 6.0, 0.0, 20.0);

    public CrystalAuraModule() {
        super("CrystalAura", "Automatically places and breaks crystals", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.level == null) return;

        // 1. Logic: Break existing crystals first
        Entity crystal = mc.level.getEntities().getAll().stream()
                .filter(e -> e instanceof EndCrystal)
                .filter(e -> mc.player.distanceTo(e) <= range.getValue())
                .min(Comparator.comparingDouble(e -> mc.player.distanceTo(e)))
                .orElse(null);

        if (crystal != null) {
            mc.gameMode.attack(mc.player, crystal);
            mc.player.swing(InteractionHand.MAIN_HAND);
            return; // Break first, place later
        }

        // 2. Logic: Place new crystals
        if (!(mc.player.getMainHandItem().getItem() instanceof EndCrystalItem) && 
            !(mc.player.getOffhandItem().getItem() instanceof EndCrystalItem)) return;

        BlockPos bestPos = findBestCrystalPos();
        if (bestPos != null) {
            // Simulate a right-click on the block
            BlockHitResult bhr = new BlockHitResult(new Vec3(bestPos.getX(), bestPos.getY(), bestPos.getZ()), 
                                 net.minecraft.core.Direction.UP, bestPos, false);
            
            mc.gameMode.useItemOn(mc.player, 
                mc.player.getOffhandItem().getItem() instanceof EndCrystalItem ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, 
                bhr);
            
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private BlockPos findBestCrystalPos() {
        // This is a simplified search. A real "CA" calculates damage for every nearby block.
        Iterable<BlockPos> blocks = BlockPos.betweenClosed(
                mc.player.blockPosition().offset(-5, -3, -5),
                mc.player.blockPosition().offset(5, 3, 5)
        );

        for (BlockPos pos : blocks) {
            if (isValidPlatform(pos)) {
                // In a professional module, you'd insert your DamageUtil.calculate() here
                return pos.immutable();
            }
        }
        return null;
    }

    private boolean isValidPlatform(BlockPos pos) {
        return (mc.level.getBlockState(pos).is(Blocks.OBSIDIAN) || mc.level.getBlockState(pos).is(Blocks.BEDROCK))
                && mc.level.isEmptyBlock(pos.above()) 
                && mc.level.isEmptyBlock(pos.above(2));
    }
}
