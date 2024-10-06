// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEmailSignUpBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatButton btnContEmailSignUp;

  @NonNull
  public final EditText edtEmailSignUp;

  @NonNull
  public final TextView txtAddEmail;

  private ActivityEmailSignUpBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatButton btnContEmailSignUp, @NonNull EditText edtEmailSignUp,
      @NonNull TextView txtAddEmail) {
    this.rootView = rootView;
    this.btnContEmailSignUp = btnContEmailSignUp;
    this.edtEmailSignUp = edtEmailSignUp;
    this.txtAddEmail = txtAddEmail;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEmailSignUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEmailSignUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_email_sign_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEmailSignUpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_cont_email_sign_up;
      AppCompatButton btnContEmailSignUp = ViewBindings.findChildViewById(rootView, id);
      if (btnContEmailSignUp == null) {
        break missingId;
      }

      id = R.id.edt_email_sign_up;
      EditText edtEmailSignUp = ViewBindings.findChildViewById(rootView, id);
      if (edtEmailSignUp == null) {
        break missingId;
      }

      id = R.id.txt_add_email;
      TextView txtAddEmail = ViewBindings.findChildViewById(rootView, id);
      if (txtAddEmail == null) {
        break missingId;
      }

      return new ActivityEmailSignUpBinding((ConstraintLayout) rootView, btnContEmailSignUp,
          edtEmailSignUp, txtAddEmail);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
