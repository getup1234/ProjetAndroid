package com.example.projet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddDemande extends AppCompatActivity {
    private EditText raisonEditText, statusEditText;
    String selectedDoc;
    String userId;
    private Button saveButton;

    private DatabaseReference databaseReference;
    private static final String[] DOCS_ARRAY = {"Doc1", "Doc2", "Doc3"};
    Button btnLogOut;
    private TextView UserEmail;
    FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demande);

        //this.UserEmail.setText("aymen");

        btnLogOut = findViewById(R.id.btnLogout);
        UserEmail = findViewById(R.id.UserEmail);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        UserEmail.setText( user.getEmail());

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(AddDemande.this, LoginActivity.class));
        });

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Log.w(TAG, userId);
        ListView listViewDocs = findViewById(R.id.listViewDocs);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_single_choice,
                DOCS_ARRAY
        );

        listViewDocs.setAdapter(adapter);
        listViewDocs.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewDocs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDoc = DOCS_ARRAY[position];

            }
        });





        // Initialize views
        raisonEditText = findViewById(R.id.raisonEditText);
        saveButton = findViewById(R.id.buttonSave);

        // Save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        String raison = raisonEditText.getText().toString().trim();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


            // Create a new Demande object
            Demande demande = new Demande(selectedDoc, "En attente",userId,raison);
        Log.w(TAG, demande.toString());
        Map<String, Object> demande1 = new HashMap<>();
        demande1.put("name", selectedDoc);
        demande1.put("status", "En attente");
        demande1.put("userId", userId);
        demande1.put("raison", raison);
        db.collection("demandes")
                .add(demande)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        startActivity(new Intent(AddDemande.this, MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });





    }
}
