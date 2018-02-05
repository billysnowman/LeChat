package com.example.andresr.lechat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * First app open
 * 1. blank SP
 * 2. MainActivty avec SP vide
 *
 *
 *
 * Display a simple chat connected to Firebase
 */
public class MainActivity extends AppCompatActivity implements ValueEventListener, MessageAdapter.Listener {




    public static final String TAG = MainActivity.class.getSimpleName();

    EditText       mInputEditText;
    ImageButton    mSendButton;
    MessageAdapter mMessageAdapter;
    Message leMessage;
    String newmessage = "NearOne c'est le plus beau";
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!UserStorage.isUserLoggedIn(this)) {
            Intent intent = new Intent(this, NamePickerActivity.class);
            this.startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mInputEditText = (EditText) findViewById(R.id.inputEditText);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);

        mMessageAdapter = new MessageAdapter(this, new ArrayList<Message>());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mMessageAdapter);

        connectAndListenToFirebase();

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitNewMessage(mInputEditText.getText().toString());
                mInputEditText.setText("");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Message> items = new ArrayList<>();
        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
            Message message = messageSnapshot.getValue(Message.class);
            message.key = messageSnapshot.getKey();
            items.add(message);
        }
        mMessageAdapter.setData(items);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(this, "Error: " + databaseError, Toast.LENGTH_SHORT).show();

    }

    private void connectAndListenToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference(Constant.FIREBASE_PATH);

        mDatabaseReference.addValueEventListener(this);
    }

    private void submitNewMessage(String message) {
        User user = UserStorage.getUserInfo(this);
        if (message.isEmpty() || user == null) {
            return;
        }
        DatabaseReference newData = mDatabaseReference.push();
        newData.setValue(
                new Message(message,
                        user.name,
                        user.email,
                        System.currentTimeMillis()));
    }

    @Override
    public void onItemClick(int position, Message message) {

        leMessage = message;

        String choix[] = getResources().getStringArray(R.array.popup);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Faite un choix");
        builder.setItems(choix, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mDatabaseReference.child(leMessage.key).removeValue();
                }
                if (which == 1) {
                    mDatabaseReference.child(leMessage.key).setValue(new Message(leMessage.key, newmessage ,leMessage.userName,leMessage.userEmail,leMessage.timestamp));
                }
            }
        });
        builder.show();
    }
}
