package com.arthurbochard.cctexier.mychatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cc.texier on 13/10/15.
 */
public class MessageAdapter extends BaseAdapter {

    private List<Message> listMessage;

    private Context mContext;

    private LayoutInflater mInflater;


    public MessageAdapter(Context context, List<Message> list) {
        mContext = context;
        listMessage = list;
        mInflater = LayoutInflater.from(mContext);
    }



    @Override
    public int getCount() {
        return listMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return listMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;
        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "message_layout.xml"
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.message_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        //(2) : Récupération des TextView de notre layout
        TextView login = (TextView)layoutItem.findViewById(R.id.login);
        TextView message = (TextView)layoutItem.findViewById(R.id.message);

        //(3) : Renseignement des valeurs
        login.setText(listMessage.get(position).getLogin());
        message.setText(listMessage.get(position).getMessage());

        //(4) Changement de la couleur du fond de notre item
      /*  if (mListP.get(position).genre == Personne.MASCULIN) {
            layoutItem.setBackgroundColor(Color.BLUE);
        } else {
            layoutItem.setBackgroundColor(Color.MAGENTA);
        }*/

        //On retourne l'item créé.
        return layoutItem;
    }
}
