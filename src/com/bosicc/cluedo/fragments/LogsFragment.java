package com.bosicc.cluedo.fragments;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bosicc.cluedo.CluedoApp;
import com.bosicc.cluedo.R;
import com.bosicc.cluedo.dialogs.LogsDialogFragment;
import com.bosicc.cluedo.dialogs.TableDialogFragment;
import com.bosicc.cluedo.dialogs.TableDialogFragment.TableDialogFragmentListener;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.pojo.GamePOJO.ShowModeType;
import com.bosicc.cluedo.pojo.PMovePOJO;
import com.bosicc.cluedo.pojo.PlayerPOJO;
import com.bosicc.cluedo.utils.CConstants;
import com.bosicc.cluedo.utils.Utils;

/**
 * A list view example where the data comes from a custom ListAdapter
 */
public class LogsFragment extends SherlockListFragment {

     private static String TAG = "LogsFragment";

    private LinearLayout mHeaderBox;
    private Button mBtnXodit;
    private Button mBtnPodtverdil;
    private TextView mTitle;
    private TextView mSlyx;

    private ListView mList;
    private MyLogsListAdapter mAdapter;
    private CluedoApp cApp;
    private GamePOJO game;
    private Utils utils;
    private boolean isFinished = false;
    private ArrayList<PlayerPOJO> mCurentDialogList;
    private int nc = 100;
    private float scale;

    private ShowModeType mViewMode = ShowModeType.ALL;
    private int mPerson = 0;

//    private static final int DIALOG_XODIT = 1;
//    private static final int DIALOG_PODTVERDIL = 2;
//    private static final int DIALOG_PEOPLE = 3;
//    private static final int DIALOG_PLACE = 4;
//    private static final int DIALOG_WEAPON = 5;
//    private static final int DIALOG_SORT_BY_XODIL = 6;
//    private static final int DIALOG_SORT_BY_PODTVERDIL = 7;
//    private static final int DIALOG_XODIT_EDIT = 8;
    

    // ======================================================
    // Menu items ID
    // ======================================================
    private static final int MENU_ITEM_SORT = 11;
    private static final int MENU_ITEM_SORT_ALL = 12;
    private static final int MENU_ITEM_SORT_BY_XODIL = 13;
    private static final int MENU_ITEM_SORT_BY_PODTVERDIL = 14;
    private static final int MENU_ITEM_LOGSTEXT = 15;

    private static final int group2Id = 2;

    private static final int sortBtnId = Menu.FIRST + 4;

    // private static final int logstextBtnId = Menu.FIRST+3;
    
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
      mBtnXodit = (Button) getView().findViewById(R.id.btnXodit);
      mBtnPodtverdil = (Button) getView().findViewById(R.id.btnPodtverdil);
      mTitle = (TextView) getView().findViewById(R.id.txtTitle);
      mSlyx = (TextView) getView().findViewById(R.id.txtSlyx);
      mHeaderBox = (LinearLayout) getView().findViewById(R.id.LLheader);

      cApp = (CluedoApp) getActivity().getApplication();
      game = cApp.getGame();
      utils = new Utils(getActivity(), game);
      scale = getResources().getDisplayMetrics().density;

      mCurentDialogList = new ArrayList<PlayerPOJO>();

//      mBtnXodit.setOnClickListener(new OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              if (mViewMode == ShowModeType.ALL) {
//                  if ((utils.getAllList().size() != 0) && (utils.getAllList().get(0).getPlayerPodtverdil() == -1)) {
//                      new AlertDialog.Builder(LogsFragment.this).setIcon(R.drawable.btn_info)
//                              .setTitle(R.string.logs_alert_title).setMessage(R.string.logs_txt2)
//                              .setPositiveButton(R.string.alert_dialog_ok, null).show();
//                  } else {
//                      showDialog(DIALOG_XODIT);
//                  }
//              }
//          }
//      });
//
//      mBtnPodtverdil.setOnClickListener(new OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              if (mViewMode == ShowModeType.ALL) {
//                  if (utils.getAllList().size() == 0) {
//                      new AlertDialog.Builder(LogsFragment.this).setIcon(R.drawable.btn_info)
//                              .setTitle(R.string.logs_alert_title).setMessage(R.string.logs_txt3)
//                              .setPositiveButton(R.string.alert_dialog_ok, null).show();
//                  } else {
//                      showDialog(DIALOG_PODTVERDIL);
//                  }
//              }
//          }
//      });

