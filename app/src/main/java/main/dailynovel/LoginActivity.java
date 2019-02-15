package main.dailynovel;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    Button btnRegister, btnForget, btnCancel;
    GoogleApiClient mGoogleApiClient;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String accountname;
    String accountemail;
    String accountimg;
    String isFacebook;
    int RC_SIGN_IN = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Twitter.initialize(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
        setContentView(R.layout.activity_login);

        //      SET HIDE STATUS BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("accountname", "null");
                i.putExtra("accountemail", "null");
                i.putExtra("accountimg", "null");
                i.putExtra("isfacebook", "null");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        findViewById(R.id.sign_in_button).setOnClickListener(this);
//
//
//        Call<User> user = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false, false);
//        user.enqueue(new Callback<User>() {
//            @Override
//            public void success(Result<User> userResult) {
//                accountname = userResult.data.name;
//                accountemail = String.valueOf(userResult.data.id);
//                // _normal (48x48px) | _bigger (73x73px) | _mini (24x24px)
//                accountimg   = userResult.data.profileImageUrl.replace("_normal", "_bigger");
//            }
//
//            @Override
//            public void failure(TwitterException exc) {
//                Log.d("TwitterKit", "Verify Credentials Failure", exc);
//            }
//        });

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        callbackManager = CallbackManager.Factory.create();

        if (AccessToken.getCurrentAccessToken()!=null){
            isFacebook = "true";
            Profile profile = Profile.getCurrentProfile();
            accountemail=AccessToken.getCurrentAccessToken().getUserId();
            accountname=profile.getFirstName() + " " + profile.getLastName();
            accountimg = "https://graph.facebook.com/" + accountemail + "/picture?type=large";
            sendIntent();
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            isFacebook = "false";
                            accountname = object.getString("first_name") + " " + object.getString("last_name");
                            accountemail = object.getString("id");
                            accountimg = "https://graph.facebook.com/" + accountemail + "/picture?type=large";
                            sendIntent();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameter = new Bundle();
                parameter.putString("fields" , "first_name, last_name, email, id, link,picture,gender");
                graphRequest.setParameters(parameter);
                graphRequest.executeAsync();
            }



            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Failed",connectionResult + "");
    }
    //Function Dang Nhap
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            accountname = account.getDisplayName();
            accountemail = account.getEmail();
            if(account.getPhotoUrl() != null) {
                accountimg = account.getPhotoUrl().toString();
            }
            sendIntent();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void sendIntent() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("accountname", accountname);
        intent.putExtra("accountemail", accountemail);
        intent.putExtra("accountimg", accountimg);
        intent.putExtra("isfacebook", isFacebook);
        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

}
