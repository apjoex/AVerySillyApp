package com.x.chill;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by apjoe on 2/17/2017.
 */

public class SettingsSheet extends BottomSheetDialogFragment {

    ImageView red_check, magneta_check, green_check, blue_check, brown_check, purple_check;
    RelativeLayout red, magneta, green, blue, brown, purple;
    Button set_btn;
    BottomSheetDialog dialog;
    EditText text;
    SharedPreferences preferences;
    Boolean color_set = false;

    static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        red_check = (ImageView)view.findViewById(R.id.red_check);
        magneta_check = (ImageView)view.findViewById(R.id.magneta_check);
        green_check = (ImageView)view.findViewById(R.id.green_check);
        blue_check = (ImageView)view.findViewById(R.id.blue_check);
        brown_check = (ImageView)view.findViewById(R.id.brown_check);
        purple_check = (ImageView)view.findViewById(R.id.purple_check);

        red = (RelativeLayout)view.findViewById(R.id.red);
        magneta = (RelativeLayout)view.findViewById(R.id.magneta);
        green = (RelativeLayout)view.findViewById(R.id.green);
        blue = (RelativeLayout)view.findViewById(R.id.blue);
        brown = (RelativeLayout)view.findViewById(R.id.brown);
        purple = (RelativeLayout)view.findViewById(R.id.purple);

        set_btn = (Button)view.findViewById(R.id.set_btn);

        text = (EditText)view.findViewById(R.id.text);

        String newText = preferences.getString("text","");
        text.setText(newText);

        unCheckAll();
        clickEvents();
        return view;

    }

    private void clickEvents() {

        //Select red
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unCheckAll();
                red_check.setVisibility(View.VISIBLE);
                setColor("red");
                color_set = true;
            }
        });

        //Select magenta
        magneta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unCheckAll();
                magneta_check.setVisibility(View.VISIBLE);
                setColor("magenta");
                color_set = true;
            }
        });

        //Select green
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unCheckAll();
                green_check.setVisibility(View.VISIBLE);
                setColor("green");
                color_set = true;
            }
        });

        //Select blue
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unCheckAll();
                blue_check.setVisibility(View.VISIBLE);
                setColor("blue");
                color_set = true;
            }
        });

        //Select brown
        brown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unCheckAll();
                brown_check.setVisibility(View.VISIBLE);
                setColor("brown");
                color_set = true;
            }
        });

        //Select purple
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unCheckAll();
                purple_check.setVisibility(View.VISIBLE);
                setColor("purple");
                color_set = true;
            }
        });

        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()){
                    onDismiss(dialog);
                    Home.refresh();
                }
            }
        });

    }

    private void setColor(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("colour",s);
        editor.apply();
    }

    private boolean validateFields() {

        if(TextUtils.isEmpty(text.getText().toString()) || text.getText().toString().trim().length() == 0){
            Toast.makeText(getActivity(), "Errr... I think you forgot to enter a new text", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("text",text.getText().toString());
            editor.apply();
        }

        if(!color_set){
            Toast.makeText(getActivity(), "Errr... I think you forgot to choose a colour", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void unCheckAll() {
        red_check.setVisibility(View.INVISIBLE);
        magneta_check.setVisibility(View.INVISIBLE);
        green_check.setVisibility(View.INVISIBLE);
        blue_check.setVisibility(View.INVISIBLE);
        brown_check.setVisibility(View.INVISIBLE);
        purple_check.setVisibility(View.INVISIBLE);
    }

}

