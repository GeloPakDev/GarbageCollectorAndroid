package com.example.garbagecollector.repository.web.impl

import android.annotation.SuppressLint
import com.example.garbagecollector.model.State
import com.example.garbagecollector.repository.web.FirebaseResponse
import com.example.garbagecollector.repository.web.LocationRepository
import com.example.garbagecollector.repository.web.dto.LocationRequestDto
import com.example.garbagecollector.repository.web.dto.LocationResponseDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import java.time.LocalDate

class LocationRepositoryImpl(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : LocationRepository {
    override suspend fun addLocation(locationRequestDto: LocationRequestDto) {
        locationRequestDto.postedUserId = auth.currentUser?.uid

        val storageRef = storage.reference
        val imageName = "image_${System.currentTimeMillis()}.jpg"
        val imageRef = storageRef.child("locations/$imageName")

        val imageStream = ByteArrayInputStream(locationRequestDto.photo)
        // Upload the image to Firebase Storage
        imageRef.putStream(imageStream).await()
        // Retrieve the URL of the uploaded image
        val uri = imageRef.downloadUrl.await()
        val imageURI = uri.toString()
        val data = hashMapOf(
            "name" to locationRequestDto.name,
            "city" to locationRequestDto.city,
            "postalCode" to locationRequestDto.postalCode,
            "latitude" to locationRequestDto.latitude,
            "longitude" to locationRequestDto.longitude,
            "photo" to imageURI,
            "state" to locationRequestDto.state,
            "claimedDate" to locationRequestDto.claimDate,
            "createDate" to locationRequestDto.creationDate,
            "postedUserId" to locationRequestDto.postedUserId,
            "claimedUserId" to locationRequestDto.claimedUserId
        )
        fireStore.collection("locations").add(data).await()
    }

    @SuppressLint("NewApi")
    override suspend fun claimLocation(id: String) {
        fireStore.collection("locations").document(id)
            .update(
                mapOf(
                    "state" to "CLAIMED",
                    "claimedDate" to LocalDate.now().toString(),
                    "claimedUserId" to auth.currentUser?.uid
                )
            ).await()
    }

    override suspend fun getLocationById(id: String): LocationResponseDto {
        val documentSnapshot = fireStore.collection("locations").document(id).get().await()
        if (documentSnapshot.exists()) {
            val location = documentSnapshot.data
            val locationResponse = LocationResponseDto()

            locationResponse.name = location?.get("name") as String
            locationResponse.city = location["city"] as String
            locationResponse.postalCode = (location["postalCode"] as Long).toInt()
            locationResponse.latitude = location["latitude"] as Double
            locationResponse.longitude = location["longitude"] as Double
            locationResponse.photo = location["photo"] as String
            locationResponse.state =
                if (location["state"] == "NEW") State.NEW else State.CLAIMED
            locationResponse.claimDate = location["claimedDate"] as String?
            locationResponse.creationDate = location["createDate"] as String
            locationResponse.postedUserId = location["postedUserId"] as String?
            locationResponse.claimedUserId = location["claimedUserId"] as String?
            return locationResponse
        } else {
            throw Exception("Location not found")
        }
    }

    override suspend fun getTotalLocationsNumber(): FirebaseResponse<Int> {
        val query = fireStore.collection("locations").whereEqualTo("state", "NEW")
        val countQuery = query.count()

        return try {
            val snapshot = countQuery.get(AggregateSource.SERVER).await()
            val count = snapshot.count.toInt()
            FirebaseResponse.Success(count)
        } catch (e: Exception) {
            FirebaseResponse.Error(e)
        }
    }

    override suspend fun getUserPostedLocations(userId: String): FirebaseResponse<List<LocationResponseDto>> {
        val locations = mutableListOf<LocationResponseDto>()
        try {
            val querySnapshot =
                fireStore.collection("locations").whereEqualTo("postedUserId", userId).get().await()
            for (document in querySnapshot.documents) {
                val location = LocationResponseDto()
                location.id = document.id
                location.name = document["name"] as String
                location.city = document["city"] as String
                location.postalCode = (document["postalCode"] as Long).toInt()
                location.latitude = document["latitude"] as Double
                location.longitude = document["longitude"] as Double
                location.photo = document["photo"] as String
                location.state = if (document["state"] == "NEW") State.NEW else State.CLAIMED
                location.claimDate = document["claimedDate"] as String?
                location.creationDate = document["createDate"] as String
                location.postedUserId = document["postedUserId"] as String?
                location.claimedUserId = document["claimedUserId"] as String?
                locations.add(location)
            }
            return FirebaseResponse.Success(locations)
        } catch (e: Exception) {
            return FirebaseResponse.Error(e)
        }
    }

    override suspend fun getUserClaimedLocations(userId: String): FirebaseResponse<List<LocationResponseDto>> {
        val locations = mutableListOf<LocationResponseDto>()
        try {
            val querySnapshot =
                fireStore.collection("locations").whereEqualTo("claimedUserId", userId).get()
                    .await()
            for (document in querySnapshot.documents) {
                val location = LocationResponseDto()
                location.id = document.id
                location.name = document["name"] as String
                location.city = document["city"] as String
                location.postalCode = (document["postalCode"] as Long).toInt()
                location.latitude = document["latitude"] as Double
                location.longitude = document["longitude"] as Double
                location.photo = document["photo"] as String
                location.state = if (document["state"] == "NEW") State.NEW else State.CLAIMED
                location.claimDate = document["claimedDate"] as String?
                location.creationDate = document["createDate"] as String
                location.postedUserId = document["postedUserId"] as String?
                location.claimedUserId = document["claimedUserId"] as String?
                locations.add(location)
            }
            return FirebaseResponse.Success(locations)
        } catch (e: Exception) {
            return FirebaseResponse.Error(e)
        }
    }

    override suspend fun getTotalPostedUserLocations(userId: String): Int {
        val query = fireStore.collection("locations").whereEqualTo("postedUserId", userId)
        val countQuery = query.count()
        val snapshot = countQuery.get(AggregateSource.SERVER).await()
        return snapshot.count.toInt()
    }

    override suspend fun getTotalClaimedUserLocations(userId: String): Int {
        val query = fireStore.collection("locations").whereEqualTo("claimedUserId", userId)
        val countQuery = query.count()
        val snapshot = countQuery.get(AggregateSource.SERVER).await()
        return snapshot.count.toInt()
    }

    override suspend fun getUserPoints(userId: String): Int {
        val query = fireStore.collection("users").document(userId).get().await()
        val points = query["points"] as Long
        return points.toInt()
    }

    override suspend fun getLocations(): FirebaseResponse<List<LocationResponseDto>> {
        val locations = mutableListOf<LocationResponseDto>()
        try {
            val querySnapshot =
                fireStore.collection("locations").whereEqualTo("state", "NEW").get().await()
            for (document in querySnapshot.documents) {
                val location = LocationResponseDto()
                location.id = document.id
                location.name = document["name"] as String
                location.city = document["city"] as String
                location.postalCode = (document["postalCode"] as Long).toInt()
                location.latitude = document["latitude"] as Double
                location.longitude = document["longitude"] as Double
                location.photo = document["photo"] as String
                location.state = if (document["state"] == "NEW") State.NEW else State.CLAIMED
                location.claimDate = document["claimedDate"] as String?
                location.creationDate = document["createDate"] as String
                location.postedUserId = document["postedUserId"] as String?
                location.claimedUserId = document["claimedUserId"] as String?
                locations.add(location)
            }
            return FirebaseResponse.Success(locations)
        } catch (e: Exception) {
            return FirebaseResponse.Error(e)
        }
    }

    override fun getAuthInstance(): FirebaseAuth {
        return auth
    }
}