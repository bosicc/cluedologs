package com.bosicc.cluedo.dialogs;

import com.bosicc.cluedo.CluedoApp;
import com.bosicc.cluedo.R;
import com.bosicc.cluedo.fragments.TableFragment;
import com.bosicc.cluedo.pojo.GamePOJO;
import com.bosicc.cluedo.pojo.GamePOJO.CardType;
import com.bosicc.cluedo.utils.Utils;
import com.bosicc.cluedo.utils.CConstants.Coord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class TableDialogFragment extends DialogFragment {
    private static final String TAG = "TableDialogFragment";
    
    private TableDialogFragmentListener listener;
    
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CURITEM = "curitem";

    public static TableDialogFragment newInstance(int id, String title, Coord curentItem) {
        TableDialogFragment frag = new TableDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        args.putString(TITLE, title);
        args.putSerializable(CURITEM, curentItem);
        frag.setArguments(args);
        return frag;
    }

    
    @Override
    public void onActivityCreated(Bundle arg0) {
        Log.d(TAG, "onActivityCreated() arg0="+arg0);
        super.onActivityCreated(arg0);
    }

    public interface TableDialogFragmentListener {
        public void onPositiveClick();
    }

    public void setConfirmationDialogFragmentListener(TableDialogFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        int id = getArguments().getInt(ID);
        final Coord curCoord = (Coord)getArguments().getSerializable(CURITEM);
        final String title = getArguments().getString(TITLE); 
     
        switch (id) {
          case R.id.table_dialog_mark:
              return new AlertDialog.Builder(getActivity()).setTitle(title)
                      .setItems(R.array.mark, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {
                              
                              CluedoApp cApp = (CluedoApp) getActivity().getApplication();
                              GamePOJO game = cApp.getGame();
                              Utils utils = new Utils(getActivity(), game);

                              switch (which) {
                                  case 0:
                                      // Set YES in position all other = NO
                                      utils.setTypeinRowNoData(curCoord.pos, curCoord.num, CardType.YES);
                                      break;
                                  case 1:
                                      utils.setCardsData(curCoord.pos, curCoord.num, CardType.QUESTION);
                                      break;
                                  case 2:
                                      utils.setCardsData(curCoord.pos, curCoord.num, CardType.NO);
                                      break;
                                  case 4:
                                      utils.setCardsData(curCoord.pos, curCoord.num, CardType.DEFAULT);
                                      break;
                                  case 3:
                                      utils.setCardsData(curCoord.pos, curCoord.num, CardType.ASK);
                                      break;
                              }
                              
                              listener.onPositiveClick();
                          }
                      }).create();

      }
      return null;
        
    }
}