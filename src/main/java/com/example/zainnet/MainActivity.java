package com.example.zainnet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import com.example.zainnet.p004me.legrange.mikrotit.ApiConnection;
import com.example.zainnet.p004me.legrange.mikrotit.MikrotikApiException;
import com.example.zainnet.p004me.legrange.mikrotit.ResultListener;

public class MainActivity extends AppCompatActivity {
    Context context;
    LinearLayout layout;
    TextView profile;
    TextView remin;
    Button search;
    EditText searchtxt;
    TextView spent;
    String srchtxt;
    TextView user_name;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        this.user_name = (TextView) findViewById(R.id.user_name);
        this.profile = (TextView) findViewById(R.id.profile);
        this.spent = (TextView) findViewById(R.id.spent);
        this.remin = (TextView) findViewById(R.id.remin);
        this.searchtxt = (EditText) findViewById(R.id.searchtxt);
        this.search = (Button) findViewById(R.id.search);
        this.layout = (LinearLayout) findViewById(R.id.layout);
        this.layout.setVisibility(8);
        this.search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.this.searchtxt.getText().toString().length() == 0) {
                    MainActivity.showSweetDialog(MainActivity.this.context, "تنبيه!", "يرجى إدخال رقم الاشتراك");
                    return;
                }
                new LongOperation().execute();
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        private LongOperation() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            Log.e("openconnect", "ok");
            try {
                ApiConnection con = ApiConnection.connect("81.8.0.5");
                ((AppCompatActivity) MainActivity.this.context).runOnUiThread(new Runnable() {
                    public void run() {
                        MainActivity.this.srchtxt = MainActivity.this.searchtxt.getText().toString();
                    }
                });
                con.login("HiNet", "1890hi");
                con.execute("/tool/user-manager/user/print where username=" + MainActivity.this.srchtxt, new ResultListener() {
                    public void receive(final Map<String, String> result) {
                        System.out.println(result);
//                        Log.e("result", result + BuildConfig.FLAVOR);
                        ((AppCompatActivity) MainActivity.this.context).runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    MainActivity.this.layout.setVisibility(View.VISIBLE);
                                    MainActivity.this.user_name.setText((CharSequence) result.get("username"));
                                    MainActivity.this.profile.setText((CharSequence) result.get("actual-profile"));
                                    double total = (((Double.parseDouble((String) result.get("download-used")) + Double.parseDouble((String) result.get("upload-used"))) / 1024.0d) / 1024.0d) / 1024.0d;
                                    NumberFormat numberFormat = new DecimalFormat("#.#");
                                    TextView textView = MainActivity.this.spent;
                                    textView.setText(numberFormat.format(total) + "GB");
                                    double remin1 = 0.0d;
                                    if (((String) result.get("actual-profile")).equals("1M")) {
                                        remin1 = 50.0d - total;
                                    } else if (((String) result.get("actual-profile")).equals("2M")) {
                                        remin1 = 85.0d - total;
                                    }
                                    TextView textView2 = MainActivity.this.remin;
                                    textView2.setText(numberFormat.format(remin1) + "GB");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    MainActivity.showSweetDialogv1(MainActivity.this.context, "تنبيه!", "هناك خطأ يرجى التواصل مع مدير الشبكة");
                                }
                            }
                        });
                    }

                    public void error(MikrotikApiException ex) {
//                        Log.e("exerror", ex.getMessage() + BuildConfig.FLAVOR);
                    }

                    public void completed() {
                    }
                });
                return null;
            } catch (MikrotikApiException e) {
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            Log.e("result", result + "is");
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Void... values) {
        }
    }

    public static void showSweetDialog(Context context2, String texttitle, String textmessage) {
        final Dialog dialog = new Dialog(context2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.sweet_dialog_layout);
        ((TextView) dialog.findViewById(R.id.title)).setText(texttitle);
        ((TextView) dialog.findViewById(R.id.message)).setText(textmessage);
        ((TextView) dialog.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(NotificationCompat.CATEGORY_ERROR, e.getMessage());
        }
    }

    public static void showSweetDialogv1(final Context context2, String texttitle, String textmessage) {
        final Dialog dialog = new Dialog(context2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.sweet_dialog_layout_v35);
        ((TextView) dialog.findViewById(R.id.title)).setText(texttitle);
        ((TextView) dialog.findViewById(R.id.message)).setText(textmessage);
        ((TextView) dialog.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
//        ((TextView) dialog.findViewById(R.id.wtsapp)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                MainActivity.openWhatsappChat(context2);
//            }
//        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(NotificationCompat.CATEGORY_ERROR, e.getMessage());
        }
    }

    public static void openWhatsappChat(Context context2) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("http://api.whatsapp.com/" + "send?phone="));
            context2.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context2, "WhatsApp cannot be opened", 0).show();
        }
    }
}
