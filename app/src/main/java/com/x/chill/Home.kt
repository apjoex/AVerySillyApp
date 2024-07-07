package com.x.chill

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.x.chill.reusables.Utilities

class Home : AppCompatActivity() {
    private var press = 0
    private lateinit var context: Context
    private lateinit var photoView: RelativeLayout
    private lateinit var textLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = "A silly title goes here 😉"
        context = this
        //Set light status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.colorWhite)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)

        animationView = findViewById<View>(R.id.animation_view) as LottieAnimationView
        animationView.setAnimation("anim.json")
        animationView.playAnimation()
        animationView.loop(true)

        view = findViewById<View>(R.id.text) as TextView
        view.setTypeface(Typeface.createFromAsset(assets, "fonts/alexbrush.ttf"), Typeface.BOLD)
        view.setShadowLayer(3f, 4f, 4f, Color.argb(100, 10, 10, 10))

        photoView = findViewById<View>(R.id.photo_view) as RelativeLayout
        textLabel = findViewById<View>(R.id.tag) as TextView

        refresh()

        view.setOnClickListener {
            if (press >= 7) {
                val dialog = AlertDialog.Builder(this@Home)
                        .setTitle("Hey you, curious cat!")
                        .setMessage("So you found out this hidden feature. \nAnyways, there isn't much here (I guess that\'s why it's hidden in the first place)\n\nYou can reach me via")
                        .setPositiveButton("TWITTER") { dialogInterface, i ->
                            dialogInterface.dismiss()
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/apjoex")))
                        }
                        .setNegativeButton("INSTAGRAM") { dialogInterface, i ->
                            val uri = Uri.parse("http://instagram.com/_u/apjoex")
                            val likeIng = Intent(Intent.ACTION_VIEW, uri)

                            likeIng.`package` = "com.instagram.android"

                            try {
                                startActivity(likeIng)
                            } catch (e: ActivityNotFoundException) {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/apjoex")))
                            }
                        }.create()
                dialog.show()
                press = 0
            } else {
                press++
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
                val settingsSheet = SettingsSheet()
                settingsSheet.show(supportFragmentManager, "bottom sheet")
            }
//            R.id.action_create -> if (ContextCompat.checkSelfPermission(this@Home, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                //Request permission
//                ActivityCompat.requestPermissions(this@Home,
//                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                        0)
//            } else {
//                savePicture()
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePicture()
                } else {
                    Toast.makeText(context, "Err.. You need to grant us those permissions tho", Toast.LENGTH_SHORT).show()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    private fun savePicture() {
        textLabel.visibility = View.VISIBLE
        val bitmap = Utilities.getBitmapFromView(photoView, photoView.measuredWidth, photoView.measuredHeight)
        try {
            val path = Utilities.storeImage(context, bitmap)
            //            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.MediaColumns.DATA, path)

            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            //   MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Demoooooo","");
            Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        textLabel.visibility = View.INVISIBLE
        photoView.gravity = RelativeLayout.CENTER_HORIZONTAL or RelativeLayout.CENTER_VERTICAL
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        internal lateinit var view: TextView
        internal lateinit var preferences: SharedPreferences
        internal lateinit var animationView: LottieAnimationView

        fun refresh() {
            val newText = preferences.getString("text", "A very silly app")
            view.text = newText

            val color = preferences.getString("colour", "blue")
            val animationJSON: String = when (color) {
                "red" -> "red_back.json"
                "magenta" -> "magneta_back.json"
                "green" -> "green_back.json"
                "blue" -> "anim.json"
                "brown" -> "brown_back.json"
                "purple" -> "purple_back.json"
                else -> "anim.json"
            }
            animationView.setAnimation(animationJSON)
            animationView.playAnimation()
            animationView.loop(true)
        }
    }

}
