package com.maskless.abyssworks.blocks;

import com.maskless.abyssworks.AbyssWorks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockRegistry {
	public static void initialize() {}

	public static Block register(Block block, String name, boolean shouldRegisterItem) {
		Identifier id = new Identifier(AbyssWorks.MOD_ID, name);

		if (shouldRegisterItem) {
			BlockItem blockItem = new BlockItem(block, new Item.Settings());
			Registry.register(Registries.ITEM, id, blockItem);
		}

		return Registry.register(Registries.BLOCK, id, block);
	}

	public static final Block AMETHYST_CROP = register(
		new AmethystCrop(AbstractBlock.Settings.create().nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.AMETHYST_CLUSTER)),
		"amethyst_crop",
		false
	);

	public static final Block PLASMOIDAL_DIRT = register(
		new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.GRASS)),
		"plasmoidal_dirt",
		true
	);
}
