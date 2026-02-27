package com.maskless.abyssworks.casting.patterns.actions;

import java.util.List;

import com.maskless.abyssworks.recipes.IntermediateInv;
import com.maskless.abyssworks.recipes.extraction.ExtractionRecipe;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.Vec3d;

public class Extract implements SpellAction {
	public int getArgc() {
		return 1;
	}

	@Override
	public SpellAction.Result execute(List<? extends Iota> args, CastingEnvironment ctx) {
		ItemEntity item = OperatorUtils.getItemEntity(args, 0, getArgc());

		ServerWorld world = ctx.getWorld();
		ExtractionRecipe recipe = world.getRecipeManager().getFirstMatch(
			ExtractionRecipe.Type.INSTANCE, 
			new IntermediateInv(item.getStack()), 
			world)
		.orElseThrow(() -> new MishapBadItem(item, Text.of("valid extraction target")));

		return new SpellAction.Result(
			new Spell(item, recipe), 
			recipe.getCost(), 
			List.of(ParticleSpray.burst(ctx.mishapSprayPos(), 2, 25)), 
			1
		);
	}

	public class Spell implements RenderedSpell {
		public final ItemEntity item;
		public final ExtractionRecipe recipe;

		public Spell(ItemEntity item, ExtractionRecipe recipe) {
			this.item = item;
			this.recipe = recipe;
		}

		@Override
		public void cast(CastingEnvironment ctx) {
			Vec3d pos = item.getPos();
			int count = item.getStack().getCount();
			int cost = recipe.getInput().get(0).getCount();
			
			ItemStack rem = ItemStack.EMPTY;
			if (count % cost != 0)
				rem = new ItemStack(item.getStack().getItem(), count % cost);
			item.setDespawnImmediately();

			ServerWorld world = ctx.getWorld();

			recipe.rollResults(count / cost).forEach(res -> {
				ItemScatterer.spawn(
					world, 
					pos.x,
					pos.y,
					pos.z, 
					res
				);
			});

			ItemScatterer.spawn(
				world, 
				pos.x, 
				pos.y, 
				pos.z, 
				rem
			);
		}

		@Override
		public CastingImage cast(CastingEnvironment arg0, CastingImage arg1) {
			return RenderedSpell.DefaultImpls.cast(this, arg0, arg1);
		}
	}

	@Override
	public boolean awardsCastingStat(CastingEnvironment ctx) {
		return SpellAction.DefaultImpls.awardsCastingStat(this, ctx);
	}

	@Override
	public Result executeWithUserdata(List<? extends Iota> args, CastingEnvironment env, NbtCompound userData) {
		return SpellAction.DefaultImpls.executeWithUserdata(this, args, env, userData);
	}

	@Override
	public boolean hasCastingSound(CastingEnvironment ctx) {
		return SpellAction.DefaultImpls.hasCastingSound(this, ctx);
	}

	@Override
	public OperationResult operate(CastingEnvironment arg0, CastingImage arg1, SpellContinuation arg2) {
		return SpellAction.DefaultImpls.operate(this, arg0, arg1, arg2);
	}
}
