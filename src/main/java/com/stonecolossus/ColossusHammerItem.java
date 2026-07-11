package com.stonecolossus;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;

import java.util.List;
import java.util.UUID;

public class ColossusHammerItem extends Item {
    private static final UUID ATTACK_DAMAGE_MODIFIER_ID = UUID.fromString("cb3f55d3-645c-4f38-a497-9c13a33db5cf");
    private static final UUID ATTACK_SPEED_MODIFIER_ID = UUID.fromString("fa233e1c-4180-4865-b01b-bcce9785aca3");

    public ColossusHammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));

        World world = attacker.getWorld();
        if (!world.isClient) {
            double radius = 3.0;
            Box area = target.getBoundingBox().expand(radius);
            List<LivingEntity> nearby = world.getEntitiesByClass(LivingEntity.class, area,
                    e -> e != attacker && e != target && e.isAlive());
            for (LivingEntity entity : nearby) {
                entity.damage(attacker.getDamageSources().mobAttack(attacker), 8.0f);
                Vec3d push = entity.getPos().subtract(target.getPos()).normalize().multiply(1.0, 0.4, 1.0);
                entity.setVelocity(push.x, 0.4, push.z);
                entity.velocityModified = true;
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 14.0,
                            EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
                    new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.2,
                            EntityAttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot);
    }
}