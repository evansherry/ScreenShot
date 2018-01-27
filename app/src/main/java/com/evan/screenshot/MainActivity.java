package com.evan.screenshot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ShotListener{

    private ShotScreenHelper shotScreenHelper;

    public static final int REQUEST_READ_PERMISSION = 0x01;
    private TextView pathTv;
    private TextView nameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTv = findViewById(R.id.tv_name);
        pathTv = findViewById(R.id.tv_path);

        shotScreenHelper = new ShotScreenHelper(getContentResolver(), this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionThenRegister();

    }

    @Override
    protected void onPause() {
        super.onPause();
        shotScreenHelper.unregister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shotScreenHelper.onDestroy();
    }

    @Override
    public void onShot(ScreenshotData data) {

        nameTv.setText(getString(R.string.file_name,data.getFileName()));
        pathTv.setText(getString(R.string.file_path,data.getPath()));
    }


    private void checkPermissionThenRegister(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_READ_PERMISSION);
        }else {
            shotScreenHelper.register();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_READ_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
                shotScreenHelper.register();
            }else {
                nameTv.setText("permission denied");
            }
        }
    }
}
