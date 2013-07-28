package kes5219.improvedfirstperson.hooks;

import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;

public class BookHook {
	
	//Class transformer is used to inject the following code at the beginning of the method
	//onItemRightClick(ItemStack stack, World world, EntityPlayer player) in ItemEditable and ItemWritable :
	//if(!OnUseBookHook.shouldSkip()) return;
	//By returning true, it will cause written books not to open their GUI.
    public static boolean shouldSkip()
	{
    	System.out.println("skip");
    	return false;
	}
    
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
    {
    	
    }
    
}
