package com.maskless.abyssworks.recipes.extraction;

import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeFactory;
import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeParams;
import com.maskless.abyssworks.recipes.SpellRecipeSerializer;

public class ExtractionRecipeSerializer extends SpellRecipeSerializer<ExtractionRecipe> {
	 private ExtractionRecipeSerializer() {
		 super(new SpellRecipeFactory<ExtractionRecipe>() {
		 	public ExtractionRecipe create(SpellRecipeParams params) {
				return new ExtractionRecipe(params);
			}
		 });
	 }

	 public static ExtractionRecipeSerializer INSTANCE = new ExtractionRecipeSerializer();
}
