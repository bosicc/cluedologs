package com.bosicc.cluedo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bosicc.cluedo.CluedoApp;
import com.bosicc.cluedo.R;
import com.bosicc.cluedo.adapters.TabsAdapter;
import com.bosicc.cluedo.dialogs.PlayersNameDialog;
import com.bosicc.cluedo.fragments.LogsFragment;
import com.bosicc.cluedo.fragments.LogsTextFragment;
import com.bosicc.cluedo.fragments.TableFragment;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.utils.CConstants;
import com.bosicc.cluedo.utils.Utils;
import com.flurry.android.FlurryAgent;

public class TabCluedoLogsActivity extends SherlockFragmentActivity {

    // private static String TAG = "CluedoLogs";

    private TabsAdapter mTabsAdapter;
    public ActionBar mActionBar;

    private CluedoApp cApp;
    private GamePOJO game;
    private Utils utils;

    // ======================================================
    // Dialog items ID
    // ======================================================
    private static final int DIALOG_NEWGAME = 1;

    // ======================================================
    // Menu items ID
    // ======================================================
    public static final int MENU_ITEM_PEOPLE = 1;
    public static final int MENU_ITEM_NEW = 2;
    public static final int MENU_ITEM_HELP = 3;
    public static final int MENU_ITEM_LOGSTEXT = 4;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab);

        cApp = (CluedoApp) getApplication();
        game = cApp.getGame();
        utils = new Utils(this, game);

        final ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO);
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayShowHomeEnabled(false);
        
        mTabsAdapter = new TabsAdapter(this, (ViewPager)findViewById(R.id.pager));
        
        mTabsAdapter.addTab(bar.newTab().setText(R.string.maintab_table),
                TableFragment.class, null);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.maintab_logs),
                LogsFragment.class, null);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.maintab_textlogs),
                LogsTextFragment.class, null);
       
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.setReportLocation(false);
        FlurryAgent.onStartSession(this, CConstants.FLURRY_KEY);
        FlurryAgent.logEvent(CConstants.FLURRY_GAME_PLAY);
        // Log.i(TAG, "onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Log.i(TAG, "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Log.i(TAG, "onResume()");
    }

    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        // Log.i(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (game.isCreated) {
            cApp.getSaveUtils().Save(game);
        }
        // Log.i(TAG, "onDestroy()");
    }

    
    

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_NEWGAME:
                return new AlertDialog.Builder(TabCluedoLogsActivity.this).setTitle(R.string.tab_dialog_newgame_titile)
                        .setIcon(R.drawable.btn_info).setMessage(R.string.tab_dialog_newgame_msg)
                        .setPositiveButton(R.string.tab_dialog_yes, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Reset all states in game
                                utils.reset();
                                Resources r = getResources();
                                game.mPeople = r.getStringArray(R.array.people_ru);
                                game.mPlace = r.getStringArray(R.array.place_ru);
                                game.mWeapon = r.getStringArray(R.array.weapon_ru);
                                // Delete current game from disk;
                                cApp.getSaveUtils().DeleteCurentGame();
                                sendBroadcast(new Intent(CConstants.ACTION_UPDATE_DATA));
                                startActivity(new Intent(TabCluedoLogsActivity.this, CluedologsActivity.class));
                                finish();

                            }
                        }).setNegativeButton(R.string.tab_dialog_no, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();

        }
        return null;
    }

    // // ==============================================================================
    // // Option Menu
    // // ==============================================================================
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
        //boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;
        
        MenuInflater inflater = this.getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

//        menu.add(R.string.mainmenu_players)
//            .setIcon(android.R.drawable.ic_menu_myplaces)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//
//        menu.add(R.string.mainmenu_new)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//        //item_2.setIcon();
//
//        menu.add(R.string.mainmenu_about)
//            .setIcon(android.R.drawable.ic_menu_info_details)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//
//        return super.onCreateOptionsMenu(menu);
    }
    // /**
    // * On options menu creation.
    // */

    
     // // ===
     // MenuItem item_4 = menu.add(group1Id, MENU_ITEM_LOGSTEXT,
     // Menu.FIRST+3, R.string.maintab_menu_logstext);
     // item_4.setIcon(R.drawable.tab_log2_icon);
    
     
    // }

    // /**
    // * On options menu item selection.
    // */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_players_names: {
                PlayersNameDialog customizeDialog = new PlayersNameDialog(this, game);
                customizeDialog.show();
                return true;
            }

            case R.id.menu_new_game: {
                showDialog(DIALOG_NEWGAME);
                return true;
            }

            case R.id.menu_about: {
                startActivity(new Intent(TabCluedoLogsActivity.this, AboutActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

   
}