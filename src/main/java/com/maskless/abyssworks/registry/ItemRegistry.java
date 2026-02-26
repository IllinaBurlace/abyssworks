package com.maskless.abyssworks.registry;

import com.maskless.abyssworks.AbyssWorks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemRegistry {
	public static void initialize() {
		Registry.register(Registries.ITEM_GROUP, ABYSSWORKS_KEY, ABYSSWORKS_ITEM_GROUP);

		ItemGroupEvents.modifyEntriesEvent(ABYSSWORKS_KEY).register(itemGroup -> {
			itemGroup.add(ItemRegistry.AMETHYST_SEEDLING);
			itemGroup.add(ItemRegistry.ESSENCE_HEX);
			itemGroup.add(ItemRegistry.ESSENCE_EARTH);
			itemGroup.add(ItemRegistry.ESSENCE_AIR);
			itemGroup.add(ItemRegistry.ESSENCE_WATER);
			itemGroup.add(ItemRegistry.ESSENCE_FIRE);
			itemGroup.add(ItemRegistry.ESSENCE_METAL);
			itemGroup.add(ItemRegistry.ESSENCE_PRECIOUS);
			itemGroup.add(ItemRegistry.ESSENCE_LIFE);
			itemGroup.add(ItemRegistry.ESSENCE_END);
			itemGroup.add(BlockRegistry.ESSENCE_JAR.asItem());
		});
	}
	
	public static final RegistryKey<ItemGroup> ABYSSWORKS_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier(AbyssWorks.MOD_ID, "abyssworks"));
	public static final ItemGroup ABYSSWORKS_ITEM_GROUP = FabricItemGroup.builder()
		.icon(() -> new ItemStack(ItemRegistry.ESSENCE_HEX))
		.displayName(Text.translatable("itemGroup.abyssworks"))
		.build();

	public static Item register(Item item, String id) {
		Identifier itemID = new Identifier(AbyssWorks.MOD_ID, id);

		Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

		return registeredItem;
	}

	public static final Item AMETHYST_SEEDLING = register(
		new AliasedBlockItem(BlockRegistry.AMETHYST_CROP, new FabricItemSettings()),
		"amethyst_seedling"
	);
	public static final Item ESSENCE_HEX = register(
		new Item(new FabricItemSettings()),
		"essence_hex"
	);
	public static final Item ESSENCE_EARTH = register(
		new Item(new FabricItemSettings()),
		"essence_earth"
	);
	public static final Item ESSENCE_AIR = register(
		new Item(new FabricItemSettings()),
		"essence_air"
	);
	public static final Item ESSENCE_WATER = register(
		new Item(new FabricItemSettings()),
		"essence_water"
	);
	public static final Item ESSENCE_FIRE = register(
		new Item(new FabricItemSettings()),
		"essence_fire"
	);
	public static final Item ESSENCE_METAL = register(
		new Item(new FabricItemSettings()),
		"essence_metal"
	);
	public static final Item ESSENCE_PRECIOUS = register(
		new Item(new FabricItemSettings()),
		"essence_precious"
	);
	public static final Item ESSENCE_LIFE = register(
		new Item(new FabricItemSettings()),
		"essence_life"
	);
	public static final Item ESSENCE_END = register(
		new Item(new FabricItemSettings()),
		"essence_end"
	);
}
