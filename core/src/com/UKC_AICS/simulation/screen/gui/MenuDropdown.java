package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.screen.controlutils.MenuSelectEvent;
import com.UKC_AICS.simulation.screen.controlutils.MenuSelectListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class MenuDropdown extends SelectBox implements MenuSelectEvent{	
	private Skin skin;
	private String identifier;
	private Array<MenuSelectListener> listeners = new Array<MenuSelectListener>();
	
	private final Array<String> menuItems = new Array<String>();
	
	private String menuRoot;
	
	public MenuDropdown(Skin skin, String root, String id) {
		super(skin);
		this.skin = skin;
		identifier = id;
		this.menuRoot = root;
		this.menuItems.add(menuRoot);
		this.setItems(menuItems);
		this.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(getSelected().equals(menuRoot)) return;
				for(MenuSelectListener l : listeners){
					l.selectionMade(this, getSelected());
				}
				setSelected(menuRoot);
			}
		});
	}
	
	public void addItems(String[] items, boolean replace){
		if(replace){
			menuItems.clear();
			menuItems.add(menuRoot);
		}
		menuItems.addAll(items);
		this.setItems(menuItems);
	}
	
	public void addItem(String item){
		menuItems.add(item);
		this.setItems(menuItems);
	}
	
	public String getID(){
		return identifier;
	}
	
	@Override
	public void addSelectionListener(MenuSelectListener menuSelectListener){
		listeners.add(menuSelectListener);
	}
	
	
}
