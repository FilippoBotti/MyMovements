package com.mobdev.mymovements.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobdev.mymovements.Activity.MainActivity;
import com.mobdev.mymovements.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private TextInputLayout mapTextView;
    private TextView mapText;
    private TextInputLayout weightTextView;
    private TextInputEditText weightText;
    private TextInputLayout nameTextView;
    private TextInputEditText nameText;
    private Button saveButton;
    public static final String MAP_PREF = "MapType";
    public static final String NAME_PREF = "NamePref";
    public static final String WEIGHT_PREF = "WeightPref";

    /*
        In questo metodo inizializziamo la vista del fragment settando le textView
        e settiamo le azioni corrispondenti al saveButton (salvataggio sharedPreference)
        e al mapTextView (richiamando il metodo per mostrare le quattro opzioni di selezione)
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_layout, container, false);
        mapTextView = (TextInputLayout) view.findViewById(R.id.settings_map);
        mapText = (TextView) view.findViewById(R.id.mapVal);
        weightTextView = (TextInputLayout) view.findViewById(R.id.settings_name);
        weightText = (TextInputEditText) view.findViewById(R.id.weightVal);
        nameTextView = (TextInputLayout) view.findViewById(R.id.settings_name);
        nameText = (TextInputEditText) view.findViewById(R.id.nameVal);
        mapText.setText(getContext().getSharedPreferences(MainActivity.PREFERENCE,0).getString(SettingsFragment.MAP_PREF, "Normal Map"));
        nameText.setText(getContext().getSharedPreferences(MainActivity.PREFERENCE,0).getString(SettingsFragment.NAME_PREF,""));
        weightText.setText(String.valueOf(getContext().getSharedPreferences(MainActivity.PREFERENCE,0).getInt(SettingsFragment.WEIGHT_PREF, 70)));
        mapText.setFocusable(false);
        mapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMap();
            }
        });
        saveButton = (Button) view.findViewById(R.id.saveButton);
        SharedPreferences settings = getContext().getSharedPreferences(MainActivity.PREFERENCE, 0);
        weightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!weightText.getText().toString().isEmpty() && !nameText.getText().toString().isEmpty() && !mapText.getText().toString().isEmpty()){
                    showSaveConfirmation();
                }
                else {
                    Toast.makeText(getContext(), "Please, insert your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    /*
        Se premuto il pulsante save mostriamo una finestra per la conferma dell'operazione
     */
    private void showSaveConfirmation(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ShortAlertDialogCustom);
        builder.setMessage(R.string.SaveButton);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.d(SettingsFragment.TAG, weightText.getText().toString());
                SharedPreferences settings = getContext().getSharedPreferences(MainActivity.PREFERENCE, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(WEIGHT_PREF, Integer.parseInt(weightText.getText().toString()));
                editor.putString(NAME_PREF,nameText.getText().toString());
                editor.putString(MAP_PREF, mapText.getText().toString());
                editor.commit();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
                Log.d(SettingsFragment.TAG, mapText.getText().toString() + weightText.getText().toString() + nameText.getText().toString());


                if(settings.getBoolean(MainActivity.FIRST_LAUNCH,true)){
                    Intent newIntent = new Intent(new Intent(getContext(), MainActivity.class));
                    getContext().startActivity(newIntent);
                    settings.edit().putBoolean(MainActivity.FIRST_LAUNCH,false).apply();
                }

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /*
        Questo metodo permette di visualizzare un alertdialog per scegliere la mappa
     */

    private void selectMap(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        builder.setTitle("Select Map Type");
        builder.setSingleChoiceItems(R.array.mapType,0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Add action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ListView lv = ((AlertDialog)dialog).getListView();
                mapText.setText(lv.getAdapter().getItem(lv.getCheckedItemPosition()).toString());
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                });


        builder.show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
