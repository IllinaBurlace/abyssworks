package com.maskless.abyssworks;

import com.maskless.abyssworks.registry.BlockRegistry;
import com.maskless.abyssworks.registry.BlockEntityRegistry;
import com.maskless.abyssworks.render.EssenceJarEntityRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class AbyssWorksClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegistry.AMETHYST_CROP, BlockRegistry.ESSENCE_JAR);
		BlockEntityRendererFactories.register(BlockEntityRegistry.ESSENCE_JAR, EssenceJarEntityRenderer::new);
	}
}
