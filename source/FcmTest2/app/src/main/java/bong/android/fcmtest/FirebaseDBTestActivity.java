package bong.android.fcmtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Polarium on 2017-10-17.
 */

public class FirebaseDBTestActivity extends Activity {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRefTestValue = mDatabase.child("TestFbDb").child("aaa");

    TextView tvView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fbdb_layout);

        tvView = (TextView)findViewById(R.id.textView2);

        //mRefTestValue.setValue("ccc")
    }

    public void mOnClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnSetValue:
                mRefTestValue.setValue("444");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mRefTestValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                tvView.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
