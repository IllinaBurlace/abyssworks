package com.maskless.abyssworks.registry;


import com.maskless.abyssworks.AbyssWorks;
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

	public static final ActionRegistryEntry EXTRACT_ESSENCE = register("extract_essence", "qaqqqqqwaeaeaeaeaea", HexDir.EAST, new Extract());

	private static ActionRegistryEntry register(
		String name,
		String sig,
		HexDir startDir,
		Action action
	) {
		return Registry.register(HexActions.REGISTRY, new Identifier(AbyssWorks.MOD_ID, name), new ActionRegistryEntry(HexPattern.fromAngles(sig, startDir), action));
	}
}
