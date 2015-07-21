package com.bosicc.cluedo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.bosicc.cluedo.R;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.flurry.android.FlurryAgent;

public class GameSave {
    private static String TAG = "GameSave";

    private static String CURENT_GAME_DIR_NAME = "save";
    private static String CURENT_GAME_FILE_NAME = "CluedoCurentGame.bin";

    private Context mContext;

    public GameSave(Context context) {
        mContext = context;
    }

    public void Save(GamePOJO game) {

        // Log.i(TAG,"Save.begin");
        // Log.i(TAG,"Filename="+CURENT_GAME_FILE_NAME);
        byte[] gameBytes = serializeObject(game);

        File path = new File(mContext.getFilesDir(), CURENT_GAME_DIR_NAME);

        File file = new File(path, CURENT_GAME_FILE_NAME);
        if (file.exists()) {
            // Log.i(TAG,"file exist! = "+file);
            if (!file.delete()) {
                Log.e(TAG, "cannot delete file !!!");
            }
        } else {
            // Log.i(TAG,"file="+file);
            // Log.i(TAG,"file.getParentFile()="+file.getParentFile());
            // Log.i(TAG,"mkdirs...");
            file.getParentFile().mkdirs();
        }

        try {
            FileOutputStream fout = new FileOutputStream(file);
            // Log.i(TAG,"Write data...");
            fout.write(gameBytes);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Log.i(TAG,"Save.end");
    }

    public GamePOJO Load() {
        byte[] gameBytes = new byte[10000000];
        GamePOJO game = null;

        File path = new File(mContext.getFilesDir(), CURENT_GAME_DIR_NAME);
        // Log.i(TAG,"Load() path="+path);
        File file = new File(path, CURENT_GAME_FILE_NAME);
        if (file.exists()) {
            // Log.i(TAG,"Load() File exist. Begin loading...");
        } else {
            // file.getParentFile().mkdirs();
            // Log.i(TAG,"Load() File not FOUND! ");
            // Log.i(TAG,"Load() file="+file);
            return game;
        }

        try {
            FileInputStream fin = new FileInputStream(file);

            fin.read(gameBytes);
            fin.close();
            // Log.i(TAG, "dataLen=" + gameBytes.length);
            game = (GamePOJO) deserializeObject(gameBytes);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Log.i(TAG,"Load.end");
        Map<String, String> params = new HashMap<String, String>();
        params.put("game.mPlayers.size()", "" + game.mPlayers.size());
        params.put("game.mLogsList.size()", "" + game.mLogsList.size());
        if (game.mPlayers.isEmpty()) {

            // Log.d(TAG,"game.mPlayers.isEmpty()");
            Utils utils = new Utils(mContext, game);
            Resources r = mContext.getResources();
            int[] colorlist = r.getIntArray(R.array.colors_default);
            utils.setDefaultPlayerNames(colorlist);
        }
        FlurryAgent.logEvent(CConstants.FLURRY_LOADGAME, params);
        return game;
    }

    public void DeleteCurentGame() {

        File path = new File(mContext.getFilesDir(), CURENT_GAME_DIR_NAME);
        File file = new File(path, CURENT_GAME_FILE_NAME);
        if (file.exists()) {
            if (!file.delete()) {
                Log.e(TAG, "cannot delete file !!!");
            }
        }
    }

    public static byte[] serializeObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();

            // Get the bytes of the serialized object
            byte[] buf = bos.toByteArray();

            return buf;
        } catch (IOException ioe) {
            Log.e("serializeObject", "error", ioe);

            return null;
        }
    }

    public static Object deserializeObject(byte[] b) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();
            return object;
        } catch (ClassNotFoundException cnfe) {

            Log.e("deserializeObject", "class not found error", cnfe);
            return null;
        } catch (IOException ioe) {
            Log.e("deserializeObject", "io error", ioe);

            return null;
        }
    }

}
