package com.demo.employeemanagementsystemdemo.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.employeemanagementsystemdemo.Login_Activity;
import com.demo.employeemanagementsystemdemo.R;
import com.demo.employeemanagementsystemdemo.adapter.Vacation_Adapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class Vacation_Details_Activity extends AppCompatActivity {

    private RecyclerView vacation_details_list;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private Vacation_Adapter adapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("User Date" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Query query = firestore.collection("Vacation");
        FirestoreRecyclerOptions<Vacation_Request> options = new FirestoreRecyclerOptions.Builder<Vacation_Request>().setQuery(query , Vacation_Request.class).build();
        adapter = new Vacation_Adapter(options , this);

        vacation_details_list = findViewById(R.id.Vacation_Details_List);
        vacation_details_list.setLayoutManager(new LinearLayoutManager(this));
        vacation_details_list.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {
                if (direction == ItemTouchHelper.RIGHT)
                {
                    AlertDialog.Builder deleteitem = new AlertDialog.Builder(Vacation_Details_Activity.this);
                    deleteitem.setMessage("Are you sure delete vacation?")
                            .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    adapter.deleteItem(viewHolder.getAdapterPosition());
                                    Toast.makeText(getApplicationContext() , getString(R.string.delete_vacation) , Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    startActivity(new Intent(Vacation_Details_Activity.this , Vacation_Details_Activity.class));
                                    finish();
                                }
                            });
                    deleteitem.create().show();
                } else
                {
                    final AlertDialog.Builder edit_dialog = new AlertDialog.Builder(Vacation_Details_Activity.this);
                    final View view_dialog = LayoutInflater.from(Vacation_Details_Activity.this).inflate(R.layout.edit_dialog , null);

                    TextView edit_dialog_title = view_dialog.findViewById(R.id.Dialog_Title);
                    edit_dialog_title.setText("Update vacation date");
                    edit_dialog_title.setGravity(Gravity.LEFT);
                    edit_dialog_title.setTextSize(16);

                    final EditText dialog_item = view_dialog.findViewById(R.id.Dialog_Item);
                    dialog_item.setHint(getString(R.string.choose_date));
                    dialog_item.setFocusable(false);
                    dialog_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int month = calendar.get(Calendar.MONTH);
                            int year = calendar.get(Calendar.YEAR);
                            // date picker dialog
                            DatePickerDialog datePickerDialog = new DatePickerDialog(Vacation_Details_Activity.this, new DatePickerDialog.OnDateSetListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    dialog_item.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
                                } // end onDateSet()
                            }, year, month, day);
                            datePickerDialog.show();
                        }
                    });

                    edit_dialog.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.updateDate(viewHolder.getAdapterPosition() , dialog_item.getText().toString());
                            Toast.makeText(getApplicationContext() , getString(R.string.successfully_update_date) , Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        startActivity(new Intent(Vacation_Details_Activity.this , Vacation_Details_Activity.class));
                        finish();
                    }
                });
                    edit_dialog.setView(view_dialog).create().show();
                }
            }
        }).attachToRecyclerView(vacation_details_list);

    } // end onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu , menu);
        return true;
    } // end onCreateOptionsMenu()

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem home_item = menu.findItem(R.id.Home_Item);
        home_item.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Home_Item:
                startActivity(new Intent(Vacation_Details_Activity.this , User_Main_Activity.class));
                Animatoo.animateSwipeRight(this);
                finish();
                return true;

            case R.id.Settings_Item:
                startActivity(new Intent(Vacation_Details_Activity.this , Settings_Activity.class));
                Animatoo.animateSwipeLeft(this);
                finish();
                return true;

            case R.id.Help_Item:
                return true;

            case R.id.Logout_Item:
                firebaseAuth.signOut();
                editor.clear();
                editor.apply();
                startActivity(new Intent(Vacation_Details_Activity.this , Login_Activity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } // end switch()
    } // end onOptionsItemSelected()

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Vacation_Details_Activity.this , Details_Activity.class));
        Animatoo.animateSwipeRight(this);
    }
} // end class