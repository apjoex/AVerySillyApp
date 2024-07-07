package com.x.chill

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by apjoe on 2/17/2017.
 */

class SettingsSheet : BottomSheetDialogFragment() {

    private lateinit var red_check: ImageView
    private lateinit var magneta_check: ImageView
    private lateinit var green_check: ImageView
    private lateinit var blue_check: ImageView
    private lateinit var brown_check: ImageView
    private lateinit var purple_check: ImageView
    private lateinit var red: RelativeLayout
    private lateinit var magneta: RelativeLayout
    private lateinit var green: RelativeLayout
    private lateinit var blue: RelativeLayout
    private lateinit var brown: RelativeLayout
    private lateinit var purple: RelativeLayout
    private lateinit var set_btn: Button
    private lateinit var dialog: BottomSheetDialog
    private lateinit var text: EditText
    private lateinit var preferences: SharedPreferences
    private var color_set: Boolean? = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.settings, container, false)

        red_check = view.findViewById<View>(R.id.red_check) as ImageView
        magneta_check = view.findViewById<View>(R.id.magneta_check) as ImageView
        green_check = view.findViewById<View>(R.id.green_check) as ImageView
        blue_check = view.findViewById<View>(R.id.blue_check) as ImageView
        brown_check = view.findViewById<View>(R.id.brown_check) as ImageView
        purple_check = view.findViewById<View>(R.id.purple_check) as ImageView

        red = view.findViewById<View>(R.id.red) as RelativeLayout
        magneta = view.findViewById<View>(R.id.magneta) as RelativeLayout
        green = view.findViewById<View>(R.id.green) as RelativeLayout
        blue = view.findViewById<View>(R.id.blue) as RelativeLayout
        brown = view.findViewById<View>(R.id.brown) as RelativeLayout
        purple = view.findViewById<View>(R.id.purple) as RelativeLayout

        set_btn = view.findViewById<View>(R.id.set_btn) as Button

        text = view.findViewById<View>(R.id.text) as EditText

        val newText = preferences.getString("text", "")
        text.setText(newText)

        unCheckAll()
        clickEvents()
        return view

    }

    private fun clickEvents() {

        //Select red
        red.setOnClickListener {
            unCheckAll()
            red_check.visibility = View.VISIBLE
            setColor("red")
            color_set = true
        }

        //Select magenta
        magneta.setOnClickListener {
            unCheckAll()
            magneta_check.visibility = View.VISIBLE
            setColor("magenta")
            color_set = true
        }

        //Select green
        green.setOnClickListener {
            unCheckAll()
            green_check.visibility = View.VISIBLE
            setColor("green")
            color_set = true
        }

        //Select blue
        blue.setOnClickListener {
            unCheckAll()
            blue_check.visibility = View.VISIBLE
            setColor("blue")
            color_set = true
        }

        //Select brown
        brown.setOnClickListener {
            unCheckAll()
            brown_check.visibility = View.VISIBLE
            setColor("brown")
            color_set = true
        }

        //Select purple
        purple.setOnClickListener {
            unCheckAll()
            purple_check.visibility = View.VISIBLE
            setColor("purple")
            color_set = true
        }

        set_btn.setOnClickListener {
            if (validateFields()) {
                onDismiss(dialog)
                Home.refresh()
            }
        }

    }

    private fun setColor(s: String) {
        val editor = preferences.edit()
        editor.putString("colour", s)
        editor.apply()
    }

    private fun validateFields(): Boolean {

        if (TextUtils.isEmpty(text.text.toString())) {
            Toast.makeText(activity, "Errr... I think you forgot to enter a new text", Toast.LENGTH_SHORT).show()
            return false
        } else {
            val editor = preferences.edit()
            editor.putString("text", text.text.toString())
            editor.apply()
        }

        if ((!color_set!!)) {
            Toast.makeText(activity, "Errr... I think you forgot to choose a colour", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun unCheckAll() {
        red_check.visibility = View.INVISIBLE
        magneta_check.visibility = View.INVISIBLE
        green_check.visibility = View.INVISIBLE
        blue_check.visibility = View.INVISIBLE
        brown_check.visibility = View.INVISIBLE
        purple_check.visibility = View.INVISIBLE
    }

    companion object {

        private fun newInstance(): BottomSheetDialogFragment {
            return BottomSheetDialogFragment()
        }
    }

}

