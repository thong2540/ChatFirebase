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
import com.example.chatfirebase.model.User;
import com.example.chatfirebase.ui.listener.ItemClickListener;
import com.example.chatfirebase.ui.listener.LastMessageCallback;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUser;
    private ItemClickListener mItemClickListener;
    private LastMessageCallback mLastMessageCallback;

    private boolean mischat;

    public void SetOnClick(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setlastMessage(LastMessageCallback lastMessageCallback) {
        this.mLastMessageCallback = lastMessageCallback;
    }

    public UserAdapter(Context context, List<User> list, boolean ischat) {
        this.mContext = context;
        this.mUser = list;
        this.mischat = ischat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = mUser.get(i);
        viewHolder.username.setText(user.getUsername());
        if (user.getImgURL().equals("default")) {
            viewHolder.pro_img.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImgURL()).into(viewHolder.pro_img);
        }

        if (mischat) {
            if (mLastMessageCallback != null)
                mLastMessageCallback.onLastMessageCallback(user.getId(), viewHolder.last_msg);
//            lastMessage(user.getId(), viewHolder.last_msg);
        } else {
            viewHolder.last_msg.setVisibility(View.GONE);
        }

        if (mischat) {
            if (user.getStatus().equals("online")) {
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            } else {
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.img_on.setVisibility(View.GONE);
            viewHolder.img_off.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClickListener(user.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView pro_img;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.text_username);
            pro_img = (ImageView) itemView.findViewById(R.id.pro_img);
            ///
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);

        }

    }
}


