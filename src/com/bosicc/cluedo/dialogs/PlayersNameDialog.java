package com.bosicc.cluedo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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




public class PlayersNameDialog extends Dialog {
	
	//private static String TAG = "PlayersName";
	
	private ImageButton okButton;

	private GamePOJO game;
	private Utils utils;
	private Context context;
    private ListView mList;
    private BaseAdapter mAdapter;
    
	public PlayersNameDialog(Context context, GamePOJO Game) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.playernames);
		okButton = (ImageButton) findViewById(R.id.OkButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				for (int i=0; i<game.mPlayers.size();i++){
//					Log.i(TAG," player="+game.mPlayers.get(i).inGame());
//				}
				dismiss();
			}
		});

		
		this.game = Game;
		this.context = context;
		utils = new Utils(context, game);
		
		//Set you name
		game.mPlayers.get(utils.getYourPlayer()).setName(context.getText(R.string.table_you_text).toString());
		
//		for (int i=0; i<game.mPlayers.size();i++){
//			Log.i(TAG,"start player="+game.mPlayers.get(i).inGame());
//		}
		
		
		mList = (ListView) findViewById(R.id.listView);
		mAdapter = new MyNameAdapter();
        mList.setAdapter(mAdapter);
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
            	view = (View) LayoutInflater.from(context).inflate(
                        R.layout.playername_row, parent, false);
            	
                cache.text = (TextView)view.findViewById(R.id.text);
                cache.editText = (EditText)view.findViewById(R.id.editText);
                cache.check = (CheckBox)view.findViewById(R.id.checkBox);
                
                
                view.setTag(cache);

            } else {
            	cache = (ListItemCache) view.getTag();
            }
            
            //Set player color
            cache.text.setBackgroundColor(game.mPlayers.get(position).getColor());
            
            //Edit text field
            String name = game.mPlayers.get(position).getName();
            if (name.equals("")){
            	cache.editText.setText("");
            	cache.editText.setHint(game.mPlayers.get(position).getCardName());
            }else{
            	cache.editText.setText(name);
            }
            
            if (utils.getYourPlayer() == position){
                cache.check.setEnabled(false);
                cache.check.setChecked(game.mPlayers.get(position).inGame());
                //Log.i(TAG,"getView pos="+position+" checked="+game.mPlayers.get(position).inGame());
            }else{
                
                //Disable edit
                cache.editText.setOnFocusChangeListener(new OnItemFocusListener(position));
                boolean in = game.mPlayers.get(position).inGame();

            	cache.editText.setEnabled(in);
            	cache.editText.setFocusable(in);
            	cache.editText.setFocusableInTouchMode(in);

                // Check Box
                cache.check.setEnabled(!game.isCreated);
                boolean state = game.mPlayers.get(position).inGame();
                //Log.i(TAG,"getView pos="+position+" current state ="+state +" BOX="+cache.check.isChecked());
                cache.check.setChecked(state);
                cache.check.setOnClickListener(new OnItemClickListener(position, cache.editText));
            }

            return view;
		}
		
	}
	/**
	 * Listener for editable text
	 * @author Bosicc
	 *
	 */
	private class OnItemFocusListener implements OnFocusChangeListener{           
        private int mPosition;
        OnItemFocusListener(int position){
                mPosition = position;
        }

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus){
				final EditText Name = (EditText) v;
				game.mPlayers.get(mPosition).setName(Name.getText().toString());
			}
		}               
    }
	
	/**
	 * Listener for Check box
	 * @author Bosicc
	 *
	 */
	private class OnItemClickListener implements android.view.View.OnClickListener{           
         private int mPosition;
         private EditText mEdit;
         OnItemClickListener(int position, EditText edit){
                 mPosition = position;
                 mEdit = edit;
        }
		@Override
		public void onClick(View v) {
			boolean curstate = !game.mPlayers.get(mPosition).inGame();
			
			game.mPlayers.get(mPosition).inGame(curstate);
			//Log.i(TAG,"<<<<<<<<<<<<<<<<onCheck pos="+mPosition+" curr="+!curstate);
			mEdit.setEnabled(curstate);
			mEdit.setFocusable(curstate);
			mEdit.setFocusableInTouchMode(curstate);
		}
     }

}
