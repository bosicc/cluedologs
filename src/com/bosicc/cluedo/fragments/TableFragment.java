package com.bosicc.cluedo.fragments;

// http://habrahabr.ru/blogs/android/103582/

//Need the following import to get access to the app resources, since this
//class is in a sub-package.
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.bosicc.cluedo.CluedoApp;
import com.bosicc.cluedo.R;
import com.bosicc.cluedo.dialogs.TableDialogFragment;
import com.bosicc.cluedo.dialogs.TableDialogFragment.TableDialogFragmentListener;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.pojo.GamePOJO.CardType;
import com.bosicc.cluedo.utils.CConstants;
import com.bosicc.cluedo.utils.CConstants.Coord;
import com.bosicc.cluedo.utils.Utils;
import com.flurry.android.FlurryAgent;

/**
 * A list view example where the data comes from a custom ListAdapter
 */
public class TableFragment extends SherlockListFragment {

    private static String TAG = "TableFragment";

    private ImageButton mNavBtn;
    private ListView mList;
    private MyListAdapter mAdapter;
    private String[] mCards;
    private CluedoApp cApp;
    private GamePOJO game;
    private Utils utils;
    private TextView[] header;

    static int kolnaekrane = 6;
    int offset = 0;

    private Coord mCurentItem = new Coord();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null){
            return null;
        }
        return inflater.inflate(R.layout.table, null);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      cApp = (CluedoApp) getActivity().getApplication();
      game = cApp.getGame();
      utils = new Utils(getActivity(), game);

      header = new TextView[kolnaekrane];
      header[0] = ((TextView) getView().findViewById(R.id.txtPeople1));
      header[1] = ((TextView) getView().findViewById(R.id.txtPeople2));
      header[2] = ((TextView) getView().findViewById(R.id.txtPeople3));
      header[3] = ((TextView) getView().findViewById(R.id.txtPeople4));
      header[4] = ((TextView) getView().findViewById(R.id.txtPeople5));
      header[5] = ((TextView) getView().findViewById(R.id.txtPeople6));

      mNavBtn = (ImageButton) getView().findViewById(R.id.IbtnNav);

      mCards = new String[game.cardnum];
      int i = 0;
      for (int j = 0; j < game.mPeople.length; j++) {
          mCards[i] = game.mPeople[j];
          i++;
      }
      for (int j = 0; j < game.mPlace.length; j++) {
          mCards[i] = game.mPlace[j];
          i++;
      }
      for (int j = 0; j < game.mWeapon.length; j++) {
          mCards[i] = game.mWeapon[j];
          i++;
      }

      if (game.mPeople.length > kolnaekrane) {
          mNavBtn.setVisibility(View.VISIBLE);
          mNavBtn.setOnClickListener(new OnClickListener() {

              @Override
              public void onClick(View v) {
                  if (offset + kolnaekrane < game.mPeople.length) {
                      offset = game.mPeople.length - kolnaekrane;
                      mNavBtn.setImageResource(R.drawable.nav_left);
                  } else {
                      mNavBtn.setImageResource(R.drawable.nav_right);
                      offset = 0;
                  }
                  mAdapter.notifyDataSetChanged();

              }
          });
      } else {
          mNavBtn.setVisibility(View.INVISIBLE);
      }

      // Set up our adapter
      mAdapter = new MyListAdapter(getActivity());
      this.setListAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        SetHeaderText();
    }


