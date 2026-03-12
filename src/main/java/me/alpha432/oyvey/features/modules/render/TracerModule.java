@Subscribe
public void onRender3D(Render3DEvent event) {
    for (Player player : mc.level.players()) {
        if (player == mc.player) continue;
        
        Vec3 targetPos = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 eyes = new Vec3(0, 0, 1).xRot(-(float)Math.toRadians(mc.player.getXRot()))
                                     .yRot(-(float)Math.toRadians(mc.player.getYRot()));
                                     
        // Draw line logic using MatrixStack
        // RenderUtil.drawLine(eyes, targetPos, Color.WHITE);
    }
}
