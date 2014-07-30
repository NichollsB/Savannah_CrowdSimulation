package com.UKC_AICS.simulation.screen.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SettingsWindow extends Window implements WindowInterface {
	private final Table contentTable = new Table();
	private final ScrollPane scroller = new ScrollPane(contentTable);
	
	private final ObjectMap<String, TextField> entryFields = new ObjectMap<String, TextField>();
	
	private Skin skin;
	
	private final Array<WindowListener> listeners = new Array<WindowListener>();
	
	private String name;

	
	public SettingsWindow(String title, String note, ObjectMap<String, String> fields, int height, int width, Skin skin) {
		super(title, skin);
		this.name = title;
		this.skin = skin;
		this.setWidth(width);
		this.setHeight(height);
		addHeading(title);
		contentTable.row();
		addHeading(note);
		contentTable.row();
		for(String header : fields.keys()){
			contentTable.add(createField(header));
			if(fields.get(header) != null && !fields.get(header).isEmpty())
				entryFields.get(header).setText(fields.get(header));
			contentTable.row();
		}
		contentTable.add(addOptions(new ButtonType[]{ButtonType.ACCEPT, ButtonType.CANCEL}));
	}
	
	
	public void addHeading(String heading){
		Label title = new Label(heading, skin);
		contentTable.add(heading).align(Align.center).center().top().expandX().fillX();
//		entryFields
	}
	
	public Group createField(String field){
		HorizontalGroup fieldGroup = new HorizontalGroup();
		fieldGroup.align(Align.left);
		TextField textField = new TextField(field, skin);
		fieldGroup.addActor(new Label(field, skin));
		fieldGroup.addActor(textField);
		
//		contentTable.add(fieldGroup);
		entryFields.put(field, textField);
		return fieldGroup;
	}
	
	public Group addOptions(ButtonType[] buttons){
		HorizontalGroup optionsGroup = new HorizontalGroup();
		optionsGroup.center().bottom();
		TextButton button;
		for(ButtonType type : buttons){
			button = new TextButton(type.getName(), skin);
			button.addListener(new optionsListener(this, type));
			optionsGroup.addActor(button);
		}
		return optionsGroup;
	}
	
	public void clearFields(){
		for(TextField field :entryFields.values()){
			field.setText("");
		}
	}


	@Override
	public void registerListener(WindowListener listener) {
		listeners.add(listener);
		
	}


	@Override
	public void buttonSelected(ButtonType button) {
		for(WindowListener listener : listeners){
			switch (button) {
				case ACCEPT :
					ObjectMap<String, String> returnValues = new ObjectMap<String, String>();
					for(String field : entryFields.keys()){
						returnValues.put(field, entryFields.get(field).getText());
					}
					listener.onConfirmed(returnValues, this);
					clearFields();
					break;
				case CANCEL :
					listener.onCancelled(this);
					clearFields();
					break;
			}
		}
	}

	


	
}
