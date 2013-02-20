package com.bosicc.cluedo;

import android.app.Application;
import android.content.ContentResolver;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;

import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.utils.GameSave;


/**
 * Android application's main class
 * 
 * @author bosicc
 * 
 */
public class CluedoApp extends Application {
	
    @Override
	public void onLowMemory() {
		//Log.i(TAG, "onLowMemory()");
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		//Log.i(TAG, "onTerminate()");
		super.onTerminate();
	}

	//private static final String TAG = "[bosicc]CluedoApp";

	private static ContentResolver resolver;

    private boolean isScreenActive = true;

	private GamePOJO game;
	private GameSave SaveUtils;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//Log.i(TAG,"onCreate()");
		resolver = getContentResolver();
		
		game = new GamePOJO();
		//Load default cards
		Resources r = getResources();
		game.mPeople = r.getStringArray(R.array.people_ru);
		game.mPlace = r.getStringArray(R.array.place_ru);
		game.mWeapon = r.getStringArray(R.array.weapon_ru);
		
		SaveUtils = new GameSave(getBaseContext());
		
		int ver = -1;
	    try {
	        ver = getPackageManager().getPackageInfo(getApplicationInfo().packageName, 0).versionCode;
	    } catch (NameNotFoundException e) {
	    }
		
		GamePOJO mLoadGame = SaveUtils.Load();
		
		if (mLoadGame != null){
			if (ver == mLoadGame.getVersion()){
				game = mLoadGame;
			}
		}
		//Set current version
		game.setVersion(ver);

	}
	
	public GamePOJO getGame(){
		return this.game;
	}
	
	public GameSave getSaveUtils(){
		return this.SaveUtils;
	}
	
}
