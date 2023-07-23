package com.example.projet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;
    private TextView UserEmail;
    FirebaseAuth mAuth;
    private TextView dataTextView;
    private Button buttonAddDemande;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

        //this.UserEmail.setText("aymen");

        btnLogOut = findViewById(R.id.btnLogout);
        UserEmail = findViewById(R.id.UserEmail);

        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });


        // Initialize views
        buttonAddDemande = ( Button ) findViewById(R.id.buttonAddDemande);

        // Retrieve demandes data from Firebase
        this.retrieveData();

        // Add Demande button click listener
        buttonAddDemande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                Intent intent = new Intent(MainActivity.this, AddDemande.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    private void retrieveData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){

            UserEmail.setText( user.getEmail());
            // Get the authenticated user's ID
            String userId = user.getUid();


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Log.d(TAG,        db.collection("demandes").get().toString());

            db.collection("demandes")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                RelativeLayout relativeLayout = findViewById(R.id.textViewData); // Replace with your RelativeLayout ID
                                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                linearLayout.setOrientation(LinearLayout.VERTICAL);

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> doc = document.getData();

                                    for (Map.Entry<String, Object> entry : doc.entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();

                                        if (key.equals("userId")) {
                                            continue; // Skip this value and move to the next iteration
                                        }

                                        else if(key.equals("status")){

                                            TextView textView = new TextView(getApplicationContext());
                                            textView.setText(value.toString());
                                            if(value.toString().equals("En attente"))
                                                textView.setTextColor(Color.YELLOW);
                                            else if(value.toString().equals("Réfusé"))
                                                textView.setTextColor(Color.RED);
                                            else if(value.toString().equals("Approuvé")){
                                                linearLayout.addView(textView);

                                                textView.setTextColor(Color.GREEN);
                                                Button button1 = new Button(getApplicationContext());
                                                button1.setText("Telecharger");
                                                linearLayout.addView(button1);
                                                continue;

                                            }
                                            linearLayout.addView(textView);


                                        }
                                        else if(key.equals("name")){

                                            TextView textView = new TextView(getApplicationContext());
                                            textView.setText("Docuement demandé : "+value.toString());
                                            linearLayout.addView(textView);
                                        }
                                        else if(key.equals("raison")){

                                            TextView textView = new TextView(getApplicationContext());
                                            textView.setText("Raison : "+value.toString());
                                            linearLayout.addView(textView);
                                        }

                                    }

                                    // Add a separator between each document
                                    View separator = new View(getApplicationContext());
                                    separator.setBackgroundColor(Color.BLACK);
                                    separator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                                    linearLayout.addView(separator);
                                }

                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                relativeLayout.addView(linearLayout, layoutParams);
                            }else {

                            }
                        }
                    });
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}