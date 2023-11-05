package com.manateam.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.engine.glengine3.Engine;
import com.example.engine.glengine3.GamePageInterface;

import java.util.function.Function;


public class MainActivity extends Activity implements View.OnTouchListener {
    Engine engine = new Engine(); //we are unable to make it static because it is impossible to use
    //android context in static way

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView v = engine.onCreate(this, new Function<Void, GamePageInterface>() {
            @Override
            public GamePageInterface apply(Void unused) {
                return new MainRenderer();
            }
        });
        setContentView(v);
        assert v != null;
        v.setOnTouchListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        engine.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        engine.onResume();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return Engine.onTouch(v, event);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}