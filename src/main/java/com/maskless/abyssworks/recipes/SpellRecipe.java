package com.maskless.abyssworks.recipes;

import java.util.ArrayList;
import java.util.List;

import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeParams;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public abstract class SpellRecipe implements Recipe<IntermediateInv> {
	protected Identifier id;
	protected DefaultedList<IngredientCounted> ingredients;
	protected DefaultedList<ChancedResult> results;
	protected long mediaCost;

	private RecipeType<?> type;
	private RecipeSerializer<?> serializer;

	public SpellRecipe(SpellRecipeParams params, RecipeType<?> type, RecipeSerializer<?> serializer) {
		this.type = type;
		this.serializer = serializer;
		this.id = params.id;
		this.ingredients = params.ingredients;
		this.results = params.results;
		this.mediaCost = params.mediaCost;
	}
	
	public List<ChancedResult> getResults() {
		return results;
	}

	public List<IngredientCounted> getInput() {
		return ingredients;
	}

	public long getCost() {
		return mediaCost;
	}

	public List<ItemStack> rollResults(int times) {
		return rollResults(this.getResults(), times);
	}

	public List<ItemStack> rollResults(List<ChancedResult> rollable, int times) {
		ArrayList<ItemStack> results = new ArrayList<>();
		rollable.forEach(res -> {
			ItemStack stack = res.rollOutput(times);
			if (!stack.isEmpty())
				results.add(stack);
		});
		return results;
	}

	// busywork
	
	@Override
	public ItemStack craft(IntermediateInv inv, DynamicRegistryManager drm) {
		return getOutput(drm);
	}

	@Override 
	public boolean fits(int w, int h) {
		return true;
	}

	@Override
	public ItemStack getOutput(DynamicRegistryManager drm) {
		return getResults().isEmpty() ? ItemStack.EMPTY
			: getResults().get(0)
			.getStack();
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override 
	public String getGroup() {
		return "spell_recipes";
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return serializer;
	}

	@Override
	public RecipeType<?> getType() {
		return type;
	}
}
