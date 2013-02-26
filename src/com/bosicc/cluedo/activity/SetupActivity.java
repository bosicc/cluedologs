/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bosicc.cluedo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bosicc.cluedo.CluedoApp;
import com.bosicc.cluedo.R;
import com.bosicc.cluedo.dialogs.PlayersNameDialog;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.pojo.GamePOJO.CardType;
import com.bosicc.cluedo.pojo.PlayerPOJO;
import com.bosicc.cluedo.utils.CConstants;
import com.bosicc.cluedo.utils.Utils;
import com.flurry.android.FlurryAgent;

public class SetupActivity extends ExpandableListActivity {

    // private static String TAG = "Setup";
    // private static final int DIALOG_INFO = 1;
    private static final int DIALOG_CARDSNOTSELECTED = 2;

    private CluedoApp cApp;
    private GamePOJO game;
    private Utils utils;
    private ExpandableListAdapter mAdapter;

    private ExpandableListView mList;
    private Spinner mGameType;
    private Spinner mPerson;
    private ArrayAdapter mPersonAdapter;
    private Button mBack;
    private Button mNext;
    private Button mPlayername;
    private ScrollView mScroll;
    private TextView mText4;

    private String[] groups = new String[3];
    private String[][] children = new String[3][];

    // All check box state
    private boolean[][] items = new boolean[4][13]; // NOTE: Maximum cards i saw
                                                    // = 13

    private int stage = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cApp = (CluedoApp) getApplication();
        game = cApp.getGame();
        utils = new Utils(this, game);

        setContentView(R.layout.setup);
        mNext = (Button) findViewById(R.id.btnNext);
        mBack = (Button) findViewById(R.id.btnBack);
        mPlayername = (Button) findViewById(R.id.btnPlayerName);
        mGameType = (Spinner) findViewById(R.id.spinnerGameType);
        mPerson = (Spinner) findViewById(R.id.spinnerYourPerson);
        mList = (ExpandableListView) findViewById(android.R.id.list);
        mScroll = (ScrollView) findViewById(R.id.scrollView);

        mText4 = (TextView) findViewById(R.id.text4);

