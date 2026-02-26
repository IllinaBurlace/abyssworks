package com.maskless.abyssworks.recipes;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.maskless.abyssworks.recipes.SpellRecipeBuilder.SpellRecipeFactory;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class SpellRecipeSerializer<T extends SpellRecipe> implements RecipeSerializer<T> {
	private final SpellRecipeFactory<T> factory;
	
	public SpellRecipeSerializer(SpellRecipeFactory<T> factory) {
		this.factory = factory;
	}

	@Override
	public T read(Identifier id, JsonObject json) {
		SpellRecipeBuilder<T> builder = new SpellRecipeBuilder<>(factory, id); 
		DefaultedList<Ingredient> ingredients = DefaultedList.of();
		DefaultedList<ChancedResult> results = DefaultedList.of();

		for (JsonElement je : JsonHelper.getArray(json, "ingredients")) {
			ingredients.add(Ingredient.fromJson(je));
		}

		for (JsonElement je : JsonHelper.getArray(json, "results")) {
			results.add(ChancedResult.deserialize(je));
		}

		builder.withIngredients(ingredients)
			.withResults(results);

		if (JsonHelper.hasElement(json, "mediaCost"))
			builder.withCost(JsonHelper.getLong(json, "mediaCost"));

		T recipe = builder.build();
		return recipe;
	}

	@Override
	public T read(Identifier id, PacketByteBuf buf) {
		DefaultedList<Ingredient> ingredients = DefaultedList.of();
		DefaultedList<ChancedResult> results = DefaultedList.of();

		int size = buf.readInt();
		for (int i = 0; i < size; i++)
			ingredients.add(Ingredient.fromPacket(buf));

		size = buf.readInt();
		for (int i = 0; i < size; i++)
			results.add(ChancedResult.read(buf));

		T recipe = new SpellRecipeBuilder<>(factory, id).withIngredients(ingredients)
			.withResults(results)
			.withCost(buf.readLong())
			.build();
		return recipe;
	}

	@Override
	public void write(PacketByteBuf buf, T recipe) {
		List<Ingredient> ingredients = recipe.ingredients;
		List<ChancedResult> results = recipe.results;

		buf.writeInt(ingredients.size());
		ingredients.forEach(i -> i.write(buf));

		buf.writeInt(results.size());
		results.forEach(o -> o.write(buf));

		buf.writeLong(recipe.getCost());
	}
}
