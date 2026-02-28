package com.maskless.abyssworks.casting.patterns.actions;

import java.util.ArrayList;
import java.util.List;

import com.maskless.abyssworks.recipes.IntermediateInv;
import com.maskless.abyssworks.recipes.combination.CombinationRecipe;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.SpellList.SpellListIterator;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.Vec3d;

public class CombineEntities implements SpellAction {
	@Override
	public int getArgc() {
		return 2;
	}

	public SpellAction.Result execute(List<? extends Iota> stack, CastingEnvironment ctx) {
		SpellList list = OperatorUtils.getList(stack, 0, getArgc());
		if (list.size() == 0) throw MishapInvalidIota.ofType(stack.get(0), 1, "abyssworks:itemlist.valid");
		SpellListIterator it = list.iterator();
		ArrayList<ItemEntity> items = new ArrayList<>();
		ArrayList<ItemStack> inputs = new ArrayList<>();

		while (it.hasNext()) {
			Iota i = it.next();
			MishapInvalidIota mishapBadList = MishapInvalidIota.ofType(i, 1, "abyssworks:itemlist.valid");
			if (i.getType() != EntityIota.TYPE)
				throw mishapBadList;

			EntityIota ent = (EntityIota)i;
			if (ent.getEntity() instanceof ItemEntity item) {
				items.add(item);
				inputs.add(item.getStack());
			}
			else throw mishapBadList;
		}

		ServerWorld world = ctx.getWorld();

		MishapInvalidIota mishapBadRecipe = MishapInvalidIota.ofType(list.getCar(), 1, "abyssworks:combrecipe.valid");

		CombinationRecipe recipe = world.getRecipeManager().getFirstMatch(
			CombinationRecipe.Type.INSTANCE, 
			new IntermediateInv(List.copyOf(inputs)), 
			world
		).orElseThrow(() -> mishapBadRecipe);

		Vec3d outPos = OperatorUtils.getVec3(stack, 1, getArgc());

		return new SpellAction.Result(
			new Spell(items, world, recipe, outPos), 
			recipe.getCost(), 
			List.of(ParticleSpray.burst(ctx.mishapSprayPos(), 2, 25)), 
			1
		);
	}

	public class Spell implements RenderedSpell {
		List<ItemEntity> items;
		ServerWorld world;
		CombinationRecipe recipe;
		Vec3d outPos;

		public Spell(
			List<ItemEntity> items,
			ServerWorld world,
			CombinationRecipe recipe,
			Vec3d outPos
		) {
			this.items = items;
			this.world = world;
			this.recipe = recipe;
			this.outPos = outPos;
		}

		public void cast(CastingEnvironment ctx) {
			items.forEach(item -> {
				item.setDespawnImmediately();
			});

			ItemScatterer.spawn(
				world, 
				outPos.x, 
				outPos.y, 
				outPos.z, 
				recipe.rollResults(1).get(0)
			);
		}

		public CastingImage cast(CastingEnvironment env, CastingImage image) {
			return RenderedSpell.DefaultImpls.cast(this, env, image);
		}
	}

	@Override 
	public boolean awardsCastingStat(CastingEnvironment env) {
		return SpellAction.DefaultImpls.awardsCastingStat(this, env);
	}

	@Override
	public SpellAction.Result executeWithUserdata(List<? extends Iota> args, CastingEnvironment env, NbtCompound userData) {
		return SpellAction.DefaultImpls.executeWithUserdata(this, args, env, userData);
	}

	@Override
	public boolean hasCastingSound(CastingEnvironment env) {
		return SpellAction.DefaultImpls.hasCastingSound(this, env);
	}

	@Override
	public OperationResult operate(CastingEnvironment env, CastingImage image, SpellContinuation continuation) {
		return SpellAction.DefaultImpls.operate(this, env, image, continuation);
	}
}
