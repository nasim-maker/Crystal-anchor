package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.Items;

public class AutoCrystal {
    private final double range = 6.0;

    public void onTick(MinecraftClient mc) {
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return;
        
        // 1. البحث عن أقرب هدف (لاعب)
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

        BlockPos targetPos = target.getBlockPos().down();

        // 2. أولاً: تفجير أي كريستال موجود فوراً وبأقصى سرعة (Break Spam)
        for (net.minecraft.entity.Entity entity : mc.world.getEntities()) {
            if (entity instanceof EndCrystalEntity && mc.player.distanceTo(entity) <= range) {
                mc.interactionManager.attackEntity(mc.player, entity);
                mc.player.swingHand(Hand.MAIN_HAND);
                // عدم التوقف هنا لعمل سبام مستمر
            }
        }

        // 3. ثانياً: إذا كنت تحمل أوبسيديان في يدك، وضعه تحت قدم الهدف فوراً
        if (mc.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.OBSIDIAN) {
            if (mc.world.getBlockState(targetPos).isAir()) {
                BlockHitResult hitResult = new BlockHitResult(new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()), Direction.UP, targetPos, false);
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hitResult);
            }
        }

        // 4. ثالثاً: إذا كان الأوبسيديان موجوداً وتحمل كريستال، اضغط سبام لوضعه (Place)
        if (mc.world.getBlockState(targetPos).getBlock() == net.minecraft.block.Blocks.OBSIDIAN) {
            BlockHitResult crystalHit = new BlockHitResult(new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()), Direction.UP, targetPos, false);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, crystalHit);
        }
    }
}
