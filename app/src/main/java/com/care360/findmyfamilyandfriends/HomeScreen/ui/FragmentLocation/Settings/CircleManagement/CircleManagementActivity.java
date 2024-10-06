package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Settings.CircleManagement;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.AddMember.AddMemberActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Settings.CircleManagement.ViewMember.ViewCircleMemberActivity;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.databinding.ActivityCircleManagementBinding;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Settings.CircleManagement.RemoveMember.RemoveCircleMemberActivity;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Constants;

import java.util.Objects;

public class CircleManagementActivity extends AppCompatActivity {

    private static final String TAG = "CIR_MANAG_ACT";

    ActivityCircleManagementBinding binding;
    private TemplateView template;
    private AdLoader adloader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initializing binding
        binding = ActivityCircleManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this);

        template = binding.adTemplate;


        adloader = new AdLoader.Builder(this, BuildConfig.NATIVE_AD_ID)
                .forNativeAd(nativeAd -> {

                    template.setStyles(new NativeTemplateStyle.Builder().build());
                    template.setNativeAd(nativeAd);
                })
                .build();

        adloader.loadAd(new AdRequest.Builder().build());

        if (!SharedPreference.getCircleName().equals(Constants.NULL))
            binding.toolbarCircleManagement.setTitle(SharedPreference.getCircleName());

        else
            binding.toolbarCircleManagement.setTitle("Circle Management");


        //toolbar
        binding.toolbarCircleManagement.setNavigationOnClickListener(v -> onBackPressed());

        // add member click listener
        binding.textAddMembers.setOnClickListener(v -> {

            if (!SharedPreference.getCircleName().equals(Constants.NULL))
                startActivity(new Intent(this, AddMemberActivity.class));
            else{
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.nothing_found_dialog_layout);

                //setting the transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //this sets the width of dialog to 90%
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (int) (displayMetrics.widthPixels * 0.9);

                //setting the width and height of alert dialog
                dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

                AppCompatButton button = dialog.findViewById(R.id.btn_cancel);
                LottieAnimationView animationView = dialog.findViewById(R.id.no_data_L);
                animationView.playAnimation();

                button.setOnClickListener(view -> {
                    dialog.dismiss();
                });

                dialog.setCancelable(true);
                dialog.show();
            }
        });

        // edit text circle name (for circle admin only)
        binding.textEditCircleName.setOnClickListener(v -> startActivity(new Intent(this, EditCircleNameActivity.class)));

        // delete circle (for circle admin only)
        binding.textDeleteCircle.setOnClickListener(v -> deleteCircle());

        // remove circle members (for circle admin only)
        binding.textRemoveMembers.setOnClickListener(v -> startActivity(new Intent(this, RemoveCircleMemberActivity.class)));

        // view circle members (for non-admin members)
        binding.textViewMembers.setOnClickListener(v -> startActivity(new Intent(this, ViewCircleMemberActivity.class)));

        // leave circle (for non-admin members)
        binding.textLeaveCircle.setOnClickListener(v -> leaveCircle());

        // get current Cirlce info
        getCirlceInfo();
    }

    private void getCirlceInfo() {

        String currentUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        if (!SharedPreference.getCircleName().equals(Constants.NULL)){
            if(SharedPreference.getCircleAdminId().equals(currentUserEmail)) {
                // current user is admin of this circle

                // setting the visibility of admin views to visible
                binding.groupCircleDetails.setVisibility(View.VISIBLE);
                binding.groupAdmin.setVisibility(View.VISIBLE);

                // setting the visibility of no admin views to gone
                binding.groupNotAdmin.setVisibility(View.GONE);
            }
            else if(!SharedPreference.getCircleAdminId().equals(currentUserEmail)) {
                // current user not admin of this circle

                // setting the visibility of admin views to visible
                binding.groupCircleDetails.setVisibility(View.GONE);
                binding.groupAdmin.setVisibility(View.GONE);

                // setting the visibility of no admin views to gone
                binding.groupNotAdmin.setVisibility(View.VISIBLE);
            }
        }else{
            binding.groupNotAdmin.setVisibility(View.GONE);
            binding.groupAdmin.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // if circle name is changed, then the updated name will be displayed
        if (!SharedPreference.getCircleName().equals(Constants.NULL))
            binding.toolbarCircleManagement.setTitle(SharedPreference.getCircleName());
        else
            binding.toolbarCircleManagement.setTitle("Circle Management");
    }

    // deletes the currently selected circle
    private void deleteCircle() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete Circle")
                .setMessage("Want to delete "+SharedPreference.getCircleName()+ " ?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {

                    // progress bar visibility
                    binding.progressBar.setVisibility(View.VISIBLE);

                    // delete the circle
                    FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                            .document(SharedPreference.getCircleAdminId())
                            .collection(Constants.CIRCLE_COLLECTION)
                            .document(SharedPreference.getCircleId())
                            .delete()
                            .addOnSuccessListener(unused ->
                            {
                                // removes the circle id from 'USER' collection circle array as well

                                FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                                        .document(SharedPreference.getCircleAdminId())
                                        .update(Constants.CIRCLE_IDS,FieldValue.arrayRemove(SharedPreference.getCircleId()))
                                        .addOnSuccessListener(success ->
                                        {
                                            // progress bar visibility
                                            binding.progressBar.setVisibility(View.GONE);
                                            Log.i(TAG, "circle deleted successfully");

                                            // setting the shared preference values
                                            SharedPreference.setCircleId(Constants.NULL);
                                            SharedPreference.setCircleAdminId(Constants.NULL);
                                            SharedPreference.setCircleName(Constants.NULL);
                                            SharedPreference.setCircleInviteCode(Constants.NULL);

                                            Toast.makeText(this, "Circle deleted successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        });

                            })
                            .addOnFailureListener(e -> {

                                // progress bar visibility
                                binding.progressBar.setVisibility(View.GONE);
                                Log.e(TAG, "error deleting circle: " + e.getMessage());
                                Toast.makeText(this, "Failed to delete circle", Toast.LENGTH_SHORT).show();
                            });

                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.create();
        builder.show();
    }

    private void leaveCircle() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Leave Circle")
                .setMessage("Want to leave "+SharedPreference.getCircleName()+ " ?")
                .setCancelable(false)
                .setPositiveButton("Leave", (dialogInterface, i) -> {

                    // setting progress view visibility
                    binding.progressBar.setVisibility(View.VISIBLE);

                    String currentUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

                    // removes the current user from circle
                    FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                            .document(SharedPreference.getCircleAdminId())
                            .collection(Constants.CIRCLE_COLLECTION)
                            .document(SharedPreference.getCircleId())
                            .update(Constants.CIRCLE_MEMBERS, FieldValue.arrayRemove(currentUserEmail))

                            .addOnSuccessListener(unused -> {

                                // setting progress view visibility
                                binding.progressBar.setVisibility(View.GONE);

                                Log.i(TAG, "circle left successfully");

                                // setting the shared preference values
                                SharedPreference.setCircleId(Constants.NULL);
                                SharedPreference.setCircleAdminId(Constants.NULL);
                                SharedPreference.setCircleName(Constants.NULL);
                                SharedPreference.setCircleInviteCode(Constants.NULL);

                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Circle left successfully", Snackbar.LENGTH_INDEFINITE);

                                snackbar.setAction("Ok", v -> {
                                    snackbar.dismiss();
                                    finish();
                                });

                                snackbar.setTextColor(Color.WHITE);
                                snackbar.show();

                            })
                            .addOnFailureListener(e -> {
                                // setting progress view visibility
                                binding.progressBar.setVisibility(View.GONE);

                                Log.i(TAG, "error leaving circle: "+e.getMessage());

                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_INDEFINITE);

                                snackbar.setAction("Ok", v -> {
                                    snackbar.dismiss();
                                });

                                snackbar.setTextColor(Color.WHITE);
                                snackbar.show();
                            });

                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        alertDialog.create().show();

    }
}