package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.ArrayList;
import java.util.List;

public class XRayModule extends Module {
    public Setting<Integer> opacity = num("Opacity", 120, 0, 255);
    
    // A list of blocks we want to see through the walls
    private final List<Block> visibleBlocks = new ArrayList<>();

    public XRayModule() {
        super("XRay", "Allows you to see ores through blocks", Category.RENDER);
        
        // Default blocks to look for
        visibleBlocks.add(Blocks.DIAMOND_ORE);
        visibleBlocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        visibleBlocks.add(Blocks.GOLD_ORE);
        visibleBlocks.add(Blocks.DEEPSLATE_GOLD_ORE);
        visibleBlocks.add(Blocks.NETHER_QUARTZ_ORE);
        visibleBlocks.add(Blocks.ANCIENT_DEBRIS);
    }

    @Override
    public void onEnable() {
        if (mc.levelRenderer != null) {
            // This forces Minecraft to reload all chunks so the Xray takes effect immediately
            mc.levelRenderer.allChanged();
        }
    }

    @Override
    public void onDisable() {
        if (mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
        }
    }

    /**
     * This method would be called from your MixinAbstractBlock or MixinBlock
     * to determine if a specific block side should be rendered.
     */
    public boolean shouldShowBlock(Block block) {
        return visibleBlocks.contains(block);
    }
}
