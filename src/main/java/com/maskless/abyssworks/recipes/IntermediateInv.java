package com.maskless.abyssworks.recipes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class IntermediateInv implements Inventory {
	ItemStack[] stack = new ItemStack[9];

	public IntermediateInv(ItemStack... stack) {
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
		return stack[slot];
	}

	@Override
	public boolean isEmpty() {
		return stack.equals(new ItemStack[9]);
	}

	@Override
	public void markDirty() {

	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack oldStack = stack[slot];
		stack[slot] = ItemStack.EMPTY;
		return oldStack;
	}

	@Override
	public ItemStack removeStack(int slot, int count) {
		stack[slot].decrement(count);
		ItemStack removed = stack[slot].copy();
		removed.setCount(count);
		return removed;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.stack[slot] = stack;
	}

	@Override
	public int size() {
		return 9;
	}
}

