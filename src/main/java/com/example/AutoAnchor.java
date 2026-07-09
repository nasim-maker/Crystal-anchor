package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoAnchor {
    private final double range = 5.0;

    public void onTick(MinecraftClient mc) {
        if (mc.world == null || mc.player == null) return;
        if (mc.world.getRegistryKey() != net.minecraft.world.World.NETHER) return;

        PlayerEntity target = null;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player != mc.player && player.isAlive() && !player.isCreative()) {
                if (mc.player.distanceTo(player) <= range) {
                    target = player;
                    break;
                }
            }
        }
        if (target == null) return;

        BlockPos placePos = target.getBlockPos().up();
        BlockHitResult hitResult = new BlockHitResult(new Vec3d(placePos.getX(), placePos.getY(), placePos.getZ()), Direction.UP, placePos, false);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hitResult);
    }
}

