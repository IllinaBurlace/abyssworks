package com.maskless.abyssworks.mixin;

import net.beholderface.oneironaut.block.ThoughtSlurryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.maskless.abyssworks.items.ItemRegistry;

import at.petrak.hexcasting.api.pigment.ColorProvider;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import miyucomics.hexical.inits.HexicalItems;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity {
	Random random = Random.create();
	int seedlingTarget = random.nextBetweenExclusive(600, 900);
	int seedlingConversionTimer = 0;

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void GummiesToAmethyst(CallbackInfo ci) {
		if (this.getStack().getItem() != HexicalItems.INSTANCE.getHEX_GUMMY()) {
			return;
		}
		BlockState state = super.getBlockStateAtPos();
		Block block = state.getBlock();
		if (block != ThoughtSlurryBlock.INSTANCE) {
			return;
		}
		if (seedlingConversionTimer == seedlingTarget) {
			ItemStack newStack = new ItemStack(ItemRegistry.AMETHYST_SEEDLING);
			World world = getWorld();
			Vec3d pos = getPos();
			ItemScatterer.spawn(world, pos.x, pos.y, pos.z, newStack);
			world.playSound(pos.x, pos.y, pos.z, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f, true);
			this.setDespawnImmediately();
		}
		Vec3d pos = getPos();
		World world = getWorld();
		ColorProvider colorProvider = FrozenPigment.DEFAULT.get().getColorProvider();
		if (random.nextFloat() < 0.17f) {
			world.playSound(pos.x, pos.y, pos.z, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f, true);
		}
		world.addParticle(
			new ConjureParticleOptions(colorProvider.getColor(((float)world.getTime()), pos)),
			pos.x, pos.y + 0.5, pos.z,
			world.random.nextFloat() * 0.05 * world.random.nextBetween(-1, 1), world.random.nextFloat() * 0.05, world.random.nextFloat() * 0.05 * world.random.nextBetween(-1, 1)
		);
		seedlingConversionTimer++;
	}

	@Shadow
	abstract public ItemStack getStack();

	@Shadow 
	abstract public void setStack(ItemStack item);

	@Shadow
	abstract public void setDespawnImmediately();

	public ItemEntityMixin(EntityType<? extends ItemEntity> type, World world) {
		super(type, world);
	}
}
