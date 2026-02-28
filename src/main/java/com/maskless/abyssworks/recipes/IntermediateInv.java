package com.maskless.abyssworks.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class IntermediateInv implements Inventory {
	ArrayList<ItemStack> stack = new ArrayList<>();

	public IntermediateInv(List<ItemStack> stacks) {
		stack.addAll(stacks);
	}

	public IntermediateInv(ItemStack... stack) {
		for (ItemStack item : stack)
			this.stack.add(item);
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
		return stack.get(slot);
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
		return stack.get(slot).copyAndEmpty();
	}

	@Override
	public ItemStack removeStack(int slot, int count) {
		stack.get(slot).decrement(count);
		ItemStack removed = stack.get(slot).copy();
		removed.setCount(count);
		return removed;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.stack.set(slot, stack);
	}

	@Override
	public int size() {
		return stack.size();
	}
}

