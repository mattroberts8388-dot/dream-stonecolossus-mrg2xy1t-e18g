package com.stonecolossus;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class StoneColossusEntity extends HostileEntity {
    private int slamCooldown = 0;

    public StoneColossusEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 50;
    }

    public static DefaultAttributeContainer.Builder createColossusAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 250.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0)
                .add(EntityAttributes.GENERIC_ARMOR, 12.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 8.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.9));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 16.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.slamCooldown > 0) {
            this.slamCooldown--;
        }

        if (!this.getWorld().isClient && this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            double distSq = this.squaredDistanceTo(target);
            if (distSq < 16.0 && this.slamCooldown <= 0) {
                performSlam();
                this.slamCooldown = 100;
            }
        }
    }

    private void performSlam() {
        if (this.getWorld().isClient) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld) this.getWorld();

        double radius = 5.0;
        Box area = new Box(
                this.getX() - radius, this.getY() - 2.0, this.getZ() - radius,
                this.getX() + radius, this.getY() + 3.0, this.getZ() + radius
        );

        List<LivingEntity> targets = this.getWorld().getEntitiesByClass(
                LivingEntity.class, area, e -> e != this && e.isAlive());

        DamageSource source = this.getDamageSources().mobAttack(this);
        for (LivingEntity entity : targets) {
            double dist = this.distanceTo(entity);
            if (dist <= radius) {
                entity.damage(source, 16.0f);
                Vec3d push = entity.getPos().subtract(this.getPos()).normalize().multiply(1.4, 0.9, 1.4);
                entity.setVelocity(push.x, 0.7, push.z);
                entity.velocityModified = true;
            }
        }

        serverWorld.spawnParticles(
                net.minecraft.particle.ParticleTypes.EXPLOSION,
                this.getX(), this.getY(), this.getZ(),
                12, radius / 2.0, 0.5, radius / 2.0, 0.1);

        this.getWorld().playSound(null, this.getBlockPos(),
                SoundEvents.ENTITY_GENERIC_EXPLODE, this.getSoundCategory(), 1.5f, 0.6f);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);
        int amount = 1 + this.random.nextInt(2);
        this.dropStack(new ItemStack(StoneColossusMod.COLOSSUS_CORE, amount));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(net.minecraft.entity.Entity entity) {
        // Colossus is immovable; do not get pushed.
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }
}