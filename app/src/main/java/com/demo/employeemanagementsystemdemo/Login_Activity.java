package com.demo.employeemanagementsystemdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.employeemanagementsystemdemo.user.User_Main_Activity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login_Activity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout login_by_google , login_by_microsoft;
    private EditText edit_email, edit_password , edit_rest_email;
    private String email , password;
    private Button login_btn , cancel_btn;
    private TextView txt_signup , txt_forget_password;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DocumentReference reference;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        defineItems();
    } // end onCreate()

    private void defineItems() {
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserID" , MODE_PRIVATE);
        editor = sharedPreferences.edit();
        firestore = FirebaseFirestore.getInstance();
        LoginByGoogle();

        login_by_google = findViewById(R.id.Login_By_Google);
        login_by_google.setOnClickListener(this);

        login_by_microsoft = findViewById(R.id.Login_By_Yahoo);
        login_by_microsoft.setOnClickListener(this);

        edit_email = findViewById(R.id.Login_Email);
        edit_password = findViewById(R.id.Login_Password);

        login_btn = findViewById(R.id.Login_Btn);
        login_btn.setOnClickListener(this);

        cancel_btn = findViewById(R.id.Login_Cancel_Btn);
        cancel_btn.setOnClickListener(this);

        txt_signup = findViewById(R.id.Signup);
        txt_signup.setOnClickListener(this);

        txt_forget_password = findViewById(R.id.Forget_Password);
        txt_forget_password.setOnClickListener(this);
    } // end defineItems()

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.Login_By_Google:
                signInByGoogle();
                break;
            case R.id.Login_By_Yahoo:
                break;
            case R.id.Login_Btn:
                gotoUserActivity();
                break;
            case R.id.Login_Cancel_Btn:
                edit_email.setText("");
                edit_password.setText("");
                break;
            case R.id.Signup:
                startActivity(new Intent(this , Signup_Activity.class));
                finish();
                break;
            case R.id.Forget_Password:
                restPassword();
                break;
        } // end switch()
    } // end onClick()

    private void LoginByGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    } // end LoginByGoogle()

    private void signInByGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    } // end signIn

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext() , getString(R.string.error) + ":- " + e.getMessage() , Toast.LENGTH_LONG).show();
            } // end catch()
        } // end if()
        super.onActivityResult(requestCode, resultCode, data);
    } // end onActivityResult()

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext() , R.string.loged_in_successful , Toast.LENGTH_SHORT).show();
                    reference = firestore.collection("Users").document(user.getUid());
                    reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            editor.putString("userid" , user.getUid());
                            editor.putString("Full Name" , documentSnapshot.getString("full_Name") );
                            editor.putString("Email" , documentSnapshot.getString("email"));
                            editor.putString("Joining Date" , documentSnapshot.getString("joining_Date") );
                            editor.putString("Designation" , documentSnapshot.getString("designation") );
                            editor.putString("Phone" , documentSnapshot.getString("phone"));
                            editor.apply();
                        } // end onSuccess()
                    });
                    startActivity(new Intent(Login_Activity.this , User_Main_Activity.class));
                    finish();
                }else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getApplicationContext() , task.getException().getMessage() , Toast.LENGTH_LONG).show();
                } // end else
            } // end onComplete
        }); // end addOnCompleteListener()
    } // end firebaseAuthWithGoogle()

    private void restPassword() {
         AlertDialog.Builder restpassworddialog = new AlertDialog.Builder(this);
         View viewDialog = LayoutInflater.from(this).inflate(R.layout.rest_password_dialog , null);
        edit_rest_email = viewDialog.findViewById(R.id.Rest_Email);
        restpassworddialog.setView(viewDialog).create();

        restpassworddialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                email = edit_rest_email.getText().toString().trim();
                if (email.isEmpty())
                {
                    edit_rest_email.setError(getString(R.string.required));
                } // end if()
                else
                {
                    firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext() , getString(R.string.reset_link_send_to_your_email) , Toast.LENGTH_LONG).show();
                        } // end onSuccess()
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext() , getString(R.string.error )+ e.getMessage() , Toast.LENGTH_LONG).show();
                        } // end onFailure()
                    }); // end addOnFailureListener()
                } // end else
            } // end onClick
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            } // end onClick()
        }); // end setNegativeButton()

        restpassworddialog.show();
    } // end restPassword()

    private void gotoUserActivity() {
        try {
            if (checkUserData())
            {
                firebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext() , R.string.loged_in_successful , Toast.LENGTH_SHORT).show();
                            reference = firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
                            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                      editor.putString("userid" , firebaseAuth.getCurrentUser().getUid());
                                      editor.putString("Full Name" , documentSnapshot.getString("full_Name") );
                                      editor.putString("Email" , documentSnapshot.getString("email"));
                                      editor.putString("Joining Date" , documentSnapshot.getString("joining_Date") );
                                      editor.putString("Designation" , documentSnapshot.getString("designation") );
                                      editor.putString("Phone" , documentSnapshot.getString("phone"));
                                      editor.apply();
                                } // end onSuccess()
                            });
                            startActivity(new Intent(Login_Activity.this , User_Main_Activity.class));
                            finish();
                        } // end if()
                        else
                            Toast.makeText(getApplicationContext() , getString(R.string.error) + " " + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                    } // end onComplete()
                }); // end addOnCompleteListener()
            } // end if()
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext() , getString(R.string.error) + " " + e.getMessage() , Toast.LENGTH_LONG).show();
        } // end catch()
    } // end gotoUserActivity()

    private boolean checkUserData() {
        email = edit_email.getText().toString().trim();
        password = edit_password.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty())
        {
            if (TextUtils.isEmpty(email))
                edit_email.setError(getString(R.string.required));
            if (TextUtils.isEmpty(password))
                edit_password.setError(getString(R.string.required));
            return false;
        }
        else
        return true;
    } // end checkUserData()

    @Override
    public void onBackPressed() {
        finish();
    } // end onBackPressed()
} // end class
