package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.HummingbirdModel;
import net.tropicraft.core.common.entity.passive.HummingbirdEntity;

@OnlyIn(Dist.CLIENT)
public class HummingbirdRenderer extends MobRenderer<HummingbirdEntity, HummingbirdModel<HummingbirdEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/hummingbird.png");

    public HummingbirdRenderer(EntityRenderDispatcher manager) {
        super(manager, new HummingbirdModel<>(), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(HummingbirdEntity entity) {
        return TEXTURE;
    }
}
