package com.care360.findmyfamilyandfriends.auth.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.ResetPassword.ByPhoneNo.OTPActivity;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.databinding.ActivityPhoneNoSignInBinding;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNoSignInActivity extends AppCompatActivity {

    //list of registered user
    private ActivityPhoneNoSignInBinding binding;
    private String countryCode;
    //variables for verifying whether entered number is valid or not
    private PhoneNumberUtil.PhoneNumberType isMobile = null;
    private boolean isPhoneNoValid = false;
    String phoneNo = "";
    private final TextWatcher numberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString().trim();

            //verifying whether entered number is valid and is a mobile phone number or not
            validatePhoneNumber(s, countryCode);

            //if number is valid, then enables the continue. Else continue button is disabled.
            if (isPhoneNoValid && (isMobile.equals(PhoneNumberUtil.PhoneNumberType.MOBILE) || isMobile.equals(PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE))) {

                //enabling the continue button

                binding.btnContPhoneSignIn.setEnabled(true);
                binding.btnContPhoneSignIn.setBackgroundResource(R.drawable.white_rounded_button);
            } else {

                binding.btnContPhoneSignIn.setEnabled(false);
                binding.btnContPhoneSignIn.setBackgroundResource(R.drawable.disabled_round_button);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing binding
        binding = ActivityPhoneNoSignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //getting the default and selected country code
        pickCountryCode();

        //TextWatcher
        binding.edtPhoneSignIn.addTextChangedListener(numberTextWatcher);

        //button continue click listener
        binding.btnContPhoneSignIn.setOnClickListener(v -> buttonCLickListener());

        //Textview login with email click listener
        binding.txtSignInWithEmail.setOnClickListener(v -> {
            startActivity(new Intent(this, EmailSignInActivity.class));
            finish();
        });
    }

    private void pickCountryCode() {

        //selects the default country code
        countryCode = binding.countryCodePickerSignIn.getSelectedCountryCodeWithPlus();

        //country code picker
        binding.countryCodePickerSignIn.setOnCountryChangeListener(() -> countryCode = binding.countryCodePickerSignIn.getSelectedCountryCode());
    }

    private void validatePhoneNumber(String number, String code) {

        PhoneNumberUtil numberUtil = PhoneNumberUtil.getInstance();

        String isoCode = numberUtil.getRegionCodeForCountryCode(Integer.parseInt(code));

        try {
            Phonenumber.PhoneNumber phoneNumber = numberUtil.parse(number, isoCode);

            isPhoneNoValid = numberUtil.isValidNumber(phoneNumber);
            isMobile = numberUtil.getNumberType(phoneNumber);

        } catch (NumberParseException e) {
            e.printStackTrace();
            Log.e("ERROR_PHONE_SIGN_IN", "NumberParseException: " + e.getMessage());
        }
    }

    private void buttonCLickListener() {

        String tempNumber = binding.edtPhoneSignIn.getText().toString().trim();

        if (tempNumber.startsWith("0")) {
            StringBuilder sb = new StringBuilder(tempNumber);
            sb.deleteCharAt(0);

            //concatenating entered number with country code
            phoneNo = countryCode;
            phoneNo = phoneNo.concat(sb.toString());
        } else {
            //concatenating entered number with country code
            phoneNo = countryCode;
            phoneNo = phoneNo.concat(tempNumber);
        }

        //saving to sharedPreference
        SharedPreference.setPhoneNoPref(phoneNo);

        //login through otp
        Intent intent = new Intent(PhoneNoSignInActivity.this, OTPActivity.class);
        intent.putExtra(Constants.OTP_ACT_KEY,"signin");
        startActivity(intent);
        finish();

    }
}