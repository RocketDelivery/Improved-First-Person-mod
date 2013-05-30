package kes5219.utils.misc;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

//Retrieves partial tick which is crucial for rendering
public class PartialTickRetriever implements ITickHandler {

	private static float partialTick;
	
	 
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.RENDER)) {
			if(Minecraft.getMinecraft().theWorld != null) {		
				partialTick = (Float)tickData[0];
			}
		}
	}	
	
	public static float getPartialTick()
	{
		return partialTick;
	}
	
	 
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}
	
	 
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	 
	public String getLabel() {
		// TODO 
		return null;
	}

}
