package com.ldimitrov.burnmessenger.adapters;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ldimitrov.burnmessenger.activities.R;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, parent, false);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.senderLabel);
            holder.sendTime = (TextView) convertView.findViewById(R.id.sendTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
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

        //CharSequence relativeTime = DateUtils.getRelativeDateTimeString(mContext, now.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.YEAR_IN_MILLIS, 0);

        holder.sendTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(message.getCreatedAt()));

        return convertView;
    }

    public void refill(List<ParseObject> messages){
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameLabel;
        TextView sendTime;
    }
}
