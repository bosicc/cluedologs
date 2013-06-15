package com.bosicc.cluedo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.bosicc.cluedo.R;
import com.bosicc.cluedo.pojo.GamePOJO.ShowModeType;
import com.bosicc.cluedo.pojo.PMovePOJO;
import com.bosicc.cluedo.utils.CConstants;

public class LogsDialogFragment extends DialogFragment {
    private static final String TAG = "TableDialogFragment";

    private TableDialogFragmentListener listener;

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CURITEM = "curitem";

    public static LogsDialogFragment newInstance(int id) {
        LogsDialogFragment frag = new LogsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        Log.d(TAG, "onActivityCreated() arg0=" + arg0);
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

        switch (id) {

            case R.id.logs_dialog_xodil:
                return new AlertDialog.Builder(getActivity()).setTitle(R.string.logs_btnxodit)
                        .setItems(utils.getString(mCurentDialogList), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, final int which) {
                                getActivity().sendBroadcast(new Intent(CConstants.ACTION_UPDATE_DATA));
                                // http://code.google.com/p/k9mail/source/detail?r=3132
                                // https://github.com/k9mail/k-9/blob/master/src/com/fsck/k9/activity/ChooseFolder.java
                                // https://github.com/sunlightlabs/congress
                                int num = mCurentDialogList.get(which).getNumber();
                                PMovePOJO item = new PMovePOJO(num);
                                utils.getAllList().add(0, item);
                                mBtnPodtverdil.setEnabled(false);
                                mBtnXodit.setEnabled(false);

                                mSlyx.setText("");
                                mTitle.setText(mCurentDialogList.get(which).getLabel());
                                mTitle.setOnClickListener(new OnClickListener() {
                                    public void onClick(View view) {
                                        showDialog(DIALOG_XODIT_EDIT);
                                    }
                                });

                                mAdapter.notifyDataSetChanged();
                                mCurentDialogList.removeAll(mCurentDialogList);
                            }
                        }).create();
            case R.id.logs_dialog_xodil_edit:
                return new AlertDialog.Builder(getActivity()).setTitle(R.string.logs_btnxodit)
                        .setItems(utils.getString(mCurentDialogList), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, final int which) {

                                // No need to remove current row, only update
                                int num = mCurentDialogList.get(which).getNumber();
                                utils.getAllList().get(0).setPlayerXodit(num);
                                sendBroadcast(new Intent(CConstants.ACTION_UPDATE_DATA));

                                mSlyx.setText("");
                                mTitle.setText(mCurentDialogList.get(which).getLabel());
                                mAdapter.notifyDataSetChanged();
                                mCurentDialogList.removeAll(mCurentDialogList);
                            }
                        }).create();
            case R.id.logs_dialog_podtverdil:
                int xodit = utils.getAllList().get(0).getPlayerXodit();
                mCurentDialogList = utils.getPodtverdilList(xodit);

                return new AlertDialog.Builder(getActivity()).setTitle(R.string.logs_btnpodtverdil)
                        .setItems(utils.getString(mCurentDialogList), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mBtnXodit.setEnabled(true);
                                if (which == 0) {
                                    which = nc;
                                } else {
                                    which = mCurentDialogList.get(which).getNumber();
                                }
                                utils.getAllList().get(0).setPlayerPodtverdil(which);
                                mAdapter.notifyDataSetChanged();
                                mCurentDialogList.removeAll(mCurentDialogList);
                                // removeDialog(DIALOG_PODTVERDIL);
                            }
                        }).create();
            case R.id.logs_dialog_people:
                return new AlertDialog.Builder(getActivity()).setTitle(R.string.title_people)
                        .setItems(game.mPeople, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                utils.getAllList().get(0).setSlyxPerson(which);
                                mAdapter.notifyDataSetChanged();
                            }
                        }).create();
            case R.id.logs_dialog_place:
                return new AlertDialog.Builder(getActivity()).setTitle(R.string.title_place)
                        .setItems(game.mPlace, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                utils.getAllList().get(0).setSlyxPlace(which);
                                mAdapter.notifyDataSetChanged();
                            }
                        }).create();
            case R.id.logs_dialog_weapon:
                return new AlertDialog.Builder(getActivity()).setTitle(R.string.title_weapon)
                        .setItems(game.mWeapon, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                utils.getAllList().get(0).setSlyxWeapon(which);
                                mAdapter.notifyDataSetChanged();
                            }
                        }).create();
            case R.id.logs_dialog_sort_by_xodil:
                mCurentDialogList = utils.getSortXodilList();
                return new AlertDialog.Builder(getActivity()).setTitle(R.string.logs_alert_title_sort_xodil)
                        .setItems(utils.getString(mCurentDialogList), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mViewMode = ShowModeType.XODIT;
                                mPerson = mCurentDialogList.get(which).getNumber();
                                ;
                                mAdapter.notifyDataSetChanged();
                                mCurentDialogList.removeAll(mCurentDialogList);
                                // removeDialog(DIALOG_SORT_BY_XODIL);
                            }
                        }).create();
            case R.id.logs_dialog_sort_by_podtverdil:
                mCurentDialogList = utils.getSortPodtverdilList();
                return new AlertDialog.Builder(getActivity()).setTitle(R.string.logs_alert_title_sort_podtverdil)
                        .setItems(utils.getString(mCurentDialogList), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    which = nc;
                                } else {
                                    which = mCurentDialogList.get(which).getNumber();
                                }
                                mViewMode = ShowModeType.PODTVERDIL;
                                mAdapter.notifyDataSetChanged();
                                mCurentDialogList.removeAll(mCurentDialogList);
                                // removeDialog(DIALOG_SORT_BY_PODTVERDIL);
                            }
                        }).create();
        }
        return null;

    }

    // @Override
    // protected void onPrepareDialog(int id, Dialog dialog) {
    //
    // AlertDialog alertDialog = (AlertDialog) dialog;
    // ArrayAdapter<CharSequence> adapter;
    // switch (id) {
    // case DIALOG_XODIT:
    // case DIALOG_XODIT_EDIT:
    // case DIALOG_SORT_BY_XODIL:
    // mCurentDialogList = utils.getSortXodilList();
    // adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.select_dialog_item, android.R.id.text1,
    // utils.getString(mCurentDialogList));
    // alertDialog.getListView().setAdapter(adapter);
    // break;
    //
    // case DIALOG_PODTVERDIL:
    // int xodit = utils.getAllList().get(0).getPlayerXodit();
    // mCurentDialogList = utils.getPodtverdilList(xodit);
    // adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.select_dialog_item, android.R.id.text1,
    // utils.getString(mCurentDialogList));
    // alertDialog.getListView().setAdapter(adapter);
    // break;
    //
    // case DIALOG_SORT_BY_PODTVERDIL:
    // mCurentDialogList = utils.getSortPodtverdilList();
    // adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.select_dialog_item, android.R.id.text1,
    // utils.getString(mCurentDialogList));
    // alertDialog.getListView().setAdapter(adapter);
    // break;
    // default:
    // super.onPrepareDialog(id, dialog);
    // }
    // }

}