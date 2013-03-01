package com.bosicc.cluedo.dialogs;

import com.bosicc.cluedo.R;
import com.bosicc.cluedo.fragments.TableFragment;
import com.bosicc.cluedo.utils.CConstants.Coord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TableDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private TableDialogFragmentListener listener;
    
    public static String ID = "id";
    public static String POS = "pos";
    public static String NUM = "num";

    public static TableDialogFragment newInstance(int id, int pos, int num) {
        TableDialogFragment frag = new TableDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        args.putInt(POS, pos);
        args.putInt(NUM, num);
        frag.setArguments(args);
        return frag;
    }

    public interface TableDialogFragmentListener {
        public void onPositiveClick();

        public void onNegativeClick();
    }

    public void setConfirmationDialogFragmentListener(TableDialogFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        int id = getArguments().getInt(ID);
        int pos = getArguments().getInt(POS);
        int num = getArguments().getInt(NUM);
        switch (id) {
          case R.id.table_dialog_mark:
              return new AlertDialog.Builder(getActivity()).setTitle(" ")
                      .setItems(R.array.mark, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {

                              switch (which) {
                                  case 0:
                                      // Set YES in position all other = NO
                                      //utils.setTypeinRowNoData(mCurentItem.pos, mCurentItem.num, CardType.YES);
                                      break;
                                  case 1:
                                      //utils.setCardsData(mCurentItem.pos, mCurentItem.num, CardType.QUESTION);
                                      break;
                                  case 2:
                                      //utils.setCardsData(mCurentItem.pos, mCurentItem.num, CardType.NO);
                                      break;
                                  case 4:
                                      //utils.setCardsData(mCurentItem.pos, mCurentItem.num, CardType.DEFAULT);
                                      break;
                                  case 3:
                                      //utils.setCardsData(mCurentItem.pos, mCurentItem.num, CardType.ASK);
                                      break;
                              }
                              //mAdapter.notifyDataSetChanged();
                          }
                      }).create();

      }
      return null;
        
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (listener != null) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    listener.onPositiveClick();
                default:
                    listener.onNegativeClick();
            }
        }
    }
}