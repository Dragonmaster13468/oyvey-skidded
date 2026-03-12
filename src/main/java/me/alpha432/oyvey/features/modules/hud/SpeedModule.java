public void renderSpeed(Render2DEvent event) {
    double deltaX = mc.player.getX() - mc.player.xo;
    double deltaZ = mc.player.getZ() - mc.player.zo;
    double speed = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 20; // Meters per second

    String speedText = String.format("Speed: %.2f b/s", speed);
    int x = mc.getWindow().getGuiScaledWidth() - mc.font.width(speedText) - 2;
    int y = mc.getWindow().getGuiScaledHeight() - 10;
    
    mc.font.drawShadow(event.getMatrix(), speedText, x, y, 0xFFFFFF);
}
