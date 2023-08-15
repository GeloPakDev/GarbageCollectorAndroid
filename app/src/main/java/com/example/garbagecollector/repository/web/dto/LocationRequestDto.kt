package com.example.garbagecollector.repository.web.dto

import com.example.garbagecollector.model.State

data class LocationRequestDto(
    var id: Long? = null,
    var name: String? = "",
    var city: String? = "",
    var postalCode: Int? = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var photo: ByteArray = ByteArray(0),
    var state: State = State.NEW,
    var creationDate: String? = null,
    var claimDate: String? = null,
    var postedUserId: String? = null,
    var claimedUserId: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocationRequestDto

        if (id != other.id) return false
        if (name != other.name) return false
        if (city != other.city) return false
        if (postalCode != other.postalCode) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (photo != null) {
            if (other.photo == null) return false
            if (!photo.contentEquals(other.photo)) return false
        } else if (other.photo != null) return false
        if (state != other.state) return false
        if (creationDate != other.creationDate) return false
        if (claimDate != other.claimDate) return false
        if (postedUserId != other.postedUserId) return false
        if (claimedUserId != other.claimedUserId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (postalCode ?: 0)
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + (photo?.contentHashCode() ?: 0)
        result = 31 * result + state.hashCode()
        result = 31 * result + (creationDate?.hashCode() ?: 0)
        result = 31 * result + (claimDate?.hashCode() ?: 0)
        result = 31 * result + (postedUserId?.hashCode() ?: 0)
        result = 31 * result + (claimedUserId?.hashCode() ?: 0)
        return result
    }
}
