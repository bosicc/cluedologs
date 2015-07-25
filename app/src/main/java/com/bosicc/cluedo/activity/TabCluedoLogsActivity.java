package com.bosicc.cluedo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.bosicc.cluedo.CluedoApp;
import com.bosicc.cluedo.R;
import com.bosicc.cluedo.dialogs.PlayersNameDialog;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.utils.CConstants;
import com.bosicc.cluedo.utils.Utils;
import com.flurry.android.FlurryAgent;

public class TabCluedoLogsActivity extends TabActivity {

    // private static String TAG = "CluedoLogs";

    private TabHost tabHost;
    private CluedoApp cApp;
    private GamePOJO game;
    private Utils utils;

    public static final String TAB_TABLE = "Table";
    public static final String TAB_LOGS = "Logs";
    public static final String TAB_LOGSTEXT = "LogsText";

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

    private static final int group1Id = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab);

        cApp = (CluedoApp) getApplication();
        game = cApp.getGame();
        utils = new Utils(this, game);

        tabHost = getTabHost();
        // tabHost.setOnTabChangedListener(this);

        setTabs(0);

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

    private void setTabs(int activeTab) {
        setupTableTab(); // tab_1
        setupLogsTab(); // tab_2
        setupLogsTextTab(); // tab_2

        tabHost.setCurrentTab(activeTab);
    }

    private void setupTableTab() {
        tabHost.addTab(tabHost
                .newTabSpec(TAB_TABLE)
                .setIndicator(getResources().getString(R.string.maintab_table),
                        getResources().getDrawable(R.drawable.tab_table_icon))
                .setContent(new Intent(this, TableActivity.class)));
    }

    private void setupLogsTab() {
        tabHost.addTab(tabHost
                .newTabSpec(TAB_LOGS)
                .setIndicator(getResources().getString(R.string.maintab_logs),
                        getResources().getDrawable(R.drawable.tab_log2_icon))
                .setContent(new Intent(this, LogsActivity.class)));
    }

    private void setupLogsTextTab() {
        tabHost.addTab(tabHost
                .newTabSpec(TAB_LOGSTEXT)
                .setIndicator(getResources().getString(R.string.maintab_textlogs),
                        getResources().getDrawable(R.drawable.tab_log_icon))
                .setContent(new Intent(this, LogsTextActivity.class)));
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

    // ==============================================================================
    // Option Menu
    // ==============================================================================
    /**
     * On options menu creation.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // ===
        MenuItem item_new_contact = menu.add(group1Id, MENU_ITEM_PEOPLE, Menu.FIRST + 1, R.string.mainmenu_players);
        item_new_contact.setIcon(android.R.drawable.ic_menu_myplaces);

        // ===
        MenuItem item_2 = menu.add(group1Id, MENU_ITEM_NEW, Menu.FIRST + 1, R.string.mainmenu_new);
        item_2.setIcon(android.R.drawable.ic_menu_agenda);

        // ===
        MenuItem item_3 = menu.add(group1Id, MENU_ITEM_HELP, Menu.FIRST + 2, R.string.mainmenu_about);
        item_3.setIcon(android.R.drawable.ic_menu_info_details);

        // // ===
        // MenuItem item_4 = menu.add(group1Id, MENU_ITEM_LOGSTEXT,
        // Menu.FIRST+3, R.string.maintab_menu_logstext);
        // item_4.setIcon(R.drawable.tab_log2_icon);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * On options menu item selection.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case MENU_ITEM_PEOPLE: {
                PlayersNameDialog customizeDialog = new PlayersNameDialog(this, game);
                customizeDialog.show();
                return true;
            }

            case MENU_ITEM_NEW: {
                showDialog(DIALOG_NEWGAME);
                return true;
            }

            case MENU_ITEM_HELP: {
                startActivity(new Intent(TabCluedoLogsActivity.this, AboutActivity.class));
                return true;
            }

            case MENU_ITEM_LOGSTEXT: {
                startActivity(new Intent(TabCluedoLogsActivity.this, LogsTextActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}