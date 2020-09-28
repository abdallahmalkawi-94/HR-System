package com.demo.employeemanagementsystemdemo.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.employeemanagementsystemdemo.Login_Activity;
import com.demo.employeemanagementsystemdemo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User_Main_Activity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout v_d_r_btn , v_l_d_btn, salary_slip_btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private EditText edit_signup_joining_date , edit_signup_phone;
    private Spinner signup_designation_spin;
    private ArrayAdapter<CharSequence> adapter;
    private TextView edit_dialog_title;
    private EditText dialog_item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        sharedPreferences = getSharedPreferences("User Date" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        v_d_r_btn = findViewById(R.id.V_D_R_Btn);
        v_d_r_btn.setOnClickListener(this);

        v_l_d_btn = findViewById(R.id.V_L_D_Btn);
        v_l_d_btn.setOnClickListener(this);

        salary_slip_btn = findViewById(R.id.Salary_Slip_Btn);
        salary_slip_btn.setOnClickListener(this);
    } // end onCreate()

    @Override
    protected void onStart() {
        super.onStart();
        checkData();
        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                editor.putString("userid" , firebaseAuth.getCurrentUser().getUid());
                editor.putString("Full Name" , documentSnapshot.getString("full_Name") );
                editor.putString("Email" , documentSnapshot.getString("email"));
                editor.putString("Joining Date" , documentSnapshot.getString("joining_Date") );
                editor.putString("Designation" , documentSnapshot.getString("designation") );
                editor.putString("Phone" , documentSnapshot.getString("phone"));
                editor.apply();
            }
        });
    }

    private void checkData() {
        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("joining_Date").equals("Not Selected Yet")
                        && documentSnapshot.getString("designation").equals("Not Selected Yet"))
                {
                    completeInfoDialog(firebaseAuth.getCurrentUser().getUid());
                } else if (documentSnapshot.getString("phone").equals("Not Selected Yet"))
                {
                    editPhone(firebaseAuth.getCurrentUser().getUid());
                }
            }
        });
    } // end checkData()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu , menu);
        return true;
    } // end onCreateOptionsMenu()

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem home_item = menu.findItem(R.id.Home_Item);
        home_item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Settings_Item:
                startActivity(new Intent(User_Main_Activity.this , Settings_Activity.class));
                Animatoo.animateSwipeLeft(this);
                finish();
                return true;

            case R.id.Help_Item:
                return true;

            case R.id.Logout_Item:
                firebaseAuth.signOut();
                editor.clear();
                editor.apply();
                startActivity(new Intent(User_Main_Activity.this , Login_Activity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } // end switch()
    } // end onOptionsItemSelected()

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.V_D_R_Btn:
                startActivity(new Intent(User_Main_Activity.this , Vacation_Departure_Request_Activity.class));
                Animatoo.animateSwipeLeft(this);
                finish();
                break;
            case R.id.V_L_D_Btn:
                startActivity(new Intent(User_Main_Activity.this , Details_Activity.class));
                Animatoo.animateSwipeLeft(this);
                finish();
                break;
            case R.id.Salary_Slip_Btn:
                break;
        } // end switch()
    } // end onClick()

    private void completeInfoDialog(final String uid){
        AlertDialog.Builder completeInfo = new AlertDialog.Builder(this);
        View view_dilaog = LayoutInflater.from(this).inflate(R.layout.sign_up_dialog , null);
        edit_signup_joining_date = view_dilaog.findViewById(R.id.Signup_Dialog_Joining_Date);
        edit_signup_phone = view_dilaog.findViewById(R.id.Signup_Dialog_Phone);

        signup_designation_spin = view_dilaog.findViewById(R.id.Signup_Dialog_Designation_Spin);
        adapter = ArrayAdapter.createFromResource(this , R.array.position_array , R.layout.spinner_item_design);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        signup_designation_spin.setAdapter(adapter);

        completeInfo.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (edit_signup_joining_date.getText().toString().isEmpty() || edit_signup_phone.getText().toString().isEmpty())
                {
                    edit_signup_joining_date.setError(getString(R.string.required));
                    edit_signup_phone.setError(getString(R.string.required));
                } // end if()
                else
                {
                    firestore.collection("Users").document(uid).update("joining_Date" , edit_signup_joining_date.getText().toString());
                    firestore.collection("Users").document(uid).update("designation" , signup_designation_spin.getSelectedItem().toString());
                    firestore.collection("Users").document(uid).update("phone" , edit_signup_phone.getText().toString());
                    editor.putString("Joining Date" , edit_signup_joining_date.getText().toString());
                    editor.putString("Designation" , signup_designation_spin.getSelectedItem().toString());
                    editor.putString("Phone" , edit_signup_phone.getText().toString());
                    editor.apply();
                } // end else
            } // end onClick()
        }); // end setPositiveButton()

        completeInfo.setView(view_dilaog).create().show();
    } // end completeInfoDialog()

    private void editPhone(final String uid) {
        AlertDialog.Builder edit_dialog = new AlertDialog.Builder(this);
        View view_dialog = LayoutInflater.from(this).inflate(R.layout.edit_dialog , null);
        edit_dialog_title = view_dialog.findViewById(R.id.Dialog_Title);
        edit_dialog_title.setText(getString(R.string.phone));

        dialog_item = view_dialog.findViewById(R.id.Dialog_Item);
        dialog_item.setInputType(InputType.TYPE_CLASS_PHONE);
        dialog_item.setHint(getString(R.string.phone));

        edit_dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialog_item.getText().toString().isEmpty()) {
                    dialog_item.setError(getString(R.string.required));
                } // end if()
                else {
                    firestore.collection("Users").document(uid).update("phone", dialog_item.getText().toString());
                    editor.putString("Phone" , dialog_item.getText().toString());
                    editor.apply();
                } // end else
            } // end onClick()
        });
        edit_dialog.setView(view_dialog).create().show();
    } // end editPhone()

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(this);
        finish();
    }

} // end class
