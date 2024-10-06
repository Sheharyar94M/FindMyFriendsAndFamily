package com.care360.findmyfamilyandfriends.auth.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.databinding.ActivityEmailSignInBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class EmailSignInActivity extends AppCompatActivity {

    private ActivityEmailSignInBinding binding;
    private final TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString();

            //validating the email
            if (Commons.validateEmailAddress(s)) {

                //enabling the 'continue' button
                binding.btnContEmailSignIn.setEnabled(true);
                binding.btnContEmailSignIn.setBackgroundResource(R.drawable.white_rounded_button);
            } else {

                //disable the 'continue' button
                binding.btnContEmailSignIn.setEnabled(false);
                binding.btnContEmailSignIn.setBackgroundResource(R.drawable.disabled_round_button);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing
        binding = ActivityEmailSignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //email TextWatcher
        binding.edtEmailSignIn.addTextChangedListener(emailTextWatcher);

        binding.btnContEmailSignIn.setOnClickListener(v -> {

            buttonClickListener();

        });

        //sign in with phone number
        binding.txtSignInWithNumber.setOnClickListener(v -> {

            startActivity(new Intent(EmailSignInActivity.this, PhoneNoSignInActivity.class));
            finish();

        });

    }

    private void buttonClickListener() {

        //saving email in preference
        SharedPreference.setEmailPref(binding.edtEmailSignIn.getText().toString());
        Log.i("signInEmail: ", String.valueOf(binding.edtEmailSignIn.getText()));

        //navigating to next activity
        startActivity(new Intent(this, EnterPasswordSignInActivity.class));
    }

}