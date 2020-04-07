package com.technotium.technotiumapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.technotium.technotiumapp.config.SessionManager;

import java.util.List;

public class LockActivity extends AppCompatActivity {
    PatternLockView patternLockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.getMyInstance(LockActivity.this).getEmpid().equalsIgnoreCase("")){
            startActivity(new Intent(LockActivity.this,LoginActivity .class));
            finish();
        }
        setContentView(R.layout.activity_lock);
        patternLockView = findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List progressPattern) {

            }

            @Override
            public void onComplete(List pattern) {
                if(PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase("01234")){
                    startActivity(new Intent(LockActivity.this,WelcomeEmpActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LockActivity.this,"Wrong pattern entered. Please try again...",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCleared() {

            }
        });
    }
}
