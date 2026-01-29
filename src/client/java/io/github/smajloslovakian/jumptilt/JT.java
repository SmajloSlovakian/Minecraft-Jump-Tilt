package io.github.smajloslovakian.jumptilt;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JT implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("jump-tilt");
    public static final Minecraft mc = Minecraft.getInstance();

	public static Config cfg;

	@Override
	public void onInitializeClient() {
		cfg=new Config();
	}
	public static void print(Object s){
		LOGGER.info(s+"");
	}
	public static void updateConfig(){
		cfg=new Config();
	}
}