//    @Override
//    protected void onPrepareDialog(int id, Dialog dialog) {
//        super.onPrepareDialog(id, dialog);
//        switch (id) {
//            case DIALOG_MARK:
//                String text = game.mPlayers.get(mCurentItem.num).getName();
//                if (text.equals("")) {
//                    text = game.mPeople[mCurentItem.num];
//                }
//                String title = text + " " + getText(R.string.table_title_mark) + " " + mCards[mCurentItem.pos] + "?";
//                // Log.i(TAG, "onCreateDialog at mCurentItem.pos=" +
//                // mCurentItem.pos + " mCurentItem.num=" + mCurentItem.num);
//                // Log.i(TAG, "onCreateDialog at mPeople - " +
//                // mPeople[mCurentItem.num] + " Item=" +
//                // mCards[mCurentItem.pos]);
//                dialog.setTitle(title);
//        }
//    }

    /**
     * Item view cache holder.
     */
    private static final class ListItemCache {

        public View header;
        public TextView Text;
        public TextView headerText;
        public View divider;

        public ImageButton[] btn = new ImageButton[kolnaekrane];
    }

    private class MyListAdapter extends BaseAdapter {

        private Context mContext;

        public MyListAdapter(Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return mCards.length;
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
                view = (View) LayoutInflater.from(mContext).inflate(R.layout.table_row, parent, false);

                cache.header = view.findViewById(R.id.header);
                cache.Text = (TextView) view.findViewById(R.id.txtLeft);
                cache.headerText = (TextView) view.findViewById(R.id.header_text);
                cache.btn[0] = (ImageButton) view.findViewById(R.id.btn1);
                cache.btn[1] = (ImageButton) view.findViewById(R.id.btn2);
                cache.btn[2] = (ImageButton) view.findViewById(R.id.btn3);
                cache.btn[3] = (ImageButton) view.findViewById(R.id.btn4);
                cache.btn[4] = (ImageButton) view.findViewById(R.id.btn5);
                cache.btn[5] = (ImageButton) view.findViewById(R.id.btn6);

                view.setTag(cache);

            } else {
                cache = (ListItemCache) view.getTag();
            }

            // Log.i("Table","pos"+position);
            Paint color = new Paint();
            for (int i = 0; i < kolnaekrane; i++) {
                cache.btn[i].setOnClickListener(new OnItemClickListener(position));
                cache.btn[i].setImageResource(getResourceByType(utils.getCardsData()[position][offset + i]));
                // cache.btn[i].setBackgroundColor(game.mPlayers.get(offset+i).getColor());
                color.setColor(game.mPlayers.get(offset + i).getColor());
                color.setAlpha(50);
                cache.btn[i].setBackgroundColor(color.getColor());
            }

            cache.header.setVisibility(View.GONE);

            if (position == 0) {
                cache.headerText.setText(R.string.title_people);
                cache.header.setVisibility(View.VISIBLE);
            }

            if (position == game.mPeople.length) {
                cache.headerText.setText(R.string.title_place);
                cache.header.setVisibility(View.VISIBLE);
            }

            if (position == (game.mPeople.length + game.mPlace.length)) {
                cache.headerText.setText(R.string.title_weapon);
                cache.header.setVisibility(View.VISIBLE);
            }

            cache.Text.setText(mCards[position]);

            // cache.btn1.setImageResource(getResourceByType(utils.getCardsData()[position][offset+0]));
            // cache.btn2.setImageResource(getResourceByType(utils.getCardsData()[position][offset+1]));
            // cache.btn3.setImageResource(getResourceByType(utils.getCardsData()[position][offset+2]));
            // cache.btn4.setImageResource(getResourceByType(utils.getCardsData()[position][offset+3]));
            // cache.btn5.setImageResource(getResourceByType(utils.getCardsData()[position][offset+4]));
            // cache.btn6.setImageResource(getResourceByType(utils.getCardsData()[position][offset+5]));

            return view;
        }

        /**
         * Update Names
         */
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            SetHeaderText();
        }

        private int getResourceByType(CardType type) {
            int res = R.drawable.btn_none;
            switch (type) {
                case DEFAULT:
                    res = R.drawable.btn_none;
                    break;
                case NO:
                    res = R.drawable.btn_no;
                    break;

                case YES:
                    res = R.drawable.btn_yes;
                    break;

                case QUESTION:
                    res = R.drawable.btn_help;
                    break;
                case ASK:
                    res = R.drawable.btn_ask;
                    break;
            }
            return res;
        }

        private class OnItemClickListener implements OnClickListener {
            private int mPosition;

            OnItemClickListener(int position) {
                mPosition = position;
            }

            @Override
            public void onClick(View view) {

                int num = 0;
                switch (view.getId()) {
                    case R.id.btn1:
                        num = 0;
                        break;
                    case R.id.btn2:
                        num = 1;
                        break;
                    case R.id.btn3:
                        num = 2;
                        break;
                    case R.id.btn4:
                        num = 3;
                        break;
                    case R.id.btn5:
                        num = 4;
                        break;
                    case R.id.btn6:
                        num = 5;
                        break;
                }
                //TODO: FIX here
                
                mCurentItem.pos = mPosition;
                mCurentItem.num = offset + num;
                showDialog();
            }
        }

    }
    
    private void showDialog() {
        
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("TableDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        
        
        String text = game.mPlayers.get(mCurentItem.num).getName();
        if (text.equals("")) {
            text = game.mPeople[mCurentItem.num];
        }
        String title = text + " " + getText(R.string.table_title_mark) + " " + mCards[mCurentItem.pos] + "?";
        
        // Create and show the dialog.
        TableDialogFragment newFragment = TableDialogFragment.newInstance(R.id.table_dialog_mark, title, mCurentItem);
        newFragment.setConfirmationDialogFragmentListener(new TableDialogFragmentListener(){
            @Override
            public void onPositiveClick() {
                mAdapter.notifyDataSetChanged();
            }
        });
        newFragment.show(ft, "TableDialog");
    }

    public void SetHeaderText() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("offset", "" + offset);
        params.put("kolnaekrane", "" + kolnaekrane);
        params.put("Players size", "" + game.mPlayers.size());
        params.put("gamePlace[0]", "" + game.mPlace[0]);
        FlurryAgent.logEvent(CConstants.FLURRY_TABLEACTIVITY_RESUME, params);

        // FlurryAgent.onError(arg0, arg1,
        // arg2)logEvent(CConstants.FLURRY_TABLEACTIVITY_RESUME,params);
        if (!game.mPlayers.isEmpty()) {
            for (int i = 0; i < kolnaekrane; i++) {
                if (game.mPlayers.size() < offset + i) {
                    FlurryAgent.onError(CConstants.FLURRY_TABLEACTIVITY_RESUME,
                            "mPlaters size =" + game.mPlayers.size() + " offset+i = " + offset + i + " gamePlace[0]="
                                    + game.mPlace[0], "");
                }
                header[i].setText(game.mPlayers.get(offset + i).getName());
                header[i].setBackgroundColor(game.mPlayers.get(offset + i).getColor());
            }
        }
    }

}