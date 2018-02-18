package com.chahil.maulik.wibot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    final  String responce[]=new String[1];
    private SensorManager mSensorManager;
    private Sensor mSensor;
    @BindView(R.id.b)
    Button b;

@BindView(R.id.ip)
    Button changeIp;
    @BindView(R.id.joy1)
    JoystickView joystickView1;
    @BindView(R.id.joy2)
    JoystickView joystickView2;
    @BindView(R.id.statusText)
    TextView status;
    @BindView(R.id.gyroOn)
    ToggleButton gyro;
    Boolean isdisable = false;
    String ip="192.168.0.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(myToolbar);
        ButterKnife.bind(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    responce[0] = MainActivity.this.run("http://" + ip + "/st");
                    responce[0] = MainActivity.this.run("http://" + ip + "/st");
                    responce[0] = MainActivity.this.run("http://" + ip + "/st");
                    responce[0] = MainActivity.this.run("http://" + ip + "/st");
                    Log.e("Stop", "Stop");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
      SensorEventListener sensorEvent = new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               Thread thread = new Thread(new Runnable() {
                   @Override
                   public void run() {

                       try {
                           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                   .permitAll().build();
                           StrictMode.setThreadPolicy(policy);
                            if (event.values[0] <= -3) {
                               Log.e("Forward", "Forward");
                               responce[0] = MainActivity.this.run("http://" + ip + "/fw");
                           } else if (event.values[0] >=3) {
                               Log.e("Backward", "Backward");
                               responce[0] = MainActivity.this.run("http://" + ip + "/bk");
                           } else if (event.values[1] >= 3) {
                               Log.e("Right", "Right");
                               responce[0] = MainActivity.this.run("http://" + ip + "/rt");
                           } else if (event.values[1] <= -3) {
                               Log.e("Left", "Left");
                               responce[0] = MainActivity.this.run("http://" + ip + "/lt");
                           }
                           else  {
                                Log.e("Stop", "Stop");
                                responce[0] = MainActivity.this.run("http://" + ip + "/st");
                                responce[0] = MainActivity.this.run("http://" + ip + "/st");
                                responce[0] = MainActivity.this.run("http://" + ip + "/st");
                                responce[0] = MainActivity.this.run("http://" + ip + "/st");
                            }


                       } catch (IOException e) {
                           e.printStackTrace();

                       }
                   }
                   });
               thread.start();

           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {

           }
       };
        gyro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(MainActivity.this, "Gyro is ON and Joystick is disabled", Toast.LENGTH_SHORT).show();
                    joystickView1.setEnabled(false);
                    mSensorManager.registerListener(sensorEvent,mSensor,mSensorManager.SENSOR_DELAY_NORMAL);
                    isdisable = true;
                } else {
                    Toast.makeText(MainActivity.this, "Gyro is OFF and Joystick is enabled", Toast.LENGTH_SHORT).show();
                    isdisable = false;
                    mSensorManager.unregisterListener(sensorEvent,mSensor);
                    joystickView1.setEnabled(true);
                }
            }
        });
        status.setText(ip);
        changeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Enter IP here")
                        .inputType(
                                InputType.TYPE_CLASS_TEXT
                                        | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRange(2, 16)
                        .positiveText("Submit")
                        .input(
                                ip,
                                ip,
                                false,
                                (dialog, input) -> setChangeIp(input.toString()))
                        .show();


            }
        });

        joystickView1.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(final int angle, final int strength) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            if(strength<=25){
                                Log.e("Stop","Angle : "+angle+" Strength : "+strength);
                                responce[0] = MainActivity.this.run("http://"+ip+"/st");
                                responce[0] = MainActivity.this.run("http://"+ip+"/st");
                                responce[0] = MainActivity.this.run("http://"+ip+"/st");
                                responce[0] = MainActivity.this.run("http://"+ip+"/st");}
                           else if(angle<=45||angle>=315){
                                Log.e("Right","Angle : "+angle+" Strength : "+strength);
                            responce[0] = MainActivity.this.run("http://"+ip+"/rt");}
                          else  if(angle<=135&&angle>=45){
                                Log.e("Forward","Angle : "+angle+" Strength : "+strength);
                                responce[0] = MainActivity.this.run("http://"+ip+"/fw");}
                          else  if(angle<=225&&angle>=135){
                                Log.e("Left","Angle : "+angle+" Strength : "+strength);
                                responce[0] = MainActivity.this.run("http://"+ip+"/lt");}
                          else  if(angle<=315||angle>=225){
                                Log.e("Backward","Angle : "+angle+" Strength : "+strength);
                                responce[0] = MainActivity.this.run("http://"+ip+"/bk");}


                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                });
                thread.start();

            }
        }, 250);


    }
    void setChangeIp(String chip){
        ip=chip;
        status.setText(ip);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
              Intent intent = new Intent(this, About.class);
              startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}


