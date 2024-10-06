package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.adapters;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.MemberDetail;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.databinding.LayoutMembersListItemviewBinding;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.List;

public class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.MemberViewHolder> {

    Context context;
    List<MemberDetail> memberDetailList;

    //binding
    LayoutMembersListItemviewBinding recyclerViewItemBinding;
    //click listener interfaces
    OnAddedMemberClickInterface addedMemberInterface;


    public MembersListAdapter(Context context, OnAddedMemberClickInterface onAddedMemberClickInterface) {
        this.context = context;
        this.addedMemberInterface = onAddedMemberClickInterface;

    }

    public void setMemberDetailList(List<MemberDetail> memberDetailList) {
        this.memberDetailList = memberDetailList;
    }

    @NonNull
    @Override
    public MembersListAdapter.MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // if view type is VIEW_TYPE_ITEM, then recyclerview item is loaded, else add new member layout is loaded

        return new MemberViewHolder(LayoutMembersListItemviewBinding.inflate(LayoutInflater.from(context), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MembersListAdapter.MemberViewHolder holder, int position) {

        MemberDetail memberItem = memberDetailList.get(holder.getAdapterPosition());



        // user profile image if any
        if (memberItem.getMemberImageUrl().equals(Constants.NULL)) {

            // make visible the name's first character text view
            recyclerViewItemBinding.txtViewMemberFirstChar.setVisibility(View.VISIBLE);
            recyclerViewItemBinding.txtViewMemberFirstChar.setText(String.valueOf(memberItem.getMemberFirstName().charAt(0)));

            if (holder.getAdapterPosition() % 2 == 0) {
                recyclerViewItemBinding.imgViewMemberProfile.setImageResource(R.drawable.drawable_no_member_profile);
            } else if (holder.getAdapterPosition() % 2 != 0) {
                recyclerViewItemBinding.imgViewMemberProfile.setImageResource(R.drawable.drawable_no_group_icon);
            }

        } else {

            // hides the name's first character text view
            recyclerViewItemBinding.txtViewMemberFirstChar.setVisibility(View.GONE);

            // displaying the image
            Glide.with(context)
                    .load(memberItem.getMemberImageUrl())
                    .into(recyclerViewItemBinding.imgViewMemberProfile);

        }

        // name
        recyclerViewItemBinding.txtViewMemberName.setText(memberItem.getMemberFirstName().concat(" ").concat(memberItem.getMemberLastName()));

        // last known location address
        if (memberItem.getLocationAddress().equals(Constants.NULL)) {

            double lat = Double.parseDouble(memberItem.getLocationLat());
            double lng = Double.parseDouble(memberItem.getLocationLng());

            if (lat != 0 && lng != 0) {
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(lat);
                location.setLongitude(lng);

                recyclerViewItemBinding.txtViewLastKnownAddress.setText(Commons.getLocationAddress(context, location));
            }
        } else {
            recyclerViewItemBinding.txtViewLastKnownAddress.setText(memberItem.getLocationAddress());
        }

        if (memberItem.getLocationTimestamp() != 0) {
            // time stamp
            recyclerViewItemBinding.txtViewTimestamp.setText(context.getString(R.string.last_seen).concat(" ").concat(Commons.timeInMilliToDateFormat(String.valueOf(memberItem.getLocationTimestamp()))));
        }

        // view click listener
        recyclerViewItemBinding.consMemberItem.setOnClickListener(v -> addedMemberInterface.onAddedMemberClicked(memberDetailList.get(position)));

        //button click
        recyclerViewItemBinding.viewCompleteMember.setOnClickListener(v -> {

            addedMemberInterface.onAddedMemberClicked(memberDetailList.get(position));

        });
    }

    @Override
    public int getItemCount() {
        if (memberDetailList != null) {
            return memberDetailList.size();
        } else {
            return 0;
        }
    }

    public interface OnAddedMemberClickInterface {
        void onAddedMemberClicked(MemberDetail memberClicked);
    }

    //view holder for inflating recyclerview item extended from base view holder class
    public class MemberViewHolder extends RecyclerView.ViewHolder {

        LayoutMembersListItemviewBinding binding;

        public MemberViewHolder(@NonNull LayoutMembersListItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            MembersListAdapter.this.recyclerViewItemBinding = binding;
        }
    }

}
