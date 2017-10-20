package bong.android.fcmtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

/**
 * Created by Polarium on 2017-10-18.
 */

public class FirebaseAuthTestActivity extends AppCompatActivity {
    private static String TAG = "FirebaseAuthTestActivity";

    private View mProgressView;

    private SignInButton mSignInBtn;

    private GoogleApiClient mGoogleAPIClient;

    private GoogleSignInOptions mGoogleSignOptions;

    private FirebaseAuth mAuth;

    private static int GOOGLE_LOGIN_OPEN = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fbauth);

        mProgressView = (ProgressBar) findViewById(R.id.progressBar2);
        mSignInBtn = (SignInButton) findViewById(R.id.google_sign_in_btn);

        mAuth = FirebaseAuth.getInstance();

        // [START config_signin]
        // Configure Google Sign In
        mGoogleSignOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //실패시 처리하는 부분
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignOptions)
                .build();

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleAPIClient);
        startActivityForResult(signInIntent, GOOGLE_LOGIN_OPEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_LOGIN_OPEN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isComplete()) {
                            if (task.isSuccessful()) {

                                Toast.makeText(FirebaseAuthTestActivity.this, "로그인에 성공", Toast.LENGTH_SHORT).show();
                                {
                                    //사용자 프로필 가져오기
                                    FirebaseUser user = task.getResult().getUser();
                                    String name = user.getDisplayName();
                                    String email = user.getEmail();
                                    Uri photoUrl = user.getPhotoUrl();
                                    boolean emailVerified = user.isEmailVerified();
                                    String uid = user.getUid();
                                    Task<GetTokenResult> idToken = user.getIdToken(true);

                                    Log.d("BONGTEST", "사용자 프로필 name " + name);
                                    Log.d("BONGTEST", "사용자 프로필 email " + email);
                                    Log.d("BONGTEST", "사용자 프로필 photoUrl " + photoUrl);
                                    Log.d("BONGTEST", "사용자 프로필 emailVerified " + emailVerified);
                                    Log.d("BONGTEST", "사용자 프로필 uid " + uid);
                                }

                                {
                                    //제공업체의 사용자 프로필 정보 가져오기
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        for (UserInfo profile : user.getProviderData()) {
                                            // Id of the provider (ex: google.com)
                                            String providerId = profile.getProviderId();

                                            // UID specific to the provider
                                            String uid = profile.getUid();

                                            // Name, email address, and profile photo Url
                                            String name = profile.getDisplayName();
                                            String email = profile.getEmail();

                                            Uri photoUrl = profile.getPhotoUrl();

                                            Log.d("BONGTEST", "---------------------------------------");
                                            Log.d("BONGTEST", "제공업체 사용자 프로필 providerId " + providerId);
                                            Log.d("BONGTEST", "제공업체 사용자 프로필 name " + name);
                                            Log.d("BONGTEST", "제공업체 사용자 프로필 email " + email);
                                            Log.d("BONGTEST", "제공업체 사용자 프로필 photoUrl " + photoUrl);
                                            Log.d("BONGTEST", "제공업체 사용자 프로필 uid " + uid);
                                        }

                                    }
                                }


                            } else {
                                Toast.makeText(FirebaseAuthTestActivity.this, "로그인에 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                       /* if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }*/


                        // ...
                    }
                });
    }
}
