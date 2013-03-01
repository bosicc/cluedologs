package com.bosicc.cluedo.fragments;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.bosicc.cluedo.CluedoApp;
import com.bosicc.cluedo.R;
import com.bosicc.cluedo.activity.TabCluedoLogsActivity;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.pojo.GamePOJO.ShowModeType;
import com.bosicc.cluedo.pojo.PlayerPOJO;
import com.bosicc.cluedo.utils.CConstants;
import com.bosicc.cluedo.utils.Utils;

/**
 * A list view example where the data comes from a custom ListAdapter
 */
public class LogsTextFragment extends SherlockListFragment {

    private static String TAG = "LogsText";

    private LinearLayout mHeaderBox;
    private TextView mTitle;
    private TextView mSlyx;

    private ListView mList;
    private MyLogsTextListAdapter mAdapter;
    private CluedoApp cApp;
    // private GamePOJO game;
    private GamePOJO gameLocal;
    private Utils utils;
    private ArrayList<PlayerPOJO> mCurentDialogList;

    private LogsTextDataChangeReceiver mLogsTextDataChangeReceiver = null;

    private ShowModeType mViewMode = ShowModeType.ALL;
    private boolean isActive = false;
    private int mPerson = 0;
    private int nc = 100;

    private static final int DIALOG_SORT_BY_XODIL = 6;
    private static final int DIALOG_SORT_BY_PODTVERDIL = 7;
    private static final int DIALOG_SORT_BY_PEOPLE = 8;
    private static final int DIALOG_SORT_BY_PLACE = 9;
    private static final int DIALOG_SORT_BY_WEAPON = 10;

    // ======================================================
    // Menu items ID
    // ======================================================
    private static final int MENU_ITEM_SORT_BY_XODIL = 11;
    private static final int MENU_ITEM_SORT_ALL = 12;
    private static final int MENU_ITEM_SORT_BY_PODTVERDIL = 13;
    private static final int MENU_ITEM_SORT_BY_PEOPLE = 14;
    private static final int MENU_ITEM_SORT_BY_PLACE = 15;
    private static final int MENU_ITEM_SORT_BY_WEAPON = 16;

    private static final int group3Id = 3;

    private static final int sortXodilBtnId = Menu.FIRST + 2;
    private static final int sortAllBtnId = sortXodilBtnId + 1;
    private static final int sortPodtverdilBtnId = sortAllBtnId + 1;
    private static final int sortPeopleBtnId = sortPodtverdilBtnId + 1;
    private static final int sortPlacelBtnId = sortPeopleBtnId + 1;
    private static final int sortWeaponBtnId = sortPlacelBtnId + 1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null){
            return null;
        }
        return inflater.inflate(R.layout.logs, null);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      

      mList = (ListView) getView().findViewById(android.R.id.list);
      mSlyx = (TextView) getView().findViewById(R.id.txtSlyx);
      mHeaderBox = (LinearLayout) getView().findViewById(R.id.LLheader);

      cApp = (CluedoApp) getActivity().getApplication();
      gameLocal = cApp.getGame();
      utils = new Utils(getActivity(), gameLocal);

      mHeaderBox.setVisibility(View.GONE);
      mSlyx.setVisibility(View.GONE);

      mCurentDialogList = new ArrayList<PlayerPOJO>();

      // Register Data change receiver
      mLogsTextDataChangeReceiver = new LogsTextDataChangeReceiver();
      IntentFilter intentFilter = new IntentFilter(CConstants.ACTION_UPDATE_DATA);
      getActivity().registerReceiver(mLogsTextDataChangeReceiver, intentFilter);

      // Set up our adapter
      mAdapter = new MyLogsTextListAdapter(getActivity());
      this.setListAdapter(mAdapter);
    }
    

    @Override
    public void onResume() {
        // Log.i(TAG,"onResume");
        // gameLocal = game;
        // mAdapter.notifyDataSetChanged();
        // mList.setAdapter(mAdapter);
        isActive = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        // /Log.i(TAG,"onPause");
        // mList.setAdapter(null);
        isActive = false;
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLogsTextDataChangeReceiver != null) {
            getActivity().unregisterReceiver(mLogsTextDataChangeReceiver);
            mLogsTextDataChangeReceiver = null;
        }
    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DIALOG_SORT_BY_XODIL:
