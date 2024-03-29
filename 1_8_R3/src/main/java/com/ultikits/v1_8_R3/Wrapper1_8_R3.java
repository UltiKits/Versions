package com.ultikits.v1_8_R3;

import com.ultikits.ultitools.interfaces.VersionWrapper;
import com.ultikits.ultitools.entities.Colors;
import com.ultikits.ultitools.entities.Sounds;
import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Field;

public class Wrapper1_8_R3 implements VersionWrapper {

    public ItemStack getColoredPlaneGlass(Colors plane) {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, plane.getCode());
    }

    public ItemStack getSign() {
        return new ItemStack(Material.SIGN, 1);
    }

    public ItemStack getEndEye() {
        return new ItemStack(Material.EYE_OF_ENDER, 1);
    }

    public ItemStack getEmailMaterial(boolean isRead) {
        if (isRead){
            return new ItemStack(Material.INK_SACK, 1, (short) 15);
        }else {
            return new ItemStack(Material.PAPER, 1);
        }
    }

    public ItemStack getHead(OfflinePlayer player) {
        if (player.isOp()) {
            return new ItemStack(Material.SKULL_ITEM, 1 , (short) 5);
        } else {
            return new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
        }
    }

    public ItemStack getGrassBlock() {
        return new ItemStack(Material.GRASS);
    }

    public Objective registerNewObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
        Objective objective = scoreboard.getObjective(displayName);
        if ( objective ==null){
            objective = scoreboard.registerNewObjective(displayName, criteria);
        }
        return objective;
    }

    public Sound getSound(Sounds sound) {
        switch (sound){
            case UI_TOAST_OUT:
                return Sound.FIRE;
            case BLOCK_CHEST_OPEN:
                return Sound.CHEST_OPEN;
            case BLOCK_CHEST_LOCKED:
            case BLOCK_CHEST_CLOSE:
                return Sound.CHEST_CLOSE;
            case ITEM_BOOK_PAGE_TURN:
                return Sound.ITEM_PICKUP;
            case BLOCK_NOTE_BLOCK_HAT:
                return Sound.NOTE_STICKS;
            case BLOCK_NOTE_BLOCK_BELL:
                return Sound.NOTE_PLING;
            case BLOCK_WET_GRASS_BREAK:
                return Sound.ITEM_BREAK;
            case BLOCK_NOTE_BLOCK_CHIME:
                return Sound.NOTE_PIANO;
            case ENTITY_ENDERMAN_TELEPORT:
                return Sound.ENDERMAN_TELEPORT;
            default:
                return null;
        }
    }

    public ItemStack getBed(Colors bedColor) {
        return new ItemStack(Material.BED, 1, bedColor.getCode());
    }

    public int getItemDurability(ItemStack itemStack) {
        return itemStack.getType().getMaxDurability() - itemStack.getDurability();
    }

    public ItemStack getItemInHand(Player player, boolean isMainHand) {
        if (isMainHand){
            return player.getInventory().getItemInHand();
        }else {
            return null;
        }
    }

    public void  sendActionBar(Player player, String message) {
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatBaseComponent, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

    public void sendPlayerList(Player player, String header, String footer) {
        IChatBaseComponent tabHeader = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tabFooter = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter tabPacket = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = tabPacket.getClass().getDeclaredField("a");
            Field footerField = tabPacket.getClass().getDeclaredField("b");
            headerField.setAccessible(true);
            footerField.setAccessible(true);
            headerField.set(tabPacket, tabHeader);
            footerField.set(tabPacket, tabFooter);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(tabPacket);
    }

    @Override
    public BlockFace getBlockFace(Block placedBlock) {
        return ((Directional) placedBlock.getState().getData()).getFacing();
    }

    public PlayerConnection getPlayerConnection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    public NetworkManager getNetworkManager(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager;
    }

    public Channel getChannel(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
    }

    public Object getHandle(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
