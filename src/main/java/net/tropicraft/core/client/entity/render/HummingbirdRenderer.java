package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.HummingbirdModel;
import net.tropicraft.core.common.entity.passive.HummingbirdEntity;

@OnlyIn(Dist.CLIENT)
public class HummingbirdRenderer extends MobRenderer<HummingbirdEntity, HummingbirdModel<HummingbirdEntity>> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/hummingbird.png");

    public HummingbirdRenderer(EntityRendererProvider.Context context) {
        super(context, new HummingbirdModel<>(context.bakeLayer(TropicraftRenderLayers.HUMMINGBIRD_LAYER)), 0.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(HummingbirdEntity entity) {
        return TEXTURE;
    }
}
