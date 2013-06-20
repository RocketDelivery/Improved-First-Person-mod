package kes5219.improvedfirstperson.client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.renderer.entity.Render;

public class AnimPlayerCompatHelper 
{
	public static Class classPlayerData;
	public static Method methodGetPlayerData;
	public static Field fieldTextureInfo;
	
	public static Class classTextureInfo;
	public static Field fieldAnimateEyes;
	public static Field fieldAnimateEyebrows;
	public static Field fieldAnimateMouth;
	
	public static Render playerRenderer;
	public static Class classRenderPlayer;
	public static Field fieldPlayerModel;
	public static Field fieldPlayerArmorHeadModel;
	
	public static Class classModelPlayer;
	public static Field fieldHead;
	public static Field fieldHeadwear;
}
