// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityContactDetailBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout adaptiveBanner;

  @NonNull
  public final Button btnResentInvite;

  @NonNull
  public final ConstraintLayout consContact;

  @NonNull
  public final TextView deleteContact;

  @NonNull
  public final TextView txtContactLetters;

  @NonNull
  public final TextView txtContactName;

  @NonNull
  public final TextView txtContactNumber;

  private ActivityContactDetailBinding(@NonNull ConstraintLayout rootView,
      @NonNull FrameLayout adaptiveBanner, @NonNull Button btnResentInvite,
      @NonNull ConstraintLayout consContact, @NonNull TextView deleteContact,
      @NonNull TextView txtContactLetters, @NonNull TextView txtContactName,
      @NonNull TextView txtContactNumber) {
    this.rootView = rootView;
    this.adaptiveBanner = adaptiveBanner;
    this.btnResentInvite = btnResentInvite;
    this.consContact = consContact;
    this.deleteContact = deleteContact;
    this.txtContactLetters = txtContactLetters;
    this.txtContactName = txtContactName;
    this.txtContactNumber = txtContactNumber;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityContactDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityContactDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_contact_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityContactDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.adaptiveBanner;
      FrameLayout adaptiveBanner = ViewBindings.findChildViewById(rootView, id);
      if (adaptiveBanner == null) {
        break missingId;
      }

      id = R.id.btn_resent_invite;
      Button btnResentInvite = ViewBindings.findChildViewById(rootView, id);
      if (btnResentInvite == null) {
        break missingId;
      }

      id = R.id.cons_contact;
      ConstraintLayout consContact = ViewBindings.findChildViewById(rootView, id);
      if (consContact == null) {
        break missingId;
      }

      id = R.id.delete_contact;
      TextView deleteContact = ViewBindings.findChildViewById(rootView, id);
      if (deleteContact == null) {
        break missingId;
      }

      id = R.id.txt_contact_letters;
      TextView txtContactLetters = ViewBindings.findChildViewById(rootView, id);
      if (txtContactLetters == null) {
        break missingId;
      }

      id = R.id.txt_contact_name;
      TextView txtContactName = ViewBindings.findChildViewById(rootView, id);
      if (txtContactName == null) {
        break missingId;
      }

      id = R.id.txt_contact_number;
      TextView txtContactNumber = ViewBindings.findChildViewById(rootView, id);
      if (txtContactNumber == null) {
        break missingId;
      }

      return new ActivityContactDetailBinding((ConstraintLayout) rootView, adaptiveBanner,
          btnResentInvite, consContact, deleteContact, txtContactLetters, txtContactName,
          txtContactNumber);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
