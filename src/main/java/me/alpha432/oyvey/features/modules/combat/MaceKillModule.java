
import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;

public class AutoMaceModule extends Module {
    public Setting<Double> range = num("Range", 4.0, 1.0, 6.0);
    public Setting<Double> minFall = num("MinFallDistance", 1.5, 0.0, 5.0);
    public Setting<Boolean> autoSwap = b("AutoSwap", true);

    public AutoMaceModule() {
        super("AutoMace", "Automatically strikes with Mace when falling", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.level == null) return;

        // 1. Check if we are falling enough to trigger the smash bonus
        if (mc.player.fallDistance < minFall.getValue()) return;

        // 2. Check what we are looking at
        if (mc.hitResult instanceof EntityHitResult result) {
            Entity target = result.getEntity();

            if (target instanceof LivingEntity && mc.player.distanceTo(target) <= range.getValue()) {
                
                // 3. Find Mace and Swap
                int maceSlot = findMaceSlot();
                if (maceSlot == -1 && !isHoldingMace()) return;

                if (autoSwap.getValue() && !isHoldingMace()) {
                    mc.player.getInventory().selected = maceSlot;
                }

                // 4. Strike!
                // We strike slightly before hitting the ground to ensure the server registers the fall distance
                if (isHoldingMace()) {
                    mc.gameMode.attack(mc.player, target);
                    mc.player.swing(InteractionHand.MAIN_HAND);
                }
            }
        }
    }

    private boolean isHoldingMace() {
        return mc.player.getMainHandItem().is(Items.MACE);
    }

    private int findMaceSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).is(Items.MACE)) return i;
        }
        return -1;
    }
}
