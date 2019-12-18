package com.example.chatfirebase.ui.fragment.chat_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.api.Notifications.Token;
import com.example.chatfirebase.model.Chat;
import com.example.chatfirebase.model.Chatlist;
import com.example.chatfirebase.model.User;
import com.example.chatfirebase.ui.adapter.UserAdapter;
import com.example.chatfirebase.ui.listener.ItemClickListener;
import com.example.chatfirebase.ui.listener.LastMessageCallback;
import com.example.chatfirebase.ui.message.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class ChatsFragment extends Fragment {

    private View chat_fragment;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUser;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private List<Chatlist> usersList = new ArrayList<>();

    private String theLastMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chat_fragment = inflater.inflate(R.layout.fragment_chats, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setView();

        setOnClick();

        setInit();

        readUser();

        return chat_fragment;
    }

    private void setView() {
        recyclerView = (RecyclerView) chat_fragment.findViewById(R.id.recycleview_chat_frag);
    }

    private void setOnClick() {

    }

    private void setInit() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void readUser() {

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    private void chatList() {
        mUser = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList) {
                        if (user.getId().equals(chatlist.getId())) {
                            mUser.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUser, true);
                recyclerView.setAdapter(userAdapter);
                setRecyclerViewOnClick();

                userAdapter.setlastMessage(new LastMessageCallback() {
                    @Override
                    public void onLastMessageCallback(String userid, TextView last_msg) {
                        lastMessage(userid, last_msg);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setRecyclerViewOnClick() {
        userAdapter.SetOnClick(new ItemClickListener() {
            @Override
            public void onItemClickListener(String data) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("userid", data);
                getContext().startActivity(intent);
            }
        });
    }

    //callback userAdapter lastMessage

    private void lastMessage(final String userid, final TextView last_msg) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                switch (theLastMessage) {
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
