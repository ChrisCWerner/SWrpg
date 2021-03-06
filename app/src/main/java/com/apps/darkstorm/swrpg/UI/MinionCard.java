package com.apps.darkstorm.swrpg.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.StarWars.Minion;
import com.google.android.gms.drive.DriveId;

import java.io.File;

public class MinionCard {
    public CardView getCard(final Fragment frag, final Minion minion, final Handler handle, final boolean gm, final Handler topHandle){
        CardView top = new CardView(frag.getContext());
        CardView.LayoutParams topLp =
                new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
        int margin = (int)(frag.getResources().getDisplayMetrics().density*4);
        topLp.setMargins(margin,margin,margin,margin);
        top.setLayoutParams(topLp);
        LinearLayout topLay = new LinearLayout(frag.getContext());
        topLay.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams topLayLp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        topLayLp.setMargins(margin,margin,margin,margin);
        topLay.setLayoutParams(topLayLp);

        TextView nameText = new TextView(frag.getContext());
        LinearLayout.LayoutParams nameTextLp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        nameText.setLayoutParams(nameTextLp);
        nameText.setTextSize(24);
        nameText.setText(minion.name);

        TypedValue value = new TypedValue();
        frag.getActivity().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,value,true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            top.setForeground(frag.getActivity().getDrawable(value.resourceId));
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gm) {
                    Message up = topHandle.obtainMessage();
                    up.obj = minion;
                    topHandle.sendMessage(up);
                }else{
                    Message mess = handle.obtainMessage();
                    mess.obj = minion;
                    handle.sendMessage(mess);
                }
            }
        });
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!gm) {
                    final SharedPreferences pref = frag.getActivity().getSharedPreferences(
                            frag.getString(R.string.preference_key), Context.MODE_PRIVATE);
                    if (pref.getBoolean(frag.getString(R.string.cloud_key), false) &&
                            pref.getBoolean(frag.getString(R.string.sync_key), true) && (((SWrpg)frag.getActivity().getApplication()).gac == null ||
                            !((SWrpg)frag.getActivity().getApplication()).gac.isConnected())) {
                        Message mess = handle.obtainMessage();
                        mess.arg1 = 20;
                        handle.sendMessage(mess);
                    } else {
                        AlertDialog.Builder build = new AlertDialog.Builder(frag.getContext());
                        build.setMessage(R.string.minion_delete);
                        build.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Message mess = handle.obtainMessage();
                                mess.obj = minion;
                                handle.sendMessage(mess);
                                File charFile = new File(minion.getFileLocation(frag.getActivity()));
                                charFile.delete();
                                if (pref.getBoolean(frag.getString(R.string.cloud_key), false)) {
                                    AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            DriveId tmp = minion.getFileId(((SWrpg)frag.getActivity().getApplication()).gac,
                                                    DriveId.decodeFromString(pref.getString(frag.getString(R.string.swchars_id_key), "")));
                                            tmp.asDriveResource().delete(((SWrpg)frag.getActivity().getApplication()).gac);
                                            return null;
                                        }
                                    };
                                    async.execute();
                                }
                                dialogInterface.cancel();
                            }
                        });
                        build.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        build.show();
                    }
                }
                return true;
            }
        });
        topLay.addView(nameText);
        top.addView(topLay);
        return top;
    }
}
