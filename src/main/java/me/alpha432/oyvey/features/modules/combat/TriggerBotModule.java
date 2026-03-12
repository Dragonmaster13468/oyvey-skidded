package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.network.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.InteractionHand;

public class TriggerBotModule extends Module {
    public Setting<Boolean> playersOnly = b("PlayersOnly", true);
    public Setting<Boolean> checkCooldown = b("Cooldown", true);
    public Setting<Double> range = num("Range", 4.0, 1.0, 6.0);

    public TriggerBotModule() {
        super("TriggerBot", "Attacks entities when you look at them", Category.COMBAT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.level == null) return;

        // 1. Check if we are looking at an entity
        if (mc.hitResult instanceof EntityHitResult result) {
            Entity target = result.getEntity();

            // 2. Validate the target
            if (isValid(target)) {
                
                // 3. Distance check (Minecraft's hitResult can sometimes be longer than reach)
                if (mc.player.distanceTo(target) > range.getValue()) return;

                // 4. Attack speed / Cooldown check
                if (checkCooldown.getValue()) {
                    if (mc.player.getAttackStrengthScale(0.0f) < 1.0f) return;
                }

                // 5. Strike
                mc.gameMode.attack(mc.player, target);
                mc.player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }

    private boolean isValid(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return false;
        if (!living.isAlive()) return false;
        if (entity == mc.player) return false;
        if (playersOnly.getValue() && !(entity instanceof Player)) return false;
        
        return true;
    }
}
