package com.maskless.abyssworks.recipes;

import java.util.ArrayList;
import java.util.List;

import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeParams;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public abstract class SpellRecipe implements Recipe<IntermediateInv> {
	protected Identifier id;
	protected DefaultedList<Ingredient> ingredients;
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

	public List<Ingredient> getInput() {
		return ingredients;
	}

	public long getCost() {
		return mediaCost;
	}

	public List<ItemStack> rollResults() {
		return rollResults(this.getResults());
	}

	public List<ItemStack> rollResults(List<ChancedResult> rollable) {
		List<ItemStack> results = new ArrayList<>();
		for (int i = 0; i < rollable.size(); i++) {
			ChancedResult output = rollable.get(i);
			ItemStack stack = output.rollOutput();
			if (!stack.isEmpty())
				results.add(stack);
		}
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
