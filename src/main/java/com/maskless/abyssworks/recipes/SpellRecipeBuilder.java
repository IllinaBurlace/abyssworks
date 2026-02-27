package com.maskless.abyssworks.recipes;

import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class SpellRecipeBuilder<T extends SpellRecipe> {
	protected Identifier recipeId;
	protected SpellRecipeFactory<T> factory;
	protected SpellRecipeParams params;
	
	public SpellRecipeBuilder(SpellRecipeFactory<T> factory, Identifier recipeId) {
		this.recipeId = recipeId;
		params = new SpellRecipeParams(recipeId);
		this.factory = factory;
	}

	public SpellRecipeBuilder<T> withIngredients(DefaultedList<IngredientCounted> ingredients) {
		params.ingredients = ingredients;
		return this;
	}

	public SpellRecipeBuilder<T> withResults(DefaultedList<ChancedResult> results) {
		params.results = results;
		return this;
	}

	public SpellRecipeBuilder<T> withCost(long cost) {
		params.mediaCost = cost;
		return this;
	}

	public T build() {
		return factory.create(params);
	}

	@FunctionalInterface
	public interface SpellRecipeFactory<T extends SpellRecipe> {
		T create(SpellRecipeParams params);
	}

	public static class SpellRecipeParams {
		protected Identifier id;
		protected DefaultedList<IngredientCounted> ingredients;
		protected DefaultedList<ChancedResult> results;
		protected long mediaCost;

		protected SpellRecipeParams(Identifier id) {
			this.id = id;
			ingredients = DefaultedList.of();
			results = DefaultedList.of();
			mediaCost = MediaConstants.SHARD_UNIT;
		}
	}
}
