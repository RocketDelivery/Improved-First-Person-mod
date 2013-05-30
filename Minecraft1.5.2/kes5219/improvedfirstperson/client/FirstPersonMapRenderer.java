package kes5219.improvedfirstperson.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;

public class FirstPersonMapRenderer implements IItemRenderer {
	public FirstPersonMapRenderer(){
		
	}
	
	 
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type == IItemRenderer.ItemRenderType.EQUIPPED)
		{
			return true;
		} else
		{
			return false;
		}
	}

	 
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	 
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Minecraft mc = Minecraft.getMinecraft();
		float f14 = 0.8F;
        GL11.glScalef(-f14, -f14, f14);
        GL11.glRotatef(-10F, 0.0F, 1.0F, 0.0F); //front and back rotation
        GL11.glRotatef(15F, 0.0F, 0.0F, 1.0F);//up and down rotation
        GL11.glTranslatef(-2.4F, -2.0F, 0.0F);
        float f20 = 0.015625F;
        GL11.glScalef(f20, f20, f20);
        //mc.renderEngine.bind
        mc.renderEngine.bindTexture("/misc/mapbg.png"); //TODO check bindTexture
        Tessellator tessellator = Tessellator.instance;
        GL11.glNormal3f(0.0F, 0.0F, -1F);
        tessellator.startDrawingQuads();
        byte byte0 = 7;
        tessellator.addVertexWithUV(0 - byte0, 128 + byte0, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(128 + byte0, 128 + byte0, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(128 + byte0, 0 - byte0, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0 - byte0, 0 - byte0, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        if(item.itemID == Item.map.itemID) {
	        MapData mapdata = Item.map.getMapData(item, mc.theWorld);
	        if(mapdata != null) {
	        	mc.entityRenderer.itemRenderer.mapItemRenderer.renderMap(mc.thePlayer, mc.renderEngine, mapdata);
	        }
	    }
	}

}
