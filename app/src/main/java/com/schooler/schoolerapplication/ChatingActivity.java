package com.schooler.schoolerapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatingActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private LinearLayout chattingView;
    private ScrollView scrollView;
    private EditText mMessage;
    private ImageButton sendButton;

    private List<ChatData> cachedChatData;

    private boolean multiChatting=false;
    private int offset = 0;
    private String room,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        init();
    }

    private void init(){
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        sendButton = (ImageButton)findViewById(R.id.button_send);
        mMessage = (EditText) findViewById(R.id.content_message);
        chattingView = (LinearLayout) findViewById(R.id.chattings);
        cachedChatData = new ArrayList<ChatData>();
        room = getIntent().getExtras().getString("room");
        name = getIntent().getExtras().getString("name");
        multiChatting = getIntent().getBooleanExtra("Multi", false);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatData chatData = new ChatData();
                chatData.setContent(mMessage.getText().toString());
                chatData.setId(name);
                cachedChatData.add(chatData);
                databaseReference.child("room").child(room).setValue(cachedChatData);
                mMessage.setText("");
            }
        });
        databaseReference.child("room").child(room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                Iterable<DataSnapshot> chattings = dataSnapshots.getChildren();
                int min = cachedChatData.size();
                for(DataSnapshot dataSnapshot : chattings){
                    if(min>0) {
                        min--;
                        continue;
                    }
                    cachedChatData.add(dataSnapshot.getValue(ChatData.class));
                }
                loadChatData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void loadChatData(){
        if(offset<cachedChatData.size()){
            for(int i = offset;i<cachedChatData.size();i++){
                chattingView.addView(addChat(cachedChatData.get(i)));
            }
            offset = cachedChatData.size();
        }
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.smoothScrollTo(0, chattingView.getBottom());
            }
        });
    }

    private View addChat(ChatData chatData){
        LayoutInflater inflater = LayoutInflater.from(chattingView.getContext());
        View card;
        if(chatData.getId().equals(name)) card =inflater.inflate(R.layout.chat_right, null);
        else card =inflater.inflate(R.layout.chat_left, null);
        ((TextView) card.findViewById(R.id.content)).setText(chatData.getContent());
        ((TextView) card.findViewById(R.id.name)).setText(chatData.getId());
        return card;
    }
}
