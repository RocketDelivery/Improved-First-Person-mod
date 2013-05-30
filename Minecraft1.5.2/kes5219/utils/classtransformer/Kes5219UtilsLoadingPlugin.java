package kes5219.utils.classtransformer;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

//@TransformerExclusions({"kes5219.utils.classtransformer", "kes5219.utils.classtransformer.helper"})
public class Kes5219UtilsLoadingPlugin implements IFMLLoadingPlugin {

	public String[] getLibraryRequestClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getASMTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getModContainerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public void injectData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}
}
