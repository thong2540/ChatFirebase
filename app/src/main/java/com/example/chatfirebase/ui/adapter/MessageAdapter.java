package com.example.chatfirebase.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatfirebase.R;
import com.example.chatfirebase.constant.ItemChat;
import com.example.chatfirebase.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<Chat> mChat;
    private String mImageurl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> list, String imageurl) {
        this.mContext = context;
        this.mChat = list;
        this.mImageurl = imageurl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == ItemChat.MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat chat = mChat.get(i);
        viewHolder.show_message.setText(chat.getMessage());
        if (mImageurl.equals("default")) {
            viewHolder.pro_img_chat.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(mImageurl).into(viewHolder.pro_img_chat);
        }
        if (i == mChat.size()-1){
            if (chat.isIsseen()){
                viewHolder.txt_seen.setText("Seen");
            } else {
                viewHolder.txt_seen.setText("Delivered");
            }
        } else {
            viewHolder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        ImageView pro_img_chat;
        TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = (TextView) itemView.findViewById(R.id.textview_chat);
            pro_img_chat = (ImageView) itemView.findViewById(R.id.profile_imge_chat);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return ItemChat.MSG_TYPE_RIGHT;
        } else {
            return ItemChat.MSG_TYPE_LEFT;
        }
    }
}
