package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

public class EntityFireworksCreeper extends EntityTakumiAbstractCreeper {

    public EntityFireworksCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3f, false);
            for (int i = 0; i < (this.getPowered() ? 6 : 3); i++) {
                ItemStack item = getItemFireworks();
                double x = this.posX + i * 2 - i;
                double z = this.posZ + i * 2 - i;
                EntityFireworkRocket firework =
                        new EntityFireworkRocket(world, x, this.world.getHeight((int) x, (int) z), z, item);
                this.world.spawnEntity(firework);
            }
            Calendar calendar = this.world.getCurrentDate();
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
            if (month == 12 && (date == 24 || date == 25)) {
                spawn(this);
                createTree(this, this.getPosition());
            } else if (month == 1 && date < 8) {
                spawn(this);
                createKagamimochi(this);
            }
        }
    }

    public static ItemStack getItemFireworks() {
        ItemStack item = new ItemStack(Items.FIREWORKS, 1);
        try {
            item.setTagCompound(JsonToNBT.getTagFromJson(
                    "{Fireworks:{Flight:0,Explosions:[{Type:3,Flicker:1,Trail:1,Colors:[I;65280]," +
                            "FadeColors:[I;65280]}]}}"));
        } catch (NBTException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static void spawn(EntityTakumiAbstractCreeper entity) {
        for (int x = (int) entity.posX - 4; x <= (int) entity.posX + 4; x++) {
            for (int z = (int) entity.posZ - 4; z <= (int) entity.posZ + 4; z++) {
                for (int y = (int) entity.posY + 2; y >= (int) entity.posY + 1; y--) {
                    for (int i = 0; i < (entity.getPowered() ? 4 : 2); i++) {
                        TakumiUtils.setBlockStateProtected(entity.world, new BlockPos(x, y, z),
                                Blocks.AIR.getDefaultState());
                        if (entity.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR &&
                                entity.world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR &&
                                entity.world.rand.nextInt(15) == 0) {
                            Class<? extends ITakumiEntity> clazz = TakumiEntityCore.getEntityList().get(
                                    entity.world.rand.nextInt(TakumiEntityCore.getEntityList().size())).getClass();
                            try {
                                Entity creeper = (Entity) clazz.getConstructor(World.class).newInstance(entity.world);
                                if (((ITakumiEntity) creeper).takumiRank() == EnumTakumiRank.LOW ||
                                        ((ITakumiEntity) creeper).takumiRank() == EnumTakumiRank.MID) {
                                    creeper.world = entity.world;
                                    creeper.setPosition(x, y, z);
                                    entity.world.spawnEntity(creeper);
                                }
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void createTree(Entity entity, BlockPos pos) {
        for (int y = 0; y < 7; y++) {
            switch (y) {
                case 1: {
                    int[] xRange = {-1, 1, 0, 0};
                    int[] zRange = {0, 0, -1, 1};
                    for (int i = 0; i < 4; i++) {
                        entity.world.setBlockState(pos.add(xRange[i], y, zRange[i]),
                                Blocks.GLOWSTONE.getDefaultState());
                        TakumiUtils.setBlockStateProtected(entity.world, pos.add(xRange[i] * 2, y, zRange[i] * 2),
                                Blocks.CHEST.getDefaultState());
                        TileEntity tile = entity.world.getTileEntity(pos.add(xRange[i] * 2, y, zRange[i] * 2));
                        if (tile instanceof TileEntityChest) {
                            int point = entity.world.rand.nextInt(10) + 1;
                            for (int p = 0; p <= point; p++) {
                                Item[] item = {Items.DIAMOND, Items.GUNPOWDER,
                                        Item.getItemFromBlock(TakumiBlockCore.TAKUMI_SUPERPOWERED), Items.EMERALD,
                                        TakumiItemCore.TAKUMI_SHIELD, TakumiItemCore.TAKUMI_BOW, Items.ELYTRA};
                                Item returner = item[entity.world.rand.nextInt(item.length)];
                                ((IInventory) tile).setInventorySlotContents(p, new ItemStack(returner,
                                        entity.world.rand.nextInt(
                                                returner.getItemStackLimit(new ItemStack(returner)) / 4 + 1) + 1));
                            }
                        }
                    }
                    break;
                }
                case 2: {
                    for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            TakumiUtils.setBlockStateProtected(entity.world, pos.add(x, y, z),
                                    Blocks.LEAVES.getDefaultState());
                        }
                    }
                    break;
                }
                case 3: {
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            TakumiUtils.setBlockStateProtected(entity.world, pos.add(x, y, z),
                                    Blocks.LEAVES.getDefaultState());
                        }
                    }
                    break;
                }
                case 4: {
                    int[] xRange = {-1, 1, 0, 0};
                    int[] zRange = {0, 0, -1, 1};
                    for (int i = 0; i < 4; i++) {
                        entity.world.setBlockState(pos.add(xRange[i], y, zRange[i]),
                                Blocks.GLOWSTONE.getDefaultState());
                    }
                    break;
                }
                case 5: {
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            TakumiUtils.setBlockStateProtected(entity.world, pos.add(x, y, z),
                                    Blocks.LEAVES.getDefaultState());
                        }
                    }
                    break;
                }
                case 6: {
                    TakumiUtils.setBlockStateProtected(entity.world, pos.up(y), Blocks.LEAVES.getDefaultState());
                    break;
                }
            }
            if (y != 6) {
                TakumiUtils.setBlockStateProtected(entity.world, pos.up(y), Blocks.LOG.getDefaultState());
            }
        }
    }

    public static void createKagamimochi(Entity entity) {
        int[] count = {3, 2, 1, 1, 0};
        int[] meta = {0, 0, 1, 1, 5};

        for (int y = 0; y < 5; y++) {
            for (int x = -1 * count[y]; x <= count[y]; x++) {
                for (int z = -1 * count[y]; z <= count[y]; z++) {
                    if (count[y] != 0) {
                        TakumiUtils.setBlockStateProtected(entity.world,
                                new BlockPos((int) (entity.posX + x), (int) (entity.posY + y), (int) (entity.posZ + z)),
                                Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR,
                                        EnumDyeColor.byMetadata(meta[y])));
                    }
                }
            }
            if (count[y] == 0) {
                entity.world.setBlockState(
                        new BlockPos((int) entity.posX, (int) (entity.posY + y - 1), (int) entity.posZ),
                        Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR,
                                EnumDyeColor.byMetadata(meta[y])));
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 11451419;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "fireworkscreeper";
    }

    @Override
    public int getRegisterID() {
        return 22;
    }
}
