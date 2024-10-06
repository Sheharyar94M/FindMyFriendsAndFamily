package com.care360.findmyfamilyandfriends.SharedPreference.ResetPassword.ByPhoneNo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.ResetPassword.receiver.SmsReceiver;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.auth.SignUp.NameSignUpActivity;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.databinding.ActivityOtpBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private static final String TAG = "OTP_ACT";
    //boolean for checking whether the activity is called from Sign up or Reset Password activity
    String calledFrom = "";
    //List of all edit texts (6 here)
    List<EditText> editTextList = new ArrayList<>();
    //firebase auth
    FirebaseAuth mAuth;
    private ActivityOtpBinding binding;
    // variable for saving the received OTP
    private String code;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private ProgressDialog dialog;
    private SmsReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing binding
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        smsReceiver = new SmsReceiver();

        MobileAds.initialize(this);

        dialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        dialog.setCancelable(false);
        dialog.setMessage("please wait.....");
        dialog.setTitle("Fetching your data");

        //banner
        adRequest = new AdRequest.Builder().build();

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        //getting intent data
        getIntentData();

        //adding all the edit texts in list
        addEditTextViews();

        //populating the edit texts as invite code view
        populateOptCodeView();

        binding.btnVerifyOtp.setOnClickListener(v -> buttonSubmitClickListener());

        //sending the OTP
        sendOTP();
    }

    private void getIntentData() {

        Intent intent = getIntent();
        calledFrom = intent.getStringExtra(Constants.OTP_ACT_KEY);

        //setting the phone no to text view from shared preference
        binding.txtViewPhoneNo.setText(SharedPreference.getPhoneNoPref());
    }

    private void addEditTextViews() {
        editTextList.add(binding.editInput1);
        editTextList.add(binding.editInput2);
        editTextList.add(binding.editInput3);
        editTextList.add(binding.editInput4);
        editTextList.add(binding.editInput5);
        editTextList.add(binding.editInput6);
    }

    private void populateOptCodeView() {

        for (int j = 0; j < 6; j++)
        {
            //current iteration variable
            int currentIndex = j;

            editTextList.get(j).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    String s = charSequence.toString().trim();

                    if(s.length() == 1)
                    {
                        if(currentIndex < 5)
                        {
                            editTextList.get(currentIndex+1).requestFocus();
                        }

                        editTextList.get(currentIndex).setBackgroundResource(R.drawable.drawable_pin_entered);
                    }
                    else if(editTextList.get(currentIndex).isFocused())
                    {
                        editTextList.get(currentIndex).setBackgroundResource(R.drawable.pin_view_state_list);
                    }
                    else if(!editTextList.get(currentIndex).isFocused() || s.length() == 0)
                    {
                        editTextList.get(currentIndex).setBackgroundResource(R.drawable.drawable_no_pin);
                    }

                    //checking state of verify otp button state (enabled/disabled)
                    optButtonStatus();

                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
        }
    }

    private boolean isEditTextLengthZero(EditText editText){

        return editText.getText().toString().trim().length() == 1;
    }

    //for checking the status (enabled/disabled) of verify otp button
    private void optButtonStatus() {

        //checking if all Edit texts length are equal to 1
        if(isEditTextLengthZero(binding.editInput1) && isEditTextLengthZero(binding.editInput2) &&
                isEditTextLengthZero(binding.editInput3) && isEditTextLengthZero(binding.editInput4) &&
                isEditTextLengthZero(binding.editInput5) && isEditTextLengthZero(binding.editInput6))
        {
            //setting the verify otp button to enabled
            binding.btnVerifyOtp.setEnabled(true);
            binding.btnVerifyOtp.setBackgroundResource(R.drawable.orange_rounded_button);
            binding.btnVerifyOtp.setTextColor(Color.WHITE);

        }
        else {
            //setting the verify otp button to disabled
            binding.btnVerifyOtp.setEnabled(false);
            binding.btnVerifyOtp.setBackgroundResource(R.drawable.disabled_round_button);
            binding.btnVerifyOtp.setTextColor(getColor(R.color.orange));
        }
    }

    private void buttonSubmitClickListener() {

        dialog.show();

        String enteredOtpCode = getEditTextData(binding.editInput1).concat(getEditTextData(binding.editInput2))
                .concat(getEditTextData(binding.editInput3).concat(getEditTextData(binding.editInput4)
                        .concat(getEditTextData(binding.editInput5).concat(getEditTextData(binding.editInput6)))));

        if(enteredOtpCode.equals(code)) {
            //navigating to next activity
            if(calledFrom.equals("signup")) {
                //called from signup
                Commons.verifyUserFromDatabaseAndGetData(OTPActivity.this,dialog,calledFrom);
            }
            else if(calledFrom.equals("reset_password")){
                //called from forget password
                Commons.verifyUserFromDatabaseAndGetData(OTPActivity.this,dialog,calledFrom);

            }
            else if(calledFrom.equals("signin")) {
                //called from sign-in (phone) activity (this else will be used if command asks to login through phone)

                //get data (FROM FIREBASE) {fetch the data from firestore through the phone number in 'SharedPreference.getPhonePref()'}
                //save in preference 'email' {if on Location fragment no data is displayed! then [this means
                // 'FirebaseAuth.getInstance().getCurrentUser()).getEmail()' is null simple words no current user and session is created through OTP}
                Commons.verifyUserFromDatabaseAndGetData(OTPActivity.this,dialog,calledFrom);
            }
        }
        else {
            Toast.makeText(this, "Unable to verify code!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //function for returning the edit text values
    private String getEditTextData(EditText editText){
        return editText.getText().toString().trim();
    }

    private void sendOTP() {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(SharedPreference.getPhoneNoPref())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void resendOtpCounter() {

        new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

                //setting the count down to text view
                binding.txtViewOtpCountDown.setText("in " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {

                //setting the count down text view visibility to gone
                binding.txtViewOtpCountDown.setVisibility(View.GONE);

                //setting the spannable properties of text view
                SpannableString spannableString = new SpannableString(binding.txtViewResendOtp.getText().toString());

                //color span for hyperlink
                ForegroundColorSpan fcsOtpResend=new ForegroundColorSpan(getColor(R.color.orange));

                //applying the hyperlink color to textview
                spannableString.setSpan(fcsOtpResend,0,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                ClickableSpan clickableSpanResendOtp = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {

                        //resend OTP
                        sendOTP();

                        Toast.makeText(OTPActivity.this, "OTP Resent Successfully.", Toast.LENGTH_LONG).show();

                        //setting count down text view visibility to Visible
                        binding.txtViewOtpCountDown.setVisibility(View.VISIBLE);

                        binding.txtViewResendOtp.setText(getString(R.string.resend_code_in));
                        binding.txtViewResendOtp.setTextColor(getColor(R.color.darkGrey));

                        //this if condition will be called in case of if user receives OTP, but resend the otp code
                        if(binding.editInput3.getText().length() == 1) {

                            for(int i =0; i < 6; i++) {
                                editTextList.get(i).setText("");
                                editTextList.get(i).clearFocus();
                            }
                        }

                        //setting the progress bar visibility to VISIBLE
                        binding.progressBar.setVisibility(View.VISIBLE);

                    }
                };

                //Making the text view clickable
                spannableString.setSpan(clickableSpanResendOtp,0,11,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                binding.txtViewResendOtp.setText(spannableString);
                binding.txtViewResendOtp.setMovementMethod(LinkMovementMethod.getInstance());

            }

        }.start();
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.i(TAG, "onVerificationCompleted: ");

            code = phoneAuthCredential.getSmsCode();

            Log.i("verificationCode: ",code);

            mAuth.signInWithCredential(phoneAuthCredential);

            if(code != null) {
                for(int i=0; i<code.length() ; i++) {
                    editTextList.get(i).setText(String.valueOf(code.charAt(i)));
                }
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e(TAG, "onVerificationFailed: " + e.getMessage());

            //setting the circular progress bar visibility to gone
            binding.progressBar.setVisibility(View.GONE);

            //calling the count down timer
            resendOtpCounter();

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Objects.requireNonNull(e.getMessage()),Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("Ok", v -> {

               finish();
            });

            snackbar.setTextColor(Color.WHITE);
            snackbar.show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.i(TAG, "onCodeSent: ");

            //setting the circular progress bar visibility to gone
            binding.progressBar.setVisibility(View.GONE);

            //requesting the focus at 1st edit text (as default)
            binding.editInput1.requestFocus();
            binding.editInput1.setBackgroundResource(R.drawable.pin_view_state_list);

            //calling the count down timer
            resendOtpCounter();

        }
    };

}