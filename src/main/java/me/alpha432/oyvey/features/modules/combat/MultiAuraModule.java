
package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;

import java.util.List;
import java.util.stream.Collectors;

public class MultiAuraModule extends Module {
    public Setting<Double> range = num("Range", 4.5, 1.0, 6.0);
    public Setting<Integer> maxTargets = num("MaxTargets", 3, 1, 10);
    public Setting<Boolean> checkCooldown = b("Cooldown", true);

    public MultiAuraModule() {
        super("MultiAura", "Attacks multiple entities in range", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.level == null) return;

        // 1. Check attack strength (optional but recommended for max damage)
        if (checkCooldown.getValue() && mc.player.getAttackStrengthScale(0.0f) < 1.0f) {
            return;
        }

        // 2. Gather all valid targets within range
        List<Entity> targets = mc.level.getEntities().getAll().stream()
                .filter(entity -> entity != mc.player)
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> ((LivingEntity) entity).isAlive())
                .filter(entity -> mc.player.distanceTo(entity) <= range.getValue())
                .limit(maxTargets.getValue()) // This is the 'Multi' part
                .collect(Collectors.toList());

        // 3. Loop through and hit each one
        for (Entity target : targets) {
            attack(target);
        }
    }

    private void attack(Entity target) {
        // In a true MultiAura, we swing for every attack to bypass some 
        // server-side reach/angle checks.
        mc.gameMode.attack(mc.player, target);
        mc.player.swing(InteractionHand.MAIN_HAND);
    }
}
