package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.level.block.entity.*;

import java.awt.Color;

public class StorageESP extends Module {
    public StorageESP() {
        super("StorageESP", "Draws boxes around storage blocks", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.level == null) return;

        // Iterate through all loaded block entities (chests, etc.)
        for (BlockEntity entity : mc.level.blockEntityList) {
            Color color = getEntityColor(entity);
            if (color != null) {
                // Draw a 3D box around the block
                RenderUtil.drawBlockOutline(event.getMatrix(), entity.getBlockPos(), color, 1.0f);
            }
        }
    }

    private Color getEntityColor(BlockEntity entity) {
        if (entity instanceof ChestBlockEntity) return Color.GREEN;
        if (entity instanceof EnderChestBlockEntity) return Color.ORANGE;
        if (entity instanceof ShulkerBoxBlockEntity) return Color.MAGENTA;
        if (entity instanceof BarrelBlockEntity) return new Color(150, 75, 0); // Brown
        return null;
    }
}