        mPlayername.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PlayersNameDialog customizeDialog = new PlayersNameDialog(SetupActivity.this, game);
                customizeDialog.show();
            }
        });

        // Set list of game types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gameTypes,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGameType.setAdapter(adapter);
        mGameType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                // Set new data
                // Log.i(TAG,"pos="+arg2);
                utils.UpdateGameDataList(arg2);

                mPersonAdapter = new ArrayAdapter<String>(SetupActivity.this, android.R.layout.simple_spinner_item,
                        game.mPeople);
                mPersonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mPerson.setAdapter(mPersonAdapter);
                mPersonAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // Set list of persons
        mPersonAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, game.mPeople);
        mPersonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPerson.setAdapter(mPersonAdapter);
        mPerson.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                // Log.i(TAG,"pos="+pos);
                if (utils.getYourPlayer() != pos) {
                    // clear preveous name
                    utils.setYourPlayer(pos);
                    game.mPlayers.get(pos).setName("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DrawStage0();
                stage--;

            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (stage == 0) {
                    mScroll.setVisibility(View.GONE);
                    mList.setVisibility(View.VISIBLE);
                    mText4.setVisibility(View.VISIBLE);
                    mBack.setVisibility(View.VISIBLE);
                    // Set up our adapter
                    mAdapter = new CardsExpandableListAdapter();
                    mList.setAdapter(mAdapter);

                    mNext.setText(R.string.setup_txtbtnStart);
                    stage++;
                } else {
                    if (stage == 1) {

                        utils.setYourPlayer(mPerson.getSelectedItemPosition());

                        utils.setPlayerNoColumn(utils.getYourPlayer());
                        int TotalCards = 0;
                        for (int i = 0; i < game.mPeople.length; i++) {
                            if (items[0][i]) {
                                utils.setTypeinRowNoData(i, utils.getYourPlayer(), CardType.YES);
                                TotalCards++;
                            }
                        }
                        for (int i = 0; i < game.mPlace.length; i++) {
                            if (items[1][i]) {
                                utils.setTypeinRowNoData(game.mPeople.length + i, utils.getYourPlayer(), CardType.YES);
                                TotalCards++;
                            }
                        }
                        for (int i = 0; i < game.mWeapon.length; i++) {
                            if (items[2][i]) {
                                utils.setTypeinRowNoData(game.mPeople.length + game.mPlace.length + i,
                                        utils.getYourPlayer(), CardType.YES);
                                TotalCards++;
                            }
                        }

                        int playerNum = 0;
                        // Mark not Played person - thay cannot have card
                        for (PlayerPOJO item : game.mPlayers) {
                            if (!item.inGame()) {
                                utils.setColumnNoData(item.getNumber());
                            } else {
                                playerNum++;
                            }
                        }

                        // Save number of Players
                        utils.setNumberOfPlayers(playerNum);

                        // Update players label
                        utils.UpdatePlayerLabels();

                        if (TotalCards < 3) {
                            showDialog(DIALOG_CARDSNOTSELECTED);
                        } else {
                            utils.setCreatedGame(true);
                            startActivity(new Intent(SetupActivity.this, TabCluedoLogsActivity.class));
                            finish();
                        }

                    }
                }
            }
        });

        // showDialog(DIALOG_INFO);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.setReportLocation(false);
        FlurryAgent.onStartSession(this, CConstants.FLURRY_KEY);
        FlurryAgent.logEvent(CConstants.FLURRY_GAME_CREATE);
        // Log.i(TAG, "onStart()");

    }

    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        // Log.i(TAG, "onStop()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (stage == 1)) {
            stage--;
            DrawStage0();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        // TODO Auto-generated method stub

        items[groupPosition][childPosition] = !items[groupPosition][childPosition];
        ListItemCache cache = (ListItemCache) v.getTag();

        if (items[groupPosition][childPosition]) {
            cache.check.setVisibility(View.VISIBLE);
        } else {
            cache.check.setVisibility(View.INVISIBLE);
        }
        return super.onChildClick(parent, v, groupPosition, childPosition, id);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_CARDSNOTSELECTED:
                return new AlertDialog.Builder(SetupActivity.this).setTitle(R.string.logs_alert_title)
                        .setIcon(R.drawable.btn_info).setMessage(R.string.setup_dialog_notenoughtpcards_msg)
                        .setPositiveButton(R.string.alert_dialog_ok, null).create();
                // case DIALOG_INFO:
                // return new AlertDialog.Builder(Setup.this)
                // .setTitle(R.string.logs_alert_title)
                // .setIcon(R.drawable.btn_info)
                // .setMessage(R.string.setup_dialog_info_msg)
                // .setPositiveButton(R.string.alert_dialog_ok, null)
                // .create();
        }

        return null;
    }

    private static final class ListItemCache {

        public TextView Text;
        public ImageView check;

    }

    /**
     * A simple adapter which maintains an ArrayList of photo resource Ids. Each
     * photo is displayed as an image. This adapter supports clearing the list
     * of photos and adding a new photo.
     * 
     */
    public class CardsExpandableListAdapter extends BaseExpandableListAdapter {

        private LayoutInflater inflater;

        public CardsExpandableListAdapter() {
            // Example how to read String array from Resources
            Resources r = getResources();
            children[0] = game.mPeople;
            children[1] = game.mPlace;
            children[2] = game.mWeapon;
            groups = r.getStringArray(R.array.category);

            inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);

            TextView textView = new TextView(SetupActivity.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                ViewGroup parent) {

            TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());

            ListItemCache cache = new ListItemCache();
            View view = null;
            if (convertView != null) {
                view = convertView;
                cache = (ListItemCache) view.getTag();
            } else {
                view = inflater.inflate(R.layout.setup_child_row, parent, false);

                cache.Text = (TextView) view.findViewById(R.id.childname);
                cache.check = (ImageView) view.findViewById(R.id.check);

                view.setTag(cache);
            }

            final Object c = (Object) getChild(groupPosition, childPosition);

            cache.Text.setText(getChild(groupPosition, childPosition).toString());
            if (items[groupPosition][childPosition]) {
                cache.check.setVisibility(View.VISIBLE);
            } else {
                cache.check.setVisibility(View.INVISIBLE);
            }

            return view;

        }

        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        public int getGroupCount() {
            return groups.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View v = null;
            if (convertView != null)
                v = convertView;
            else
                v = inflater.inflate(R.layout.setup_group_row, parent, false);

            TextView item = (TextView) v.findViewById(R.id.groupname);
            item.setText(getGroup(groupPosition).toString());

            return v;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

        public void updateData() {
            children[0] = game.mPeople;
            children[1] = game.mPlace;
            children[2] = game.mWeapon;
        }

    }

    private void DrawStage0() {
        mList.setVisibility(View.GONE);
        mScroll.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.INVISIBLE);
        mNext.setText(R.string.setup_txtbtnNext);
        mText4.setVisibility(View.GONE);
    }

}
