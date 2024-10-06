// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutRecyclerViewBottomSheetBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout consBattery;

  @NonNull
  public final ConstraintLayout consMemberBottomSheet;

  @NonNull
  public final AppCompatImageView imgViewBatteryStatus;

  @NonNull
  public final ImageFilterView imgViewMemberProfile;

  @NonNull
  public final AppCompatTextView txtViewBatteryPercentage;

  @NonNull
  public final AppCompatTextView txtViewBottomLine;

  @NonNull
  public final AppCompatTextView txtViewLastKnownAddress;

  @NonNull
  public final TextView txtViewMemberFirstChar;

  @NonNull
  public final AppCompatTextView txtViewMemberName;

  @NonNull
  public final AppCompatTextView txtViewTimestamp;

  private LayoutRecyclerViewBottomSheetBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout consBattery, @NonNull ConstraintLayout consMemberBottomSheet,
      @NonNull AppCompatImageView imgViewBatteryStatus,
      @NonNull ImageFilterView imgViewMemberProfile,
      @NonNull AppCompatTextView txtViewBatteryPercentage,
      @NonNull AppCompatTextView txtViewBottomLine,
      @NonNull AppCompatTextView txtViewLastKnownAddress, @NonNull TextView txtViewMemberFirstChar,
      @NonNull AppCompatTextView txtViewMemberName, @NonNull AppCompatTextView txtViewTimestamp) {
    this.rootView = rootView;
    this.consBattery = consBattery;
    this.consMemberBottomSheet = consMemberBottomSheet;
    this.imgViewBatteryStatus = imgViewBatteryStatus;
    this.imgViewMemberProfile = imgViewMemberProfile;
    this.txtViewBatteryPercentage = txtViewBatteryPercentage;
    this.txtViewBottomLine = txtViewBottomLine;
    this.txtViewLastKnownAddress = txtViewLastKnownAddress;
    this.txtViewMemberFirstChar = txtViewMemberFirstChar;
    this.txtViewMemberName = txtViewMemberName;
    this.txtViewTimestamp = txtViewTimestamp;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutRecyclerViewBottomSheetBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutRecyclerViewBottomSheetBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_recycler_view_bottom_sheet, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutRecyclerViewBottomSheetBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cons_battery;
      ConstraintLayout consBattery = ViewBindings.findChildViewById(rootView, id);
      if (consBattery == null) {
        break missingId;
      }

      ConstraintLayout consMemberBottomSheet = (ConstraintLayout) rootView;

      id = R.id.img_view_battery_status;
      AppCompatImageView imgViewBatteryStatus = ViewBindings.findChildViewById(rootView, id);
      if (imgViewBatteryStatus == null) {
        break missingId;
      }

      id = R.id.img_view_member_profile;
      ImageFilterView imgViewMemberProfile = ViewBindings.findChildViewById(rootView, id);
      if (imgViewMemberProfile == null) {
        break missingId;
      }

      id = R.id.txt_view_battery_percentage;
      AppCompatTextView txtViewBatteryPercentage = ViewBindings.findChildViewById(rootView, id);
      if (txtViewBatteryPercentage == null) {
        break missingId;
      }

      id = R.id.txt_view_bottom_line;
      AppCompatTextView txtViewBottomLine = ViewBindings.findChildViewById(rootView, id);
      if (txtViewBottomLine == null) {
        break missingId;
      }

      id = R.id.txt_view_last_known_address;
      AppCompatTextView txtViewLastKnownAddress = ViewBindings.findChildViewById(rootView, id);
      if (txtViewLastKnownAddress == null) {
        break missingId;
      }

      id = R.id.txt_view_member_first_char;
      TextView txtViewMemberFirstChar = ViewBindings.findChildViewById(rootView, id);
      if (txtViewMemberFirstChar == null) {
        break missingId;
      }

      id = R.id.txt_view_member_name;
      AppCompatTextView txtViewMemberName = ViewBindings.findChildViewById(rootView, id);
      if (txtViewMemberName == null) {
        break missingId;
      }

      id = R.id.txt_view_timestamp;
      AppCompatTextView txtViewTimestamp = ViewBindings.findChildViewById(rootView, id);
      if (txtViewTimestamp == null) {
        break missingId;
      }

      return new LayoutRecyclerViewBottomSheetBinding((ConstraintLayout) rootView, consBattery,
          consMemberBottomSheet, imgViewBatteryStatus, imgViewMemberProfile,
          txtViewBatteryPercentage, txtViewBottomLine, txtViewLastKnownAddress,
          txtViewMemberFirstChar, txtViewMemberName, txtViewTimestamp);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
