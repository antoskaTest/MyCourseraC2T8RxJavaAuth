package com.courseraandroid.myfirstappcoursera;

import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.courseraandroid.myfirstappcoursera.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private EditText mLogin;
    private EditText mName;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mRegistration;
   // private SharedPreferencesHelper mSharedPreferencesHelper;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isInputValid()) {
                User user = new User(
                        mLogin.getText().toString(),
                        mName.getText().toString(),
                        mPassword.getText().toString()
                );


                ApiUtils.getApi().registration(user)
                        .enqueue(new Callback<Void>() {
                                    //Используем Handler чтобы показывать ошибки в Main потоке
                                     Handler handler = new Handler(getActivity().getMainLooper());
                                     Gson gson = new Gson();
                                     @Override
                                     public void onResponse(Call<Void> call, final Response<Void> response) {
                                         handler.post(new Runnable() {
                                             @Override
                                             public void run() {
                                                 if (response.isSuccessful()) {
                                                     showMessage(R.string.login_register_success);
                                                     getFragmentManager().popBackStack();
                                                 } else {
                                                     //todo детальная обработка ошибок
                                                  switch (response.code()){
                                                      case 500:
                                                          showMessage(R.string.error_500);
                                                          break;
                                                      case 400:
                                                          showMessage(R.string.error_400);
                                                          try {
//                                                              JsonObject json = gson.fromJson(response.errorBody().string(), JsonObject.class);
//                                                              JsonElement jsonElement = json.get("errors");
//
//                                                              ErrorRegistration error = gson.fromJson(jsonElement, ErrorRegistration.class);
//                                                              printError(error);

                                                              Type type = new TypeToken<ErrorRegistration>() {}.getType();
                                                              ErrorRegistration errorRegistration = gson.fromJson(response.errorBody().charStream(),type);
                                                              //printError(errorRegistration);
                                                              showError(errorRegistration);
                                                          } catch (Exception e) {
                                                              e.printStackTrace();
                                                          }
                                                          break;
                                                  }
                                                     //showMessage(R.string.login_register_error);
                                                 }
                                             }
                                         });
                                     }

                                     @Override
                                     public void onFailure(Call<Void> call, Throwable t) {
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

    private void showError(ErrorRegistration error) {
        List<String> listEmail = error.getErrors().getEmail();
        List<String> listName = error.getErrors().getName();
        List<String> listPassword = error.getErrors().getPassword();
        if(listEmail != null) {
            mLogin.setError(listEmail.get(0));
        }
        if(listName != null) {
            mName.setError(listName.get(0));
        }
        if(listPassword != null) {
            mPassword.setError(listPassword.get(0));
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        //mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        mLogin = view.findViewById(R.id.etLogin);
        mName = view.findViewById(R.id.etName);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordAgain = view.findViewById(R.id.tvPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegistration);

        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    private boolean isInputValid() {
        String email = mLogin.getText().toString();
        if (isEmailValid(email) && isPasswordsValid()) {
            return true;
        }

        return false;
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordsValid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordAgain);
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}
