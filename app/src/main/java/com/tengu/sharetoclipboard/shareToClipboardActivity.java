package com.tengu.sharetoclipboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;

public class shareToClipboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null &&  HTTP.PLAIN_TEXT_TYPE.equals(type))
            handleSendText(intent);

        finish();
    }

    @SuppressLint("NewApi")
    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        String sharedTitle = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        if (sharedText == null && sharedTitle == null) {
            Toast.makeText(this, "Nothing to Share", Toast.LENGTH_LONG).show();
            return;
        }

        String clipboardText = "";
        if (sharedTitle != null)
            clipboardText+= sharedTitle;
        if (sharedText != null && sharedTitle != null)
            clipboardText += "\n";
        if (sharedText != null)
            clipboardText+= sharedText;


        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) this
                    .getSystemService(this.CLIPBOARD_SERVICE);
            clipboard.setText(sharedText);
        } else {
            ClipboardManager clipboard = (android.content.ClipboardManager) this
                    .getSystemService(this.CLIPBOARD_SERVICE);
            ClipData clip = ClipData
                    .newPlainText(
                            "text", clipboardText);
            clipboard.setPrimaryClip(clip);
        }
    }

}