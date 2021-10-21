package dev.icheppy.aotf.utils;

import dev.icheppy.aotf.AspectOfTheFlipper;
import dev.icheppy.aotf.dataclasses.ApiResponse;
import dev.icheppy.aotf.dataclasses.Flip;
import dev.icheppy.aotf.requests.KeyUnauthorizedException;
import dev.icheppy.aotf.requests.SniperRequest;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Date;

public class ScanSnipes extends Thread implements Runnable{
    private ICommandSender iCommandSender;
    public ScanSnipes(ICommandSender iCommandSender2) {
        iCommandSender = iCommandSender2;
    }

    public void run() {
        AspectOfTheFlipper.scanning = true;

        long lastUpdated = 0;
        long timeSinceLastUpdate = 0;
        SniperRequest sniperRequest = new SniperRequest();


        while (AspectOfTheFlipper.scanning) {
            try{
                ApiResponse apiResponse = sniperRequest.getResponse();
                if (apiResponse.getUpdated() != lastUpdated) {
                    Date d = new Date();
                    iCommandSender.addChatMessage(MessageBuilder.of(Utils.getPrefix())
                            .append(String.format(
                                    "Found %s auctions. Time since last update %s",
                                    apiResponse.getAuctions().size(),
                                    d.getTime() - apiResponse.getUpdated()))
                            .build());




                    Collections.sort(apiResponse.auctions);

                    for (Flip f : apiResponse.getAuctions()){
                        if ((f.getSellPrice()-f.getBuyPrice()) >= AspectOfTheFlipper.config.minProfit
                                && f.getRoi() >= AspectOfTheFlipper.config.minROI
                                && (AspectOfTheFlipper.config.maxPrice == 0
                                || f.getBuyPrice() <= AspectOfTheFlipper.config.maxPrice)){

                            iCommandSender.addChatMessage(Utils.flipToMsg(f));
                        }
                    }

                    lastUpdated = apiResponse.getUpdated();
                    timeSinceLastUpdate = d.getTime() - lastUpdated;

                    if (69500 + apiResponse.getUpdated() - d.getTime() > 0) {
                        System.out.println("Fetching next request in " + Long.toString(69500 + apiResponse.getUpdated() - d.getTime())+ "ms");
                        Thread.sleep(69500 + apiResponse.getUpdated() - d.getTime());
                    }
                }

            } catch (KeyUnauthorizedException e){
                iCommandSender.addChatMessage(MessageBuilder.of(Utils.getPrefix())
                        .append("Failed to fetch flips. You are not authorized by the server. Aborting the scan loop")
                        .setColor(EnumChatFormatting.RED)
                        .build());
                AspectOfTheFlipper.scanning = false;

                break;
            } catch (SocketTimeoutException e){
                System.out.println("Request timed out. Retrying");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
                AspectOfTheFlipper.scanning = false;
                break;

            }catch (InterruptedException e){
                AspectOfTheFlipper.scanning = false;
                e.printStackTrace();
                break;
            }

        }
        Thread.currentThread().interrupt();
    }
}

