package com.maskless.abyssworks.blocks;

import java.util.List;

import com.maskless.abyssworks.blocks.entities.EssenceJarEntity;
import com.maskless.abyssworks.blocks.entities.BlockEntityRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class EssenceJar extends BlockWithEntity {

	public static final VoxelShape footprint = VoxelShapes.combine(
		Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D),
		Block.createCuboidShape(4.0D, 12.0D, 4.0D, 12.0D, 15.0D, 12.0D),
		BooleanBiFunction.OR
	);

	public EssenceJar(Settings settings) {
		super(settings);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return footprint;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return footprint;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EssenceJarEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext context) {
		NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
		if (nbt == null) return;
		String name = nbt.getString("essence").substring(19);
		name = ("" +name.charAt(0)).toUpperCase() + name.substring(1);
		tooltip.add(Text.literal("Contains: " + name + " Essence x" + nbt.getInt("count")));
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		EssenceJarEntity entity = (EssenceJarEntity)world.getBlockEntity(pos);
		if (entity == null)
			return ActionResult.FAIL;

		ActionResult result = entity.insert(player, hand);

		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		EssenceJarEntity entity = (EssenceJarEntity)world.getBlockEntity(pos);
		if (entity == null) {
			return;
		}

		entity.extract(player);
		
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker (World world, BlockState state, BlockEntityType<T> type) {
		if (type == BlockEntityRegistry.ESSENCE_JAR) return (a, b, c, bE) -> ((EssenceJarEntity)bE).tick(a, b, c);
		return null;
	}
}
