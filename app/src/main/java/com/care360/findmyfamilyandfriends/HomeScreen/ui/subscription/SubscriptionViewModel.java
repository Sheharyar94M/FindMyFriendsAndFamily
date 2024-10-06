package com.care360.findmyfamilyandfriends.HomeScreen.ui.subscription;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.subscription.model.Features;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SubscriptionViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Features>> featuresMutableLiveData;
    private final FirebaseFirestore firestore;
    private final ArrayList<Features> features;
    private final ExecutorService executor;


    public SubscriptionViewModel() {
        featuresMutableLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        features = new ArrayList<>();
        executor = Executors.newSingleThreadExecutor();
    }

    public MutableLiveData<ArrayList<Features>> getFeaturesMutableLiveData() {
        return featuresMutableLiveData;
    }

    public void getDataFirestoreDatabase() {

        executor.execute(() -> {
            firestore.collection("features").get()
                    .addOnCompleteListener(task -> {

                        try{
                            if (task.isSuccessful()) {

//                                Toast.makeText(App.getAppContext(), "ReadsSubscription: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    firestore.collection("features").document(snapshot.getId()).get()
                                            .addOnSuccessListener(documentSnapshot -> {

                                                features.add(new Features(
                                                        documentSnapshot.getString("imageUrl_feature"),
                                                        documentSnapshot.getString("title_feature"),
                                                        documentSnapshot.getString("description_feature")));

                                                Log.i("getDataFirestoreDatabase: ", documentSnapshot.getString("imageUrl_feature"));

                                                featuresMutableLiveData.setValue(features);

                                            });
                                }

                            }

                        }catch (Exception e){
                            Log.i( "getDataFirestoreDatabaseS: ",e.getMessage());
                        }
                    })
                    .addOnFailureListener(e -> {

                        Log.i("getDataFirestoreDatabaseF: ", e.getMessage());
                        featuresMutableLiveData.setValue(null);

                    });
        });

        executor.shutdown();
    }
}
