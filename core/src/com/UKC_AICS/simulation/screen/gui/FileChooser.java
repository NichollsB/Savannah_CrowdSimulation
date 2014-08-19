package com.UKC_AICS.simulation.screen.gui;

import java.io.File;

import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.gui.controlutils.MenuSelectEvent;
import com.UKC_AICS.simulation.gui.controlutils.MenuSelectListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

public class FileChooser extends Dialog implements MenuSelectEvent{
	private static final File defaultDir = new File("./");
	private File startDir = defaultDir;
	private File currentDir;
	private File selectedFile;
	
	private Skin skin;
	private boolean internal = true;
	
	//Content Components
	private final Table listTable = new Table();
	private TextField fileField;
	
	private Array<File> dirRecord = new Array<File>();
	
	TextButton.TextButtonStyle listStyle = new TextButtonStyle();
	
	private final Array<MenuSelectListener> listeners = new Array<MenuSelectListener>();
	
	private boolean pathFromTextField = false;
	
	private String type;
	private String identifier;
	
	private Stage stage;
	
	private String command;
	
	private TextField pathField;
	private Label headerLabel;
	
	private final ObjectMap<String, TextButton> buttons = new ObjectMap<String, TextButton>(){{
		put("confirm", null);
		put("cancel", null);
	}};

	public FileChooser(String title, Skin skin, String command, String id, String dir, String confirmText, String cancelText, Stage stage) {
		super(title, skin);
		this.command = command;
		this.identifier = id;
		startDir = defaultDir;
		this.skin = skin;
		if(dir != null || !dir.isEmpty()){
			File f = new File(dir);
			if(f.exists() && f.isDirectory())
				startDir = f;
		}
		
		create(confirmText, cancelText, stage);
	}
	public FileChooser(String title, Skin skin, String command, String id, File dir, String confirmText, String cancelText, Stage stage) {
		super(title, skin);
		this.command = command;
		this.identifier = id;
		identifier = title;
		if(dir != null)
			startDir = dir;
		this.skin = skin;
//		dirRecord.add(startDir);
//		currentDir = startDir;
		create(confirmText, cancelText, stage);
		
	}
	
	private void create(String confirmText, String cancelText, Stage stage){
//		this.hide();
		this.setWidth(400);
		this.setHeight(400);
		this.setPosition((stage.getWidth()/2)-(this.getWidth()/2), (stage.getHeight()/2)-(this.getHeight()/2));
		//Try adding this back in on full build
//		if(Gdx.files.isLocalStorageAvailable())
//			startDir = Gdx.files.internal(Gdx.files.getLocalStoragePath());
		
		pathField = new TextField("", skin);
		pathField.setDisabled(true);
		fileField = new TextField("", skin);
		fileField.setTextFieldListener(new TextField.TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				pathFromTextField = true;
			}
		});
		
		if(confirmText != null)
			buttons.put("confirm", new TextButton(confirmText, skin));
		else
			buttons.put("confirm", new TextButton("Confirm", skin));
		
		if(cancelText != null)
			buttons.put("cancel", new TextButton(cancelText, skin));
		else
			buttons.put("cancel", new TextButton("Cancel", skin));
		
		button(buttons.get("confirm"), "confirm");
		button(buttons.get("cancel"), "cancel");
		
		Label.LabelStyle lstyle = new Label("", skin).getStyle();
		TextButton.TextButtonStyle style = new TextButton("", skin).getStyle();
		listStyle = new TextButton.TextButtonStyle(lstyle.background, style.down, style.checked, style.font);
		listStyle.over = style.down;
		Table content = getContentTable();
		headerLabel = new Label(command + " " + identifier + " file.", skin);
		content.add(headerLabel).align(Align.left).expandX().fillX();
		content.row();
        Table t = new Table();
		t.add(new Label("Directory Path", skin));
		t.add(pathField).fillX().expandX();
        content.add(t).fillX().expandX();
        content.row();
		ScrollPane pane = (ScrollPane) createScrollPane(listTable, false, true, true, false);
		content.add(pane).left().fill().expand();
//		content.add(new Table()).fill().expand();
		content.row();
		Table fileBar = new Table();
		fileBar.add(fileField).fillX().expandX();
		TextButton upBtn = new TextButton("Back", skin);
		upBtn.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
//				if(dirRecord.size > 0)
//					openDir(dirRecord.peek());#
				if(!currentDir.equals(startDir))
					openDir(currentDir.getParentFile());
			}
		});
		fileBar.add(upBtn);
		content.add(fileBar).expandX().fillX();
