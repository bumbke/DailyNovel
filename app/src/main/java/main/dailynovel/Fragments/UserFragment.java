package main.dailynovel.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;

import main.dailynovel.LoginActivity;
import main.dailynovel.MainActivity;
import main.dailynovel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    Dialog dialog;
    GoogleSignInClient gsc;
    MainActivity mainActivity;
    Button btnAbout, btnPolicy, btnContact, btnLogout;
    TextView txtUser, txtId;
    ImageView imgAvatar;
    Bitmap bm;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mainActivity = new MainActivity();
        FacebookSdk.sdkInitialize(getActivity());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);

        txtId = (TextView)view.findViewById(R.id.txtId);
        txtUser = (TextView)view.findViewById(R.id.txtUser);
        imgAvatar = (ImageView)view.findViewById(R.id.imgAvatar);

        if(getArguments() != null) {
            if (getArguments().getString("accountemail").equalsIgnoreCase("null")) {
                imgAvatar.setImageResource(R.drawable.ic_appicon96);
                txtId.setText("");
                txtUser.setText("Đăng Nhập");
            } else {
                try {
                    InputStream in = new java.net.URL(getArguments().getString("accountimg")).openStream();
                    bm = BitmapFactory.decodeStream(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgAvatar.setImageBitmap(bm);
                txtUser.setText(getArguments().getString("accountname"));
                txtId.setText(getArguments().getString("accountemail"));
            }
        }

        btnAbout = view.findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettingDialog("about");
            }
        });

        btnContact = view.findViewById(R.id.btnContact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettingDialog("contact");
            }
        });

        btnPolicy = view.findViewById(R.id.btnPolicy);
        btnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettingDialog("policy");
            }
        });

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnLogout:
                        LoginManager.getInstance().logOut();
                        Intent login = new Intent(getActivity(), LoginActivity.class);
                        startActivity(login);
                        mainActivity.finish();
                        break;
                }
            }
        });

        if(getArguments() != null) {
            if (getArguments().getString("accountemail").equalsIgnoreCase("null")) {
                txtUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                    }
                });
            }else {
                txtUser.setOnClickListener(null);
            }
        }

        return view;
    }

    private void signOut() {
        switch (getArguments().getString("isfacebook")) {
            case "false":
                gsc.signOut()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_SHORT).show();
                            }
                        });
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
                mainActivity.finish();
                break;
            case "true":
                LoginManager.getInstance().logOut();
                Intent login1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(login1);
                mainActivity.finish();
                break;
            case "null": break;
        }

    }

    private void getSettingDialog(String s) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        switch (s){
            case "about":
                dialog.setContentView(R.layout.dialog_setting_info);
                break;
            case "policy":
                dialog.setContentView(R.layout.dialog_setting_policy);
                break;
            case "contact":
                dialog.setContentView(R.layout.dialog_setting_contact);
                break;
        }

        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        ImageButton imbCancel = (ImageButton)dialog.findViewById(R.id.imbCancel);
        imbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
