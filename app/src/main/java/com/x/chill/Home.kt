package com.x.chill;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.x.chill.reusables.Utilities;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Home extends AppCompatActivity {

    static TextView view;
    int press = 0;
    static SharedPreferences preferences;
    Context context;
    static LottieAnimationView animationView;
    RelativeLayout photo_view;
    TextView tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("A silly title goes here 😉");
        context = this;
        //Set light status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("anim.json");
        animationView.playAnimation();
        animationView.loop(true);

        view = (TextView)findViewById(R.id.text);
        view.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/alexbrush.ttf"),Typeface.BOLD);
        view.setShadowLayer(3, 4, 4, Color.argb(100,10, 10, 10));

        photo_view = (RelativeLayout)findViewById(R.id.photo_view);
        tag = (TextView)findViewById(R.id.tag);

        refresh();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(press >= 7){
                    AlertDialog dialog = new AlertDialog.Builder(Home.this)
                            .setTitle("Hey you, curious cat!")
                            .setMessage("So you found out this hidden feature. \nAnyways, there isn't much here (I guess that\'s why it's hidden in the first place)\n\nYou can reach me via")
                            .setPositiveButton("TWITTER", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/apjoex")));
                                }
                            })
                            .setNegativeButton("INSTAGRAM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Uri uri = Uri.parse("http://instagram.com/_u/apjoex");
                                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                                    likeIng.setPackage("com.instagram.android");

                                    try {
                                        startActivity(likeIng);
                                    } catch (ActivityNotFoundException e) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/apjoex")));
                                    }
                                }
                            }).create();
                    dialog.show();
                    press = 0;
                }else{
                    press++;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                SettingsSheet settingsSheet = new SettingsSheet();
                settingsSheet.show(getSupportFragmentManager(), "bottom sheet");
                break;
            case R.id.action_create:
                if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                   //Request permission
                    ActivityCompat.requestPermissions(Home.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            0);
                }else{
                  savePicture();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  savePicture();
                } else {
                    Toast.makeText(context,"Err.. You need to grant us those permissions tho",Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void savePicture() {
        tag.setVisibility(View.VISIBLE);
        Bitmap bitmap = Utilities.getBitmapFromView(photo_view,photo_view.getMeasuredWidth(),photo_view.getMeasuredHeight());
        try {
            String path = Utilities.storeImage(context,bitmap);
//            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, path);

            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

         //   MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Demoooooo","");
            Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        tag.setVisibility(View.INVISIBLE);
        photo_view.setGravity(RelativeLayout.CENTER_HORIZONTAL | RelativeLayout.CENTER_VERTICAL);
    }

    public static void refresh() {
        String newText = preferences.getString("text","A very silly app");
        view.setText(newText);

        String color = preferences.getString("colour","blue");
        switch (color){
            case "red":
                animationView.setAnimation("red_back.json");
                animationView.playAnimation();
                animationView.loop(true);
                break;
            case "magenta":
                animationView.setAnimation("magneta_back.json");
                animationView.playAnimation();
                animationView.loop(true);
                break;
            case "green":
                animationView.setAnimation("green_back.json");
                animationView.playAnimation();
                animationView.loop(true);
                break;
            case "blue":
                animationView.setAnimation("anim.json");
                animationView.playAnimation();
                animationView.loop(true);
                break;
            case "brown":
                animationView.setAnimation("brown_back.json");
                animationView.playAnimation();
                animationView.loop(true);
                break;
            case "purple":
                animationView.setAnimation("purple_back.json");
                animationView.playAnimation();
                animationView.loop(true);
                break;
        }
    }

}
