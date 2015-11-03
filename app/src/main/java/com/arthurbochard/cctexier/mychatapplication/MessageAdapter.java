package com.arthurbochard.cctexier.mychatapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cc.texier on 13/10/15.
 */
public class MessageAdapter extends BaseAdapter {

    private List<Message> listMessage;

    private Context mContext;

    private LayoutInflater mInflater;

    private String mLogin;

    public MessageAdapter(Context context, List<Message>list, String login)
    {
        mContext = context;
        listMessage = list;
        mInflater = LayoutInflater.from(mContext);
        mLogin = login;
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
        TextView login = (TextView) layoutItem.findViewById(R.id.login);
        TextView message = (TextView) layoutItem.findViewById(R.id.message);

        //(3) : Renseignement des valeurs
        login.setText(listMessage.get(position).getLogin());
        message.setText(listMessage.get(position).getMessage());

        RelativeLayout rl= (RelativeLayout) layoutItem.findViewById(R.id.relativeLayout);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(rl.getLayoutParams());
        if (listMessage.get(position).getLogin().equals(mLogin))
        {
            param.setMargins(600, 5, 5, 5);
            rl.setLayoutParams(param);
            rl.setGravity(Gravity.END);
            rl.setBackground(mContext.getResources().getDrawable(R.drawable.back_me));
            //rl.setBackgroundColor(Color.rgb(215,215,215));

        }
        else
        {
            param.setMargins(5, 5, 600, 5);
            rl.setLayoutParams(param);
            rl.setGravity(Gravity.START);
            rl.setBackground(mContext.getResources().getDrawable(R.drawable.back));
           // rl.setBackgroundColor(Color.rgb(160,160,160));
        }

        return layoutItem;
    }
}