//		dirSelectLabels.put(new Label(startDir.name(), skin), startDir);
//		for(File handle : startDir.list()){
//			if(handle.isDirectory()){
//				label = new Label(("\t"+handle.name()), skin);
//				dirSelectLabels.put(label, handle);
//			}
//		}
		
		
		this.internal = internal;
		
		System.out.println("Try to open " + startDir.getPath());
		openDir(startDir);
		
//		stage.addActor(this);
//		this.pack();

		
	}
	
	
	/**
	 * Should be called upon dialog button selection
	 */
	protected void result (Object object){
		String btn;
		
		for(String button : buttons.keys()){
			btn = (String)object;
//			System.out.println(buttons.get(button).getText());
			if(btn.equals("confirm")){
				if(pathFromTextField){
					updateSelectedFile(fileField.getText());
				}
					
				for(MenuSelectListener l : listeners){
					l.selectionMade(this, selectedFile);
				}
			}
		}
		
	}

	/**
	 * Open a given directory in the FileChooser
	 * @param dirHandle File of the directory (or file) to open
	 * @return If the directory was successfully opened
	 */
	public boolean openDir(File dirHandle){
		
//		System.out.println("Opening directory" + dirHandle.path() + " dirRecord " + dirRecord.size);
		if(!dirHandle.exists()) return false;
		System.out.println("OpenDir File path " + dirHandle.getPath());
		File dir = dirHandle;
		if(dirRecord.size>0){
//			System.out.println("Pop parent ")
			
			if(dirRecord.peek().equals(dirHandle) && !dir.equals(startDir))
				dir = dirRecord.pop();
			
		}
//		System.out.println("Opening " + dir.getPath() + " is directory " + dir.isDirectory() + " current " + currentDir.getPath()) ;
		if(dir.isDirectory() && !dir.equals(currentDir)){
			if(!dir.equals(startDir))
				dirRecord.add(currentDir);
			System.out.println("Opening " + dir.getPath());
//			lastDir = currentDir;
			this.currentDir = dir;
			listTable.clear();
//			if(filters != null){
//				for(String filter : filters){
//					listFiles(dir.list(), false);
//				}
//			}
//			else 
			listFiles(dir.listFiles(), false);
			String path = currentDir.getPath();
			if(path.contains("./bin/")){//path.startsWith("./bin")){
				path = path.replaceFirst("./bin/", "./");
			}
			else if(path.contains("./bin")){
				path = path.replace("./bin", "./");
				fileField.setText(currentDir.getName());
			}
			return true;
		}
		else if (!dir.isDirectory() && !dir.equals(currentDir)){
//			lastDir = currentDir;
			if(!dir.getParentFile().getParent().equals(currentDir)){
				if(!dir.equals(startDir))
					dirRecord.add(currentDir);
				this.currentDir = dir.getParentFile();
			}
			updateSelectedFile(dir);
		}
		pathFromTextField = false;
        pathField.setDisabled(false);
        pathField.setText(currentDir.getPath());
        pathField.setDisabled(true);
		return false;
	}
	
	/**
	 * Open a directory from a given String path
	 * @param dir String path to try and open
	 * @param internal boolean indicating if this file is an internal directory type, or not. If not, will open from local soource
	 * @return If the directory was opened or not
	 */
	public boolean openDir(String dir, boolean internal){
		if(dir == null || dir.isEmpty()) return false;
//		if(internal) return openDir(new File(dir));
//		else return openDir(newF);
		File f = new File(dir);
		return openDir(f);
		
	}
	
	/**
	 * Generates the sequence of buttons displayed from a given array of File's
	 * @param files File[] array which should be used to create buttons
	 * @param clear boolean indicating whether or not to clear the list of buttons or not. If not the new items will be added to those already present
	 */
	private void listFiles(File[] files, boolean clear){
//		System.out.println("Lsiting files");
		String path;
		Label fileLabel;
		Button fileButton;
		Table content = new Table();
		if(clear)
			listTable.clear();
		
		for(File handle : files){
			System.out.println("Adding file " + handle.getName());
			path = handle.getName();
			fileLabel = new Label(path, skin);
			fileButton = new Button(listStyle);
			fileButton.add(fileLabel).fillX().expandX();
			fileButton.addListener(new PathSelectListener(handle));
//	
			fileLabel.setAlignment(Align.left);
			listTable.add(fileButton).expandX().fillX();
			listTable.row();
			
		}
		listTable.add(content).fill().expand();
//		listTable.add(new Table()).fillX().expandX();
	}
	/**
	 * Sets the currently selectedFile and updates the TextField displaying the file path
	 * @param handle The File to set as currently selected
	 */
	private void updateSelectedFile(File handle){
		selectedFile = handle;
//		String path = handle.get;
//		File f = handle;
//		String fieldPath = "";
//		while(!f.getParentFile().equals(startDir)){
//			fieldPath = f.getName();
//			f = f.getParentFile();
//		}
		fileField.setText(handle.getName());
		pathFromTextField = false;
	}
	
	private void updateSelectedFile(String path){
		File targetPath = new File(path);
		String filePath = currentDir.getPath();
//		if(!filePath.endsWith("/")&&!targetPath.getName().startsWith("/"))
//			filePath += "/";
		filePath = filePath + targetPath.getName();
				
		selectedFile = new File(filePath);
//		if(targetPath.exists())
//			selectedFile = targetPath;
//		else 
//			selectedFile = currentDir.
//		if(path.contains(currentDir.getAbsolutePath())){
//			selectedFile = Gdx.files.internal(path);
//		}
//		else {
//			selectedFile = Gdx.files.internal(currentDir.path()+path);
//		}
	}
	
	/**
	 * Listener class for detecting selection of a file list button - in turn calling openDir to open the contained File
	 * @author Ben Nicholls
	 *
	 */
	class PathSelectListener extends ClickListener{
		private File handle;
		/**
		 * Initialise the listener
		 * @param handle File associated with the button this listener is instantiated for
		 */
		PathSelectListener(File handle){
			this.handle = handle;
		}
		/**
		 * When clicked the openDir is called to open the attached File
		 */
		public void clicked (InputEvent event, float x, float y) {
			if((handle.isDirectory() && getTapCount() >= 2))
				if(!handle.equals(selectedFile)||!handle.equals(currentDir))	{
					openDir(handle);
					return;
				}
			if(handle.isFile())
				updateSelectedFile(handle);
		} 
	}
	
	/**
	 * Generate a ScrollPane containing the content argument actor.
	 * @param content Actor the scrollpane will contain
	 * @param showX boolean indicating whether to permanently display the horizontal scroll bar or not
	 * @param showY boolean indicating whether to permanently display the vertical scroll bar or not
	 * @param bottomBar boolean indicating whether the horizontal scroll bar should be displayed on the bottom of the scroll pane
	 * @param rightBar boolean indicating whether the vertical scroll bar should be displayed on the right of the scroll pane
	 * @return The ScrollPane Actor
	 */
	private Actor createScrollPane(Actor content, boolean showX, boolean showY, boolean bottomBar, boolean rightBar){
    	Table scrollTable = new Table(skin);
//    	scrollTable.add("east");
    	Table fillTable = new Table();
		scrollTable.add(content).left().fill().expand();
		scrollTable.add(new Table()).fill().expand();
		ScrollPane scroll = new ScrollPane(scrollTable, skin);
    	InputListener stopTouchDown = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};
