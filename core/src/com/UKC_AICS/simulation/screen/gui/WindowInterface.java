package com.UKC_AICS.simulation.screen.gui;

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
	
	
	public void registerListener(WindowListener listener);
	public void buttonSelected(ButtonType button);
	
	class optionsListener extends ClickListener{
		private WindowInterface window;
		private ButtonType button;
		
		public optionsListener(WindowInterface window, ButtonType button){
			this.window = window;
			this.button = button;
		}
		public void clicked(InputEvent event){
			window.buttonSelected(button);
		}
	};
}
