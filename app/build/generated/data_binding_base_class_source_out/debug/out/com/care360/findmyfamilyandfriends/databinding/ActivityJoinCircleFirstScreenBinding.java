// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityJoinCircleFirstScreenBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton btnCreateCircle;

  @NonNull
  public final MaterialButton btnSubmit;

  @NonNull
  public final ConstraintLayout cons1;

  @NonNull
  public final ConstraintLayout cons2;

  @NonNull
  public final EditText edtInput1;

  @NonNull
  public final EditText edtInput2;

  @NonNull
  public final EditText edtInput3;

  @NonNull
  public final EditText edtInput4;

  @NonNull
  public final EditText edtInput5;

  @NonNull
  public final EditText edtInput6;

  @NonNull
  public final Flow flowPinView;

  @NonNull
  public final MaterialTextView matTxt1Cons2;

  @NonNull
  public final MaterialTextView matTxt2Cons2;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final TextView txt1Cons1;

  @NonNull
  public final TextView txtMsgCons1;

  private ActivityJoinCircleFirstScreenBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialButton btnCreateCircle, @NonNull MaterialButton btnSubmit,
      @NonNull ConstraintLayout cons1, @NonNull ConstraintLayout cons2, @NonNull EditText edtInput1,
      @NonNull EditText edtInput2, @NonNull EditText edtInput3, @NonNull EditText edtInput4,
      @NonNull EditText edtInput5, @NonNull EditText edtInput6, @NonNull Flow flowPinView,
      @NonNull MaterialTextView matTxt1Cons2, @NonNull MaterialTextView matTxt2Cons2,
      @NonNull ProgressBar progressBar, @NonNull TextView txt1Cons1,
      @NonNull TextView txtMsgCons1) {
    this.rootView = rootView;
    this.btnCreateCircle = btnCreateCircle;
    this.btnSubmit = btnSubmit;
    this.cons1 = cons1;
    this.cons2 = cons2;
    this.edtInput1 = edtInput1;
    this.edtInput2 = edtInput2;
    this.edtInput3 = edtInput3;
    this.edtInput4 = edtInput4;
    this.edtInput5 = edtInput5;
    this.edtInput6 = edtInput6;
    this.flowPinView = flowPinView;
    this.matTxt1Cons2 = matTxt1Cons2;
    this.matTxt2Cons2 = matTxt2Cons2;
    this.progressBar = progressBar;
    this.txt1Cons1 = txt1Cons1;
    this.txtMsgCons1 = txtMsgCons1;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityJoinCircleFirstScreenBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityJoinCircleFirstScreenBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_join_circle_first_screen, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityJoinCircleFirstScreenBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_create_circle;
      MaterialButton btnCreateCircle = ViewBindings.findChildViewById(rootView, id);
      if (btnCreateCircle == null) {
        break missingId;
      }

      id = R.id.btn_submit;
      MaterialButton btnSubmit = ViewBindings.findChildViewById(rootView, id);
      if (btnSubmit == null) {
        break missingId;
      }

      id = R.id.cons_1;
      ConstraintLayout cons1 = ViewBindings.findChildViewById(rootView, id);
      if (cons1 == null) {
        break missingId;
      }

      id = R.id.cons_2;
      ConstraintLayout cons2 = ViewBindings.findChildViewById(rootView, id);
      if (cons2 == null) {
        break missingId;
      }

      id = R.id.edt_input_1;
      EditText edtInput1 = ViewBindings.findChildViewById(rootView, id);
      if (edtInput1 == null) {
        break missingId;
      }

      id = R.id.edt_input_2;
      EditText edtInput2 = ViewBindings.findChildViewById(rootView, id);
      if (edtInput2 == null) {
        break missingId;
      }

      id = R.id.edt_input_3;
      EditText edtInput3 = ViewBindings.findChildViewById(rootView, id);
      if (edtInput3 == null) {
        break missingId;
      }

      id = R.id.edt_input_4;
      EditText edtInput4 = ViewBindings.findChildViewById(rootView, id);
      if (edtInput4 == null) {
        break missingId;
      }

      id = R.id.edt_input_5;
      EditText edtInput5 = ViewBindings.findChildViewById(rootView, id);
      if (edtInput5 == null) {
        break missingId;
      }

      id = R.id.edt_input_6;
      EditText edtInput6 = ViewBindings.findChildViewById(rootView, id);
      if (edtInput6 == null) {
        break missingId;
      }

      id = R.id.flow_pin_view;
      Flow flowPinView = ViewBindings.findChildViewById(rootView, id);
      if (flowPinView == null) {
        break missingId;
      }

      id = R.id.mat_txt_1_cons_2;
      MaterialTextView matTxt1Cons2 = ViewBindings.findChildViewById(rootView, id);
      if (matTxt1Cons2 == null) {
        break missingId;
      }

      id = R.id.mat_txt_2_cons_2;
      MaterialTextView matTxt2Cons2 = ViewBindings.findChildViewById(rootView, id);
      if (matTxt2Cons2 == null) {
        break missingId;
      }

      id = R.id.progress_bar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.txt_1_cons_1;
      TextView txt1Cons1 = ViewBindings.findChildViewById(rootView, id);
      if (txt1Cons1 == null) {
        break missingId;
      }

      id = R.id.txt_msg_cons_1;
      TextView txtMsgCons1 = ViewBindings.findChildViewById(rootView, id);
      if (txtMsgCons1 == null) {
        break missingId;
      }

      return new ActivityJoinCircleFirstScreenBinding((ConstraintLayout) rootView, btnCreateCircle,
          btnSubmit, cons1, cons2, edtInput1, edtInput2, edtInput3, edtInput4, edtInput5, edtInput6,
          flowPinView, matTxt1Cons2, matTxt2Cons2, progressBar, txt1Cons1, txtMsgCons1);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
