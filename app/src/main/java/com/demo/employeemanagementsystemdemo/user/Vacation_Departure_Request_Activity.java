package com.demo.employeemanagementsystemdemo.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.employeemanagementsystemdemo.Login_Activity;
import com.demo.employeemanagementsystemdemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Vacation_Departure_Request_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView name_txt , phone_txt , designation_txt , vacation_date , departure_date , departure_time_from , departure_time_to , edit_dialog_title , Edit_Dialog_Time_From_Txt , Edit_Dialog_Time_To_Txt;
    private LinearLayout Edit_Dialog_Departure_Time;
    private EditText dialog_item;
    private Spinner request_type_spin;
    private ArrayAdapter<CharSequence> adapter;
    private Button submit_btn , cancel_btn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;


    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String DEFAULT_STATUS;
    private SimpleDateFormat sdf = new SimpleDateFormat("d / M / yyyy - hh:mm a");
    private Calendar calendar = Calendar.getInstance();
    private boolean test = true;
    private String V_D_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation__departure__request);
        defineItems();
        checkType();
    } // end onCreate()

    private void checkType() {
        try {
            request_type_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (request_type_spin.getSelectedItem().toString().equals("Vacation"))
                    {
                        vacation_date.setEnabled(true);
                        vacation_date.setBackgroundResource(R.drawable.selected_item_style);

                        departure_date.setEnabled(false);
                        departure_date.setBackgroundResource(R.drawable.edit_text_style);
                        departure_date.setText("");
                        departure_date.setError(null);

                        departure_time_from.setEnabled(false);
                        departure_time_from.setBackgroundResource(R.drawable.edit_text_style);
                        departure_time_from.setText("");
                        departure_time_from.setError(null);

                        departure_time_to.setEnabled(false);
                        departure_time_to.setBackgroundResource(R.drawable.edit_text_style);
                        departure_time_to.setText("");
                        departure_time_to.setError(null);

                    } // end if()
                    else if (request_type_spin.getSelectedItem().toString().equals("Departure"))
                    {
                        vacation_date.setEnabled(false);
                        vacation_date.setBackgroundResource(R.drawable.edit_text_style);
                        vacation_date.setText("");
                        vacation_date.setError(null);

                        departure_time_from.setEnabled(true);
                        departure_time_from.setBackgroundResource(R.drawable.selected_item_style);

                        departure_time_to.setEnabled(true);
                        departure_time_to.setBackgroundResource(R.drawable.selected_item_style);

                        departure_date.setEnabled(true);
                        departure_date.setBackgroundResource(R.drawable.selected_item_style);
                    } // end else if()
                    else
                    {
                        vacation_date.setEnabled(false);
                        vacation_date.setBackgroundResource(R.drawable.edit_text_style);
                        vacation_date.setText("");
                        vacation_date.setError(null);

                        departure_date.setEnabled(false);
                        departure_date.setBackgroundResource(R.drawable.edit_text_style);
                        departure_date.setText("");
                        departure_date.setError(null);

                        departure_time_from.setEnabled(false);
                        departure_time_from.setBackgroundResource(R.drawable.edit_text_style);
                        departure_time_from.setText("");
                        departure_time_from.setError(null);

                        departure_time_to.setEnabled(false);
                        departure_time_to.setBackgroundResource(R.drawable.edit_text_style);
                        departure_time_to.setText("");
                        departure_time_to.setError(null);
                    } // end else
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                } // end onNothingSelected()
            }); // end setOnItemSelectedListener()
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext() , e.getMessage() , Toast.LENGTH_LONG).show();
        } // end catch()

    } // end checkType()

    private void defineItems() {
        sharedPreferences = getSharedPreferences("User Date" , MODE_PRIVATE);
        editor = sharedPreferences.edit();
        DEFAULT_STATUS = getString(R.string.under_review);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        name_txt = findViewById(R.id.V_D_Name_Txt);
        name_txt.setText(getString(R.string.name)+":- " + sharedPreferences.getString("Full Name" , ""));

        phone_txt = findViewById(R.id.V_D_Phone_Txt);
        phone_txt.setText(getString(R.string.phone) + ":- " + sharedPreferences.getString("Phone" , ""));

        designation_txt = findViewById(R.id.V_D_Designation_Txt);
        designation_txt.setText(getString(R.string.designation) + ":- " + sharedPreferences.getString("Designation" , ""));

        vacation_date = findViewById(R.id.V_Date_Txt);
        vacation_date.setOnClickListener(this);

        departure_date = findViewById(R.id.D_Date_Txt);
        departure_date.setOnClickListener(this);

        departure_time_from = findViewById(R.id.D_Time_From_Txt);
        departure_time_from.setOnClickListener(this);

        departure_time_to = findViewById(R.id.D_Time_To_Txt);
        departure_time_to.setOnClickListener(this);

        request_type_spin = findViewById(R.id.Request_Type_Spin);
        adapter = ArrayAdapter.createFromResource(this , R.array.request_type_spin, R.layout.spinner_item_design);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        request_type_spin.setAdapter(adapter);

        submit_btn = findViewById(R.id.V_D_Submit_Btn);
        submit_btn.setOnClickListener(this);

        cancel_btn = findViewById(R.id.V_D_Cancel_Btn);
        cancel_btn.setOnClickListener(this);
    } // end defineItems()

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
                startActivity(new Intent(Vacation_Departure_Request_Activity.this , User_Main_Activity.class));
                Animatoo.animateSwipeRight(this);
                finish();
                return true;

            case R.id.Settings_Item:
                startActivity(new Intent(Vacation_Departure_Request_Activity.this , Settings_Activity.class));
                Animatoo.animateSwipeLeft(this);
                finish();
                return true;

            case R.id.Help_Item:
                return true;

            case R.id.Logout_Item:
                firebaseAuth.signOut();
                editor.clear();
                editor.apply();
                startActivity(new Intent(Vacation_Departure_Request_Activity.this , Login_Activity.class));
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
            case R.id.V_Date_Txt:
                getCalander('V');
                break;

            case R.id.D_Date_Txt:
                getCalander('D');
                break;

            case R.id.D_Time_From_Txt:
                getTime("from");
                break;

            case R.id.D_Time_To_Txt:
                getTime("to");
                break;

            case R.id.V_D_Submit_Btn:
                if (request_type_spin.getSelectedItem().toString().equals("Choose Type"))
                    Toast.makeText(getApplicationContext() , R.string.not_choose_request_message , Toast.LENGTH_SHORT).show();
                else
                    Submit(request_type_spin.getSelectedItem().toString());
                break;

            case R.id.V_D_Cancel_Btn:
               request_type_spin.setSelection(0);
                break;

            case R.id.Dialog_Item:
                getCalander('U');
                break;
            case R.id.Edit_Dialog_Time_From_Txt:
                getTime("update-from");
                break;
            case R.id.Edit_Dialog_Time_To_Txt:
                getTime("update-to");
                break;
        } // end switch()
    } // end onClick()

    private void Submit(String RT) {
        if (RT.equals("Vacation"))
        {
            if (vacation_date.getText().toString().isEmpty())
            {
                vacation_date.setError(getString(R.string.required));
            }
            else
                checkVacationDate(RT , "vacation_date");
        } // end if()
        else if (RT.equals("Departure"))
        {
            if (departure_date.getText().toString().isEmpty() || departure_time_from.getText().toString().isEmpty() || departure_time_to.getText().toString().isEmpty())
            {
                departure_date.setError(getString(R.string.required));
                departure_time_from.setError(getString(R.string.required));
                departure_time_to.setError(getString(R.string.required));
            }
            else
                checkDepartureDateTime(RT , "departure_date");
        } // end else
    } // end Submit()

    private void checkDepartureDateTime(final String RT , final String date_field) {
        firestore.collection("Departure").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
             for (QueryDocumentSnapshot documentSnapshot : task.getResult())
             {
                 if (documentSnapshot.getString("departure_date").equals(departure_date.getText().toString()) && documentSnapshot.getString("name").equals(sharedPreferences.getString("Full Name" , "")))
                 {
                     test = false;
                     V_D_id = documentSnapshot.getId();
                     break;
                 } else
                     test = true;
             }
             if (test)
                 insertNewDeparture();
             else
                 showDepartureWrongDialog(RT , date_field , V_D_id);
            } // end onComplete()
        }); // end addOnCompleteListener()
    } // end insertNewDeparture()

    private void showDepartureWrongDialog(final String RT, final String date_field, final String V_D_id) {
        final AlertDialog.Builder edit_dialog = new AlertDialog.Builder(this);
        final View view_dialog = LayoutInflater.from(this).inflate(R.layout.edit_dialog , null);

        edit_dialog_title = view_dialog.findViewById(R.id.Dialog_Title);
        edit_dialog_title.setText(R.string.update_departure_message);
        edit_dialog_title.setGravity(Gravity.LEFT);
        edit_dialog_title.setTextSize(16);

        dialog_item = view_dialog.findViewById(R.id.Dialog_Item);
        dialog_item.setHint(getString(R.string.choose_date));
        dialog_item.setFocusable(false);
        dialog_item.setOnClickListener(this);

        Edit_Dialog_Departure_Time = view_dialog.findViewById(R.id.Edit_Dialog_Departure_Time);
        Edit_Dialog_Departure_Time.setVisibility(View.VISIBLE);

        Edit_Dialog_Time_From_Txt = view_dialog.findViewById(R.id.Edit_Dialog_Time_From_Txt);
        Edit_Dialog_Time_From_Txt.setOnClickListener(this);

        Edit_Dialog_Time_To_Txt = view_dialog.findViewById(R.id.Edit_Dialog_Time_To_Txt);
        Edit_Dialog_Time_To_Txt.setOnClickListener(this);

        edit_dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                if (dialog_item.getText().toString().isEmpty() || Edit_Dialog_Time_From_Txt.getText().toString().isEmpty() || Edit_Dialog_Time_To_Txt.getText().toString().isEmpty() )
                {
                    dialog_item.setError(getString(R.string.required));
                    Edit_Dialog_Time_From_Txt.setError(getString(R.string.required));
                    Edit_Dialog_Time_To_Txt.setError(getString(R.string.required));
                }
                else
                {
                    firestore.collection("Departure").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                if (documentSnapshot.getString("departure_date").equals(dialog_item.getText().toString()) && documentSnapshot.getString("name").equals(sharedPreferences.getString("Full Name" , "")))
                                {
                                    test = false;
                                    break;
                                } else
                                    test = true;
                            } // end for()
                            if (test)
                            {
                                firestore.collection(RT).document(V_D_id).update(date_field , dialog_item.getText().toString());
                                firestore.collection(RT).document(V_D_id).update("from" , Edit_Dialog_Time_From_Txt.getText().toString());
                                firestore.collection(RT).document(V_D_id).update("to" , Edit_Dialog_Time_To_Txt.getText().toString());
                                Toast.makeText(getApplicationContext() , R.string.successfully_update_date , Toast.LENGTH_SHORT).show();
                            } // end fi()
                            else
                                Toast.makeText(getApplicationContext() , R.string.submit_vacation_before , Toast.LENGTH_SHORT).show();
                        } // end onComplete()
                    }); // end addOnCompleteListener()
                } // end else
            } // end onClick()
        }).setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firestore.collection(RT).document(V_D_id).delete();
                Toast.makeText(getApplicationContext() , R.string.delete_departure , Toast.LENGTH_SHORT).show();
                request_type_spin.setSelection(0);
                dialogInterface.cancel();
            }
        }).setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        edit_dialog.setView(view_dialog).create().show();

    }

    private void insertNewDeparture() {
        String departure_id = firestore.collection("Departure").document().getId();
        Departure_Request departure_request = new Departure_Request(departure_id
                ,sharedPreferences.getString("Full Name" , "")
                , sharedPreferences.getString("Phone" , "")
                , sharedPreferences.getString("Designation" , "")
                , departure_date.getText().toString()
                , departure_time_from.getText().toString()
                , departure_time_to.getText().toString()
                , sdf.format(calendar.getTime())
                , DEFAULT_STATUS);

        DocumentReference reference = firestore.collection("Departure").document(sharedPreferences.getString("Full Name" , "") + " - " + departure_id);
        reference.set(departure_request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext() , getString(R.string.under_review) , Toast.LENGTH_SHORT).show();
                    request_type_spin.setSelection(0);
                } else
                {
                    Toast.makeText(getApplicationContext() , task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                } // end else
            } // end onComplete()
        }); // end addOnCompleteListener()
    }

    private void insertNewVacation() {
        String vacation_id = firestore.collection("Vacation").document().getId();
        Vacation_Request vacation_request = new Vacation_Request(
                vacation_id
                , sharedPreferences.getString("Full Name" , "")
                , sharedPreferences.getString("Phone" , "")
                , sharedPreferences.getString("Designation" , "")
                , vacation_date.getText().toString()
                , sdf.format(calendar.getTime())
                , DEFAULT_STATUS);

        DocumentReference reference = firestore.collection("Vacation").document(sharedPreferences.getString("Full Name" , "") + " - " + vacation_id);
        reference.set(vacation_request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext() , getString(R.string.under_review) , Toast.LENGTH_SHORT).show();
                    request_type_spin.setSelection(0);
                } else
                {
                    Toast.makeText(getApplicationContext() , task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                } // end else
            } // end onComplete()
        }); // end addOnCompleteListener()
    } // end insertNewVacation()

    private void checkVacationDate(final String RT , final String date_field) {
       firestore.collection(RT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               for (QueryDocumentSnapshot documentSnapshot : task.getResult())
               {
                   if (documentSnapshot.get(date_field).equals(vacation_date.getText().toString()) && documentSnapshot.get("name").equals(sharedPreferences.getString("Full Name" , "")))
                   {
                       test = false;
                       V_D_id = documentSnapshot.getId();
                       break;
                   } // end if()
                   else
                   {
                       test = true;
                   }
               } // end for()

               if (test)
                   insertNewVacation();
               else
                   showVacationWrongDialog(RT , date_field ,  V_D_id);
           }
       });
    } // end checkTheDate()

    private void showVacationWrongDialog(final String RT , final String date_field , final String V_ID) {
        final AlertDialog.Builder edit_dialog = new AlertDialog.Builder(this);
        final View view_dialog = LayoutInflater.from(this).inflate(R.layout.edit_dialog , null);

        edit_dialog_title = view_dialog.findViewById(R.id.Dialog_Title);
        edit_dialog_title.setText(R.string.update_vacation_message);
        edit_dialog_title.setGravity(Gravity.LEFT);
        edit_dialog_title.setTextSize(16);

        dialog_item = view_dialog.findViewById(R.id.Dialog_Item);
        dialog_item.setHint(getString(R.string.choose_date));
        dialog_item.setFocusable(false);
        dialog_item.setOnClickListener(this);

        edit_dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                if (dialog_item.getText().toString().isEmpty())
                    dialog_item.setError(getString(R.string.required));
                else
                {
                    firestore.collection(RT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                if (documentSnapshot.get(date_field).equals(dialog_item.getText().toString()) && documentSnapshot.get("name").equals(sharedPreferences.getString("Full Name" , "")))
                                {
                                    test = false;
                                    break;
                                } // end if()
                                else
                                {
                                    test = true;
                                }
                            } // end for()
                            if (test)
                               {
                                   firestore.collection(RT).document(V_ID).update(date_field , dialog_item.getText().toString());
                                   Toast.makeText(getApplicationContext() , R.string.successfully_update_date, Toast.LENGTH_SHORT).show();
                                   request_type_spin.setSelection(0);
                                   dialogInterface.cancel();
                               }
                            else
                            {
                              Toast.makeText(getApplicationContext() , R.string.submit_vacation_before , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firestore.collection(RT).document(V_ID).delete();
                Toast.makeText(getApplicationContext() , R.string.delete_vacation , Toast.LENGTH_SHORT).show();
                request_type_spin.setSelection(0);
                dialogInterface.cancel();
            }
        }).setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        edit_dialog.setView(view_dialog).create().show();

    } // end showWrongDialog()

    private void getCalander(final char type){
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(Vacation_Departure_Request_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
               switch (type)
               {
                   case 'V':
                       vacation_date.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
                       vacation_date.setError(null);
                       break;
                   case 'D':
                       departure_date.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
                       departure_date.setError(null);
                       break;
                   case 'U':
                       dialog_item.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
                       break;
               } // end switch()
            } // end onDateSet()
        }, year, month, day);
        datePickerDialog.show();
    } // end getCalander()

    private void getTime(final String period) {
        timePickerDialog = new TimePickerDialog(Vacation_Departure_Request_Activity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                switch (period)
                {
                    case "from":
                        departure_time_from.setText(String.format("%02d:%02d" , i , i1));
                        departure_time_from.setError(null);
                        break;
                    case "to":
                        departure_time_to.setText(String.format("%02d:%02d" , i , i1));
                        departure_time_to.setError(null);
                        break;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Vacation_Departure_Request_Activity.this , User_Main_Activity.class));
        Animatoo.animateSwipeRight(this);
        finish();
    } // end onBackPressed()

} // end class