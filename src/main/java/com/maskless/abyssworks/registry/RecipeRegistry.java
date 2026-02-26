package com.maskless.abyssworks.registry;

import com.maskless.abyssworks.AbyssWorks;
import com.maskless.abyssworks.recipes.extraction.ExtractionRecipe;
import com.maskless.abyssworks.recipes.extraction.ExtractionRecipeSerializer;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RecipeRegistry {
	public static void initialize() {}

	public static RecipeSerializer<?> registerSerializer(
		String id,
		RecipeSerializer<?> serializerInstance
	) { return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(AbyssWorks.MOD_ID, id), serializerInstance); }

	public static RecipeType<?> registerType(
		String id,
		RecipeType<?> typeInstance
	) { return Registry.register(Registries.RECIPE_TYPE, new Identifier(AbyssWorks.MOD_ID, id), typeInstance); }
	
	public static final RecipeSerializer<?> EXTRACTION_SERIALIZER = registerSerializer("essence_extraction", ExtractionRecipeSerializer.INSTANCE); 
	public static final RecipeType<?> EXTRACTION_TYPE = registerType("essence_extraction", ExtractionRecipe.Type.INSTANCE);

}
