package com.demo.employeemanagementsystemdemo.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.employeemanagementsystemdemo.Login_Activity;
import com.demo.employeemanagementsystemdemo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Settings_Activity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "TAG";
    private TextView txt_name , txt_email , txt_joining_date , txt_phone , txt_designation , txt_change_password , txt_change_language , txt_edit_phone , edit_dialog_title;
    private EditText dialog_item;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        defineItems();
    } // end onCreate()

    private void defineItems() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        sharedPreferences = getSharedPreferences("UserID" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        txt_name = findViewById(R.id.Settings_Name);
        txt_name.setText(getString(R.string.name)+":- " + sharedPreferences.getString("Full Name" , ""));

        txt_email = findViewById(R.id.Settings_Email);
        txt_email.setText(getString(R.string.email)+":- " + sharedPreferences.getString("Email" , ""));

        txt_joining_date = findViewById(R.id.Settings_Joining_Date);
        txt_joining_date.setText(getString(R.string.joining_date)+":- " + sharedPreferences.getString("Joining Date" , ""));

        txt_phone = findViewById(R.id.Settings_Phone);
        txt_phone.setText(getString(R.string.phone) + ":- " + sharedPreferences.getString("Phone" , ""));

        txt_designation = findViewById(R.id.Settings_Designation);
        txt_designation.setText(getString(R.string.designation) + ":- " + sharedPreferences.getString("Designation" , ""));

        txt_change_password = findViewById(R.id.Change_Password);
        txt_change_password.setOnClickListener(this);

        txt_change_language = findViewById(R.id.Change_Language);
        txt_change_language.setOnClickListener(this);

        txt_edit_phone = findViewById(R.id.Edit_Phone);
        txt_edit_phone.setOnClickListener(this);

    } // end defineItems()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu , menu);
        return true;
    } // end onCreateOptionsMenu()

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settings_item = menu.findItem(R.id.Settings_Item);
        settings_item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Home_Item:
                startActivity(new Intent(Settings_Activity.this , User_Main_Activity.class));
                finish();
                return true;
            case R.id.Help_Item:
                return true;

            case R.id.Logout_Item:
                    firebaseAuth.signOut();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(Settings_Activity.this , Login_Activity.class));
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
            case R.id.Change_Password:
                changePassword();
                break;
            case R.id.Change_Language:
                break;
            case R.id.Edit_Phone:
                editPhone();
                break;

        } // end switch()
    } // end onClick()

    private void editPhone() {
        AlertDialog.Builder edit_dialog = new AlertDialog.Builder(this);
        View view_dialog = LayoutInflater.from(this).inflate(R.layout.edit_dialog , null);
        edit_dialog_title = view_dialog.findViewById(R.id.Dialog_Title);
        dialog_item = view_dialog.findViewById(R.id.Dialog_Item);
        dialog_item.setInputType(InputType.TYPE_CLASS_PHONE);
        edit_dialog.setView(view_dialog).create();
        edit_dialog_title.setText(getString(R.string.edit_phone));
        dialog_item.setHint(R.string.edit_phone);

        edit_dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialog_item.getText().toString().isEmpty())
                {
                    dialog_item.setError(getString(R.string.required));
                } // end if()
                else
                {
                   firestore.collection("Users").document(user.getUid()).update("phone" , dialog_item.getText().toString())
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Toast.makeText(getApplicationContext() , getString(R.string.successfully_edit_phone) , Toast.LENGTH_LONG).show();
                           editor.putString("Phone" , dialog_item.getText().toString());
                           editor.apply();
                           txt_phone.setText(getString(R.string.phone) + ":- " + sharedPreferences.getString("Phone" , ""));
                       } // end onSuccess()
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(getApplicationContext() , e.getMessage() , Toast.LENGTH_LONG).show();
                       } // end ()
                   });

                } // end else
            } // end onClick()
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            } // end onClick()
        }); // end setNegativeButton()

        edit_dialog.show();
    } // end editPhone()

    private void changePassword() {
        AlertDialog.Builder edit_dialog = new AlertDialog.Builder(this);
        View view_dialog = LayoutInflater.from(this).inflate(R.layout.edit_dialog , null);
        edit_dialog_title = view_dialog.findViewById(R.id.Dialog_Title);
        dialog_item = view_dialog.findViewById(R.id.Dialog_Item);
        dialog_item.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        edit_dialog.setView(view_dialog).create();
        edit_dialog_title.setText(getString(R.string.change_password));
        dialog_item.setHint(R.string.new_password);

        edit_dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialog_item.getText().toString().isEmpty())
                {
                    dialog_item.setError(getString(R.string.required));
                } // end if()
                else
                {
                    user.updatePassword(dialog_item.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext() , R.string.password_changed_successfully , Toast.LENGTH_SHORT).show();
                        } // end onSuccess()
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext() ,getString(R.string.error ) + e.getMessage() , Toast.LENGTH_LONG).show();
                        } // end onFailure()
                    }); // end addOnFailureListener()
                } // end else
            } // end onClick()
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            } // end onClick()
        }); // end setNegativeButton()

        edit_dialog.show();
    } // end changePassword()
} // end class