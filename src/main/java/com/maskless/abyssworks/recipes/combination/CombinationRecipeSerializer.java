package com.maskless.abyssworks.recipes.combination;

import com.maskless.abyssworks.recipes.SpellRecipeSerializer;
import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeParams;
import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeFactory;

public class CombinationRecipeSerializer extends SpellRecipeSerializer<CombinationRecipe> {
	private CombinationRecipeSerializer() {
		super(new SpellRecipeFactory<CombinationRecipe>() {
			public CombinationRecipe create(SpellRecipeParams params) {
				return new CombinationRecipe(params);
			}
		});
	}

	public static CombinationRecipeSerializer INSTANCE = new CombinationRecipeSerializer();
}
