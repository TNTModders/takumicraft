package com.tntmodders.takumi.entity.item;


import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.block.BlockChest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class EntityTakumiRandomChest extends Entity implements IInventory, ILootContainer {
    public static final NonNullList<Item> DEFAULT_ITEMS = NonNullList.create();

    static {
        DEFAULT_ITEMS.add(Items.GOLDEN_APPLE);
        DEFAULT_ITEMS.add(Items.DIAMOND_CHESTPLATE);
        DEFAULT_ITEMS.add(Items.DIAMOND_HELMET);
        DEFAULT_ITEMS.add(Items.DIAMOND_LEGGINGS);
        DEFAULT_ITEMS.add(Items.DIAMOND_BOOTS);
        DEFAULT_ITEMS.add(Items.DIAMOND_SWORD);
        DEFAULT_ITEMS.add(TakumiItemCore.TAKUMI_SHIELD);
        DEFAULT_ITEMS.add(TakumiItemCore.TAKUMI_MINE_AXE);
        DEFAULT_ITEMS.add(TakumiItemCore.TAKUMI_MINE_PICKAXE);
        DEFAULT_ITEMS.add(TakumiItemCore.TAKUMI_MINE_SHOVEL);
        DEFAULT_ITEMS.add(TakumiItemCore.TAKUMI_SWORD);
        DEFAULT_ITEMS.add(TakumiItemCore.TAKUMI_SPRING_BUCKET);
        DEFAULT_ITEMS.add(TakumiItemCore.TAKUMI_BOW);
        DEFAULT_ITEMS.add(Items.GUNPOWDER);
        DEFAULT_ITEMS.add(Items.DIAMOND_HOE);
        DEFAULT_ITEMS.add(Items.FIRE_CHARGE);
        DEFAULT_ITEMS.add(Items.TOTEM_OF_UNDYING);
    }

    private final NonNullList<ItemStack> inventoryContents = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ThreadLocal<List<IInventoryChangedListener>> changeListeners = new ThreadLocal<>();
    public boolean spawnedByBomb;
    private ResourceLocation lootTable;
    private long lootTableSeed;

    public EntityTakumiRandomChest(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
        this.noClip = false;
        this.setEntityInvulnerable(false);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.inventoryContents.clear();
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventoryContents);
        }
        this.spawnedByBomb = compound.getBoolean("SpawnedByBomb");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventoryContents);
        }
        compound.setBoolean("SpawnedByBomb", this.spawnedByBomb);
    }

    protected boolean checkLootAndRead(NBTTagCompound compound) {
        if (compound.hasKey("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    protected boolean checkLootAndWrite(NBTTagCompound compound) {
        if (this.lootTable != null) {
            compound.setString("LootTable", this.lootTable.toString());

            if (this.lootTableSeed != 0L) {
                compound.setLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        } else {
            return false;
        }
    }

    public void fillWithLoot(@Nullable EntityPlayer player) {
        if (this.lootTable != null) {
            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
            this.lootTable = null;
            Random random;

            if (this.lootTableSeed == 0L) {
                random = new Random();
            } else {
                random = new Random(this.lootTableSeed);
            }

            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) this.world);

            if (player != null) {
                lootcontext$builder.withLuck(player.getLuck()).withPlayer(player); // Forge: add player to LootContext
            }

            loottable.fillInventory(this, random, lootcontext$builder.build());
        }
    }

    public void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_) {
        this.lootTable = p_189404_1_;
        this.lootTableSeed = p_189404_2_;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            if (this.inventoryContents.stream().allMatch(ItemStack::isEmpty)) {
                this.initItems();
            }
            if (this.onGround) {
                if (this.world.isAirBlock(this.getPosition())) {
                    if (this.world.isAirBlock(this.getPosition().down())) {
                        this.world.setBlockState(this.getPosition().down(), Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.SOUTH));
                        if (this.world.getTileEntity(this.getPosition().down()) instanceof TileEntityChest) {
                            int j = 0;
                            for (int i = 0; i < 27; i++) {
                                ItemStack stack = this.inventoryContents.get(i);
                                if (!stack.isEmpty()) {
                                    ((TileEntityChest) this.world.getTileEntity(this.getPosition().down())).setInventorySlotContents(j, stack);
                                    j++;
                                }
                            }
                        }
                    } else {
                        this.world.setBlockState(this.getPosition(), Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.SOUTH));
                        if (this.world.getTileEntity(this.getPosition()) instanceof TileEntityChest) {
                            int j = 0;
                            for (int i = 0; i < 27; i++) {
                                ItemStack stack = this.inventoryContents.get(i);
                                if (!stack.isEmpty()) {
                                    ((TileEntityChest) this.world.getTileEntity(this.getPosition())).setInventorySlotContents(j, stack);
                                    j++;
                                }
                            }
                        }
                    }
                    this.setDead();

                }
            } else {
                this.motionY = -0.25;
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            }
        } else {
            this.setGlowing(true);
            if (this.getRidingEntity() != null) {
                this.getRidingEntity().setGlowing(true);
            }
            double y = this.world.getHeight(this.getPosition()).getY() - 0.5;
            for (int i = 0; i < 20; i++) {
                double dx = this.rand.nextDouble() - 0.5;
                double dz = this.rand.nextDouble() - 0.5;
                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX - dx / 5, y, this.posZ - dx / 5, dx / 10, 0.2 + this.rand.nextDouble() / 4, dz / 10);
            }
        }
        if (this.ticksExisted % 40 == 0) {
            this.playSound(SoundEvents.BLOCK_NOTE_BELL, 5f, 1f);
        }
    }

    private void initItems() {
        if (this.lootTable != null) {
            this.fillWithLoot(null);
        } else {
            int maxItems = 10 + this.rand.nextInt(17);
            for (int i = 0; i < maxItems; i++) {
                Item item = DEFAULT_ITEMS.get(this.rand.nextInt(DEFAULT_ITEMS.size()));
                ItemStack stack = new ItemStack(item, Math.max(1, this.rand.nextInt(item.getItemStackLimit()) / 3));
                if (stack.getCount() > 0) {
                    if (stack.isItemEnchantable()) {
                        EnchantmentHelper.addRandomEnchantment(this.rand, stack, 50, true);
                    }
                    this.inventoryContents.set(i, stack);
                }
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public boolean isEmpty() {
        return !this.inventoryContents.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventoryContents.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);

        if (!itemstack.isEmpty()) {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemstack = this.inventoryContents.get(index);

        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.inventoryContents.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventoryContents.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (this.changeListeners.get() != null) {
            for (IInventoryChangedListener changeListener : this.changeListeners.get()) {
                changeListener.onInventoryChanged(this);
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.inventoryContents.clear();
    }

    @Override
    public ResourceLocation getLootTable() {
        return this.lootTable;
    }
}
