package smsk.jumptilt;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JT implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("jumptilt");
    public static final MinecraftClient mc = MinecraftClient.getInstance();

	public static Config cfg;

	@Override
	public void onInitialize() {
		cfg=new Config();
	}
	public static void print(Object s){
		LOGGER.info(s+"");
	}
	public static void updateConfig(){
		cfg=new Config();
	}
}
