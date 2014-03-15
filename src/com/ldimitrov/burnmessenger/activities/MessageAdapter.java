package com.ldimitrov.burnmessenger.activities;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldimitrov.burnmessenger.util.ParseConstants;
import com.parse.ParseObject;

public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages) {
        super(context, R.layout.message_item, messages);
        mContext = context;
        mMessages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView)convertView.findViewById(R.id.messageIcon);
            holder.nameLabel = (TextView)convertView.findViewById(R.id.senderLabel);
            holder.sendTime = (TextView)convertView.findViewById(R.id.sendTime);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        ParseObject message = mMessages.get(position);

        if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
            holder.iconImageView.setImageResource(R.drawable.ic_action_picture);
        } else if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_VIDEO)) {
            holder.iconImageView.setImageResource(R.drawable.ic_action_play_over_video);
        } else {
            holder.iconImageView.setImageResource(R.drawable.ic_action_chat_light);
        }
        holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
        holder.sendTime.setText(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(message.getCreatedAt()));

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameLabel;
        TextView sendTime;
    }
}
