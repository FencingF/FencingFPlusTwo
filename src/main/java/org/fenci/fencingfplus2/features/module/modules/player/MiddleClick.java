package org.fenci.fencingfplus2.features.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.manager.friend.Friend;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.lwjgl.input.Mouse;

public class MiddleClick extends Module {

    public static final Setting<Boolean> pearl = new Setting<>("Pearl", true);
    public static final Setting<Boolean> friend = new Setting<>("Friend", true);
    public static final Setting<Boolean> exp = new Setting<>("Exp", true);
    public static final Setting<Float> packets = new Setting<>("Packets", 2f, 0f, 10f);
    public static final Setting<Boolean> chatAnnounce = new Setting<>("ChatAnnounce", true);
    public static final Setting<Integer> threshold = new Setting<>("Threshold", 99, 0, 100);
    public static final Setting<Enum> mendMode = new Setting<>("MendMode", MendMode.Never);
    private final Timer timer = new Timer();
    private final long delay = 50L;
    boolean aBoolean = true;
    float playerDistance;
    private boolean clicked;
    private boolean mending = true;

    public MiddleClick() {
        super("MiddleClick", "Allows you to do different functions with middle click", Category.Player);
    }

    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown(2) && (pearl.getValue() || friend.getValue() || exp.getValue())) {
            Item oldslot = mc.player.getHeldItemMainhand().getItem();
            if (!this.clicked) {
                RayTraceResult result;
                if (friend.getValue() && (result = MiddleClick.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.ENTITY) {
                    Entity entity = result.entityHit;
                    if (!(entity instanceof EntityPlayer)) return;
                    else {
                        if (FencingFPlus2.INSTANCE.friendManager.isFriend(entity.getUniqueID())) {
                            Friend friend = FencingFPlus2.INSTANCE.friendManager.getFriend(entity.getUniqueID());
                            if (friend == null) return;
                            FencingFPlus2.INSTANCE.friendManager.remove(friend);
                            ClientMessage.sendMessage(entity.getName() + " has been " + ChatFormatting.RED + "unfriended!");
                        } else {
                            FencingFPlus2.INSTANCE.friendManager.add(new Friend(entity.getUniqueID(), entity.getName()));
                            ClientMessage.sendMessage(entity.getName() + " has been " + ChatFormatting.GREEN + "friended!");
                        }
                        this.clicked = true;
                        return;
                    }
                }
                if (exp.getValue() && (result = MiddleClick.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.BLOCK || (result = MiddleClick.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.ENTITY) {

                    if (getArmorDurability() == 0) {
                        unremoveArmor();
                        return;
                    }

                    mc.player.connection.sendPacket(new CPacketHeldItemChange(findExp()));

                    for (int i = 0; i < packets.getValue(); i++) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    }
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));

                    if (mendMode.getValue().equals(MendMode.Never)) return;

                    if (mendMode.getValue().equals(MendMode.Smart) && (getNearestPlayer() >= 10 || mc.world.playerEntities.size() == 1)) {
                        removeArmor();
                    }
                    mending = false;
                    aBoolean = true;

                }

                if (!pearl.getValue()) return;
                if ((result = MiddleClick.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.ENTITY || (result = MiddleClick.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
                    return;
                if (!InventoryUtil.isInHotbar(Items.ENDER_PEARL)) {
                    ClientMessage.sendMessage("No pearl found in hotbar!");
                    this.clicked = true;
                    return;
                } else if (InventoryUtil.isInHotbar(Items.ENDER_PEARL)) {
                    InventoryUtil.switchTo(Items.ENDER_PEARL);
                    mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                    if (chatAnnounce.getValue()) ClientMessage.sendMessage("Throwing Pearl!");
                    InventoryUtil.switchTo(oldslot);
                }
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (!Mouse.isButtonDown(2) && !mending && aBoolean &&
                (mc.player.inventory.getStackInSlot(5).getItem() != Items.DIAMOND_HELMET
                        || mc.player.inventory.getStackInSlot(6).getItem() != Items.DIAMOND_CHESTPLATE
                        || mc.player.inventory.getStackInSlot(7).getItem() != Items.DIAMOND_LEGGINGS
                        || mc.player.inventory.getStackInSlot(8).getItem() != Items.DIAMOND_BOOTS)) {
            unremoveArmor();
            aBoolean = false;
        }

    }

    public float getNearestPlayer() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (!(getFencing().friendManager.isFriendByName(player.getName()))) {
                playerDistance = player.getDistance(mc.player);

            }
        }
        return playerDistance;
    }

    public void unremoveArmor() {
        int helmetSlot = getHelmetSlot();
        int chestplateSlot = getChestplateSlot();
        int leggingsSlot = getLeggingsSlot();
        int bootsSlot = getBootsSlot();
        if (helmetSlot != -1 && timer.hasReached(delay)) {
            clickSlot(helmetSlot, 5);
            timer.reset();
        }
        if (chestplateSlot != -1 && timer.hasReached(delay)) {
            clickSlot(chestplateSlot, 6);
            timer.reset();
        }
        if (leggingsSlot != -1 && timer.hasReached(delay)) {
            clickSlot(leggingsSlot, 7);
            timer.reset();
        }
        if (bootsSlot != -1 && timer.hasReached(delay)) {
            clickSlot(bootsSlot, 8);
            timer.reset();
        }
    }

    public void clickSlot(Integer slot, Integer moveTo) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, moveTo, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }

    private void removeArmor() {
        int slot = 5;
        while (slot <= 8) {
            ItemStack item;
            item = getArmor(slot);
            double max_dam = item.getMaxDamage();
            double dam_left = item.getMaxDamage() - item.getItemDamage();
            double percent = (dam_left / max_dam) * 100;

            if (percent >= threshold.getValue() && !item.equals(Items.AIR)) {
                if (!notInInv(Items.AIR)) {
                    return;
                }

                mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, mc.player);

            }
            slot++;
        }
    }

    private ItemStack getArmor(int first) {
        return mc.player.inventoryContainer.getInventory().get(first);
    }

    public Boolean notInInv(Item itemOfChoice) {
        int n;
        n = 0;
        if (itemOfChoice == mc.player.getHeldItemOffhand().getItem()) return true;

        for (int i = 35; i >= 0; i--) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == itemOfChoice) {
                return true;

            } else if (item != itemOfChoice) {
                n++;
            }
        }
        return n < 35;
    }

    private int getArmorDurability() {
        int TotalDurability = 0;

        for (ItemStack itemStack : mc.player.inventory.armorInventory) {
            TotalDurability = TotalDurability + itemStack.getItemDamage();
        }
        return TotalDurability;
    }

    private int findExp() {
        int slot = 0;

        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }

        return slot;
    }

    private int getHelmetSlot() {
        for (int i = 0; i < 36; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == Items.DIAMOND_HELMET) {
                if (i < 9) {
                    i += 36;
                }

                return i;
            }
        }
        return -1;
    }

    private int getChestplateSlot() {
        for (int i = 0; i < 36; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == Items.DIAMOND_CHESTPLATE) {
                if (i < 9) {
                    i += 36;
                }

                return i;
            }
        }
        return -1;
    }

    private int getLeggingsSlot() {
        for (int i = 0; i < 36; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == Items.DIAMOND_LEGGINGS) {
                if (i < 9) {
                    i += 36;
                }

                return i;
            }
        }
        return -1;
    }

    private int getBootsSlot() {
        for (int i = 0; i < 36; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == Items.DIAMOND_BOOTS) {
                if (i < 9) {
                    i += 36;
                }

                return i;
            }
        }
        return -1;
    }

    public enum MendMode {
        Smart, Always, Never
    }
}
