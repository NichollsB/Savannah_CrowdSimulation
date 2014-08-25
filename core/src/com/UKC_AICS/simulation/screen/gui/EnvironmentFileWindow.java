package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.screen.controlutils.MenuSelectEvent;
import com.UKC_AICS.simulation.screen.controlutils.MenuSelectListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.File;

/**
 * {@link com.badlogic.gdx.scenes.scene2d.ui.Dialog dialog} for the loading of environment file pack files.
 * Environment files must consist of a texture sheet image file, and a pack .txt document indicating the
 * regions of the image file, as demonstrated in the default pack file "\core\assets\data\Maps\EnvSettings.txt" and
 * "\core\assets\data\Maps\EnvSettings.png", in order to load. Files selected in this dialog should be passed
 * to {@link com.UKC_AICS.simulation.utils.EnvironmentLoader} to load the environment
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public class EnvironmentFileWindow extends Dialog implements MenuSelectEvent {

    private Table packFileTable = new Table();
    private Table separateFileTable = new Table();
    private Table variableTable = new Table();
    private boolean fromPackFile;
    private final ObjectMap<String, File> packFiles = new ObjectMap<String, File>();
    private final ObjectMap<String, File> separateFiles = new ObjectMap<String, File>();

    private FileChooser fileChooser;

    private Skin skin;
    private Stage stage;

    private final Array<MenuSelectListener> listeners = new Array<MenuSelectListener>();

    /**
     * Constructor, passes in the title and skin to apply to the dialog, and the
     * {@link com.badlogic.gdx.scenes.scene2d.Stage stage} the dialog is added to
     * @param title
     * @param skin
     * @param stage
     */
    public EnvironmentFileWindow(String title, Skin skin, Stage stage) {
        super(title, skin);
        this.skin = skin;
        this.stage=stage;
        create();
    }

    /**
     * Create the dialog layout. Initialises the file choosers, and adds textFields for displaying the selected
     * files. Adds the confirm/cancel buttons.
     */
    private void create(){
        fileChooser = new FileChooser("Load", skin, "Load", "packfile", "../", "Confirm", "Cancel", stage);


        Table content = this.getContentTable();
//        content.add(new Label("Select environment files", skin)).fillX().expandX();
//        final CheckBox check = new CheckBox("Load from packfile", skin);
//        content.row();
//        content.add(check).fillX().expandX();

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

//        //SEPARATE FILES
//        separateFileTable.add(new Label("Load environment settings from several images /n" +
//                "Select image files for each of the following environment layers", skin)).fillX().expandX().colspan(2);
//        separateFileTable.row();
//        final TextField waterField = new TextField("Select water layer image file", skin);
//        separateFileTable.add(new Label("Water file ", skin)).padLeft(5);
//        separateFileTable.add(waterField).padLeft(5).fillX().expandX();
//        TextButton waterFileBtn = new TextButton("Load", skin);
//        waterFileBtn.addListener(new ClickListener(){
//            public void clicked(InputEvent event, float x, float y) {
//                fileChooser.setIdentifier("water");
//                fileChooser.open(stage);
//            }
//        });
//        separateFileTable.add(waterFileBtn).padLeft(5).padRight(5);
//        separateFileTable.row();
//
//        final TextField grassField = new TextField("Select grass layer image file", skin);
//        separateFileTable.add(new Label("Grass file ", skin)).padLeft(5);
//        separateFileTable.add(grassField).padLeft(5).fillX().expandX();
//        TextButton grassFileBtn = new TextButton("Load", skin);
//        grassFileBtn.addListener(new ClickListener(){
//            public void clicked(InputEvent event, float x, float y) {
//                fileChooser.setIdentifier("grass");
//                fileChooser.open(stage);
//            }
//        });
//        separateFileTable.add(grassFileBtn).padLeft(5).padRight(5);
//        separateFileTable.row();
//
//        final TextField terrainField = new TextField("Select terrain image file", skin);
//        separateFileTable.add(new Label("Terrain file ", skin)).padLeft(5);
//        separateFileTable.add(terrainField).padLeft(5).fillX().expandX();
//        TextButton terrainFileBtn = new TextButton("Load", skin);
//        terrainFileBtn.addListener(new ClickListener(){
//            public void clicked(InputEvent event, float x, float y) {
//                fileChooser.setIdentifier("terrain");
//                fileChooser.open(stage);
//            }
//        });
//        separateFileTable.add(terrainFileBtn).padLeft(5).padRight(5);
//        separateFileTable.row();

        fileChooser.addSelectionListener(new MenuSelectListener(){
            public void selectionMade(java.lang.Object menu, java.lang.Object object) {
                FileChooser chooser = (FileChooser) menu;
                File file;
                file = (File) object;
                try {
                    System.out.println(file.getPath());
                    if (chooser.getIdentifier().equals("packfile")) {
                        packFiles.put("packfile", file);
                        packField.setText(file.getPath());
                        return;
                    }
                    if (chooser.getIdentifier().equals("packatlas")) {
                        packFiles.put("packatlas", file);
                        atlasField.setText(file.getPath());
                        return;
                    }
//                    if (chooser.getIdentifier().equals("water")) {
//                        separateFiles.put("water", file);
//                        waterField.setText(file.getPath());
//                        return;
//                    }
//                    if (chooser.getIdentifier().equals("grass")) {
//                        separateFiles.put("grass", file);
//                        grassField.setText(file.getPath());
//                        return;
//                    }
//                    if (chooser.getIdentifier().equals("terrain")) {
//                        separateFiles.put("terrain", file);
//                        terrainField.setText(file.getPath());
//                        return;
//                    }
                }
                catch(NullPointerException e){

                }
            }
        });

//        check.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                showFileSelection(check.isChecked());
//            }
//        });
//        check.setChecked(true);
        showFileSelection(true);

        content.row();
        content.add(variableTable).fill().expand();

        button("Load", "load");
        button("Cancel", "cancel");
        this.pack();
//        packFileTable
    }

    /**
     * Adds the file selection table to the dialog
     * @param packFile
     */
    private void showFileSelection(boolean packFile){
        fromPackFile = packFile;
        variableTable.clear();
        if(packFile)
            variableTable.add(packFileTable).fill().expand();
        else
            variableTable.add(separateFileTable).fill().expand();
        pack();
    }
    /**
     * Called on selecting one of the dialogs buttons (confirm, or cancel). If the
     * button selected is the "load" button then it will notify listeners implementing {@link com.UKC_AICS.simulation.screen.controlutils.MenuSelectListener}
     */
    protected void result (Object object){
        String btn = (String)object;
//        System.out.println("Dialog result " + btn);
        if(!btn.equalsIgnoreCase("load")) return;
//        System.out.println("Dialog result " + btn);
        for(MenuSelectListener l : listeners){

                l.selectionMade(this, fromPackFile);
        }
    }

    /**
     * Retrieve the dialog identifier
     * @return {@link #getTitle()}
     */
    public String getIdentifier(){
        return getTitle();
    }

    /**
     * If the dialog is loading from a pack file.
     * @return True if loading from a pack file
     */
    public boolean fromPackFile(){
        return fromPackFile;
    }

    public ObjectMap<String, File> getFiles(){
        return separateFiles;
    }

    /**
     * Retrieve the pack files selected
     * @return Map of pack files. Should consist of a File to a texture sheet image, and one to
     * a .txt document indicating the regions of the texture sheet
     */
    public ObjectMap<String, File> getPackFiles(){
        return packFiles;
    }

    /**
     * Open the dialog window.
     * @param stage Stage to show the dialog in
     */
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