      // Set up our adapter
      mAdapter = new MyLogsListAdapter(getActivity());
      this.setListAdapter(mAdapter);
      
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        // Log.i(TAG,"onResume");
        // mList.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onPause() {
        // Log.i(TAG,"onPause");
        // mList.setVisibility(View.GONE);
        super.onResume();
    }



    // ==============================================================================
    // Option Menu
    // ==============================================================================
    /**
     * On options menu creation.
     */
    @Override
    public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, MenuInflater inflater) {

        Log.i(TAG, "onCreateOptionsMenu() size=" + menu.size());
        // ===

//        SubMenu sortMenu = menu.addSubMenu(group2Id, MENU_ITEM_SORT, sortBtnId, R.string.logsmenu_sort);
//        sortMenu.add(MENU_ITEM_SORT, MENU_ITEM_SORT_ALL, 0, R.string.logsmenu_sort_all);
//        sortMenu.add(MENU_ITEM_SORT, MENU_ITEM_SORT_BY_XODIL, 1, R.string.logsmenu_sort_xodil);
//        sortMenu.add(MENU_ITEM_SORT, MENU_ITEM_SORT_BY_PODTVERDIL, 2, R.string.logsmenu_sort_podtverdil);
//        sortMenu.setIcon(android.R.drawable.ic_menu_sort_alphabetically);

        // // ===
        // MenuItem item_2 = menu.add(group2Id, MENU_ITEM_LOGSTEXT,
        // logstextBtnId, R.string.logsmenu_sort_all);
        // item_2.setIcon(android.R.drawable.ic_menu_edit);

        // // ===
        // MenuItem item_2 = menu.add(group2Id, MENU_ITEM_SORT_ALL,
        // sortAllBtnId, R.string.logsmenu_sort_all);
        // item_2.setIcon(android.R.drawable.ic_menu_edit);
        //
        // // ===
        // MenuItem item_3 = menu.add(group2Id, MENU_ITEM_SORT_BY_PODTVERDIL,
        // sortPodtverdilBtnId, R.string.logsmenu_sort_podtverdil);
        // item_3.setIcon(android.R.drawable.ic_menu_sort_by_size);

        menu.clear();
//        menu.removeItem(R.id.menu_about);
//        menu.removeItem(R.id.menu_players_names);
//        menu.removeItem(R.id.menu_new_game);
//        
        inflater.inflate(R.menu.textlogs, menu);
       
        menu.removeItem(R.id.logsmenu_people);
        menu.removeItem(R.id.logsmenu_place);
        menu.removeItem(R.id.logsmenu_weapon);

        super.onCreateOptionsMenu(menu, inflater);
    }

    

    /**
     * On options menu item selection.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logsmenu_sort_xodil: {
                showDialog(R.id.logs_dialog_sort_by_xodil);
                return true;
            }

            case R.id.logsmenu_sort_all: {
                mViewMode = ShowModeType.ALL;
                mAdapter.notifyDataSetChanged();
                return true;
            }

            case R.id.logsmenu_sort_podtverdil: {
                showDialog(R.id.logs_dialog_sort_by_podtverdil);
                return true;
            }
            
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Item view cache holder.
     */
    private static final class ListItemCache {

        public LinearLayout LLmain;
        public TextView TextXodil;
        public TextView TextPodtverdil;
        public View divider;

        public Button btn1;
        public Button btn2;
        public Button btn3;

    }

    private class MyLogsListAdapter extends BaseAdapter {

        private Context mContext;

        public MyLogsListAdapter(Context context) {
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
                view = (View) LayoutInflater.from(mContext).inflate(R.layout.logs_row, parent, false);

                cache.LLmain = (LinearLayout) view.findViewById(R.id.LLmainrow);
                cache.TextXodil = (TextView) view.findViewById(R.id.txtLeft);
                cache.TextPodtverdil = (TextView) view.findViewById(R.id.txtRight);
                cache.btn1 = (Button) view.findViewById(R.id.btn1);
                cache.btn2 = (Button) view.findViewById(R.id.btn2);
                cache.btn3 = (Button) view.findViewById(R.id.btn3);

                view.setTag(cache);

            } else {
                cache = (ListItemCache) view.getTag();
            }

            cache.btn1.setOnClickListener(new OnItemClickListener(position));
            cache.btn2.setOnClickListener(new OnItemClickListener(position));
            cache.btn3.setOnClickListener(new OnItemClickListener(position));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    (int) (72 * scale + 0.5f));
            // if (mViewMode == ShowModeType.ALL){
            // if (position == 0){
            // lp = new LinearLayout.LayoutParams(
            // ViewGroup.LayoutParams.FILL_PARENT, (int) (72 * scale + 0.5f));
            // }
            // }
            cache.LLmain.setLayoutParams(lp);

            cache.TextXodil.setText(" ");
            int num = utils.getCurentList(mViewMode, mPerson).get(position).getPlayerXodit();
            if (num != -1) {
                // cache.TextXodil.setBackgroundResource(game.mPlayers.get(num).getColor());
                cache.TextXodil.setBackgroundColor(game.mPlayers.get(num).getColor());
            }

            num = utils.getCurentList(mViewMode, mPerson).get(position).getPlayerPodtverdil();
            cache.TextPodtverdil.setBackgroundResource(R.color.bgMain);
            if (num != -1) {
                if (num == 100) {
                    cache.TextPodtverdil.setBackgroundResource(R.color.bgBlack);
                } else {
                    cache.TextPodtverdil.setBackgroundColor(game.mPlayers.get(num).getColor());
                }
            }

            int[] slux = utils.getCurentList(mViewMode, mPerson).get(position).getSlyx();
            // Log.i(TAG,"slyx:"+slux[0]+slux[1]+slux[2]);
            // cache.btn1.setImageResource(cApp.getIconForPlayer(slux[0]));
            // cache.btn2.setImageResource(cApp.getIconForPlace(slux[1]));
            // cache.btn3.setImageResource(cApp.getIconForWeapon(slux[2]));
            // Layout parameters for the ExpandableListView

            if ((position == 0) && (mViewMode == ShowModeType.ALL)) {
                cache.btn1.setBackgroundResource(android.R.drawable.btn_default);
                cache.btn2.setBackgroundResource(android.R.drawable.btn_default);
                cache.btn3.setBackgroundResource(android.R.drawable.btn_default);
            } else {
                cache.btn1.setBackgroundResource(R.drawable.bgempty);
                cache.btn2.setBackgroundResource(R.drawable.bgempty);
                cache.btn3.setBackgroundResource(R.drawable.bgempty);
            }

            if (slux[0] != -1) {
                cache.btn1.setText(game.mPeople[slux[0]]);
                cache.btn1.setTextColor(game.mPlayers.get(slux[0]).getColor());
            } else {
                cache.btn1.setText("[---]");
                cache.btn1.setTextColor(R.color.bgBlack);
            }
            if (slux[1] != -1) {
                cache.btn2.setText(game.mPlace[slux[1]]);
            } else {
                cache.btn2.setText("[---]");
            }
            if (slux[2] != -1) {
                cache.btn3.setText(game.mWeapon[slux[2]]);
            } else {
                cache.btn3.setText("[---]");
            }

            return view;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            if (!utils.getAllList().isEmpty()) {
                if ((utils.getAllList().get(0).getSlyxPerson() == -1)
                        || (utils.getAllList().get(0).getSlyxPlace() == -1)
                        || (utils.getAllList().get(0).getSlyxWeapon() == -1)) {
                    mBtnPodtverdil.setEnabled(false);
                } else {
                    mBtnPodtverdil.setEnabled(true);
                    mBtnPodtverdil.setClickable(true);
                }
            } else {
                Toast.makeText(mContext, R.string.logs_sort_nothing_to_show, Toast.LENGTH_SHORT).show();
            }
        }

        private class OnItemClickListener implements OnClickListener {
            private int mPosition;

            OnItemClickListener(int position) {
                mPosition = position;
            }

            @Override
            public void onClick(View view) {
                // Perform Clicks only for current row
                if (mPosition != 0) {
                    return;
                }

                switch (view.getId()) {
                    case R.id.btn1:
                        showDialog(R.id.logs_dialog_people);
                        break;
                    case R.id.btn2:
                        showDialog(R.id.logs_dialog_place);
                        break;
                    case R.id.btn3:
                        showDialog(R.id.logs_dialog_weapon);
                        break;
                }
            }
        }

    }
    
    private void showDialog (int id){
        
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("LogsDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        
        // Create and show the dialog.
        TableDialogFragment newFragment = LogsDialogFragment.newInstance(id);
        newFragment.setConfirmationDialogFragmentListener(new TableDialogFragmentListener(){
            @Override
            public void onPositiveClick() {
                mAdapter.notifyDataSetChanged();
            }
        });
        newFragment.show(ft, "LogsDialog");
    }
    
    public void doOnClickDialogItemAction() {
        // Do stuff here.
        
    }
    
    
    // /**
    // * @param messages Never {@code null}.
    // */
    // public void addNewMove() {
    //
    // final boolean wasEmpty = mAdapter.messages.isEmpty();
    // runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    // for (final MessageInfoHolder message : messages) {
    // if (mFolderName == null || (message.folder != null &&
    // message.folder.name.equals(mFolderName))) {
    // int index;
    // synchronized (mAdapter.messages) {
    // index = Collections.binarySearch(mAdapter.messages, message,
    // getComparator());
    // }
    //
    // if (index < 0) {
    // index = (index * -1) - 1;
    // }
    //
    // mAdapter.messages.add(index, message);
    // }
    // }
    //
    // resetUnreadCountOnThread();
    //
    // mAdapter.notifyDataSetChanged();
    // }
    // });
    // }

    // private void setSlyxText() {
    // if (mViewMode == ShowModeType.ALL) {
    // String text1 = "[???]";
    // String text2 = "[???]";
    // String text3 = "[???]";
    // mHeaderBox.setVisibility(View.VISIBLE);
    //
    // if (utils.getAllList().size() != 0) {
    // int[] slux = utils.getAllList().get(0).getSlyx();
    // if (utils.getAllList().get(0).getPlayerPodtverdil() == -1) {
    // // Log.i(TAG,"Slyxi=" + slux[0] +" " + slux[1] + slux[2]);
    // if (slux[0] != -1) {
    // text1 = game.mPeople[slux[0]];
    // }
    // if (slux[1] != -1) {
    // text2 = game.mPlace[slux[1]];
    // }
    // if (slux[2] != -1) {
    // text3 = game.mWeapon[slux[2]];
    // }
    // mSlyx.setText(text1 + " + " + text2 + " + " + text3);
    // } else {
    // mSlyx.setText(R.string.logs_txt4);
    // mTitle.setText("");
    // }
    // }
    // } else {
    // mHeaderBox.setVisibility(View.GONE);
    // String text = "";
    // if (mViewMode == ShowModeType.XODIT) {
    // text = this.getText(R.string.logs_toast_xoda)
    // + game.mPeople[mPerson];
    // } else {
    // text = this.getText(R.string.logs_toast_podtverdil)
    // + game.mPeople[mPerson];
    // }
    // mSlyx.setText(text);
    // }
    //
    // }
}