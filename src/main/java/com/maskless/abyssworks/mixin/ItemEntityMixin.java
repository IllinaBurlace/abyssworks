package com.maskless.abyssworks.mixin;

import net.beholderface.oneironaut.block.ThoughtSlurryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
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

import miyucomics.hexical.inits.HexicalItems;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity {
	int seedlingConversionTimer = Random.create().nextBetweenExclusive(600, 900);

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
		if (seedlingConversionTimer == 0) {
			ItemStack newStack = new ItemStack(ItemRegistry.AMETHYST_SEEDLING);
			World world = getWorld();
			Vec3d pos = getPos();
			ItemScatterer.spawn(world, pos.x, pos.y, pos.z, newStack);
			this.setDespawnImmediately();
		}
		seedlingConversionTimer--;
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
