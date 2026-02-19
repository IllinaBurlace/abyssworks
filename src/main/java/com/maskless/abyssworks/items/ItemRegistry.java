package com.maskless.abyssworks.items;

import com.maskless.abyssworks.AbyssWorks;
import com.maskless.abyssworks.blocks.BlockRegistry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {
	public static void initialize() {}
	
	public static Item register(Item item, String id) {
		Identifier itemID = new Identifier(AbyssWorks.MOD_ID, id);

		Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

		return registeredItem;
	}

	public static final Item AMETHYST_SEEDLING = register(
			new AliasedBlockItem(BlockRegistry.AMETHYST_CROP, new FabricItemSettings()),
			"amethyst_seedling"
			);
}
