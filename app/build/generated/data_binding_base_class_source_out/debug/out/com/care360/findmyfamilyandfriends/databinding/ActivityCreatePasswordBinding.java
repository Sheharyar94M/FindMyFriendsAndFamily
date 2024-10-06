// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCreatePasswordBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatButton btnContPasswordSignUp;

  @NonNull
  public final TextInputEditText edtPasswordSignUp;

  @NonNull
  public final TextInputLayout layoutPasswordSignUp;

  @NonNull
  public final TextView txtPasswordSignUp;

  private ActivityCreatePasswordBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatButton btnContPasswordSignUp, @NonNull TextInputEditText edtPasswordSignUp,
      @NonNull TextInputLayout layoutPasswordSignUp, @NonNull TextView txtPasswordSignUp) {
    this.rootView = rootView;
    this.btnContPasswordSignUp = btnContPasswordSignUp;
    this.edtPasswordSignUp = edtPasswordSignUp;
    this.layoutPasswordSignUp = layoutPasswordSignUp;
    this.txtPasswordSignUp = txtPasswordSignUp;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCreatePasswordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCreatePasswordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_create_password, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCreatePasswordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_cont_password_sign_up;
      AppCompatButton btnContPasswordSignUp = ViewBindings.findChildViewById(rootView, id);
      if (btnContPasswordSignUp == null) {
        break missingId;
      }

      id = R.id.edt_password_sign_up;
      TextInputEditText edtPasswordSignUp = ViewBindings.findChildViewById(rootView, id);
      if (edtPasswordSignUp == null) {
        break missingId;
      }

      id = R.id.layout_password_sign_up;
      TextInputLayout layoutPasswordSignUp = ViewBindings.findChildViewById(rootView, id);
      if (layoutPasswordSignUp == null) {
        break missingId;
      }

      id = R.id.txt_password_sign_up;
      TextView txtPasswordSignUp = ViewBindings.findChildViewById(rootView, id);
      if (txtPasswordSignUp == null) {
        break missingId;
      }

      return new ActivityCreatePasswordBinding((ConstraintLayout) rootView, btnContPasswordSignUp,
          edtPasswordSignUp, layoutPasswordSignUp, txtPasswordSignUp);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
