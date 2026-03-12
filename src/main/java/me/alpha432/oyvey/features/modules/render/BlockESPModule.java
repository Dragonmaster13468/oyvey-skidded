package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class SearchModule extends Module {
    public SearchModule() {
        super("Search", "Highlights valuable blocks", Category.RENDER);
    }

    // This logic usually lives in your WorldRenderer Mixin
    public boolean shouldHighlight(BlockEntity entity) {
        return isEnabled() && (entity instanceof ChestBlockEntity || 
               mc.level.getBlockState(entity.getBlockPos()).is(Blocks.ENDER_CHEST));
    }
}
