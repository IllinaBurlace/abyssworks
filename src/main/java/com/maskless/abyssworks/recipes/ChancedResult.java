package com.maskless.abyssworks.recipes;

import java.util.Random;

import org.spongepowered.include.com.google.gson.JsonSyntaxException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class ChancedResult {
	public static final ChancedResult EMPTY = new ChancedResult(ItemStack.EMPTY, 1);

	private static final Random r = new Random();
	private final ItemStack stack;
	private final float chance;

	public ChancedResult(ItemStack stack, float chance) {
		this.stack = stack;
		this.chance = chance;
	}

	public ItemStack getStack() {
		return stack;
	}

	public float getChance() {
		return chance;
	}

	public ItemStack rollOutput(int times) {
		int count = stack.getCount() * times;
		for (int roll = 0; roll < times; roll++) {
			if (r.nextFloat() > chance)
				count--;
		}
		if (count == 0)
			return ItemStack.EMPTY;
		ItemStack out = stack.copy();
		out.setCount(count);
		return out;
	}

	public JsonElement serialize() {
		JsonObject json = new JsonObject();
		Identifier id = Registries.ITEM.getId(stack.getItem());

		json.addProperty("item", id.toString());
		int count = stack.getCount();
		if (count != 1) 
			json.addProperty("count", count);
		if (chance != 1)
			json.addProperty("chance", chance);
		return json;
	}

	public static ChancedResult deserialize(JsonElement je) {
		if (!je.isJsonObject())
			throw new JsonSyntaxException("ChancedResult must be a json object");
		
		JsonObject json = je.getAsJsonObject();
		String itemId = JsonHelper.getString(json, "item");
		int count = JsonHelper.getInt(json, "count", 1);
		float chance = JsonHelper.hasElement(json, "chance") ? JsonHelper.getFloat(json, "chance") : 1;
		ItemStack itemStack = new ItemStack(Registries.ITEM.get(new Identifier(itemId)), count);

		return new ChancedResult(itemStack, chance);
	}

	public void write(PacketByteBuf buf) {
		buf.writeItemStack(getStack());
		buf.writeFloat(getChance());
	}

	public static ChancedResult read(PacketByteBuf buf) {
		return new ChancedResult(buf.readItemStack(), buf.readFloat());
	}
}
