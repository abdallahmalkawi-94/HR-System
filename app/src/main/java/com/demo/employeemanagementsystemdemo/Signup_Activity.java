package com.demo.employeemanagementsystemdemo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class Signup_Activity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "TAG";
    private EditText edit_name , edit_email , edit_password , edit_confirm_Password , edit_joining_date;
    private String name , email , password , confirm_password , joining_date , UserId;
    private Button signup_btn , cancel_btn;
    private Spinner designation;
    private ArrayAdapter<CharSequence> adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        defineItems();

    } // end onCreate()

    private void defineItems() {

        sharedPreferences = getSharedPreferences("User Date" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edit_name = findViewById(R.id.Signup_Name);
        edit_email = findViewById(R.id.Signup_Email);
        edit_joining_date = findViewById(R.id.Signup_Joining_Date);
        edit_password = findViewById(R.id.Signup_Password);
        edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edit_confirm_Password = findViewById(R.id.Signup_Confirm_Password);
        edit_confirm_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        designation = findViewById(R.id.Signup_Designation_Spin);
        adapter = ArrayAdapter.createFromResource(this , R.array.position_array , R.layout.spinner_item_design);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designation.setAdapter(adapter);

        signup_btn = findViewById(R.id.Signup_Btn);
        signup_btn.setOnClickListener(this);

        cancel_btn = findViewById(R.id.Signup_Cancel_Btn);
        cancel_btn.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    } // end defineItems()

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.Signup_Btn:
                startRegister();
                break;
            case R.id.Signup_Cancel_Btn:
                edit_name.setText("");
                edit_email.setText("");
                edit_password.setText("");
                edit_confirm_Password.setText("");
                break;
        } // end switch()
    } // end onClick()

    private void startRegister() {
        try {
            if (checkEnterData())
            {
                firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            UserId = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("Users").document(UserId);
                            Users users = new Users(name , email , "Not Selected Yet" , joining_date , designation.getSelectedItem().toString());
                            documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    successfulSignUp();
                                } // end onSuccess()
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, getString(R.string.error) + " "+e.getMessage());
                                } // end onFailure()
                            });

                        } // end if()
                        else
                        {
                            Toast.makeText(getApplicationContext() , getString(R.string.error) + " " + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                        } // end else
                    } // end onComplete()
                }); // end createUserWithEmailAndPassword()
            } // end if()
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext() , getString(R.string.error) + " " + e.getMessage() , Toast.LENGTH_LONG).show();
        } // end catch()
    } // end startRegister()

    private void successfulSignUp() {
        firebaseAuth.signOut();
        AlertDialog.Builder successful_registered_builder = new AlertDialog.Builder(this);
        successful_registered_builder.setMessage(getString(R.string.successfully_registered_part1) + getString(R.string.successfully_registered_part2));
        Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Signup_Activity.this , Login_Activity.class));
                Animatoo.animateSplit(Signup_Activity.this);
                finish();
            }
        } ,3000);
        successful_registered_builder.create().show();
    }

    private boolean checkEnterData() {
        name = edit_name.getText().toString().trim();
        email = edit_email.getText().toString().trim();
        password = edit_password.getText().toString().trim();
        confirm_password = edit_confirm_Password.getText().toString().trim();
        joining_date = edit_joining_date.getText().toString().trim();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty())
        {
            if (TextUtils.isEmpty(name))
            {
                edit_name.setError(getString(R.string.required));
            } // end if()
            if (TextUtils.isEmpty(email))
            {
                edit_email.setError(getString(R.string.required));
            } // end if()
            if (TextUtils.isEmpty(password))
            {
                edit_password.setError(getString(R.string.required));
            } // end if()
            if (TextUtils.isEmpty(confirm_password))
            {
                edit_confirm_Password.setError(getString(R.string.required));
            } // end if()
            return false;
        } // end if()
        else if (!confirm_password.equals(password)) {
            edit_password.setError(getString(R.string.password_not_matched));
            edit_confirm_Password.setError(getString(R.string.password_not_matched));
            return false;
        } // end else if()
        else
        return true;
    } // end checkEnterData()

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Signup_Activity.this , Login_Activity.class));
        Animatoo.animateSplit(this);
        finish();
    } // end onBackPressed()
} // end class
