package com.bosicc.cluedo.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bosicc.cluedo.R;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.utils.Utils;

public class PlayersNameDialog extends DialogFragment {

    private static String TAG = "PlayersNameDialog";

    private GamePOJO game;
    private Utils utils;
    private ListView mList;
    private BaseAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playernames, null);
        v.findViewById(R.id.OkButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mList = (ListView) v.findViewById(R.id.listView);
        mAdapter = new MyNameAdapter();
        mList.setAdapter(mAdapter);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return v;
    }

    public void onClick(View v) {
        dismiss();
    }

    public void setGameData(Context context, GamePOJO Game) {
        this.game = Game;
        utils = new Utils(context, game);

        // Set you name
        String name = context.getText(R.string.table_you_text).toString();
        game.mPlayers.get(utils.getYourPlayer()).setName(name);

    }

    /**
     * Item view cache holder.
     */
    private static final class ListItemCache {
        public TextView text;
        public EditText editText;
        public CheckBox check;
    }

    public class MyNameAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return game.mPlayers.size();
        }

        @Override
        public Object getItem(int position) {
            return game.mPlayers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return game.mPlayers.get(position).getNumber();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ListItemCache cache = new ListItemCache();
            if (view == null) {
                view = (View) LayoutInflater.from(getActivity()).inflate(R.layout.playername_row, parent, false);

                cache.text = (TextView) view.findViewById(R.id.text);
                cache.editText = (EditText) view.findViewById(R.id.editText);
                cache.check = (CheckBox) view.findViewById(R.id.checkBox);

                view.setTag(cache);

            } else {
                cache = (ListItemCache) view.getTag();
            }

            // Set player color
            cache.text.setBackgroundColor(game.mPlayers.get(position).getColor());

            // Edit text field
            String name = game.mPlayers.get(position).getName();
            if (name.equals("")) {
                cache.editText.setText("");
                cache.editText.setHint(game.mPlayers.get(position).getCardName());
            } else {
                cache.editText.setText(name);
            }

            if (utils.getYourPlayer() == position) {
                cache.check.setEnabled(false);
                cache.check.setChecked(game.mPlayers.get(position).inGame());
                // Log.i(TAG,"getView pos="+position+" checked="+game.mPlayers.get(position).inGame());
            } else {

                // Disable edit
                cache.editText.setOnFocusChangeListener(new OnItemFocusListener(position));
                boolean in = game.mPlayers.get(position).inGame();

                cache.editText.setEnabled(in);
                cache.editText.setFocusable(in);
                cache.editText.setFocusableInTouchMode(in);

                // Check Box
                cache.check.setEnabled(!game.isCreated);
                boolean state = game.mPlayers.get(position).inGame();
                // Log.i(TAG,"getView pos="+position+" current state ="+state
                // +" BOX="+cache.check.isChecked());
                cache.check.setChecked(state);
                cache.check.setOnClickListener(new OnItemClickListener(position, cache.editText));
            }

            return view;
        }

    }

    /**
     * Listener for editable text
     * 
     * @author Bosicc
     * 
     */
    private class OnItemFocusListener implements OnFocusChangeListener {
        private int mPosition;

        OnItemFocusListener(int position) {
            mPosition = position;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                final EditText Name = (EditText) v;
                game.mPlayers.get(mPosition).setName(Name.getText().toString());
            }
        }
    }

    /**
     * Listener for Check box
     * 
     * @author Bosicc
     * 
     */
    private class OnItemClickListener implements android.view.View.OnClickListener {
        private int mPosition;
        private EditText mEdit;

        OnItemClickListener(int position, EditText edit) {
            mPosition = position;
            mEdit = edit;
        }

        @Override
        public void onClick(View v) {
            boolean curstate = !game.mPlayers.get(mPosition).inGame();

            game.mPlayers.get(mPosition).inGame(curstate);
            // Log.i(TAG,"<<<<<<<<<<<<<<<<onCheck pos="+mPosition+" curr="+!curstate);
            mEdit.setEnabled(curstate);
            mEdit.setFocusable(curstate);
            mEdit.setFocusableInTouchMode(curstate);
        }
    }

}
