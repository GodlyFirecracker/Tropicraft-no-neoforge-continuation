package net.tropicraft.core.client.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.basilisk.BasiliskLizardEntity;

public class BasiliskLizardModel<T extends BasiliskLizardEntity> extends HierarchicalModel<T> {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    private static final float BACK_LEG_ANGLE = 65.0f * ModelAnimator.DEG_TO_RAD;
    private static final float FRONT_LEG_ANGLE = -40.0f * ModelAnimator.DEG_TO_RAD;

    private final ModelPart root;
    private final ModelPart body_base;
    private final ModelPart leg_back_left;
    private final ModelPart leg_front_left;
    private final ModelPart head_base;
    private final ModelPart tail_base;
    private final ModelPart tail_tip;
    private final ModelPart leg_back_right;
    private final ModelPart leg_front_right;

    public BasiliskLizardModel(ModelPart root) {
        this.root = root;
        body_base = root.getChild("body_base");
        leg_back_left = body_base.getChild("leg_back_left");
        leg_front_left = body_base.getChild("leg_front_left");
        head_base = body_base.getChild("head_base");
        tail_base = body_base.getChild("tail_base");
        tail_tip = tail_base.getChild("tail_tip");
        leg_back_right = body_base.getChild("leg_back_right");
        leg_front_right = body_base.getChild("leg_front_right");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0f, -1.0f, -5.0f, 2.0f, 2.0f, 6.0f, false),
                PartPose.offsetAndRotation(0.0f, 22.5f, 1.6f, -15.0f * ModelAnimator.DEG_TO_RAD, 0.0f, 0.0f));

        body.addOrReplaceChild("sail_back",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(0.0f, -2.0f, 0.0f, 0.0f, 2.0f, 6.0f, false),
                PartPose.offsetAndRotation(0.0f, -1.0f, -5.0f, -2.5f * ModelAnimator.DEG_TO_RAD, 0.0f, 0.0f));

        body.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(5, 25)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 4.0f, 1.0f, false),
                PartPose.offsetAndRotation(1.0f, 0.0f, 0.0f, BACK_LEG_ANGLE, 0.0f, -77.5f * ModelAnimator.DEG_TO_RAD));

        body.addOrReplaceChild("leg_front_left",
                CubeListBuilder.create()
                        .texOffs(15, 25)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 3.0f, 1.0f, false),
                PartPose.offsetAndRotation(1.0f, 0.5f, -4.0f, FRONT_LEG_ANGLE, 40.0f * ModelAnimator.DEG_TO_RAD, -57.5f * ModelAnimator.DEG_TO_RAD));

        PartDefinition modelPartHead = body.addOrReplaceChild("head_base",
                CubeListBuilder.create().mirror(false)
                        .texOffs(0, 18)
                        .addBox(-1.0f, -1.0f, -3.0f, 2.0f, 2.0f, 3.0f, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(0.0f, -1.0f, -5.0f, 7.5f * ModelAnimator.DEG_TO_RAD, 0.0f, 0.0f));

        modelPartHead.addOrReplaceChild("sail_head",
                CubeListBuilder.create()
                        .texOffs(20, 18)
                        .addBox(0.0f, -3.0f, 0.0f, 0.0f, 3.0f, 3.0f, false),
                PartPose.offsetAndRotation(0.0f, -1.0f, -2.0f, -20.0f * ModelAnimator.DEG_TO_RAD, 0.0f, 0.0f));

        PartDefinition modelPartTail = body.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(13, 9)
                        .addBox(-0.5f, -2.0f, 0.0f, 1.0f, 2.0f, 4.0f, false),
                PartPose.offsetAndRotation(0.0f, 1.0f, 1.0f, 5.0f * ModelAnimator.DEG_TO_RAD, 0.0f, 0.0f));

        modelPartTail.addOrReplaceChild("sail_tail",
                CubeListBuilder.create()
                        .texOffs(11, 18)
                        .addBox(0.0f, -2.0f, 0.0f, 0.0f, 2.0f, 4.0f, false),
                PartPose.offsetAndRotation(0.0f, -2.0f, 0.0f, -2.5f * ModelAnimator.DEG_TO_RAD, 0.0f, 0.0f));

        modelPartTail.addOrReplaceChild("tail_tip",
                CubeListBuilder.create()
                        .texOffs(17, 0)
                        .addBox(-0.5f, -1.0f, 0.0f, 1.0f, 1.0f, 6.0f, false),
                PartPose.offsetAndRotation(0.0f, -1.0f, 4.0f, 5.0f * ModelAnimator.DEG_TO_RAD, 0.0f, 0.0f));

        body.addOrReplaceChild("leg_back_right",
                CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 4.0f, 1.0f, false),
                PartPose.offsetAndRotation(-1.0f, 0.0f, 0.0f, BACK_LEG_ANGLE, 0.0f, 77.5f * ModelAnimator.DEG_TO_RAD));

        body.addOrReplaceChild("leg_front_right",
                CubeListBuilder.create()
                        .texOffs(10, 25)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 3.0f, 1.0f, false),
                PartPose.offsetAndRotation(-1.0f, 0.5f, -4.0f, FRONT_LEG_ANGLE, -40.0f * ModelAnimator.DEG_TO_RAD, 57.5f * ModelAnimator.DEG_TO_RAD));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        float running = entity.getRunningAnimation(CLIENT.getTimer().getGameTimeDeltaTicks());
        body_base.xRot = Mth.lerp(running, -15.0f, -50.0f) * ModelAnimator.DEG_TO_RAD;
        tail_base.xRot = Mth.lerp(running, 5.0f, 30.0f) * ModelAnimator.DEG_TO_RAD;
        tail_tip.xRot = Mth.lerp(running, 5.0f, 20.0f) * ModelAnimator.DEG_TO_RAD;
        head_base.xRot = Mth.lerp(running, 7.5f, 35.0f) * ModelAnimator.DEG_TO_RAD;

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.25f, limbSwingAmount)) {
            leg_front_left.xRot = FRONT_LEG_ANGLE + walk.eval(-1.0f, 1.0f, 0.0f, 1.0f);
            leg_front_right.xRot = FRONT_LEG_ANGLE + walk.eval(1.0f, 1.0f, 0.0f, 1.0f);
            leg_back_left.xRot = BACK_LEG_ANGLE + walk.eval(-1.0f, -0.9f, 0.0f, -0.9f);
            leg_back_right.xRot = BACK_LEG_ANGLE + walk.eval(1.0f, -0.9f, 0.0f, -0.9f);

            body_base.xRot += walk.eval(0.5f, running * 0.1f);
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
