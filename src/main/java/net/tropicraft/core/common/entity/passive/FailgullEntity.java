package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.TropicraftEntities;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FailgullEntity extends Animal implements FlyingAnimal {

    private boolean isFlockLeader;
    private static final EntityDataAccessor<Optional<UUID>> FLOCK_LEADER_UUID = SynchedEntityData.defineId(FailgullEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public FailgullEntity(EntityType<? extends FailgullEntity> type, Level world) {
        super(type, world);
        xpReward = 1;
        moveControl = new FlyingMoveControl(this, 5, true);
        setPathfindingMalus(PathType.WATER, -1.0f);
        setPathfindingMalus(PathType.COCOA, -1.0f);
        setPathfindingMalus(PathType.FENCE, -1.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.6)
                .add(Attributes.FLYING_SPEED, 0.9)
                .add(Attributes.FOLLOW_RANGE, 12.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLOCK_LEADER_UUID, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        isFlockLeader = nbt.getBoolean("IsFlockLeader");
        if (nbt.contains("FlockLeader")) {
            setFlockLeader(Optional.of(nbt.getUUID("FlockLeader")));
        } else {
            setFlockLeader(Optional.empty());
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsFlockLeader", isFlockLeader);
        entityData.get(FLOCK_LEADER_UUID).ifPresent(uuid -> nbt.putUUID("FlockLeader", uuid));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.getBlockState(pos).isAir() ? 10.0f : 0.0f;
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(0, new ValidateFlockLeader(this));
        goalSelector.addGoal(1, new SelectFlockLeader(this));
        goalSelector.addGoal(2, new SetTravelDestination());
        goalSelector.addGoal(2, new FollowLeaderGoal());
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, worldIn) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !level.isEmptyBlock(pos.below());
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(false);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    private void poop() {
        if (!level().isClientSide && level().random.nextInt(20) == 0) {
            Snowball s = new Snowball(level(), getX(), getY(), getZ());
            s.shoot(0, 0, 0, 0, 0);
            level().addFreshEntity(s);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob partner) {
        return null;
    }

    private void setIsFlockLeader(boolean isFlockLeader) {
        this.isFlockLeader = isFlockLeader;
    }

    private void setFlockLeader(Optional<UUID> flockLeaderUUID) {
        entityData.set(FLOCK_LEADER_UUID, flockLeaderUUID);
    }

    private boolean getIsFlockLeader() {
        return isFlockLeader;
    }

    private boolean hasFlockLeader() {
        return entityData.get(FLOCK_LEADER_UUID).isPresent();
    }

    @Nullable
    private Entity getFlockLeader() {
        if (level() instanceof ServerLevel && hasFlockLeader()) {
            return ((ServerLevel) level()).getEntity(entityData.get(FLOCK_LEADER_UUID).get());
        }

        return null;
    }

    @Nullable
    private BlockPos getRandomLocation() {
        RandomSource random = getRandom();
        for (int i = 0; i < 20; i++) {
            BlockPos pos = BlockPos.containing(
                    getX() + (random.nextFloat() * 2.0f - 1.0f) * 48,
                    getY() + (random.nextFloat() * 2.0f - 1.0f) * 3,
                    getZ() + (random.nextFloat() * 2.0f - 1.0f) * 48
            );
            if (level().isEmptyBlock(pos)) {
                return pos;
            }
        }

        Vec3 direction = getViewVector(0.0f);
        final float maxAngle = ((float) Math.PI / 2.0f);

        Vec3 target = HoverRandomPos.getPos(this, 40, 3, direction.x, direction.z, maxAngle, 2, 1);
        Vec3 groundPos = AirAndWaterRandomPos.getPos(this, 40, 4, -2, direction.x, direction.z, maxAngle);
        return target != null ? BlockPos.containing(target) : groundPos != null ? BlockPos.containing(groundPos) : null;
    }

    @Override
    public boolean isFlying() {
        return !onGround();
    }

    class FollowLeaderGoal extends Goal {
        FollowLeaderGoal() {
            setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
        }

        private boolean canFollow() {
            return !getIsFlockLeader() && hasFlockLeader();
        }

        @Override
        public boolean canUse() {
            return canFollow() && getNavigation().isDone() && random.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return canFollow() && getNavigation().isInProgress();
        }

        @Override
        public void start() {
            Entity flockLeader = getFlockLeader();
            PathNavigation navigator = getNavigation();
            if (flockLeader != null && flockLeader.getType() == TropicraftEntities.FAILGULL.get()) {
                navigator.moveTo(navigator.createPath(flockLeader.blockPosition(), 1), 1.0);
                return;
            }
            BlockPos Vector3d = getRandomLocation();
            if (Vector3d != null) {
                navigator.moveTo(navigator.createPath(Vector3d, 1), 1.0);
            }
        }
    }

    class SetTravelDestination extends Goal {
        SetTravelDestination() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        private boolean shouldLead() {
            return getIsFlockLeader() || !hasFlockLeader();
        }

        @Override
        public boolean canUse() {
            return shouldLead() && getNavigation().isDone() && getRandom().nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return shouldLead() && getNavigation().isInProgress();
        }

        @Override
        public void start() {
            BlockPos Vector3d = getRandomLocation();
            if (Vector3d != null) {
                PathNavigation navigator = getNavigation();
                navigator.moveTo(navigator.createPath(Vector3d, 1), 1.0);
            }
        }
    }

    private static class ValidateFlockLeader extends Goal {
        final FailgullEntity mob;

        public ValidateFlockLeader(FailgullEntity failgullEntity) {
            mob = failgullEntity;
        }

        @Override
        public boolean canUse() {
            if (mob.getIsFlockLeader()) {
                return false;
            }

            Entity flockLeader = mob.getFlockLeader();
            return flockLeader == null || !flockLeader.isAlive();
        }

        @Override
        public void start() {
            mob.setFlockLeader(Optional.empty());
        }
    }

    private static class SelectFlockLeader extends Goal {
        final FailgullEntity mob;

        public SelectFlockLeader(FailgullEntity failgullEntity) {
            mob = failgullEntity;
        }

        @Override
        public boolean canUse() {
            return !mob.hasFlockLeader();
        }

        @Override
        public void start() {
            List<FailgullEntity> list = mob.level().getEntitiesOfClass(FailgullEntity.class, mob.getBoundingBox().inflate(10, 10, 10));
            list.remove(mob);

            Optional<FailgullEntity> oldest = list.stream().min(Comparator.comparingInt(FailgullEntity::getId));
            // Found an older one nearby, set it as the flock leader
            if (oldest.isPresent() && !oldest.get().uuid.equals(mob.getUUID())) {
                FailgullEntity oldestFailgull = oldest.get();
                oldestFailgull.setIsFlockLeader(true);
                oldestFailgull.setFlockLeader(Optional.empty());
                mob.setIsFlockLeader(false);
                mob.setFlockLeader(Optional.of(oldestFailgull.getUUID()));
            }
        }
    }
}
