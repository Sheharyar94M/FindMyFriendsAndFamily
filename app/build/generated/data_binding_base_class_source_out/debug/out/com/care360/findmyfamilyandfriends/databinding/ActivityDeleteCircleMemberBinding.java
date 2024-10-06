// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDeleteCircleMemberBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout adaptiveBanner;

  @NonNull
  public final RecyclerView recyclerDeleteMembers;

  @NonNull
  public final Toolbar toolbarDeleteCircleMembers;

  private ActivityDeleteCircleMemberBinding(@NonNull ConstraintLayout rootView,
      @NonNull FrameLayout adaptiveBanner, @NonNull RecyclerView recyclerDeleteMembers,
      @NonNull Toolbar toolbarDeleteCircleMembers) {
    this.rootView = rootView;
    this.adaptiveBanner = adaptiveBanner;
    this.recyclerDeleteMembers = recyclerDeleteMembers;
    this.toolbarDeleteCircleMembers = toolbarDeleteCircleMembers;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDeleteCircleMemberBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDeleteCircleMemberBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_delete_circle_member, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDeleteCircleMemberBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.adaptiveBanner;
      FrameLayout adaptiveBanner = ViewBindings.findChildViewById(rootView, id);
      if (adaptiveBanner == null) {
        break missingId;
      }

      id = R.id.recycler_delete_members;
      RecyclerView recyclerDeleteMembers = ViewBindings.findChildViewById(rootView, id);
      if (recyclerDeleteMembers == null) {
        break missingId;
      }

      id = R.id.toolbar_delete_circle_members;
      Toolbar toolbarDeleteCircleMembers = ViewBindings.findChildViewById(rootView, id);
      if (toolbarDeleteCircleMembers == null) {
        break missingId;
      }

      return new ActivityDeleteCircleMemberBinding((ConstraintLayout) rootView, adaptiveBanner,
          recyclerDeleteMembers, toolbarDeleteCircleMembers);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
