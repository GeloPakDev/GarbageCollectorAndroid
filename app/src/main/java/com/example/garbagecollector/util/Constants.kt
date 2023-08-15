package com.example.garbagecollector.util

class Constants {
    companion object {
        const val HOME_FRAGMENT_TAG = "Home Fragment"
        const val GARBAGE_IMAGE_INTENT = "data"
        const val LOCATION = "LatLng"
        const val DATASTORE_NAME = "AUTHORIZATION"
        const val BASE_URL = "https://garabagecollectorapp.azurewebsites.net"
        const val MARKER_ID = "marker_id"

        //Database
        const val LOCAL_LOCATION_TABLE_NAME = "local_location"
        const val LOCATION_TABLE_NAME = "location"
        const val CLAIMED_LOCATION_TABLE_NAME = "claimed_location"
        const val POSTED_LOCATION_TABLE_NAME = "posted_location"
        const val DATABASE_NAME = "garbage_collector"
    }
}