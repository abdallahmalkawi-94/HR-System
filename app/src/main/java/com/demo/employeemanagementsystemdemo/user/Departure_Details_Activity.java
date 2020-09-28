package com.demo.employeemanagementsystemdemo.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.employeemanagementsystemdemo.Login_Activity;
import com.demo.employeemanagementsystemdemo.R;
import com.demo.employeemanagementsystemdemo.adapter.Departure_Adapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class Departure_Details_Activity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView Departure_Details_List;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private Departure_Adapter adapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LinearLayout Edit_Dialog_Departure_Time;
    private TextView Edit_Dialog_Time_From_Txt , Edit_Dialog_Time_To_Txt;
    private EditText dialog_item;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_details);

        sharedPreferences = getSharedPreferences("User Date" , MODE_PRIVATE);
        editor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        Query query = firestore.collection("Departure");
        FirestoreRecyclerOptions<Departure_Request> options = new FirestoreRecyclerOptions.Builder<Departure_Request>().setQuery(query , Departure_Request.class).build();

        adapter = new Departure_Adapter(options , this);
        Departure_Details_List = findViewById(R.id.Departure_Details_List);
        Departure_Details_List.setLayoutManager(new LinearLayoutManager(this));
        Departure_Details_List.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove (@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target){
                return false;
            }

            @Override
            public void onSwiped ( @NonNull final RecyclerView.ViewHolder viewHolder, final int direction){
                if (direction == ItemTouchHelper.RIGHT) {
                    AlertDialog.Builder deleteitem = new AlertDialog.Builder(Departure_Details_Activity.this);
                    deleteitem.setMessage("Are you sure delete departure?")
                            .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    adapter.deleteItem(viewHolder.getAdapterPosition());
                                    Toast.makeText(getApplicationContext() , getString(R.string.delete_departure) , Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        startActivity(new Intent(Departure_Details_Activity.this, Departure_Details_Activity.class));
                                        finish();
                                    }
                            });
                    deleteitem.create().show();
                } else {
                    final AlertDialog.Builder edit_dialog = new AlertDialog.Builder(Departure_Details_Activity.this);
                    final View view_dialog = LayoutInflater.from(Departure_Details_Activity.this).inflate(R.layout.edit_dialog, null);

                    TextView edit_dialog_title = view_dialog.findViewById(R.id.Dialog_Title);
                    edit_dialog_title.setText("Update vacation date");
                    edit_dialog_title.setGravity(Gravity.LEFT);
                    edit_dialog_title.setTextSize(16);

                    Edit_Dialog_Departure_Time = view_dialog.findViewById(R.id.Edit_Dialog_Departure_Time);
                    Edit_Dialog_Departure_Time.setVisibility(View.VISIBLE);

                    Edit_Dialog_Time_From_Txt = view_dialog.findViewById(R.id.Edit_Dialog_Time_From_Txt);
                    Edit_Dialog_Time_From_Txt.setOnClickListener(Departure_Details_Activity.this);

                    Edit_Dialog_Time_To_Txt = view_dialog.findViewById(R.id.Edit_Dialog_Time_To_Txt);
                    Edit_Dialog_Time_To_Txt.setOnClickListener(Departure_Details_Activity.this);

                    dialog_item = view_dialog.findViewById(R.id.Dialog_Item);
                    dialog_item.setHint(getString(R.string.choose_date));
                    dialog_item.setFocusable(false);
                    dialog_item.setOnClickListener(Departure_Details_Activity.this);

                    edit_dialog.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (dialog_item.getText().toString().isEmpty() && (!Edit_Dialog_Time_From_Txt.getText().toString().isEmpty()) && !Edit_Dialog_Time_To_Txt.getText().toString().isEmpty())
                            {
                                adapter.updateTime(viewHolder.getAdapterPosition() , Edit_Dialog_Time_From_Txt.getText().toString() , Edit_Dialog_Time_To_Txt.getText().toString());
                                Toast.makeText(getApplicationContext() , "Time has been modified" , Toast.LENGTH_SHORT).show();
                            } else if (!dialog_item.getText().toString().isEmpty() && (Edit_Dialog_Time_From_Txt.getText().toString().isEmpty() && Edit_Dialog_Time_To_Txt.getText().toString().isEmpty()))
                            {
                               adapter.updateDate(viewHolder.getAdapterPosition() , dialog_item.getText().toString());
                                Toast.makeText(getApplicationContext() , getString(R.string.successfully_update_date) , Toast.LENGTH_SHORT).show();
                            } else if (!dialog_item.getText().toString().isEmpty() && !Edit_Dialog_Time_From_Txt.getText().toString().isEmpty() && !Edit_Dialog_Time_To_Txt.getText().toString().isEmpty())
                            {
                                adapter.updateTime(viewHolder.getAdapterPosition() , Edit_Dialog_Time_From_Txt.getText().toString() , Edit_Dialog_Time_To_Txt.getText().toString());
                                adapter.updateDate(viewHolder.getAdapterPosition() , dialog_item.getText().toString());
                                Toast.makeText(getApplicationContext() , "Departure has been updated" , Toast.LENGTH_SHORT).show();
                            } else
                                dialogInterface.cancel();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            startActivity(new Intent(Departure_Details_Activity.this, Departure_Details_Activity.class));
                            finish();
                        }
                    });
                    edit_dialog.setView(view_dialog).create().show();
                }
            }
        }
        ).attachToRecyclerView(Departure_Details_List);
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
                startActivity(new Intent(Departure_Details_Activity.this , User_Main_Activity.class));
                Animatoo.animateSwipeRight(this);
                finish();
                return true;

            case R.id.Settings_Item:
                startActivity(new Intent(Departure_Details_Activity.this , Settings_Activity.class));
                Animatoo.animateSwipeLeft(this);
                finish();
                return true;

            case R.id.Help_Item:
                return true;

            case R.id.Logout_Item:
                firebaseAuth.signOut();
                editor.clear();
                editor.apply();
                startActivity(new Intent(Departure_Details_Activity.this , Login_Activity.class));
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
        startActivity(new Intent(Departure_Details_Activity.this , Details_Activity.class));
        Animatoo.animateSwipeRight(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.Edit_Dialog_Time_From_Txt:
                getTime("update-from");
                break;
            case R.id.Edit_Dialog_Time_To_Txt:
                getTime("update-to");
                break;
            case R.id.Dialog_Item:
                getDate();
                break;
        }
    }

    private void getDate()
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(Departure_Details_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dialog_item.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
            } // end onDateSet()
        }, year, month, day);
        datePickerDialog.show();
    }

    private void getTime(final String period) {
        timePickerDialog = new TimePickerDialog(Departure_Details_Activity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                switch (period)
                {
                    case "update-from":
                        Edit_Dialog_Time_From_Txt.setText(String.format("%02d:%02d" , i , i1));
                        break;
                    case "update-to":
                        Edit_Dialog_Time_To_Txt.setText(String.format("%02d:%02d" , i , i1));
                        break;
                } // end switch()
            }
        } , 0 , 0 , true);
        timePickerDialog.show();
    } // end getTime()

} // end class