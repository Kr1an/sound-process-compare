package kulturraum.org.myapplication;


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kulturraum.org.myapplication.core.SoundMerge;

public class MainActivityState {
    public static final Integer PRE_STAGE = 0;
    public static final Integer UPLOAD_STAGE = 1;
    public static final Integer ANDROID_STAGE = 2;
    public static final Integer CPP_STAGE = 3;
    public static final Integer POST_STAGE = 4;

    public static final Long TICK_TIME = 200l;

    public static List<Integer> stages = Arrays.asList(PRE_STAGE, UPLOAD_STAGE, ANDROID_STAGE, CPP_STAGE, POST_STAGE);

    private Integer currentStage = PRE_STAGE;
    private Uri zippedFileUri;
    private String unzippedFilename = "";
    private String androidStatistic = "";
    private String cppStatistic = "";

    private String time = "Hi, friend";

    private MainActivity _context;
    private Handler _updateHandler;
    private Boolean _shouldUpdate = false;

    private Boolean play = false;

    private MediaPlayer _player;

    public MainActivityState(MainActivity context) {
        _context = context;
        _player = MediaPlayer.create(_context, R.raw.got_s2e9_bells);
        _player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        _updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                _context.callback();
            }
        };
        setUpTimer();
        _shouldUpdate = true;
    }

    public void nextStage() {
        if (MainActivityState.stages.indexOf(currentStage) < MainActivityState.stages.size() - 1) {
            currentStage = MainActivityState.stages.get(MainActivityState.stages.indexOf(currentStage) + 1);
            _shouldUpdate = true;
        }
    }

    public void prevStage() {
        if (MainActivityState.stages.indexOf(currentStage) > 0) {
            currentStage = MainActivityState.stages.get(MainActivityState.stages.indexOf(currentStage) - 1);
            _shouldUpdate = true;
        }

    }

    public void setUpTimer() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (_shouldUpdate) {
                    _updateHandler.obtainMessage().sendToTarget();
                    _shouldUpdate = false;
                }
            }
        }, 0, TICK_TIME);
    }
    public void onUpdate() {
        _context.callback();
    }

    public Integer getCurrentStage() {
        return currentStage;
    }

    public String getZipedFilename() {
        return zippedFileUri.toString();
    }

    public String getUnzipedFilename() {
        return unzippedFilename;
    }

    public String getAndroidStatistic() {
        return androidStatistic;
    }

    public String getCppStatistic() {
        return cppStatistic;
    }

    public void setCurrentStage(Integer currentStage) {
        this._shouldUpdate = true;
        this.currentStage = currentStage;
    }

    public void setZippedFileUri(Uri zippedFileUri) {
        this._shouldUpdate = true;
        this.zippedFileUri = zippedFileUri;
    }

    public void setUnzipedFilename(String unzipedFilename) {
        this.unzippedFilename = unzipedFilename;
    }

    public void setAndroidStatistic(String androidStatistic) {
        this._shouldUpdate = true;
        this.androidStatistic = androidStatistic;
    }

    public void setCppStatistic(String cppStatistic) {
        this._shouldUpdate = true;
        this.cppStatistic = cppStatistic;
    }

    public Boolean getPlay() {
        return play;
    }

    public void setPlay(Boolean play) {
        this._shouldUpdate = true;
        this.play = play;
        this.managePlayer();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this._shouldUpdate = true;
        this.time = time;
    }

    private void managePlayer() {
        try {
            if (play) {
                _player.start();
            } else {
                _player.pause();
            }
        } catch(Exception e) {
            Log.e("MANAGE_PLAYER::ERROR", e.getMessage());
        }
    }
}
