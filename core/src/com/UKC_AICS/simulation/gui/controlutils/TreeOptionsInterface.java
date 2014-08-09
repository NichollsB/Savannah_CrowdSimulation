package com.UKC_AICS.simulation.gui.controlutils;

import com.UKC_AICS.simulation.gui.controlutils.WindowInterface.ButtonType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;

public interface TreeOptionsInterface {
	
	enum ButtonType{
		ADD("Add"),
		REMOVE("Remove"),
		CHECKED("Place");
		private final String name;
		ButtonType(String name){ 
			this.name = name;
			}
		public String getName(){
			return name;
		}
	}
	static ButtonType buttonTypes[] = new ButtonType[] {ButtonType.ADD, ButtonType.REMOVE};
	
	public void registerListener(TreeOptionsListener listener);
	public void buttonSelected(ButtonType button);
//	public void showButtonHelper(boolean showHelper, String helper);

}
