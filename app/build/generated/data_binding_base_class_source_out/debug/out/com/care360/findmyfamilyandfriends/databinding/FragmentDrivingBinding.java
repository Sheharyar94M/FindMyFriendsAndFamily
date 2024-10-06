// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDrivingBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout adaptiveBanner;

  @NonNull
  public final CoordinatorLayout coordinatorLayout;

  @NonNull
  public final LayoutLayoutSheetBinding layoutSheetMembers;

  @NonNull
  public final FragmentContainerView map;

  private FragmentDrivingBinding(@NonNull ConstraintLayout rootView,
      @NonNull FrameLayout adaptiveBanner, @NonNull CoordinatorLayout coordinatorLayout,
      @NonNull LayoutLayoutSheetBinding layoutSheetMembers, @NonNull FragmentContainerView map) {
    this.rootView = rootView;
    this.adaptiveBanner = adaptiveBanner;
    this.coordinatorLayout = coordinatorLayout;
    this.layoutSheetMembers = layoutSheetMembers;
    this.map = map;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDrivingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDrivingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_driving, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDrivingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.adaptiveBanner;
      FrameLayout adaptiveBanner = ViewBindings.findChildViewById(rootView, id);
      if (adaptiveBanner == null) {
        break missingId;
      }

      id = R.id.coordinator_layout;
      CoordinatorLayout coordinatorLayout = ViewBindings.findChildViewById(rootView, id);
      if (coordinatorLayout == null) {
        break missingId;
      }

      id = R.id.layout_sheet_members;
      View layoutSheetMembers = ViewBindings.findChildViewById(rootView, id);
      if (layoutSheetMembers == null) {
        break missingId;
      }
      LayoutLayoutSheetBinding binding_layoutSheetMembers = LayoutLayoutSheetBinding.bind(layoutSheetMembers);

      id = R.id.map;
      FragmentContainerView map = ViewBindings.findChildViewById(rootView, id);
      if (map == null) {
        break missingId;
      }

      return new FragmentDrivingBinding((ConstraintLayout) rootView, adaptiveBanner,
          coordinatorLayout, binding_layoutSheetMembers, map);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
