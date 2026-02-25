package com.maskless.abyssworks.blocks.entities;

import com.maskless.abyssworks.items.Essences;

import com.maskless.abyssworks.AbyssWorks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EssenceJarEntity extends BlockEntity {
	public Item essence = Items.AIR;
	public int count = 0;

	public int lastClickTime = 10;

	public EssenceJarEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistry.ESSENCE_JAR, pos, state);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putString("essence", Registries.ITEM.getId(essence).toString());
		nbt.putInt("count", count);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		essence = Registries.ITEM.get(Identifier.splitOn(nbt.getString("essence"), ':'));
		count = nbt.getInt("count");
		AbyssWorks.LOGGER.info("read NBT");
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	public ActionResult insert(PlayerEntity player, Hand hand) {
		if (lastClickTime < 10 && hand == Hand.MAIN_HAND) {
			PlayerInventory inv = player.getInventory();
			for (int idx = 0; idx < inv.size(); idx++) {
				ItemStack current = inv.getStack(idx);
				if (!current.isOf(essence)) continue;

				count += current.getCount();
				inv.setStack(idx, ItemStack.EMPTY);
			}
			lastClickTime = 0;
			markDirty();
			return ActionResult.success(true);
		}
		lastClickTime = 0;
		ItemStack item = player.getStackInHand(hand);
		for (Item i : Essences.ESSENCES) {
			if (item.getItem() != i) continue;
			if (essence != Items.AIR && essence != item.getItem()) break;
			if (essence == Items.AIR)
				essence = item.getItem();
			count += item.getCount();
			player.setStackInHand(hand, ItemStack.EMPTY);
			markDirty();
			return ActionResult.success(hand == Hand.MAIN_HAND);
			
		}
		return ActionResult.FAIL;
	}

	public void extract(PlayerEntity player) {
		int outCount = player.isSneaking() ? 64 : 1;
		outCount = Math.min(outCount, count);
		PlayerInventory inv = player.getInventory();
		ItemStack itemStack = essence.getDefaultStack();
		itemStack.setCount(outCount);
		boolean success = inv.insertStack(itemStack);
		if (success)
			count -= outCount;
		if (count <= 0) {
			essence = Items.AIR;
			count = 0;
		}
		markDirty();
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if (lastClickTime < 10) {
			lastClickTime++;
		}
	}
}
