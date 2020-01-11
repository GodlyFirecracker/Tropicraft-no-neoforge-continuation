package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class AshenHeldItemLayer<T extends AshenEntity, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {

    private AshenModel model;

    public AshenHeldItemLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    public void setAshenModel(final AshenModel model) {
        this.model = model;
    }

    @Override
    public void render(T ashen, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        final ItemStack blowGunHand = ashen.getHeldItemMainhand();
        final ItemStack daggerHand = ashen.getHeldItemOffhand();

        if (!blowGunHand.isEmpty() || !daggerHand.isEmpty()) {
            GlStateManager.pushMatrix();

            if (model.isChild) {
                GlStateManager.translatef(0.0F, 0.625F, 0.0F);
                GlStateManager.rotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            }

            renderHeldItem(ashen, daggerHand, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT);
            renderHeldItem(ashen, blowGunHand, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT);

            GlStateManager.popMatrix();
        }
    }

    private void renderHeldItem(AshenEntity entity, ItemStack itemstack, ItemCameraTransforms.TransformType transformType, HandSide handSide) {
        if (itemstack.isEmpty()) {
            return;
        }

        if (entity.getActionState() == AshenEntity.AshenState.HOSTILE) {
            float scale = 0.5F;
            if (handSide == HandSide.LEFT) {
                GL11.glPushMatrix();
                model.leftArm.postRender(0.0625F);
                GL11.glTranslatef(-0.375F, -0.35F, -0.125F);
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(scale, scale, scale);
                Minecraft.getInstance().getItemRenderer().renderItem(itemstack, entity, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false);

                GL11.glPopMatrix();
            } else {
                GL11.glPushMatrix();
                model.rightArm.postRender(0.0625F);

                GL11.glTranslatef(0.3F, -0.30F, -0.045F);
                GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(10F, 0.0F, 0.0F, 1.0F);

                GL11.glScalef(scale, scale, scale);
                Minecraft.getInstance().getItemRenderer().renderItem(itemstack, entity, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, false);
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    protected void translateToHand(HandSide handSide) {
        GlStateManager.translatef(0.09375F, 0.1875F, 0.0F);
    }
}
