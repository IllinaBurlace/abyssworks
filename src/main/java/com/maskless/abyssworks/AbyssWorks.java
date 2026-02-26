package com.maskless.abyssworks;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maskless.abyssworks.registry.*;


public class AbyssWorks implements ModInitializer {
	public static final String MOD_ID = "abyssworks";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ItemRegistry.initialize();
		BlockRegistry.initialize();
		BlockEntityRegistry.initialize();
		PatternRegistry.initialize();
		RecipeRegistry.initialize();
	}
}

