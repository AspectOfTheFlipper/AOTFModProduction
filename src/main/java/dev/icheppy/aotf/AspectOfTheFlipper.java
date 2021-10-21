package dev.icheppy.aotf;

import dev.icheppy.aotf.commands.AspectOfTheFlipperCommand;
import dev.icheppy.aotf.config.Config;
import dev.icheppy.aotf.listeners.JoinHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;

import java.io.IOException;;

@Mod(modid = AspectOfTheFlipper.MODID, version = AspectOfTheFlipper.VERSION, clientSideOnly = true)
public class AspectOfTheFlipper
{
    public static final String MODID = "AspectOfTheFlipper";
    public static final String VERSION = "1.1";
    public static final String baseUrl = "https://aotfmod.com/api";
    public static final String discordUrl = "https://discord.gg/8X3yAVxmjz";
    public static boolean scanning = false;
    public static Config config;


    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        config = new Config();
        ClientCommandHandler.instance.registerCommand(new AspectOfTheFlipperCommand());
        MinecraftForge.EVENT_BUS.register(new JoinHandler());
    }
}