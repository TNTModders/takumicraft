package com.tntmodders.takumi.entity.item;


import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.ILootContainer;

import java.util.List;

public class EntityTakumiRandomChest extends Entity implements IInventory, ILootContainer {
    private final NonNullList<ItemStack> inventoryContents = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ThreadLocal<List<IInventoryChangedListener>> changeListeners = new ThreadLocal<>();
    private ResourceLocation lootTable;

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
        for (int i = 0; i < 27; ++i) {
            this.inventoryContents.set(i, ItemStack.EMPTY);
        }

        NBTBase nbt = compound.getTag("Items");
        if (nbt != null && nbt instanceof NBTTagList) {
            for (int k = 0; k < 27; ++k) {
                NBTTagCompound nbttagcompound = ((NBTTagList) nbt).getCompoundTagAt(k);
                int j = nbttagcompound.getByte("Slot") & 255;

                if (j >= 0 && j < 27) {
                    this.inventoryContents.set(j, new ItemStack(nbttagcompound));
                }
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < 27; ++i) {
            ItemStack itemstack = this.inventoryContents.get(i);

            if (!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag("Items", nbttaglist);
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
                this.world.setBlockState(this.getPosition(), Blocks.CHEST.getDefaultState());
                if (this.world.getTileEntity(this.getPosition()) instanceof TileEntityChest) {
                    int j = 0;
                    for (int i = 0; i < 27; i++) {
                        ItemStack stack = this.inventoryContents.get(i);
                        if (!stack.isEmpty()) {
                            ((TileEntityChest) this.world.getTileEntity(this.getPosition())).setInventorySlotContents(j, stack);
                            j++;
                        }
                    }
                    this.setDead();
                }
            } else {
                this.motionY = -0.1;
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            }
        }
    }

    private void initItems() {
        int maxItems = 5 + this.rand.nextInt(6);
        for (int i = 0; i < maxItems; i++) {
            ItemStack stack = new ItemStack(TakumiBlockCore.CREEPER_BOMB, this.rand.nextInt(32) + 1);
            this.inventoryContents.set(i, stack);
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
