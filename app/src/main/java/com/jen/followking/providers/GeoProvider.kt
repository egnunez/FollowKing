package com.jen.followking.providers

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import org.imperiumlabs.geofirestore.GeoFirestore


class GeoProvider {

    val collection = FirebaseFirestore.getInstance().collection("Locations")
    val geoFirestore = GeoFirestore(collection)

    fun savelocation(idDriver: String, position: LatLng){
        geoFirestore.setLocation(idDriver, GeoPoint(position.latitude, position.longitude))
    }

    fun removeLocation(idDriver: String){
        geoFirestore.removeLocation(idDriver)
        collection.document(idDriver).delete()
    }
    fun getLocation(idDriver: String): Task<DocumentSnapshot> {
        return collection.document(idDriver).get().addOnFailureListener{exception ->
            Log.d("Firebase","Error: ${exception.toString()}")}

    }

}
