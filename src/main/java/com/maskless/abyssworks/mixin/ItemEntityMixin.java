package com.maskless.abyssworks.mixin;

import net.beholderface.oneironaut.block.ThoughtSlurryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import miyucomics.hexical.inits.HexicalItems;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity {
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
		ItemStack newStack = new ItemStack(net.minecraft.item.Items.AMETHYST_SHARD);
		this.setStack(newStack);
	}

	@Shadow
	abstract public ItemStack getStack();

	@Shadow 
	abstract public void setStack(ItemStack item);

	public ItemEntityMixin(EntityType<? extends ItemEntity> type, World world) {
		super(type, world);
	}
}
