package kes5219.improvedfirstperson.client;

import java.util.ArrayList;
import java.util.EnumSet;

import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class IFPKeyHandler extends KeyHandler {
	
	static KeyBinding toggle = new KeyBinding("Toggle IFP View", Keyboard.KEY_F6);
	
	// Simple constructor that sends arguments to the KeyHandler constructor.
	public IFPKeyHandler() {
		super(new KeyBinding[]{toggle},
				new boolean[]{false});
	}
	
	@Override
	public String getLabel() {
		return "ModImprovedFirstPersonBindings";
	}

	// Key down method, used to toggle the body rendering.
	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		if (tickEnd && IFPClientProxy.getMC().currentScreen == null)
		{
			if (kb.keyCode == toggle.keyCode)
			{
				ModImprovedFirstPerson.enableBodyRender = !ModImprovedFirstPerson.enableBodyRender;
			}
		}
	}
	
	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) { }
	
	@Override
	public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
	}

}
