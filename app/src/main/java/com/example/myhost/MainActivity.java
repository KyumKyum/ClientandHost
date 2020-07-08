package com.example.myhost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("List");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view_show);
        editText = findViewById(R.id.edit_text_target);
    }

    @Override
    protected void onStart() {
        super.onStart();

        colRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String data = "";

                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    Note note = queryDocumentSnapshot.toObject(Note.class);
                    note.setDocId(queryDocumentSnapshot.getId());

                    String docId = queryDocumentSnapshot.getId();
                    String email  = note.getEmail();
                    String password = note.getPassword();

                    data += "ID: " + docId + "\nE-mail: " + email + "\nPassword: " + password + "\n\n";

                }

                textView.setText(data);

            }
        });
    }

    public void loadNote(View v) {
        colRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            String docId = documentSnapshot.getId();

                            String email = note.getEmail();
                            String password = note.getPassword();

                            data += "ID: " + docId + "\nE-mail: " + email + "\nPassword: " + password + "\n\n";
                        }

                        textView.setText(data);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR! Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteNote(View v) {
        String target = editText.getText().toString();

        colRef.document(target).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR! Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
