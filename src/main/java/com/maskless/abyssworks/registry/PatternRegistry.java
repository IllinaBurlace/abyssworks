package com.maskless.abyssworks.registry;


import com.maskless.abyssworks.AbyssWorks;
import com.maskless.abyssworks.casting.patterns.actions.CombineEntities;
import com.maskless.abyssworks.casting.patterns.actions.Extract;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PatternRegistry {
	public static void initialize() {
	}

	private static ActionRegistryEntry register(
		String name,
		String sig,
		HexDir startDir,
		Action action
	) {
		return Registry.register(HexActions.REGISTRY, new Identifier(AbyssWorks.MOD_ID, name), new ActionRegistryEntry(HexPattern.fromAngles(sig, startDir), action));
	}

	public static final ActionRegistryEntry EXTRACT_ESSENCE = register("extract_essence", "qaqqqqqwaeaeaeaeaea", HexDir.EAST, new Extract());

	public static final ActionRegistryEntry COMBINE_ENTITIES = register("combine_entities", "qaqwawqwwawwqwwa", HexDir.NORTH_EAST, new CombineEntities());
}
