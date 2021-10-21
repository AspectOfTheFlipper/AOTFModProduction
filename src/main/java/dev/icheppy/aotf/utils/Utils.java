package dev.icheppy.aotf.utils;

import dev.icheppy.aotf.AspectOfTheFlipper;
import dev.icheppy.aotf.dataclasses.Flip;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Utils {
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static EnumChatFormatting rarityColor(String rarity){
        if (rarity.equalsIgnoreCase("COMMON")){
            return EnumChatFormatting.WHITE;
        } else if (rarity.equalsIgnoreCase("UNCOMMON")){
            return EnumChatFormatting.GREEN;
        }else if (rarity.equalsIgnoreCase("RARE")){
            return EnumChatFormatting.BLUE;
        }else if (rarity.equalsIgnoreCase("EPIC")){
            return EnumChatFormatting.DARK_PURPLE;
        }else if (rarity.equalsIgnoreCase("LEGENDARY")){
            return EnumChatFormatting.GOLD;
        }else if (rarity.equalsIgnoreCase("MYTHIC")){
            return EnumChatFormatting.LIGHT_PURPLE;
        }else if (rarity.equalsIgnoreCase("SUPREME")){
            return EnumChatFormatting.DARK_RED;
        }else if (rarity.equalsIgnoreCase("SPECIAL") || rarity.equalsIgnoreCase("VERY SPECIAL")){
            return EnumChatFormatting.RED;
        } else{
            return EnumChatFormatting.GRAY;
        }

    }

    public static String toReadable(int num){
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);

        if (num < 1000){
            return Integer.toString(num);
        } else if (num < 1000000){
            return Integer.toString(num/1000) + "K";
        } else if (num < 1000000000){
            return df.format((float) num/1000000) + "M";
        }else if (num < 2147000000){
            return df.format((float)num/1000000000) + "B";
        } else{
            return Integer.toString(num);
        }
    }

    public static double round(int num, int precision){
        double n = Math.pow(10, precision);
        return Math.round((double) num * n) / n;
    }

    public static String getCommand(String uuid){
        return "/viewauction " + uuid;
    }

    public static IChatComponent flipToMsg(Flip f){
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        myFormat.setMaximumFractionDigits(1);
        myFormat.setRoundingMode(RoundingMode.HALF_UP);

        return MessageBuilder.of("AOTF: ").setColor(EnumChatFormatting.GRAY)
                .append(f.getItemName()).setColor(rarityColor(f.getTier())).setClickEvent(ClickEvent.Action.RUN_COMMAND, getCommand(f.getUuid()))
                .append(" " + toReadable(f.getBuyPrice())).setColor(EnumChatFormatting.WHITE).setClickEvent(ClickEvent.Action.RUN_COMMAND, getCommand(f.getUuid()))
                .setHoverEvent(EnumChatFormatting.YELLOW + myFormat.format(f.getBuyPrice()))
                .append(" -> ").setColor(EnumChatFormatting.WHITE).setClickEvent(ClickEvent.Action.RUN_COMMAND, getCommand(f.getUuid()))
                .append(toReadable(f.getSellPrice())).setColor(EnumChatFormatting.WHITE).setClickEvent(ClickEvent.Action.RUN_COMMAND, getCommand(f.getUuid()))
                .setHoverEvent(EnumChatFormatting.YELLOW + myFormat.format(f.getSellPrice()))
                .append(" [BUY ITEM]").setBold(true).setColor(EnumChatFormatting.GOLD)
                .setHoverEvent(EnumChatFormatting.YELLOW + "Click to buy the item")
                .setClickEvent(ClickEvent.Action.RUN_COMMAND, getCommand(f.getUuid())).build();
    }

    public static String boolToWord(boolean b){
        if (b){
            return EnumChatFormatting.GREEN + "Yes" + EnumChatFormatting.RESET;
        } else{
            return EnumChatFormatting.RED + "No" + EnumChatFormatting.RESET;
        }
    }

    public static String getRequestParams(){
        String uuid = Minecraft.getMinecraft().thePlayer.getUniqueID().toString();
        return "?uuid=" + uuid.replaceAll("-", "") + "&key=" + AspectOfTheFlipper.config.key;
    }

    public static String getPrefix(){
        return EnumChatFormatting.GRAY + "AOTF: " + EnumChatFormatting.RESET;
    }
}


