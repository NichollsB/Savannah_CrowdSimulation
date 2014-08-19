package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.gui.controlutils.MenuSelectEvent;
import com.UKC_AICS.simulation.gui.controlutils.MenuSelectListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Ben on 19/08/2014.
 */
public class EnvironmentFileWindow extends Dialog implements MenuSelectEvent {

    private Table packFileTable = new Table();
    private Table seperateFileTable = new Table();
    private Table variableTable = new Table();
    private boolean fromPackFile;
    private final ObjectMap<String, File> packFiles = new ObjectMap<String, File>(){{
        put("packfile", null);
        put("packatlas", null);
    }};
    private final ObjectMap<String, File> seperateFiles = new ObjectMap<String, File>(){{
        put("water", null);
        put("grass", null);
        put("terrain", null);
    }};

    private FileChooser fileChooser;

    private Skin skin;
    private Stage stage;

    private final Array<MenuSelectListener> listeners = new Array<MenuSelectListener>();

    public EnvironmentFileWindow(String title, Skin skin, Stage stage) {
        super(title, skin);
        this.skin = skin;
        this.stage=stage;
        create();
    }

    private void create(){
        fileChooser = new FileChooser("Load", skin, "Load", "packfile", "../", "Confirm", "Cancel", stage);


        Table content = this.getContentTable();
        content.add(new Label("Select environment files", skin)).fillX().expandX();
        final CheckBox check = new CheckBox("Load from packfile", skin);
        content.row();
        content.add(check).fillX().expandX();

        //PACK FILES
        packFileTable.add(new Label("Load environment settings from a pack file /n" +
                "Select a pack file and associated texture sheet image file", skin)).fillX().expandX().colspan(2);
        packFileTable.row();
        final TextField packField = new TextField("Select pack file", skin);
        packFileTable.add(new Label("Pack file ", skin)).padLeft(5);
        packFileTable.add(packField).padLeft(5).fillX().expandX();
        TextButton packFileBtn = new TextButton("Load", skin);
        packFileBtn.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                fileChooser.setIdentifier("packfile");
                fileChooser.open(stage);
            }
        });
        packFileTable.add(packFileBtn).padLeft(5).padRight(5);
        packFileTable.row();
        final TextField atlasField = new TextField("Select pack texture sheet", skin);
        packFileTable.add(new Label("Sheet file ", skin)).padLeft(5);
        packFileTable.add(atlasField).padLeft(5).fillX().expandX();
        TextButton atlasFileBtn = new TextButton("Load", skin);
        atlasFileBtn.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                fileChooser.setIdentifier("packatlas");
                fileChooser.open(stage);
            }
        });
        packFileTable.add(atlasFileBtn).padLeft(5).padRight(5);
        packFileTable.row();

        //SEPARATE FILES
        seperateFileTable.add(new Label("Load environment settings from several images /n" +
                "Select image files for each of the following environment layers", skin)).fillX().expandX().colspan(2);
        seperateFileTable.row();
        final TextField waterField = new TextField("Select water layer image file", skin);
        seperateFileTable.add(new Label("Water file ", skin)).padLeft(5);
        seperateFileTable.add(waterField).padLeft(5).fillX().expandX();
        TextButton waterFileBtn = new TextButton("Load", skin);
        waterFileBtn.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                fileChooser.setIdentifier("water");
                fileChooser.open(stage);
            }
        });
        seperateFileTable.add(waterFileBtn).padLeft(5).padRight(5);
        seperateFileTable.row();
        final TextField grassField = new TextField("Select grass layer image file", skin);
        seperateFileTable.add(new Label("Grass file ", skin)).padLeft(5);
        seperateFileTable.add(grassField).padLeft(5).fillX().expandX();
        TextButton grassFileBtn = new TextButton("Load", skin);
        grassFileBtn.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                fileChooser.setIdentifier("grass");
                fileChooser.open(stage);
            }
        });
        seperateFileTable.add(grassFileBtn).padLeft(5).padRight(5);
        seperateFileTable.row();
        final TextField terrainField = new TextField("Select terrain image file", skin);
        seperateFileTable.add(new Label("Terrain file ", skin)).padLeft(5);
        seperateFileTable.add(terrainField).padLeft(5).fillX().expandX();
        TextButton terrainFileBtn = new TextButton("Load", skin);
        grassFileBtn.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                fileChooser.setIdentifier("terrain");
                fileChooser.open(stage);
            }
        });
        seperateFileTable.add(terrainFileBtn).padLeft(5).padRight(5);
        seperateFileTable.row();

        fileChooser.addSelectionListener(new MenuSelectListener(){
            public void selectionMade(java.lang.Object menu, java.lang.Object object) {
                FileChooser chooser = (FileChooser) menu;
                File file;
                if(chooser.getIdentifier().equals("packfile")){
                    file = (File) object;
                    packFiles.put("packfile", file);
                    packField.setText(file.getPath());
                    return;
                }
                if(chooser.getIdentifier().equals("packatlas")){
                    file = (File) object;
                    packFiles.put("packatlas", file);
                    atlasField.setText(file.getPath());
                    return;
                }
                if(chooser.getIdentifier().equals("water")){
                    file = (File) object;
                    seperateFiles.put("water", file);
                    waterField.setText(file.getPath());
                    return;
                }
                if(chooser.getIdentifier().equals("grass")){
                    file = (File) object;
                    seperateFiles.put("grass", file);
                    waterField.setText(file.getPath());
                    return;
                }
                if(chooser.getIdentifier().equals("terrain")){
                    file = (File) object;
                    seperateFiles.put("terrain", file);
                    waterField.setText(file.getPath());
                    return;
                }
            }
        });

        check.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                showFileSelection(check.isChecked());
            }
        });
        check.setChecked(true);
        showFileSelection(true);

        content.row();
        content.add(variableTable).fill().expand();

        button("Load", "load");
        button("Cancel", "cancel");
        this.pack();
//        packFileTable
    }

    private void showFileSelection(boolean packfile){
        fromPackFile = packfile;
        variableTable.clear();
        if(packfile)
            variableTable.add(packFileTable).fill().expand();
        else
            variableTable.add(seperateFileTable).fill().expand();
        pack();
    }
    /**
     * Should be called upon dialog button selection
     */
    protected void result (Object object){
        String btn = (String)object;
        System.out.println("Dialog result " + btn);
        if(!btn.equalsIgnoreCase("load")) return;
        System.out.println("Dialog result " + btn);
        for(MenuSelectListener l : listeners){

                l.selectionMade(this, fromPackFile);
        }
    }

    public String getIdentifier(){
        return getTitle();
    }
    public boolean fromPackFile(){
        return fromPackFile;
    }
    public ObjectMap<String, File> getFiles(){
        return seperateFiles;
    }
    public ObjectMap<String, File> getPackFiles(){
        return packFiles;
    }
    public void open(Stage stage){
//		stage.addActor(this);
        this.show(stage);
//        this.setHeight(400);
//        this.setWidth(400);
        this.setPosition((stage.getWidth()/2)-(this.getWidth()/2), (stage.getHeight()/2)-(this.getHeight()/2));

    }

    @Override
    public void addSelectionListener(MenuSelectListener listener) {
        listeners.add(listener);
    }
}
