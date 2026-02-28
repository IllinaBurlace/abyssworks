package com.maskless.abyssworks.recipes.combination;

import com.maskless.abyssworks.recipes.IntermediateInv;
import com.maskless.abyssworks.recipes.SpellRecipe;
import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeParams;

import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;

public class CombinationRecipe extends SpellRecipe {
	public static class Type implements RecipeType<CombinationRecipe> {
		private Type() {}
		public static Type INSTANCE = new Type();
	}

	public CombinationRecipe(SpellRecipeParams params) {
		super(params, Type.INSTANCE, CombinationRecipeSerializer.INSTANCE);
	}

	public boolean matches(IntermediateInv inv, World world) {
		boolean matches = !inv.isEmpty();
		for (int i = 0; i < ingredients.size(); i++) {
			boolean has = false;
			for (int j = 0; j < inv.size(); j++) {
				has |= ingredients.get(i).testWithCount(inv.getStack(j));
			}
			matches &= has;
		}
		return matches;
	}
}
