package layout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schooler.schoolerapplication.ChatData;
import com.schooler.schoolerapplication.EditInfoActivity;
import com.schooler.schoolerapplication.MakeCardActivity;
import com.schooler.schoolerapplication.R;
import com.schooler.schoolerapplication.datamodel.MyInfo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ChatingFragment extends Fragment {
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

    public ChatingFragment() {
    }

    public static ChatingFragment newInstance() {
        ChatingFragment fragment = new ChatingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.activity_chatting, null);
        scrollView = (ScrollView)fragment.findViewById(R.id.scrollView);
        sendButton = (ImageButton)fragment.findViewById(R.id.button_send);
        mMessage = (EditText) fragment.findViewById(R.id.content_message);
        chattingView = (LinearLayout) fragment.findViewById(R.id.chattings);
        chattingView.bringToFront();
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(getContext().getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        cachedChatData = new ArrayList<ChatData>();
        room = "group";
        name = realm.where(MyInfo.class).findFirst().getName();
        multiChatting = true;
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
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
