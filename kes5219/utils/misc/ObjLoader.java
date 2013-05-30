package kes5219.utils.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class ObjLoader {
	
	//Not used currently, but may be used in future mods
	/**
	 * @param stream 	The stream you are reading from, such as FileInputStream or ZipInputStream. 
	 * 					Model has to have vertex position(3 floats), texture coordinate(2 floats), 
	 * 					and normal vector(3 floats) for all of the vertices.
	 * @return 	The display list index that can be used in GL11.glCallList(index) to render 
	 * 			your model. Returns 0 if the model could not be loaded.
	 */
	public static int loadObjToDisplaylist(InputStream stream) {
		ArrayList<Triple<Float, Float, Float>> vertices = 
				new ArrayList<Triple<Float, Float, Float>>();
		ArrayList<Triple<Float, Float, Float>> texCoords = 
				new ArrayList<Triple<Float, Float, Float>>();
		ArrayList<Triple<Float, Float, Float>> normals = 
				new ArrayList<Triple<Float, Float, Float>>();
		ArrayList<Triple<Integer, Integer, Integer>> faceDef =
				new ArrayList<Triple<Integer, Integer, Integer>>();
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(stream));
			while(true)
			{
				String line;
				line = reader.readLine();
				if(line == null)
				{
					break;
				}
				if(line.startsWith("#"))
				{
					continue;
				} else
				if(line.startsWith("vt"))
				{
					//Texture Coordinate
					String[] splitString = line.split("\\s+");
					texCoords.add(new Triple<Float, Float, Float>(
							new Float(splitString[1]),
							new Float(splitString[2])));
				} else
				if(line.startsWith("vn"))
				{
					//Vertex Normal
					String[] splitString = line.split("\\s+");
					normals.add(new Triple<Float, Float, Float>(
							new Float(splitString[1]),
							new Float(splitString[2]),
							new Float(splitString[3])));
				} else
				if(line.startsWith("v"))
				{
					//Vertex
					String[] splitString = line.split("\\s+");
					vertices.add(new Triple<Float, Float, Float>(
							new Float(splitString[1]),
							new Float(splitString[2]),
							new Float(splitString[3])));
				} else
				if(line.startsWith("f"))
				{
					//Face Definition
					String[] splitString = line.split("\\s+");
					String splitString1;
					String[] splitStrings;
					int count = 1;
					while(count < 4)
					{
						splitString1 = splitString[count];
						splitStrings = splitString1.split("/");
						int vertexIndex = 0;
						int texCoordIndex = 0;
						int normalIndex = 0;
						
						vertexIndex = Integer.parseInt(splitStrings[0]);
						texCoordIndex = Integer.parseInt(splitStrings[1]);
						normalIndex = Integer.parseInt(splitStrings[2]);
						
						faceDef.add(new Triple<Integer, Integer, Integer>(
								vertexIndex, texCoordIndex,normalIndex));//texCoordIndex, normalIndex));
						count++;
					}
					if(splitString.length == 5)
					{
						Triple<Integer, Integer, Integer> firstVertex = faceDef.get(faceDef.size()-3);
						Triple<Integer, Integer, Integer> lastVertex = faceDef.get(faceDef.size()-1);
						splitString1 = splitString[4];
						splitStrings = splitString1.split("/");
						int vertexIndex = 0;
						int texCoordIndex = 0;
						int normalIndex = 0;
						
						vertexIndex = Integer.parseInt(splitStrings[0]);
						texCoordIndex = Integer.parseInt(splitStrings[1]);
						normalIndex = Integer.parseInt(splitStrings[2]);

						Triple<Integer, Integer, Integer> fourthVertex = 
								new Triple<Integer, Integer, Integer>(vertexIndex, texCoordIndex, normalIndex);
						faceDef.add(firstVertex);
						faceDef.add(lastVertex);
						faceDef.add(fourthVertex);
					}
				} else
				{
					continue;
				}
			}
		} catch (IOException e)
		{
			return 0;
		} catch (ArrayIndexOutOfBoundsException e)
		{
			//The "dimension" of the texture coordinate is less than 2
			return 0;
		} catch (NumberFormatException e)
		{
			//most likely an invalid file
			return 0;
		} finally
		{
			try 
			{
				reader.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		int displayListIndex = GL11.glGenLists(1);
		if(displayListIndex == 0)
		{
			//failed to create a display list for some reason
			return 0;
		}
		
		int arraySize = faceDef.size();
		ArrayList<Triple<Float, Float, Float>> vertexArrayList =
				new ArrayList<Triple<Float, Float, Float>>();
		for(int i = 0; i < arraySize; i++)
		{
			int index = faceDef.get(i).first;
			vertexArrayList.add(vertices.get(index-1));
		}
		float[] vertexArray = new float[vertexArrayList.size()*3];
		for(int i = 0; i < vertexArrayList.size(); i++)
		{
			vertexArray[i*3] = vertexArrayList.get(i).first;
			vertexArray[i*3+1] = vertexArrayList.get(i).second;
			vertexArray[i*3+2] = vertexArrayList.get(i).third;
		}
		FloatBuffer vertexPointer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexPointer.put(vertexArray);
		vertexPointer.flip();
		
		ArrayList<Triple<Float, Float, Float>> texCoordArrayList = 
				new ArrayList<Triple<Float, Float, Float>>();
		for(int i = 0; i < arraySize; i++)
		{
			int index = faceDef.get(i).second;
			texCoordArrayList.add(texCoords.get(index - 1));
		}
		float[] texCoordArray = new float[texCoordArrayList.size()*2];
		for(int i = 0; i < texCoordArrayList.size(); i++)
		{
			texCoordArray[i*2] = texCoordArrayList.get(i).first/16;
			texCoordArray[i*2+1] = texCoordArrayList.get(i).second/16;
		}
		FloatBuffer texCoordPointer = BufferUtils.createFloatBuffer(texCoordArray.length);
		texCoordPointer.put(texCoordArray);
		texCoordPointer.flip();
		
		ArrayList<Triple<Float, Float, Float>> normalArrayList = 
				new ArrayList<Triple<Float, Float, Float>>();
		for(int i = 0; i < arraySize; i ++)
		{
			int index = faceDef.get(i).third;
			normalArrayList.add(normals.get(index - 1));
		}
		float[] normalArray = new float[normalArrayList.size()*3];
		for(int i = 0; i < normalArrayList.size(); i++)
		{
			normalArray[i*3] = normalArrayList.get(i).first;
			normalArray[i*3+1] = normalArrayList.get(i).second;
			normalArray[i*3+2] = normalArrayList.get(i).third;
		}
		FloatBuffer normalPointer = BufferUtils.createFloatBuffer(normalArray.length);
		normalPointer.put(normalArray);
		normalPointer.flip();
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glVertexPointer(3, 0, vertexPointer);
		
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glTexCoordPointer(2, 0, texCoordPointer);
		
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glNormalPointer(0, normalPointer);
		
		GL11.glNewList(displayListIndex, GL11.GL_COMPILE);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, arraySize);
		GL11.glEndList();
		
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		
		return displayListIndex;
	}
}
