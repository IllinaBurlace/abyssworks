package com.maskless.abyssworks.recipes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class IntermediateInv implements Inventory {
	ItemStack stack;

	public IntermediateInv(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		return stack;
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public void markDirty() {

	}

	@Override
	public ItemStack removeStack(int slot) {
		stack = ItemStack.EMPTY;
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int count) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
	}

	@Override
	public int size() {
		return 1;
	}
}

