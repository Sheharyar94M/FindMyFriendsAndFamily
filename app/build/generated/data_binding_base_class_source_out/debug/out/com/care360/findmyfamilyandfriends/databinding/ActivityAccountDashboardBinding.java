// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import com.google.android.ads.nativetemplates.TemplateView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAccountDashboardBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TemplateView adTemplate;

  @NonNull
  public final ConstraintLayout consProfile;

  @NonNull
  public final ImageFilterView imageProfile;

  @NonNull
  public final TextView textAccountDetails;

  @NonNull
  public final TextView textAccountManagement;

  @NonNull
  public final TextView textBottomLine1;

  @NonNull
  public final TextView textBottomLine2;

  @NonNull
  public final TextView textChangePassword;

  @NonNull
  public final TextView textDeleteAccount;

  @NonNull
  public final TextView textEditPhoneNo;

  @NonNull
  public final TextView textNameLetter;

  @NonNull
  public final TextView textProfile;

  @NonNull
  public final TextView textUserFullName;

  @NonNull
  public final Toolbar toolbarAccount;

  private ActivityAccountDashboardBinding(@NonNull ConstraintLayout rootView,
      @NonNull TemplateView adTemplate, @NonNull ConstraintLayout consProfile,
      @NonNull ImageFilterView imageProfile, @NonNull TextView textAccountDetails,
      @NonNull TextView textAccountManagement, @NonNull TextView textBottomLine1,
      @NonNull TextView textBottomLine2, @NonNull TextView textChangePassword,
      @NonNull TextView textDeleteAccount, @NonNull TextView textEditPhoneNo,
      @NonNull TextView textNameLetter, @NonNull TextView textProfile,
      @NonNull TextView textUserFullName, @NonNull Toolbar toolbarAccount) {
    this.rootView = rootView;
    this.adTemplate = adTemplate;
    this.consProfile = consProfile;
    this.imageProfile = imageProfile;
    this.textAccountDetails = textAccountDetails;
    this.textAccountManagement = textAccountManagement;
    this.textBottomLine1 = textBottomLine1;
    this.textBottomLine2 = textBottomLine2;
    this.textChangePassword = textChangePassword;
    this.textDeleteAccount = textDeleteAccount;
    this.textEditPhoneNo = textEditPhoneNo;
    this.textNameLetter = textNameLetter;
    this.textProfile = textProfile;
    this.textUserFullName = textUserFullName;
    this.toolbarAccount = toolbarAccount;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAccountDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAccountDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_account_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAccountDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ad_template;
      TemplateView adTemplate = ViewBindings.findChildViewById(rootView, id);
      if (adTemplate == null) {
        break missingId;
      }

      id = R.id.cons_profile;
      ConstraintLayout consProfile = ViewBindings.findChildViewById(rootView, id);
      if (consProfile == null) {
        break missingId;
      }

      id = R.id.image_profile;
      ImageFilterView imageProfile = ViewBindings.findChildViewById(rootView, id);
      if (imageProfile == null) {
        break missingId;
      }

      id = R.id.text_account_details;
      TextView textAccountDetails = ViewBindings.findChildViewById(rootView, id);
      if (textAccountDetails == null) {
        break missingId;
      }

      id = R.id.text_account_management;
      TextView textAccountManagement = ViewBindings.findChildViewById(rootView, id);
      if (textAccountManagement == null) {
        break missingId;
      }

      id = R.id.text_bottom_line_1;
      TextView textBottomLine1 = ViewBindings.findChildViewById(rootView, id);
      if (textBottomLine1 == null) {
        break missingId;
      }

      id = R.id.text_bottom_line_2;
      TextView textBottomLine2 = ViewBindings.findChildViewById(rootView, id);
      if (textBottomLine2 == null) {
        break missingId;
      }

      id = R.id.text_change_password;
      TextView textChangePassword = ViewBindings.findChildViewById(rootView, id);
      if (textChangePassword == null) {
        break missingId;
      }

      id = R.id.text_delete_account;
      TextView textDeleteAccount = ViewBindings.findChildViewById(rootView, id);
      if (textDeleteAccount == null) {
        break missingId;
      }

      id = R.id.text_edit_phone_no;
      TextView textEditPhoneNo = ViewBindings.findChildViewById(rootView, id);
      if (textEditPhoneNo == null) {
        break missingId;
      }

      id = R.id.text_name_letter;
      TextView textNameLetter = ViewBindings.findChildViewById(rootView, id);
      if (textNameLetter == null) {
        break missingId;
      }

      id = R.id.text_profile;
      TextView textProfile = ViewBindings.findChildViewById(rootView, id);
      if (textProfile == null) {
        break missingId;
      }

      id = R.id.text_user_full_name;
      TextView textUserFullName = ViewBindings.findChildViewById(rootView, id);
      if (textUserFullName == null) {
        break missingId;
      }

      id = R.id.toolbar_account;
      Toolbar toolbarAccount = ViewBindings.findChildViewById(rootView, id);
      if (toolbarAccount == null) {
        break missingId;
      }

      return new ActivityAccountDashboardBinding((ConstraintLayout) rootView, adTemplate,
          consProfile, imageProfile, textAccountDetails, textAccountManagement, textBottomLine1,
          textBottomLine2, textChangePassword, textDeleteAccount, textEditPhoneNo, textNameLetter,
          textProfile, textUserFullName, toolbarAccount);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
