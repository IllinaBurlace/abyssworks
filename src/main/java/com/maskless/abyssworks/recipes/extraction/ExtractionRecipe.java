package com.maskless.abyssworks.recipes.extraction;

import com.maskless.abyssworks.recipes.IntermediateInv;
import com.maskless.abyssworks.recipes.SpellRecipe;
import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeParams;

import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;

public class ExtractionRecipe extends SpellRecipe {
	public static class Type implements RecipeType<ExtractionRecipe>{
		private Type() {}
		public static Type INSTANCE = new Type();
	}

	public ExtractionRecipe(SpellRecipeParams params) {
		super(params, Type.INSTANCE, ExtractionRecipeSerializer.INSTANCE);
	}

	public boolean matches(IntermediateInv inv, World world) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0)
			.testWithCount(inv.getStack(0));
	}
}
