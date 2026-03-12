
package me.alpha432.oyvey.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import java.awt.Color;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    /**
     * Draws a wireframe box around a block position.
     */
    public static void drawBlockOutline(PoseStack stack, BlockPos pos, Color color, float lineWidth) {
        Vec3 renderPos = mc.gameRenderer.getMainCamera().getPosition();
        double x = pos.getX() - renderPos.x;
        double y = pos.getY() - renderPos.y;
        double z = pos.getZ() - renderPos.z;

        setupRender();
        RenderSystem.lineWidth(lineWidth);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        Matrix4f matrix = stack.last().pose();

        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        
        // Bottom
        buildBox(bufferBuilder, matrix, x, y, z, x + 1, y + 1, z + 1, color);

        tesselator.end();
        releaseRender();
    }

    private static void buildBox(BufferBuilder builder, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, Color c) {
        int r = c.getRed(), g = c.getGreen(), b = c.getBlue(), a = c.getAlpha();
        
        // Standard 12-line vertex set for a cube outline
        builder.vertex(matrix, (float)x1, (float)y1, (float)z1).color(r, g, b, a).endVertex();
        builder.vertex(matrix, (float)x2, (float)y1, (float)z1).color(r, g, b, a).endVertex();
        // ... (repeat for all edges of the cube)
    }

    public static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest(); // Makes ESP visible through walls
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
    }

    public static void releaseRender() {
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
