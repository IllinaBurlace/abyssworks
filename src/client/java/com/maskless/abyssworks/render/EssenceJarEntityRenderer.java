package com.maskless.abyssworks.render;

import com.maskless.abyssworks.blocks.entities.EssenceJarEntity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class EssenceJarEntityRenderer implements BlockEntityRenderer<EssenceJarEntity> {
	public EssenceJarEntityRenderer (BlockEntityRendererFactory.Context ctx) {

	}

	@Override
	public void render(EssenceJarEntity bE, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		ItemStack item = bE.essence.getDefaultStack();
		item.setCount(bE.count);

		double offset = Math.sin((bE.getWorld().getTime() + tickDelta) / 8.0 ) / 6.0;

		matrices.translate(0.5, 0.3 + offset, 0.5);

		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((bE.getWorld().getTime() + tickDelta) * 4));
		
		matrices.scale(0.6f, 0.6f, 0.6f);

		MinecraftClient.getInstance().getItemRenderer().renderItem(item, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, bE.getWorld(), 0);

		matrices.pop();
	}
}
