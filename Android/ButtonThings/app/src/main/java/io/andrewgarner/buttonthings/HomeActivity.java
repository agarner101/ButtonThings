package io.andrewgarner.buttonthings;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

/**
 * Shows usage of a button to turn on an LED
 * Created by Andrew Garner 8/3/18
 */
public class HomeActivity extends Activity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    public static final String BUTTON_PIN = "BCM2";
    public static final String LED_PIN = "BCM3";

    private Gpio mButtonGpio;
    private Gpio mLedGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(LOG_TAG,"Hello Home");

        PeripheralManager manager = PeripheralManager.getInstance();

        try {
            mButtonGpio = manager.openGpio(BUTTON_PIN);
            mButtonGpio.setDirection(Gpio.DIRECTION_IN);
            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
            mButtonGpio.registerGpioCallback(mCallback);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to open Button GPIO", e);
        }

        try {
            mLedGpio = manager.openGpio(LED_PIN);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to open LED GPIO", e);
        }
    }

    /**
     * On button state change, changes LED state.
     * Button pressed down -> LED ON
     * Button neutral -> LED OFF
     */
    private void onButtonStateChange(final boolean isPressedDown) {
        try {
            mLedGpio.setValue(isPressedDown);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to set value LED GPIO", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mButtonGpio != null) {
            mButtonGpio.unregisterGpioCallback(mCallback);
            try {
                mButtonGpio.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to close Button GPIO", e);
            }
        }

        if (mLedGpio != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to close LED GPIO", e);
            }
        }
    }

    private GpioCallback mCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(final Gpio gpio) {
            try {
                boolean buttonValue = gpio.getValue();
                if (buttonValue) {
                    Log.d(LOG_TAG, "Button Released");
                } else {
                    Log.d(LOG_TAG, "Button Pressed");
                }
                onButtonStateChange(!buttonValue);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to get value Button GPIO", e);
            }

            //Returns true to continue getting updates.
            return true;
        }
    };
}
