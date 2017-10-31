package kulturraum.org.myapplication;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ICallback {
    TextView tv;

    FloatingActionButton uploadBtn;
    FloatingActionButton androidBtn;
    FloatingActionButton cppBtn;

    TextView filename;
    TextView androidStatistic;
    TextView cppStatistic;

    FloatingActionButton soundBtn;

    MainActivityState state;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setListeners();

        state = new MainActivityState(this);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mHandler.obtainMessage().sendToTarget();
            }
        }, 0, 1000);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
//            }
//        });

    }

    private Boolean setListeners() {
        Boolean result = false;
        try {
            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent()
                            .setAction(Intent.ACTION_GET_CONTENT)
                            .setType("*/*");
                    startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);

                }
            });
            androidBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    state.setCurrentStage(MainActivityState.CPP_STAGE);
                    state.setAndroidStatistic("100%");
                }
            });
            cppBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View vt) {
                    state.setCurrentStage(MainActivityState.POST_STAGE);
                    state.setCppStatistic("done!");
                }
            });
            soundBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state.setPlay(!state.getPlay());
                }
            });



            result = true;
        } catch(Exception e) {
            result = false;
        } finally {
            return result;
        }
    }

    private Boolean setViews() {
        Boolean result = false;
        try {
            tv = (TextView) findViewById(R.id.sample_text);
            uploadBtn = (FloatingActionButton) findViewById(R.id.loadBtn);
            androidBtn = (FloatingActionButton) findViewById(R.id.androidBtn);
            cppBtn = (FloatingActionButton) findViewById(R.id.cppBtn);
            filename = (TextView) findViewById(R.id.filename);
            androidStatistic = (TextView) findViewById(R.id.androidStatistic);
            cppStatistic = (TextView) findViewById(R.id.CppStatistic);
            soundBtn = (FloatingActionButton) findViewById(R.id.soundBtn);
            if (soundBtn == null || tv == null || uploadBtn == null || androidBtn == null || cppBtn == null || filename == null || androidStatistic == null || cppStatistic == null) {
                throw new Exception("components did not bind");
            }
            result = true;
        } catch(Exception e) {
            Log.e("MAIN_ACTIVITY::setView", "can not bind all components");
            result = false;
        } finally {
            return result;
        }
    }

    private void onFileSelected(Uri selectedFile) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("EXPLORER::CHOSEN", "in OnActivityResult method");
        if(requestCode==123 && resultCode==RESULT_OK) {
            Uri selectedFile = data.getData();
            String absPath = selectedFile.toString();
            state.setZippedFileUri(selectedFile);
            state.setCurrentStage(MainActivityState.ANDROID_STAGE);
            Log.d("EXPLORER::CHOSEN", "chosen file path: " + absPath);
        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            state.setTime(stringFromJNI());
        }
    };

    public native String stringFromJNI();

    @Override
    public void callback() {
        tv.setText(state.getTime());
        Integer currentStage = state.getCurrentStage();

        if (currentStage >= MainActivityState.PRE_STAGE) {
            uploadBtn.setVisibility(View.VISIBLE);
        } else {
            uploadBtn.setVisibility(View.VISIBLE);
        }

        if (currentStage >= MainActivityState.UPLOAD_STAGE) {
            uploadBtn.setVisibility(View.VISIBLE);
            filename.setText(state.getZipedFilename());
        } else {
            uploadBtn.setVisibility(View.VISIBLE);
            filename.setText("");
        }

        if (currentStage >= MainActivityState.ANDROID_STAGE) {
            androidBtn.setVisibility(View.VISIBLE);
            androidStatistic.setText(state.getAndroidStatistic());
        } else {
            androidBtn.setVisibility(View.INVISIBLE);
            androidStatistic.setText("");
        }

        if (currentStage >= MainActivityState.CPP_STAGE) {
            cppBtn.setVisibility(View.VISIBLE);
            cppStatistic.setText(state.getCppStatistic());

        } else {
            cppBtn.setVisibility(View.INVISIBLE);
            cppStatistic.setText("");
        }


        if (currentStage >= MainActivityState.POST_STAGE) {
            soundBtn.setImageResource(state.getPlay() ? R.drawable.ic_pause_big : R.drawable.ic_play_big);
            soundBtn.setVisibility(View.VISIBLE);
        } else {
            soundBtn.setVisibility(View.INVISIBLE);
        }



    }
}
