package dev.icheppy.aotf.commands;

import dev.icheppy.aotf.AspectOfTheFlipper;
import dev.icheppy.aotf.requests.BinRequest;
import dev.icheppy.aotf.utils.MessageBuilder;
import dev.icheppy.aotf.utils.ScanSnipes;
import dev.icheppy.aotf.utils.Utils;
import dev.icheppy.aotf.dataclasses.ApiResponse;
import dev.icheppy.aotf.dataclasses.Flip;
import dev.icheppy.aotf.requests.SniperRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AspectOfTheFlipperCommand extends CommandBase {
    public final String commandName = "aspectoftheflipper";
    public final String commandAlias = "aotf";
    public final String commandUsage = "/aspectoftheflipper [help/start/stop]";
    public final String aotfStartUsage = "/aotf start {time in ticks}";


    @Override
    public String getCommandName() {
        return "aspectoftheflipper";
    }

    @Override
    public String getCommandUsage(ICommandSender ICommandSender) {
        return commandUsage;
    }

    @Override
    public void processCommand(final ICommandSender ICommandSender, String[] strings) throws CommandException {
        if (strings.length == 0){
            ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() + "no subcommand was given. Refer to the command usage: " + commandUsage));
        } else{

// ----------------------------------------------------------------------------------------------------------------------------------------------------
// /aotf start
// Starts the sniper scan loop
// The loop is automatically broken if the user is not authorized
            
            if (strings[0].equalsIgnoreCase("start")){

                if (AspectOfTheFlipper.scanning){
                    ICommandSender.addChatMessage(MessageBuilder.of(Utils.getPrefix())
                            .append("There is already a scan in progress.")
                            .build());
                } else{

                    ICommandSender.addChatMessage(MessageBuilder.of(Utils.getPrefix())
                            .append(" Starting an auction scan loop. ")
                            .setColor(EnumChatFormatting.GREEN)
                            .append("Make sure to disable the scan loop when you are not using the mod. Execute")
                            .setColor(EnumChatFormatting.WHITE)
                            .append(" /aotf stop ").setColor(EnumChatFormatting.GRAY)
                            .append("to stop the auction scan loop").setColor(EnumChatFormatting.WHITE)
                            .build());

                    new ScanSnipes(ICommandSender).start();
                }

// ----------------------------------------------------------------------------------------------------------------------------------------------------
// /aotf stop
// Command to stop the sniper scan loop

            } else if (strings[0].equalsIgnoreCase("stop")){
                if (!AspectOfTheFlipper.scanning){
                    ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() + EnumChatFormatting.GRAY
                            + "There is no active scan in progress"));
                    return;
                }

                AspectOfTheFlipper.scanning = false;

                ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() + EnumChatFormatting.GREEN
                        + "The scan was successfully stopped"));

// ----------------------------------------------------------------------------------------------------------------------------------------------------
// /aotf help
// Returns a description of all AOTF commands

            } else if(strings[0].equalsIgnoreCase("help")){
                ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() +
                        EnumChatFormatting.BOLD + EnumChatFormatting.GRAY + "-- List Of Available Commands --"));
                ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() +
                        EnumChatFormatting.WHITE + "/aotf start {scan time in ticks}" + EnumChatFormatting.GRAY
                        + " - Starts the flipper scan loop. Requires an additional argument specifying the delay between every scan in minecraft ticks (20 ticks = 1 second)"));
                ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() +
                        EnumChatFormatting.WHITE + "/aotf stop" + EnumChatFormatting.GRAY + " - stops the scan loop"));
                ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() +
                        EnumChatFormatting.WHITE + "/aotf help" + EnumChatFormatting.GRAY + " - displays this message"));

// ----------------------------------------------------------------------------------------------------------------------------------------------------
// /aotf key {key}
// Sets the user key and writes it into config
// Takes one argument
                
            } else if(strings[0].equalsIgnoreCase("key")){
                if (strings.length == 2){

                    AspectOfTheFlipper.config.key = strings[1];
                    try {
                        AspectOfTheFlipper.config.write();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() + EnumChatFormatting.GREEN
                            + "The key was successfully set!"));
                }else{
                    ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() + EnumChatFormatting.GRAY
                            + "Provide a single authentication key: /aotf key {your key here}"));
                }
