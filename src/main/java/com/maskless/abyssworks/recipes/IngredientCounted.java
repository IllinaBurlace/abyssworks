package com.maskless.abyssworks.recipes;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.JsonHelper;

public class IngredientCounted {
	@Nullable private final Ingredient ingredient;
	@Nullable private final TagKey<Item> tag;
	private final int count;
	
	public IngredientCounted(Ingredient ingredient) {
		this.ingredient = ingredient;
		this.tag = null;
		this.count = 1;
	}
	public IngredientCounted(Ingredient ingredient, int count) {
		this.ingredient = ingredient;
		this.tag = null;
		this.count = count;
	}
	public IngredientCounted(TagKey<Item> tag) {
		this.ingredient = null;
		this.tag = tag;
		this.count = 1;
	}
	public IngredientCounted(TagKey<Item> tag, int count) {
		this.ingredient = null;
		this.tag = tag;
		this.count = count;
	}

	public static IngredientCounted fromJson(JsonObject json) {
		int count = JsonHelper.hasNumber(json, "count") ? JsonHelper.getInt(json, "count") : 1;
		return new IngredientCounted(
			Ingredient.fromJson(json),
			count
		);
	}
	public static IngredientCounted fromJson(JsonElement json) {
		return fromJson(json.getAsJsonObject());
	}
	
	public static IngredientCounted read(PacketByteBuf buf) {
		if (buf.readBoolean())
			return new IngredientCounted(Ingredient.fromPacket(buf), buf.readInt());
		return new IngredientCounted(
			TagKey.of(RegistryKeys.ITEM, buf.readIdentifier()),
			buf.readInt()
		);
	}

	public void write(PacketByteBuf buf) {
		write(buf, this);
	}
	public static void write(PacketByteBuf buf, IngredientCounted ingredient) {
		buf.writeBoolean(ingredient.tag == null);
		if (ingredient.tag == null)
			ingredient.ingredient.write(buf);
		else
			buf.writeIdentifier(ingredient.tag.id());
		buf.writeInt(ingredient.count);
	}
	
	public int getCount() { return this.count; }
	
	public boolean test(Item item) { return test(new ItemStack(item)); }
	public boolean test(ItemStack stack) {
		if (tag == null)
			return ingredient.test(stack);
		return stack.isIn(tag);
	}
	public boolean testWithCount(ItemStack stack) {
		return test(stack) && stack.getCount() >= getCount();
	}

	public boolean equals(IngredientCounted other) {
		if (other == null) return false;

		if (tag == null && other.tag == null)
			return ingredient.equals(other.ingredient) && count == other.count;
		if (ingredient == null && other.ingredient == null)
			return tag.equals(other.tag) && count == other.count;
		return false;
	}
}
