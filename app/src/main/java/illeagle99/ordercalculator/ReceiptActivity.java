package illeagle99.ordercalculator;

import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import illeagle99.ordercalculator.backend.Receipt;

public class ReceiptActivity extends AppCompatActivity {
    private Receipt receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        receipt = (Receipt)
                getIntent().getBundleExtra("receipt").getBinder("receipt");

        ((TextView) findViewById(R.id.textView4)).setText(receipt.toString());
    }

    @Override
    public void onBackPressed(){
        receipt.remove("shipping fee"); /* just for consistent aesthetic */
        super.onBackPressed();

    }

    public void email(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "YOUR ORDER RECEIPT");
        i.putExtra(Intent.EXTRA_TEXT   , receipt.toString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public void text(View v){
        String msg = "YOUR ORDER RECEIPT\n\n"+receipt.toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            //String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);

            /*if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }*/
            startActivity(sendIntent);

        }
        else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("sms_body",msg);
            startActivity(smsIntent);
        }
    }
}