// ----------------------------------------------------------------------------------------------------------------------------------------------------
// /aotf minROI
// Writes the minimum return on investment for a flip to be displayed
// Takes ROI as its only argument

            } else if (strings[0].equalsIgnoreCase("minroi")){
                if (strings.length != 2){
                    ICommandSender.addChatMessage(MessageBuilder.of(Utils.getPrefix())
                        .append(String.format(
                                "Current minimum return on investment is %f. To set the minimum for a flip to be displayed do /aotf minroi {lower bound}",
                                AspectOfTheFlipper.config.minROI
                        )).build());
                }
                else{
                    if (Utils.isNumeric(strings[1])) {
                        AspectOfTheFlipper.config.minROI = Double.parseDouble(strings[1]);
                        try {
                        AspectOfTheFlipper.config.write();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    }else{
                        ICommandSender.addChatMessage(MessageBuilder.of(Utils.getPrefix())
                                .append("Enter a numeric value instead")
                                .build());
                    }
                }
// ----------------------------------------------------------------------------------------------------------------------------------------------------
// /aotf maxPrice
            }else if (strings[0].equalsIgnoreCase("maxprice")){
                if (strings.length != 2){
                    ICommandSender.addChatMessage(new ChatComponentText(
                            "Current maximum buy price is "
                                    + Utils.toReadable(AspectOfTheFlipper.config.maxPrice)
                                    +" To set the max buy price for a flip to be displayed do /aotf maxprice {upper bound}. Use 0 to make in unlimited"
                    ));
                }
                else{
                    if (Utils.isNumeric(strings[1])) {
                        AspectOfTheFlipper.config.maxPrice = Integer.parseInt(strings[1]);
                    try {
                        AspectOfTheFlipper.config.write();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    }else{
                        ICommandSender.addChatMessage(MessageBuilder.of(Utils.getPrefix())
                                .append("Enter a numeric value instead")
                                .build());
                    }

                }
// ----------------------------------------------------------------------------------------------------------------------------------------------------
// /aotf maxProfit             
            }else if (strings[0].equalsIgnoreCase("minprofit")){
                if (strings.length != 2){
                    ICommandSender.addChatMessage(
                            new ChatComponentText(
                                    "Current minimum profit is "
                                            + Utils.toReadable(AspectOfTheFlipper.config.minProfit)
                                            +" To set the minimum profit for a flip to be displayed do /aotf minprofit {lower bound}"
                            ));
                }
                else{
                    if (Utils.isNumeric(strings[1])) {
                        AspectOfTheFlipper.config.minProfit = Integer.parseInt(strings[1]);

                    try {
                        AspectOfTheFlipper.config.write();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    }else{
                        ICommandSender.addChatMessage(MessageBuilder.of(Utils.getPrefix())
                                .append("Enter a numeric value instead")
                                .build());
                    }
                }
            } else {
                ICommandSender.addChatMessage(new ChatComponentText(Utils.getPrefix() + "no valid subcommand was given. Refer to the command usage: " + commandUsage));
            }
        } 
    }

    @Override
    public ArrayList<String> getCommandAliases() {
        return new ArrayList<String>() {
            {
                add("aotf");
            }
        };
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }


    public void scanBins(final ICommandSender ICommandSender){
        final BinRequest binRequest = new BinRequest();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // Send request
                    ApiResponse apiResponse = binRequest.getResponse();
                    // Print flips
                    Collections.sort(apiResponse.auctions);
                    for (Flip f : apiResponse.getAuctions()){
                        if (f.getSellPrice() > 0){
                            ICommandSender.addChatMessage(Utils.flipToMsg(f));
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
    }



    public static IChatComponent getNoAccessMsg(){
        return MessageBuilder.of(Utils.getPrefix())
                .append("You don't have access to the sniper module of the flipper. To get more information" +
                        " about how you can gain access, join ")
                .setColor(EnumChatFormatting.RED)
                .append("iCheppy's Hub")
                .setBold(true)
                .setColor(EnumChatFormatting.GOLD)
                .setClickEvent(ClickEvent.Action.OPEN_URL, AspectOfTheFlipper.discordUrl)
                .append(" (click) discord server")
                .setColor(EnumChatFormatting.RED)
                .build();
    }
}
