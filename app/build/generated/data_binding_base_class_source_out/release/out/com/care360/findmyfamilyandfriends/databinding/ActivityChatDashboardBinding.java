// Generated by view binder compiler. Do not edit!
package com.care360.findmyfamilyandfriends.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.care360.findmyfamilyandfriends.R;
import com.google.android.material.imageview.ShapeableImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityChatDashboardBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout adaptiveBanner;

  @NonNull
  public final ConstraintLayout consNoMembers;

  @NonNull
  public final ConstraintLayout consSearch;

  @NonNull
  public final EditText editTextSearch;

  @NonNull
  public final ShapeableImageView icSearch;

  @NonNull
  public final ShapeableImageView icon;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final TextView subTitle;

  @NonNull
  public final TextView title;

  @NonNull
  public final TextView txtTitle;

  private ActivityChatDashboardBinding(@NonNull ConstraintLayout rootView,
      @NonNull FrameLayout adaptiveBanner, @NonNull ConstraintLayout consNoMembers,
      @NonNull ConstraintLayout consSearch, @NonNull EditText editTextSearch,
      @NonNull ShapeableImageView icSearch, @NonNull ShapeableImageView icon,
      @NonNull RecyclerView recyclerView, @NonNull TextView subTitle, @NonNull TextView title,
      @NonNull TextView txtTitle) {
    this.rootView = rootView;
    this.adaptiveBanner = adaptiveBanner;
    this.consNoMembers = consNoMembers;
    this.consSearch = consSearch;
    this.editTextSearch = editTextSearch;
    this.icSearch = icSearch;
    this.icon = icon;
    this.recyclerView = recyclerView;
    this.subTitle = subTitle;
    this.title = title;
    this.txtTitle = txtTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityChatDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityChatDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_chat_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityChatDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.adaptiveBanner;
      FrameLayout adaptiveBanner = ViewBindings.findChildViewById(rootView, id);
      if (adaptiveBanner == null) {
        break missingId;
      }

      id = R.id.cons_no_members;
      ConstraintLayout consNoMembers = ViewBindings.findChildViewById(rootView, id);
      if (consNoMembers == null) {
        break missingId;
      }

      id = R.id.cons_search;
      ConstraintLayout consSearch = ViewBindings.findChildViewById(rootView, id);
      if (consSearch == null) {
        break missingId;
      }

      id = R.id.edit_text_search;
      EditText editTextSearch = ViewBindings.findChildViewById(rootView, id);
      if (editTextSearch == null) {
        break missingId;
      }

      id = R.id.ic_search;
      ShapeableImageView icSearch = ViewBindings.findChildViewById(rootView, id);
      if (icSearch == null) {
        break missingId;
      }

      id = R.id.icon;
      ShapeableImageView icon = ViewBindings.findChildViewById(rootView, id);
      if (icon == null) {
        break missingId;
      }

      id = R.id.recycler_view;
      RecyclerView recyclerView = ViewBindings.findChildViewById(rootView, id);
      if (recyclerView == null) {
        break missingId;
      }

      id = R.id.sub_title;
      TextView subTitle = ViewBindings.findChildViewById(rootView, id);
      if (subTitle == null) {
        break missingId;
      }

      id = R.id.title;
      TextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      id = R.id.txt_title;
      TextView txtTitle = ViewBindings.findChildViewById(rootView, id);
      if (txtTitle == null) {
        break missingId;
      }

      return new ActivityChatDashboardBinding((ConstraintLayout) rootView, adaptiveBanner,
          consNoMembers, consSearch, editTextSearch, icSearch, icon, recyclerView, subTitle, title,
          txtTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
