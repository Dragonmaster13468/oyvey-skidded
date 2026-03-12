package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAuraModule extends Module {
    public Setting<Double> range = num("Range", 4.5, 1.0, 6.0);
    public Setting<Boolean> playersOnly = b("PlayersOnly", true);
    public Setting<Boolean> checkCooldown = b("Cooldown", true);

    public KillAuraModule() {
        super("KillAura", "Automatically attacks nearby entities", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.level == null) return;

        // 1. Find potential targets within range
        List<Entity> targets = mc.level.getEntities().getAll().stream()
                .filter(entity -> entity != mc.player) // Don't hit yourself
                .filter(entity -> entity instanceof LivingEntity) // Must be alive
                .filter(entity -> ((LivingEntity) entity).getHealth() > 0) // Must not be dead
                .filter(entity -> mc.player.distanceTo(entity) <= range.getValue())
                .filter(entity -> !playersOnly.getValue() || entity instanceof Player)
                .sorted(Comparator.comparingDouble(entity -> mc.player.distanceTo(entity)))
                .collect(Collectors.toList());

        if (targets.isEmpty()) return;

        Entity target = targets.get(0);

        // 2. Check Attack Cooldown (1.9+ mechanic)
        if (checkCooldown.getValue()) {
            if (mc.player.getAttackStrengthScale(0.0f) < 1.0f) return;
        }

        // 3. Perform the attack
        attack(target);
    }

    private void attack(Entity target) {
        // Rotations would usually happen here (Send Packet or Set Client Pitch/Yaw)
        
        mc.gameMode.attack(mc.player, target);
        mc.player.swing(InteractionHand.MAIN_HAND);
    }
}
