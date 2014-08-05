package com.UKC_AICS.simulation;

import com.UKC_AICS.simulation.entity.ObjectData;
import com.UKC_AICS.simulation.entity.Species;

import java.util.HashMap;

/**
 * Created by Emily on 07/07/2014.
 */
public class Constants {



    public static HashMap<Byte, Species> speciesData = new HashMap<Byte, Species>();

    public static HashMap<Byte,HashMap<Byte, ObjectData>> objectData = new HashMap<Byte, HashMap<Byte, ObjectData>>(); //need byte + subtype access?

    public static int screenWidth = 1280;
    public static int screenHeight = 720;

    public static int mapWidth = 1280;
    public static int mapHeight= 720;

    public static final int TILE_SIZE = 16;




}
