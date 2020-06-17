package com.tntmodders.takumi.entity.item;

/*public class EntityAttackBlock extends EntityLiving {
    public static final List<Class<? extends Entity>> ENGINEERS = new ArrayList<>();
    public static final List<Class<? extends Entity>> ARTILLERIES = new ArrayList<>();

    static {
        ENGINEERS.add(EntityIceCreeper.class);
        ENGINEERS.add(EntitySpongeCreeper.class);
        ENGINEERS.add(EntitySandCreeper.class);
        ENGINEERS.add(EntityTerracottaCreeper.class);

        ARTILLERIES.add(EntityCannonCreeper.class);
        ARTILLERIES.add(EntitySkeletonCreeper.class);
        ARTILLERIES.add(EntitySnowCreeper.class);
        ARTILLERIES.add(EntityStrayCreeper.class);
    }

    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.attackblock.name"),
                    BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_20).setDarkenSky(true);
    private final List<ITakumiEntity> entities = new ArrayList<>();
    private int spawnTick;

    public List<EntityLivingBase> spawns = new ArrayList<>();

    public EntityAttackBlock(World worldIn) {
        super(worldIn);
        this.setSize(1, 2);
        this.isImmuneToFire = true;

        TakumiEntityCore.getEntityList().forEach(iTakumiEntity -> {
            if (iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ||
                    iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.MID) {
                if (iTakumiEntity.getClass() != EntitySeaGuardianCreeper.class &&
                        iTakumiEntity.getClass() != EntitySquidCreeper.class &&
                        ((EntityLivingBase) iTakumiEntity).isNonBoss() &&
                        iTakumiEntity.getClass() != EntityEvokerCreeper.class &&
                        iTakumiEntity.getClass() != EntityVexCreeper.class &&
                        iTakumiEntity.getClass() != EntityRoboCreeper.class &&
                        iTakumiEntity.getClass() != EntityRoboCreeper_Evo.class) {
                    entities.add(iTakumiEntity);
                }
            }
        });
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5000);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        this.spawnTick++;
        this.world.setBlockToAir(this.getPosition());
        this.world.setBlockToAir(this.getPosition().up());
        if (this.ticksExisted % 200 == 0) {
            List<Entity> list = new ArrayList<>(this.world.loadedEntityList);
            list.removeIf(entity -> this.getDistanceSqToEntity(entity) > 10000 || !(entity instanceof EntityLivingBase));
            list.sort(Comparator.comparingDouble(EntityAttackBlock.this::getDistanceSqToEntity));
            list.forEach(entity -> {
                if (entity instanceof EntityCreeper) {
                    if (((EntityCreeper) entity).getAttackTarget() == null) {
                        ((EntityCreeper) entity).setAttackTarget(this);
                    }
                }
            });
            BlockPos pos = null;
            if (list.size() < 500) {
                boolean flg = true;
                while (flg) {
                    Random random = this.rand;
                    double x = this.posX - 50 + random.nextInt(100);
                    double z = this.posZ - 50 + random.nextInt(100);
                    double y = Math.min(this.world.getHeight((int) x, (int) z), this.posY) + random.nextInt(40) - random.nextInt(40);
                    pos = new BlockPos(x, y, z);
                    if (y > 1 && this.world.isAirBlock(pos) && this.world.isAirBlock(pos.up()) && this.getDistanceSq(pos) > 400) {
                        flg = false;
                    }
                }

                if (pos != null) {
                    if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobSpawning")) {
                        if (this.rand.nextInt(10) == 0) {
                            int id = this.rand.nextInt(2);
                            switch (id) {
                                case 1: {
                                    for (int i = 0; i < 10; i++) {
                                        double offX = -4 + this.rand.nextInt(9);
                                        double offY = -4 + this.rand.nextInt(9);
                                        double offZ = -4 + this.rand.nextInt(9);
                                        try {
                                            EntityLiving entity = (EntityCreeper) ARTILLERIES.get(this.rand.nextInt(ARTILLERIES.size())).getConstructor(World.class).newInstance(this.world);
                                            entity.setPosition(pos.getX() + offX, pos.getY() + offY, pos.getZ() + offZ);
                                            entity.setGlowing(true);
                                            entity.setAttackTarget(this);
                                            this.world.spawnEntity(entity);
                                            this.spawns.add(entity);
                                            if (entity.isNotColliding()) {
                                                if (entity.posY > 1 && this.world.spawnEntity(entity)) {
                                                    EntityLightningBolt bolt = new EntityLightningBolt(this.world, entity.posX, entity.posY, entity.posZ, true);
                                                    this.world.addWeatherEffect(bolt);
                                                    this.world.spawnEntity(bolt);
                                                    i++;
                                                }
                                            } else if (this.rand.nextInt(20) == 0) {
                                                i++;
                                            }
                                        } catch (Exception ignored) {
                                        }
                                    }
                                    break;
                                }
                                default: {
                                    for (int i = 0; i < 5; i++) {
                                        EntityLapisCreeper lapisCreeper = new EntityLapisCreeper(this.world);
                                        lapisCreeper.setPosition(pos.getX(), pos.getY(), pos.getZ());
                                        lapisCreeper.setGlowing(true);
                                        lapisCreeper.setHealth(2f);
                                        this.world.spawnEntity(lapisCreeper);
                                        this.spawns.add(lapisCreeper);
                                        try {
                                            EntityCreeper creeper = (EntityCreeper) ENGINEERS.get(this.rand.nextInt(ENGINEERS.size())).getConstructor(World.class).newInstance(this.world);
                                            creeper.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
                                            creeper.setGlowing(true);
                                            this.world.spawnEntity(creeper);
                                            this.spawns.add(creeper);
                                            creeper.startRiding(lapisCreeper);
                                            EntityLightningBolt bolt = new EntityLightningBolt(this.world, creeper.posX, creeper.posY, creeper.posZ, true);
                                            this.world.addWeatherEffect(bolt);
                                            this.world.spawnEntity(bolt);
                                        } catch (Exception ignored) {
                                        }
                                    }
                                    break;
                                }
                            }
                        } else {
                            int max = 10;
                            if (this.world.playerEntities != null) {
                                max += this.world.playerEntities.size() * 1.5;
                            }
                            for (int i = 0; i < max; ) {
                                double offX = -4 + this.rand.nextInt(9);
                                double offY = -4 + this.rand.nextInt(9);
                                double offZ = -4 + this.rand.nextInt(9);
                                try {
                                    EntityLiving entity = ((EntityLiving) entities.get(this.rand.nextInt(entities.size())).getClass().getConstructor(World.class).newInstance(this.world));
                                    entity.setPosition(pos.getX() + offX, pos.getY() + offY, pos.getZ() + offZ);
                                    if (entity.posY > 1 && entity.isNotColliding()) {
                                        if (this.world.spawnEntity(entity)) {
                                            this.spawns.add(entity);
                                            EntityLightningBolt bolt = new EntityLightningBolt(this.world, entity.posX, entity.posY, entity.posZ, true);
                                            this.world.addWeatherEffect(bolt);
                                            this.world.spawnEntity(bolt);
                                            i++;
                                        }
                                    } else if (this.rand.nextInt(20) == 0) {
                                        i++;
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("sp", spawnTick);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.spawnTick = compound.getInteger("sp");
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    protected void onDeathUpdate() {
        super.onDeathUpdate();
        if (this.getHealth() != -334 && this.getHealth() <= 0 && !this.world.isRemote) {
            this.world.playerEntities.forEach(player -> {
                player.sendMessage(new TextComponentTranslation("entity.attackblock.win"));
                player.addExperience(100);
            });
            EntityBigCreeper bigCreeper = new EntityBigCreeper(this.world);
            bigCreeper.setPosition(this.posX, this.posY, this.posZ);
            //this.world.spawnEntity(bigCreeper);
            this.setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.attackEntityFrom(source, amount);
        }
        if (source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).isCreative()) {
            this.setHealth(-334);
            return true;
        }
        if (source.isExplosion() && amount > 2) {
            this.setDead();
            this.world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityLiving) {
                    ((EntityLiving) entity).setAttackTarget(null);
                }
            });
            this.world.playerEntities.forEach(player -> {
                if (*//*!player.isCreative() && *//*!player.isSpectator()) {
                    player.sendMessage(new TextComponentTranslation("entity.attackblock.lose"));
                }
            });
        }
        return false;
    }

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }
}*/
