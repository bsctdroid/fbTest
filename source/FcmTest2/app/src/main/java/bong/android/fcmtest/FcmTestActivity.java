package bong.android.fcmtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FcmTestActivity extends Activity {
    @BindView(R.id.btnCalendarTest)
    View btnCalendarTest;

    String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm_test);
        //setContentView(R.layout.activity_schedule);
        //butterKnife 추가
        ButterKnife.bind(this);

        //FCM 추가한 라인
        Log.d("TEST", "token start");
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        token = FirebaseInstanceId.getInstance().getToken();
        new Thread(new Runnable() {
            public void run() {
                sendRegistrationToServer(token);
            }
        }).start();

        Log.d("TEST", "token end " + token);

        testHtmlTextView();


    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        if(token == null)
            return;

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", token)

                .build();

        //request
        Request request = new Request.Builder()
                .url(Define.DOMAIN_FCM_REGISTER)
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //https://developer.android.com/reference/android/widget/CalendarView.html참고
    public void setDataSet()
    {

    }

    @OnClick(R.id.btnCalendarTest)
    public void onClickView(View view)
    {
        Intent intent = new Intent(FcmTestActivity.this, CalendarTestActivity.class);
        startActivity(intent);

        switch (view.getId())
        {
            case R.id.btnCalendarTest:
                break;
        }
    }

    public void mOnClick(View view)
    {
        Intent intent;
        switch(view.getId())
        {
            case R.id.btnCalendarTest:
                intent = new Intent(FcmTestActivity.this, CalendarTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTestDataPicker:
                intent = new Intent(FcmTestActivity.this, DataPickerTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTest2:
                intent = new Intent(FcmTestActivity.this, DataPickerTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTestFbDB:
                intent = new Intent(FcmTestActivity.this, FirebaseDBTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTestFbStorage:
                intent = new Intent(FcmTestActivity.this, FirebaseStorageTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTestFbAuth:
                intent = new Intent(FcmTestActivity.this, FirebaseAuthTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTestFbFbokAuth:
                intent = new Intent(FcmTestActivity.this, FirebaseAuthFacebookTestActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void testHtmlTextView()
    {
        TextView tvTest = (TextView)findViewById(R.id.textView);
        String strTest = getResources().getString(R.string.string_id, "홍길동 ", "baldkd@naver.com");
        tvTest.setText(fromHtml(strTest));
    }

    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            // noinspection deprecation
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }

}
