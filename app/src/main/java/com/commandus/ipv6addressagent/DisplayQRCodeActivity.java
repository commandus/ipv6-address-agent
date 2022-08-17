package com.commandus.ipv6addressagent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.commandus.utilipv6.Subscription;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @brief Display QR code of the
 * - public key & auth to subscribe or
 * - subscription token & auth
 */
public class DisplayQRCodeActivity extends AppCompatActivity {

    private static final String TAG = DisplayQRCodeActivity.class.getSimpleName();
    private ImageView mImageViewQRCode;
    private ImageButton mImageButtonShareQRCode;
    private String vapidPublicKey;
    private String authSecret;
    private String subscriptionRecord;
    private ConstraintLayout mLayoutDisplayQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_vapid_key);
        mLayoutDisplayQRCode = findViewById(R.id.layoutDisplayQRCode);
        mImageViewQRCode = findViewById(R.id.imageViewQRCode);
        mImageButtonShareQRCode = findViewById(R.id.imageButtonShareQRCode);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            vapidPublicKey = intent.getStringExtra(Settings.VAPID_PUBLIC_KEY);
            authSecret = intent.getStringExtra(Settings.VAPID_AUTH_SECRET);
            subscriptionRecord = intent.getStringExtra(Settings.ADDRESS);
        }
        if (vapidPublicKey == null)
            vapidPublicKey = "";
        if (authSecret == null)
            authSecret = "";
        if (subscriptionRecord == null)
            subscriptionRecord = "";
        if (vapidPublicKey.isEmpty() && subscriptionRecord.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_scan_code_wrong), Toast.LENGTH_LONG).show();
            finish();
        }
        if (mImageButtonShareQRCode != null) {
            mImageButtonShareQRCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareCode();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showQRCode();
    }

    /**
     * @brief back key pressed event
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * @brief Show QR code bitmap in the image view. QR code corresponds to this device to subscribe
     * or subscription token.
     * @see #getCode()
     */
    private void showQRCode() {
        if (mImageViewQRCode != null) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int w = metrics.widthPixels;
            int h = metrics.heightPixels;
            if (w > h)
                w = h;

            String value = getCode();
            if (!value.isEmpty()) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(value, BarcodeFormat.QR_CODE, w, w);
                    mImageViewQRCode.setImageBitmap(bitmap);
                } catch(Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    }

    /**
     * @brief Serialized string encoded in the QR code used for make subscription or
     * subscription token.
     * Return serialized public key and auth for subscription.
     * To subscribe only public key is enough, auth secret anyway is
     * required later to send messages. Therefore QR code consists of the public key
     * and auth secret (for later usage).
     * Subscription token also has auth.
     * @return public key and auth secret of this device
     * or of the subscription
     */
    private String getCode() {
        String value;
        if (!vapidPublicKey.isEmpty()) {
            value = vapidPublicKey + "," + authSecret;
        } else {
            if (!subscriptionRecord.isEmpty()) {
                Subscription s = new Subscription(subscriptionRecord);
                value = s.getToken()  + "," + s.authSecret;
            } else {
                value = "";
            }
        }
        return value;
    }

    /**
     * @brief Share URL to subscribe or subscription token using other applications.
     * URL looks like https://vapidchatter.commandus.com/code/?publicKey=&authSecret=
     * @see Settings#getShareLink(String, String) 
     */
    private void shareCode() {
        String value = getCode();
        if (!value.isEmpty()) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.msg_share_code));
                String shareMessage = getString(R.string.msg_share_code) + " ";
                if (!vapidPublicKey.isEmpty()) {
                    shareMessage += Settings.getShareLink(vapidPublicKey, authSecret);
                } else {
                    Subscription s = new Subscription(subscriptionRecord);
                    shareMessage += Settings.getShareLinkSubscription(s.getToken(), s.authSecret);
                }

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.intent_msg_choose_share)));
            } catch(Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

}
