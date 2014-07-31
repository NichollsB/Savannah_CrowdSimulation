package com.UKC_AICS.simulation.gui.controlutils;

import com.UKC_AICS.simulation.gui.controlutils.WindowInterface.ButtonType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public interface TreeOptionsInterface {
	
	enum ButtonType{
		ADD("Add"),
		REMOVE("Remove");
		private final String name;
		ButtonType(String name){ 
			this.name = name;
			}
		public String getName(){
			return name;
		}
	}
	static ButtonType buttons[] = new ButtonType[] {ButtonType.ADD, ButtonType.REMOVE};
	
	public void registerListener(TreeOptionsHandler listener);
	public void buttonSelected(ButtonType button);

}
