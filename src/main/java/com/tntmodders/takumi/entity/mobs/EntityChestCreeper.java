package com.tntmodders.takumi.entity.mobs;

import com.google.common.collect.Lists;
import com.tntmodders.takumi.client.render.RenderChestCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class EntityChestCreeper extends EntityTakumiAbstractCreeper implements IInventory {
    
    private final int slotsCount = 27;
    private final NonNullList <ItemStack> inventoryContents = NonNullList.withSize(27, ItemStack.EMPTY);
    /**
     * Listeners notified when any item in this inventory is changed.
     */
    private final ThreadLocal <List <IInventoryChangedListener>> changeListeners = new ThreadLocal <>();
    private String inventoryTitle;
    private boolean hasCustomName;
    
    public EntityChestCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
    }
    
    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Items.DIAMOND, this.rand.nextInt(32));
        } super.onDeath(source);
    }
    
    @Override
    public boolean canBePushed() {
        return false;
    }
    
    @Override
    protected void initEntityAI() {
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
    }
    
    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (player.getHeldItemMainhand().getItem() != Items.FLINT_AND_STEEL) {
            player.displayGUIChest(this);
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    public void takumiExplode() {
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }
    
    @Override
    public int getExplosionPower() {
        return 10;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0xffff00;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "chestcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 231;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x005500;
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderChestCreeper <>(manager);
    }
    
    public ItemStack addItem(ItemStack stack) {
        ItemStack itemstack = stack.copy();
        
        for (int i = 0; i < this.slotsCount; ++i) {
            ItemStack itemstack1 = this.getStackInSlot(i);
            
            if (itemstack1.isEmpty()) {
                this.setInventorySlotContents(i, itemstack);
                this.markDirty();
                return ItemStack.EMPTY;
            }
            
            if (ItemStack.areItemsEqual(itemstack1, itemstack)) {
                int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());
                
                if (k > 0) {
                    itemstack1.grow(k);
                    itemstack.shrink(k);
                    
                    if (itemstack.isEmpty()) {
                        this.markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        
        if (itemstack.getCount() != stack.getCount()) {
            this.markDirty();
        }
        
        return itemstack;
    }
    
    public void loadInventoryFromNBT(NBTTagList p_70486_1_) {
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, ItemStack.EMPTY);
        }
        
        for (int k = 0; k < p_70486_1_.tagCount(); ++k) {
            NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
            int j = nbttagcompound.getByte("Slot") & 255;
            
            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        }
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return this.slotsCount;
    }
    
    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventoryContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Returns the stack in the given slot.
     */
    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.inventoryContents.size() ? this.inventoryContents.get(index) : ItemStack.EMPTY;
    }
    
    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);
    
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
    
        return itemstack;
    }
    
    /**
     * Removes a stack from the given slot and returns it.
     */
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
    
    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventoryContents.set(index, stack);
    
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    
        this.markDirty();
    }
    
    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    @Override
    public void markDirty() {
        if (this.changeListeners.get() != null) {
            for (IInventoryChangedListener changeListener : this.changeListeners.get()) {
                changeListener.onInventoryChanged(this);
            }
        }
    }
    
    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }
    
    @Override
    public void openInventory(EntityPlayer player) {
        this.ignite();
        List <IRecipe> list = Lists.newArrayList(CraftingManager.REGISTRY.iterator());
        for (int i = 0; i < MathHelper.getInt(this.rand, 5, this.slotsCount); i++) {
            this.inventoryContents.set(i, list.get(this.rand.nextInt(list.size())).getRecipeOutput());
        }
    }
    
    @Override
    public void closeInventory(EntityPlayer player) {
    }
    
    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
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
    public void setDead() {
        this.clear();
        super.setDead();
    }
    
    @Override
    protected void setRotation(float yaw, float pitch) {
    }
    
    @Override
    public void move(MoverType type, double x, double y, double z) {
    }
    
    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (!this.isRidingSameEntity(entityIn)) {
            if (!entityIn.noClip && !this.noClip) {
                double d0 = entityIn.posX - this.posX;
                double d1 = entityIn.posZ - this.posZ;
                double d2 = MathHelper.absMax(d0, d1);
    
                if (d2 >= 0.009999999776482582D) {
                    d2 = MathHelper.sqrt(d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;
                    double d3 = 1.0D / d2;
        
                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }
        
                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
                    d0 = d0 * (1.0F - this.entityCollisionReduction);
                    d1 = d1 * (1.0F - this.entityCollisionReduction);

/*                    if (!this.isBeingRidden()) {
                        //this.addVelocity(-d0, 0.0D, -d1);
                    }*/
        
                    if (!entityIn.isBeingRidden()) {
                        entityIn.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
        }
    }
    
    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
    public String getName() {
        return TakumiUtils.takumiTranslate("entity.chestcreeper.name");
    }
    
    @Override
    public boolean isPushedByWater() {
        return false;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }
    
    @Override
    public boolean hasCustomName() {
        return this.hasCustomName;
    }
    
    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.BLOCK;
    }
    
    public NBTTagList saveInventoryToNBT() {
        NBTTagList nbttaglist = new NBTTagList();
        
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);
            
            if (!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        
        return nbttaglist;
    }
}