//                mCurentDialogList = utils.getSortXodilList();
//                return new AlertDialog.Builder(LogsTextFragment.this).setTitle(R.string.logs_alert_title_sort_xodil)
//                        .setItems(utils.getString(mCurentDialogList), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                mViewMode = ShowModeType.XODIT;
//                                mPerson = mCurentDialogList.get(which).getNumber();
//                                ;
//                                mAdapter.notifyDataSetChanged();
//                                mCurentDialogList.removeAll(mCurentDialogList);
//                            }
//                        }).create();
//            case DIALOG_SORT_BY_PODTVERDIL:
//                mCurentDialogList = utils.getSortPodtverdilList();
//                return new AlertDialog.Builder(LogsTextFragment.this)
//                        .setTitle(R.string.logs_alert_title_sort_podtverdil)
//                        .setItems(utils.getString(mCurentDialogList), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (which == 0) {
//                                    which = nc;
//                                } else {
//                                    which = mCurentDialogList.get(which).getNumber();
//                                }
//                                mPerson = which;
//                                mViewMode = ShowModeType.PODTVERDIL;
//                                mAdapter.notifyDataSetChanged();
//                                mCurentDialogList.removeAll(mCurentDialogList);
//                            }
//                        }).create();
//
//            case DIALOG_SORT_BY_PEOPLE:
//                return new AlertDialog.Builder(LogsTextFragment.this).setTitle(R.string.title_people)
//                        .setItems(gameLocal.mPeople, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                mViewMode = ShowModeType.PEOPLE;
//                                mPerson = which;
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        }).create();
//            case DIALOG_SORT_BY_PLACE:
//                return new AlertDialog.Builder(LogsTextFragment.this).setTitle(R.string.title_place)
//                        .setItems(gameLocal.mPlace, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                mViewMode = ShowModeType.PLACE;
//                                mPerson = which;
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        }).create();
//            case DIALOG_SORT_BY_WEAPON:
//                return new AlertDialog.Builder(LogsTextFragment.this).setTitle(R.string.title_weapon)
//                        .setItems(gameLocal.mWeapon, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                mViewMode = ShowModeType.WEAPON;
//                                mPerson = which;
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        }).create();
//
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPrepareDialog(int id, Dialog dialog) {
//
//        AlertDialog alertDialog = (AlertDialog) dialog;
//        ArrayAdapter<CharSequence> adapter;
//        switch (id) {
//
//            case DIALOG_SORT_BY_XODIL:
//                mCurentDialogList = utils.getSortXodilList();
//                adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.select_dialog_item, android.R.id.text1,
//                        utils.getString(mCurentDialogList));
//                alertDialog.getListView().setAdapter(adapter);
//                break;
//
//            case DIALOG_SORT_BY_PODTVERDIL:
//                mCurentDialogList = utils.getSortPodtverdilList();
//                adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.select_dialog_item, android.R.id.text1,
//                        utils.getString(mCurentDialogList));
//                alertDialog.getListView().setAdapter(adapter);
//                break;
//            default:
//                super.onPrepareDialog(id, dialog);
//        }
//    }

//    // ==============================================================================
//    // Option Menu
//    // ==============================================================================
//    /**
//     * On options menu creation.
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        Log.i(TAG,"onCreateOptionsMenu(): menu size=" + menu.size());
//        // ===
//        MenuItem item_1 = menu.add(group3Id, MENU_ITEM_SORT_BY_XODIL, sortXodilBtnId, R.string.logsmenu_sort_xodil);
//        // item_1.setIcon(android.R.drawable.ic_menu_sort_alphabetically);
//
//        // ===
//        MenuItem item_2 = menu.add(group3Id, MENU_ITEM_SORT_ALL, sortAllBtnId, R.string.logsmenu_sort_all);
//        item_2.setIcon(android.R.drawable.ic_menu_edit);
//
//        // ===
//        MenuItem item_3 = menu.add(group3Id, MENU_ITEM_SORT_BY_PODTVERDIL, sortPodtverdilBtnId,
//                R.string.logsmenu_sort_podtverdil);
//        // item_3.setIcon(android.R.drawable.ic_menu_sort_by_size);
//
//        // ===
//        MenuItem item_4 = menu.add(group3Id, MENU_ITEM_SORT_BY_PEOPLE, sortPeopleBtnId, R.string.title_people);
//        item_4.setIcon(R.drawable.ic_menu_people);
//
//        // ===
//        MenuItem item_5 = menu.add(group3Id, MENU_ITEM_SORT_BY_PLACE, sortPlacelBtnId, R.string.title_place);
//        item_5.setIcon(R.drawable.ic_menu_place);
//
//        // ===
//        MenuItem item_6 = menu.add(group3Id, MENU_ITEM_SORT_BY_WEAPON, sortWeaponBtnId, R.string.title_weapon);
//        item_6.setIcon(R.drawable.ic_menu_weapon);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        menu.removeItem(TabCluedoLogsActivity.MENU_ITEM_PEOPLE);
//        menu.removeItem(TabCluedoLogsActivity.MENU_ITEM_NEW);
//        menu.removeItem(TabCluedoLogsActivity.MENU_ITEM_HELP);
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    /**
//     * On options menu item selection.
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Log.i(TAG,"onCreateOptionsMenu(): item group id=" + item.getGroupId());
//        switch (item.getItemId()) {
//
//            case MENU_ITEM_SORT_BY_XODIL: {
//                showDialog(DIALOG_SORT_BY_XODIL);
//                return true;
//            }
//
//            case MENU_ITEM_SORT_ALL: {
//                mViewMode = ShowModeType.ALL;
//                mAdapter.notifyDataSetChanged();
//                return true;
//            }
//
//            case MENU_ITEM_SORT_BY_PODTVERDIL: {
//                showDialog(DIALOG_SORT_BY_PODTVERDIL);
//                return true;
//            }
//
//            case MENU_ITEM_SORT_BY_PEOPLE: {
//                showDialog(DIALOG_SORT_BY_PEOPLE);
//                return true;
//            }
//
//            case MENU_ITEM_SORT_BY_PLACE: {
//                showDialog(DIALOG_SORT_BY_PLACE);
//                return true;
//            }
//
//            case MENU_ITEM_SORT_BY_WEAPON: {
//                showDialog(DIALOG_SORT_BY_WEAPON);
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * Item view cache holder.
     */
    private static final class ListItemCache {

        public TextView TextXodil;
        public TextView TextPodtverdil;

        public TextView txt1;
        public TextView txt2;
        public TextView txt3;

    }

    private class MyLogsTextListAdapter extends BaseAdapter {

        private Context mContext;

        public MyLogsTextListAdapter(Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return utils.getCurentList(mViewMode, mPerson).size();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        /**
         * On get view
         */
        @Override
        public View getView(int position, View view, ViewGroup parent) {

            ListItemCache cache = new ListItemCache();
            if (view == null) {
                view = (View) LayoutInflater.from(mContext).inflate(R.layout.logs_row_text, parent, false);

                cache.TextXodil = (TextView) view.findViewById(R.id.txtLeft);
                cache.TextPodtverdil = (TextView) view.findViewById(R.id.txtRight);
                cache.txt1 = (TextView) view.findViewById(R.id.txt1);
                cache.txt2 = (TextView) view.findViewById(R.id.txt2);
                cache.txt3 = (TextView) view.findViewById(R.id.txt3);

                view.setTag(cache);

            } else {
                cache = (ListItemCache) view.getTag();
            }

            cache.TextXodil.setText(" ");
            int num = utils.getCurentList(mViewMode, mPerson).get(position).getPlayerXodit();
            if (num != -1) {
                cache.TextXodil.setBackgroundColor(gameLocal.mPlayers.get(num).getColor());
            }
            num = utils.getCurentList(mViewMode, mPerson).get(position).getPlayerPodtverdil();
            cache.TextPodtverdil.setBackgroundResource(R.color.bgMain);
            if (num != -1) {
                if (num == 100) {
                    cache.TextPodtverdil.setBackgroundResource(R.color.bgBlack);
                } else {
                    cache.TextPodtverdil.setBackgroundColor(gameLocal.mPlayers.get(num).getColor());
                }
            }

            int[] slux = utils.getCurentList(mViewMode, mPerson).get(position).getSlyx();
            // Log.i(TAG,"slyx:"+slux[0]+slux[1]+slux[2]);

            if (slux[0] != -1) {
                cache.txt1.setText(gameLocal.mPeople[slux[0]]);
                cache.txt1.setTextColor(gameLocal.mPlayers.get(slux[0]).getColor());
            } else {
                cache.txt1.setText("");
                cache.txt1.setTextColor(Color.BLACK);
            }

            if (slux[1] != -1) {
                cache.txt2.setText(gameLocal.mPlace[slux[1]]);
            } else {
                cache.txt2.setText("");
            }

            if (slux[2] != -1) {
                cache.txt3.setText(gameLocal.mWeapon[slux[2]]);
            } else {
                cache.txt3.setText("");
            }

            return view;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            if (isActive) {
                String text = "";

                switch (mViewMode) {
                    case ALL:
                        text = mContext.getText(R.string.logsmenu_sort_all).toString();
                        break;
                    case XODIT:
                        text = mContext.getText(R.string.logs_toast_xoda) + " "
                                + gameLocal.mPlayers.get(mPerson).getLabel();
                        break;
                    case PODTVERDIL:
                        if (mPerson == nc) {
                            text = mContext.getText(R.string.logs_toast_podtverdil) + " "
                                    + mContext.getText(R.string.logs_notconfirm);
                        } else {
                            text = mContext.getText(R.string.logs_toast_podtverdil) + " "
                                    + gameLocal.mPlayers.get(mPerson).getLabel();
                        }
                        break;
                    case PEOPLE:
                        text = mContext.getText(R.string.title_people) + ": " + gameLocal.mPeople[mPerson];
                        break;
                    case PLACE:
                        text = mContext.getText(R.string.title_place) + ": " + gameLocal.mPlace[mPerson];
                        break;
                    case WEAPON:
                        text = mContext.getText(R.string.title_weapon) + ": " + gameLocal.mWeapon[mPerson];
                        break;
                }
                Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
            }
        }
    }

    public class LogsTextDataChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter.notifyDataSetChanged();
        }
    }

}