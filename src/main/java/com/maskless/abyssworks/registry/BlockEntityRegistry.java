package com.maskless.abyssworks.registry;

import com.maskless.abyssworks.blocks.entities.EssenceJarEntity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityRegistry {
	public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("abyssworks", path), blockEntityType);
	}

	public static final BlockEntityType<EssenceJarEntity> ESSENCE_JAR = register(
		"essence_jar", 
		BlockEntityType.Builder.create(EssenceJarEntity::new, BlockRegistry.ESSENCE_JAR).build(null)
	);

	public static void initialize() {

	}
}
