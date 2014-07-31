package com.UKC_AICS.simulation.gui.controlutils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public interface WindowInterface {
	
	enum ButtonType{
		ACCEPT("Accept"),
		CANCEL("Cancel");
		private final String name;
		ButtonType(String name){ 
			this.name = name;
			}
		public String getName(){
			return name;
		}
	}
	
	static ButtonType buttons[] = new ButtonType[] {ButtonType.ACCEPT, ButtonType.CANCEL};
	
	
	public void registerListener(DialogueWindowHandler listener);
	public void buttonSelected(ButtonType button);
	
}
