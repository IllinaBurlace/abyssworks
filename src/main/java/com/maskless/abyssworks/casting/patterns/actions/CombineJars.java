package com.maskless.abyssworks.casting.patterns.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.maskless.abyssworks.blocks.entities.EssenceJarEntity;
import com.maskless.abyssworks.recipes.IntermediateInv;
import com.maskless.abyssworks.recipes.combination.CombinationRecipe;
import com.maskless.abyssworks.registry.BlockEntityRegistry;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CombineJars implements SpellAction {
	@Override
	public int getArgc() {
		return 4;
	}

	@Override
	public SpellAction.Result execute(List<? extends Iota> stack, CastingEnvironment ctx) {
		SpellList jars = OperatorUtils.getList(stack, 0, getArgc());
		SpellList ratios = OperatorUtils.getList(stack, 1, getArgc());
		int count = OperatorUtils.getInt(stack, 2, getArgc());
		Vec3d outPos = OperatorUtils.getVec3(stack, 3, getArgc());

		MishapInvalidIota mishapJars = MishapInvalidIota.ofType(stack.get(0), 3, "abyssworks:jars");
		MishapInvalidIota mishapRatios = MishapInvalidIota.ofType(stack.get(1), 2, "abyssworks:ratios");

		if (jars.size() != ratios.size())
			throw mishapRatios;
		
		Function<Iota, EssenceJarEntity> grabJars = i -> grabJars(i, ctx.getWorld(), mishapJars);
		List<EssenceJarEntity> jarEntities = StreamSupport.stream(jars.spliterator(), false).map(grabJars).collect(Collectors.toList());

		Function<Iota, Integer> grabCounts = i -> grabCounts(i, mishapRatios);
		List<Integer> counts = StreamSupport.stream(ratios.spliterator(), false).map(grabCounts).collect(Collectors.toList());

		for (int i = 0; i < jarEntities.size(); i++) {
			EssenceJarEntity jar = jarEntities.get(i);
			if (jar.count < counts.get(i) * count)
				throw MishapBadBlock.of(jar.getPos(), "abyssworks:notEnoughItems");
		}

		Function<EssenceJarEntity, Item> grabItems = i -> i.essence;
		List<Item> items = jarEntities.stream().map(grabItems).collect(Collectors.toList());

		ArrayList<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			ItemStack newStack = new ItemStack(items.get(i), counts.get(i));
			stacks.add(newStack);
		}

		CombinationRecipe recipe = ctx.getWorld()
			.getRecipeManager()
			.getFirstMatch(
				CombinationRecipe.Type.INSTANCE, 
				new IntermediateInv(stacks), 
				ctx.getWorld()
			).orElseThrow(() -> mishapRatios);

		return new SpellAction.Result(
			new Spell(jarEntities, recipe, count, ctx.getWorld(), outPos), 
			recipe.getCost(), 
			List.of(ParticleSpray.burst(ctx.mishapSprayPos(), 2, 25)), 
			1
		);

	}

	public EssenceJarEntity grabJars(Iota iota, ServerWorld world, MishapInvalidIota mishap) {
		if (iota.getType() != Vec3Iota.TYPE)
			throw mishap;

		Vec3d vec = ((Vec3Iota)iota).getVec3();
		BlockPos pos = new BlockPos(
			(int)Math.floor(vec.x),
			(int)Math.floor(vec.y),
			(int)Math.floor(vec.z)
		);

		return world.getBlockEntity(pos, BlockEntityRegistry.ESSENCE_JAR)
			.orElseThrow(() -> mishap);
	}

	public int grabCounts(Iota iota, MishapInvalidIota mishap) {
		if (iota.getType() != DoubleIota.TYPE)
			throw mishap;
		return (int)((DoubleIota)iota).getDouble();
	}

	public class Spell implements RenderedSpell {
		List<EssenceJarEntity> jars;
		CombinationRecipe recipe;
		int count;
		ServerWorld world;
		Vec3d pos;

		public Spell(
			List<EssenceJarEntity> jars,
			CombinationRecipe recipe,
			int count,
			ServerWorld world,
			Vec3d pos
		) {
			this.jars = jars;
			this.recipe = recipe;
			this.count = count;
			this.world = world;
			this.pos = pos;
		}

		public void cast(CastingEnvironment ctx) {
			recipe.getInput().forEach(input -> {
				jars.forEach(jar -> {
					if (input.test(jar.essence)) {
						jar.count -= input.getCount() * count;
						jar.markDirty();
					}
				});
			});

			recipe.rollResults(count).forEach(res -> {
				ItemScatterer.spawn(
					world, 
					pos.x, 
					pos.y, 
					pos.z, 
					res
				);
			});
		}
		
		public CastingImage cast(CastingEnvironment ctx, CastingImage image) {
			return RenderedSpell.DefaultImpls.cast(this, ctx, image);
		}
	}

	@Override
	public boolean awardsCastingStat(CastingEnvironment env) {
		return SpellAction.DefaultImpls.awardsCastingStat(this, env);
	}

	@Override
	public boolean hasCastingSound(CastingEnvironment env) {
		return SpellAction.DefaultImpls.hasCastingSound(this, env);
	}

	@Override
	public SpellAction.Result executeWithUserdata(List<? extends Iota> stack, CastingEnvironment env, NbtCompound userData) {
		return SpellAction.DefaultImpls.executeWithUserdata(this, stack, env, userData);
	}

	@Override
	public OperationResult operate(CastingEnvironment env, CastingImage image, SpellContinuation continuation) {
		return SpellAction.DefaultImpls.operate(this, env, image, continuation);
	}
}
