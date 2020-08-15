package com.courseraandroid.myfirstappcoursera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.courseraandroid.myfirstappcoursera.albums.AlbumsActivity;
import com.courseraandroid.myfirstappcoursera.model.User;
import com.courseraandroid.myfirstappcoursera.model.UserForAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView mLogin;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;


    public static AuthFragment newInstance() {
        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmailValid() && isPasswordValid()) {

                OkHttpClient client = ApiUtils.getBasicAuthClient(mLogin.getText().toString(),
                        mPassword.getText().toString(),
                        true);
                ApiUtils.resetRetrofit();
                ApiUtils.getApi().authentication().enqueue(new Callback<UserForAuth>() {
                    Handler handler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(Call<UserForAuth> call, Response<UserForAuth> response) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!response.isSuccessful()) {
                                    showMessage(R.string.auth_error);

                                } else {
                                    try {
                                        //Gson gson = new Gson();
                                        //JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                                        //User user = gson.fromJson(json.get("data"), User.class);

                                        //UserForAuth userForAuth = response.body();
                                        //User user = new User(userForAuth.getData().getEmail(), userForAuth.getData().getName(), "");

                                       // Intent startProfileIntent =
                                       //         new Intent(getActivity(), ProfileActivity.class);
                                        //startProfileIntent.putExtra(ProfileActivity.USER_KEY, user);
                                        //startActivity(startProfileIntent);
                                        startActivity(new Intent(getActivity(), AlbumsActivity.class));
                                        getActivity().finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("EXcEPTION");
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<UserForAuth> call, Throwable t) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showMessage(R.string.request_error);
                            }
                        });
                    }
                });

            } else {
                showMessage(R.string.input_error);
            }
        }
    };

    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
                    .addToBackStack(RegistrationFragment.class.getName())
                    .commit();
        }
    };

    private View.OnFocusChangeListener mOnLoginFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                mLogin.showDropDown();
            }
        }
    };

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mLogin.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mLogin.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(mPassword.getText());
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mLogin = v.findViewById(R.id.etLogin);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.butEnter);
        mRegister = v.findViewById(R.id.butRegister);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
        mLogin.setOnFocusChangeListener(mOnLoginFocusChangeListener);


        return v;
    }
}
