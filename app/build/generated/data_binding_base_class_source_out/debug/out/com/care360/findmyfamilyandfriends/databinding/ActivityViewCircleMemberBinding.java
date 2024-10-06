// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.care360.findmyfamilyandfriends.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityViewCircleMemberBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout adaptiveBanner;

  @NonNull
  public final AppCompatTextView noListView;

  @NonNull
  public final LottieAnimationView noResults;

  @NonNull
  public final ProgressBar progress;

  @NonNull
  public final RecyclerView recyclerDeleteMembers;

  @NonNull
  public final Toolbar toolbarViewMembers;

  private ActivityViewCircleMemberBinding(@NonNull ConstraintLayout rootView,
      @NonNull FrameLayout adaptiveBanner, @NonNull AppCompatTextView noListView,
      @NonNull LottieAnimationView noResults, @NonNull ProgressBar progress,
      @NonNull RecyclerView recyclerDeleteMembers, @NonNull Toolbar toolbarViewMembers) {
    this.rootView = rootView;
    this.adaptiveBanner = adaptiveBanner;
    this.noListView = noListView;
    this.noResults = noResults;
    this.progress = progress;
    this.recyclerDeleteMembers = recyclerDeleteMembers;
    this.toolbarViewMembers = toolbarViewMembers;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityViewCircleMemberBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityViewCircleMemberBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_view_circle_member, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityViewCircleMemberBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.adaptiveBanner;
      FrameLayout adaptiveBanner = ViewBindings.findChildViewById(rootView, id);
      if (adaptiveBanner == null) {
        break missingId;
      }

      id = R.id.no_list_view;
      AppCompatTextView noListView = ViewBindings.findChildViewById(rootView, id);
      if (noListView == null) {
        break missingId;
      }

      id = R.id.no_results;
      LottieAnimationView noResults = ViewBindings.findChildViewById(rootView, id);
      if (noResults == null) {
        break missingId;
      }

      id = R.id.progress;
      ProgressBar progress = ViewBindings.findChildViewById(rootView, id);
      if (progress == null) {
        break missingId;
      }

      id = R.id.recycler_delete_members;
      RecyclerView recyclerDeleteMembers = ViewBindings.findChildViewById(rootView, id);
      if (recyclerDeleteMembers == null) {
        break missingId;
      }

      id = R.id.toolbar_view_members;
      Toolbar toolbarViewMembers = ViewBindings.findChildViewById(rootView, id);
      if (toolbarViewMembers == null) {
        break missingId;
      }

      return new ActivityViewCircleMemberBinding((ConstraintLayout) rootView, adaptiveBanner,
          noListView, noResults, progress, recyclerDeleteMembers, toolbarViewMembers);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
