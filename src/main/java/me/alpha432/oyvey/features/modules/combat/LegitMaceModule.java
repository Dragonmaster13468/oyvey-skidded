package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.Items;

public class MaceDetector extends Module {

    public MaceDetector() {
        super("MaceDetector", "Detects when a mace smash is possible", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // check if player is holding a mace
        if (mc.player.getMainHandItem().getItem() != Items.MACE) return;

        // check if player is falling
        if (mc.player.getDeltaMovement().y < -0.6) {

            // check if elytra is equipped
            if (mc.player.getInventory().armor.get(2).getItem() == Items.ELYTRA) {

                // here you could trigger UI, sound, or indicator
                System.out.println("Mace smash opportunity detected!");
            }
        }
    }
