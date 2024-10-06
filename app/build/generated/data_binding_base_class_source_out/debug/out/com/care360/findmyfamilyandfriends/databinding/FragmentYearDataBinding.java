// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.card.MaterialCardView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentYearDataBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatImageView appCompatImageView;

  @NonNull
  public final AppCompatImageView appCompatImageView2;

  @NonNull
  public final AppCompatImageView appCompatImageView3;

  @NonNull
  public final AppCompatTextView appCompatTextView3;

  @NonNull
  public final AppCompatTextView appCompatTextView4;

  @NonNull
  public final AppCompatTextView appCompatTextView5;

  @NonNull
  public final PieChart chart;

  @NonNull
  public final LinearLayout linearLayout3;

  @NonNull
  public final CardView listLayoutYear;

  @NonNull
  public final MaterialCardView locationCardButtonYear;

  @NonNull
  public final MovementRecordItemviewDotBinding movementRecordLayout;

  @NonNull
  public final AppCompatTextView noActivitiesFound;

  @NonNull
  public final AppCompatTextView noDataFound;

  @NonNull
  public final ProgressBar progressChart;

  @NonNull
  public final ProgressBar progressList;

  @NonNull
  public final ShimmerFrameLayout shimmerLayoutYear;

  @NonNull
  public final LayoutRecyclerViewBottomSheetBinding userProfileItemview;

  private FragmentYearDataBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatImageView appCompatImageView,
      @NonNull AppCompatImageView appCompatImageView2,
      @NonNull AppCompatImageView appCompatImageView3,
      @NonNull AppCompatTextView appCompatTextView3, @NonNull AppCompatTextView appCompatTextView4,
      @NonNull AppCompatTextView appCompatTextView5, @NonNull PieChart chart,
      @NonNull LinearLayout linearLayout3, @NonNull CardView listLayoutYear,
      @NonNull MaterialCardView locationCardButtonYear,
      @NonNull MovementRecordItemviewDotBinding movementRecordLayout,
      @NonNull AppCompatTextView noActivitiesFound, @NonNull AppCompatTextView noDataFound,
      @NonNull ProgressBar progressChart, @NonNull ProgressBar progressList,
      @NonNull ShimmerFrameLayout shimmerLayoutYear,
      @NonNull LayoutRecyclerViewBottomSheetBinding userProfileItemview) {
    this.rootView = rootView;
    this.appCompatImageView = appCompatImageView;
    this.appCompatImageView2 = appCompatImageView2;
    this.appCompatImageView3 = appCompatImageView3;
    this.appCompatTextView3 = appCompatTextView3;
    this.appCompatTextView4 = appCompatTextView4;
    this.appCompatTextView5 = appCompatTextView5;
    this.chart = chart;
    this.linearLayout3 = linearLayout3;
    this.listLayoutYear = listLayoutYear;
    this.locationCardButtonYear = locationCardButtonYear;
    this.movementRecordLayout = movementRecordLayout;
    this.noActivitiesFound = noActivitiesFound;
    this.noDataFound = noDataFound;
    this.progressChart = progressChart;
    this.progressList = progressList;
    this.shimmerLayoutYear = shimmerLayoutYear;
    this.userProfileItemview = userProfileItemview;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentYearDataBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentYearDataBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_year_data, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentYearDataBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appCompatImageView;
      AppCompatImageView appCompatImageView = ViewBindings.findChildViewById(rootView, id);
      if (appCompatImageView == null) {
        break missingId;
      }

      id = R.id.appCompatImageView2;
      AppCompatImageView appCompatImageView2 = ViewBindings.findChildViewById(rootView, id);
      if (appCompatImageView2 == null) {
        break missingId;
      }

      id = R.id.appCompatImageView3;
      AppCompatImageView appCompatImageView3 = ViewBindings.findChildViewById(rootView, id);
      if (appCompatImageView3 == null) {
        break missingId;
      }

      id = R.id.appCompatTextView3;
      AppCompatTextView appCompatTextView3 = ViewBindings.findChildViewById(rootView, id);
      if (appCompatTextView3 == null) {
        break missingId;
      }

      id = R.id.appCompatTextView4;
      AppCompatTextView appCompatTextView4 = ViewBindings.findChildViewById(rootView, id);
      if (appCompatTextView4 == null) {
        break missingId;
      }

      id = R.id.appCompatTextView5;
      AppCompatTextView appCompatTextView5 = ViewBindings.findChildViewById(rootView, id);
      if (appCompatTextView5 == null) {
        break missingId;
      }

      id = R.id.chart;
      PieChart chart = ViewBindings.findChildViewById(rootView, id);
      if (chart == null) {
        break missingId;
      }

      id = R.id.linearLayout3;
      LinearLayout linearLayout3 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout3 == null) {
        break missingId;
      }

      id = R.id.list_layout_year;
      CardView listLayoutYear = ViewBindings.findChildViewById(rootView, id);
      if (listLayoutYear == null) {
        break missingId;
      }

      id = R.id.location_card_button_year;
      MaterialCardView locationCardButtonYear = ViewBindings.findChildViewById(rootView, id);
      if (locationCardButtonYear == null) {
        break missingId;
      }

      id = R.id.movement_record_layout;
      View movementRecordLayout = ViewBindings.findChildViewById(rootView, id);
      if (movementRecordLayout == null) {
        break missingId;
      }
      MovementRecordItemviewDotBinding binding_movementRecordLayout = MovementRecordItemviewDotBinding.bind(movementRecordLayout);

      id = R.id.noActivitiesFound;
      AppCompatTextView noActivitiesFound = ViewBindings.findChildViewById(rootView, id);
      if (noActivitiesFound == null) {
        break missingId;
      }

      id = R.id.noDataFound;
      AppCompatTextView noDataFound = ViewBindings.findChildViewById(rootView, id);
      if (noDataFound == null) {
        break missingId;
      }

      id = R.id.progress_chart;
      ProgressBar progressChart = ViewBindings.findChildViewById(rootView, id);
      if (progressChart == null) {
        break missingId;
      }

      id = R.id.progress_list;
      ProgressBar progressList = ViewBindings.findChildViewById(rootView, id);
      if (progressList == null) {
        break missingId;
      }

      id = R.id.shimmer_layout_year;
      ShimmerFrameLayout shimmerLayoutYear = ViewBindings.findChildViewById(rootView, id);
      if (shimmerLayoutYear == null) {
        break missingId;
      }

      id = R.id.user_profile_itemview;
      View userProfileItemview = ViewBindings.findChildViewById(rootView, id);
      if (userProfileItemview == null) {
        break missingId;
      }
      LayoutRecyclerViewBottomSheetBinding binding_userProfileItemview = LayoutRecyclerViewBottomSheetBinding.bind(userProfileItemview);

      return new FragmentYearDataBinding((ConstraintLayout) rootView, appCompatImageView,
          appCompatImageView2, appCompatImageView3, appCompatTextView3, appCompatTextView4,
          appCompatTextView5, chart, linearLayout3, listLayoutYear, locationCardButtonYear,
          binding_movementRecordLayout, noActivitiesFound, noDataFound, progressChart, progressList,
          shimmerLayoutYear, binding_userProfileItemview);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
