package com.example.chatfirebase.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.MenuPagerAdapter;
import com.example.chatfirebase.constant.FragmentTag;
import com.example.chatfirebase.model.Chat;
import com.example.chatfirebase.model.User;
import com.example.chatfirebase.ui.fragment.chat_fragment.ChatsFragment;
import com.example.chatfirebase.ui.fragment.profile_fragment.ProfileFragment;
import com.example.chatfirebase.ui.fragment.user_fragment.UsersFragment;
import com.example.chatfirebase.ui.login.LoginActivity;

import com.example.chatfirebase.util.ActivityTransition;
import com.example.chatfirebase.util.dialog_manager.DialogClickListener;
import com.example.chatfirebase.util.dialog_manager.DialogPopup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView textView_username;
    private ActionBar actionBar;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private User user;
    private MenuPagerAdapter menuPagerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        setTabBar();

        setView();

        setOnClick();

        setInit();

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.tobar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void setTabBar() {

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabbar_laout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_page);

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()) {
                        unread++;
                    }
                }
                if (unread == 0) {
                    menuPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                } else {
                    menuPagerAdapter.addFragment(new ChatsFragment(), "(" + unread + ") Chats");
                }
                menuPagerAdapter.addFragment(new UsersFragment(), "Users");
                menuPagerAdapter.addFragment(new ProfileFragment(), "Profile");

                viewPager.setAdapter(menuPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setView() {
        profile_image = (CircleImageView) findViewById(R.id.profile_imge);
        textView_username = (TextView) findViewById(R.id.main_username);
    }

    private void setOnClick() {

    }

    private void setInit() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid());
        setInitReference();
    }

    private void setInitReference() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                textView_username.setText(user.getUsername());
                if (user.getImgURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);

                } else {
                    //change this
                    Glide.with(getApplicationContext()).load(user.getImgURL()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                logOut();
                return true;
        }
        return false;
    }

    private void status(String status) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    public void onBackPressed() {
        if (!checkFragmentOpen()) {
            DialogPopup dialogPopup = new DialogPopup(this);
            dialogPopup.dialogChoice(this, "แจ้งเตือน", "คุณต้องการออกจากระบบ");
            dialogPopup.setOnDialogListener(new DialogClickListener() {
                @Override
                public void onDialogClicked(int viewId) {
                    if (viewId == R.id.button_ok_choice_dialog) {
                        logOut();
                    }
                }
            });
        }

    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        ActivityTransition.GoLeft(MainActivity.this);
        finish();
    }

    private boolean checkFragmentOpen() {
        if (getSupportFragmentManager().findFragmentByTag(FragmentTag.ChatsFragment) != null ||
                getSupportFragmentManager().findFragmentByTag(FragmentTag.UsersFragment) != null ||
                getSupportFragmentManager().findFragmentByTag(FragmentTag.ProfileFragment) != null) {
            super.onBackPressed();
            return true;
        }
        return false;
    }
}