//		scroll.setForceScroll(true, true);
		scroll.setSmoothScrolling(true);
		scroll.setScrollBarPositions(bottomBar, rightBar);
		scroll.setForceScroll(showX, showY);
//	    scroll.setFlickScroll(true);
	    scroll.setOverscroll(false, false);
	    scroll.setFadeScrollBars(false);
		return scroll;
	}
	
	public File getSelected(){
		return selectedFile;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	public String getCommand(){
		return command;
	}
	public void setIdentifier(String id){
		identifier = id;
		setTitle(id);
	}
	public String getIdentifier(){
		return identifier;
	}
	
	/**
	 * Set the text of the dialog buttons
	 * @param confirm String to apply to the confirm button - if null, does not change
	 * @param cancel String to apply to the cancel button - if null, does not change
	 */
	public void setOptionsText(String confirm, String cancel){
		if(confirm != null && !confirm.isEmpty()){
			buttons.get("confirm").setText(confirm);
		}
		if(cancel != null && !cancel.isEmpty()){
			buttons.get("cancel").setText(cancel);
		}
	}
	
	public void open(Stage stage){
//		stage.addActor(this);
		selectedFile = null;
		this.show(stage);
		this.setHeight(400);
		this.setWidth(400);
		this.setPosition((stage.getWidth()/2)-(this.getWidth()/2), (stage.getHeight()/2)-(this.getHeight()/2));
		headerLabel.setText(command + " " + identifier + " file.");
		openDir(startDir);
	}
	
	
	@Override
	public void addSelectionListener(MenuSelectListener menuSelectListener){
		listeners.add(menuSelectListener);
	}
	
}
