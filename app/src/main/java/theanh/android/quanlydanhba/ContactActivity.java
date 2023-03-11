package theanh.android.quanlydanhba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import theanh.android.quanlydanhba.Object.Contacts;

public class ContactActivity extends AppCompatActivity {

    TextView txvName, txvNum, back;
    TextView message, call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        txvName = findViewById(R.id.contactName);
        txvNum = findViewById(R.id.contactPhoneNum);
        back = findViewById(R.id.backToContacts);

        String name = getIntent().getExtras().getString("name");
        String num = getIntent().getExtras().getString("num");

        getSupportActionBar().setTitle(name);
        txvName.setText(name);
        txvNum.setText(num);

        message = findViewById(R.id.message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + num));
                startActivity(smsIntent);
//                sendMessage();
            }
        });


        call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                if (ActivityCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                startActivity(callIntent);
            }
        });

        back();
    }

//    private void sendMessage() {
//        final Dialog dialog = new Dialog(ContactActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_sms);
//        Window window = dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCancelable(false);
//
//        TextView cancel = dialog.findViewById(R.id.cancel);
//        TextView send = dialog.findViewById(R.id.send);
//        EditText message = dialog.findViewById(R.id.message);
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//                    String num = getIntent().getExtras().getString("num");
//                    String sms = message.getText().toString().trim();
//
//                    if (!num.equals("") && !sms.equals("")) {
//                        SmsManager smsManager = SmsManager.getDefault();
//                        smsManager.sendTextMessage(num, null, sms, null, null);
//                        Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }

    private void back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}