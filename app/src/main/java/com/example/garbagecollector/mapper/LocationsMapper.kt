package com.example.garbagecollector.mapper

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import com.example.garbagecollector.repository.database.model.Location
import com.example.garbagecollector.repository.database.model.PostedLocation
import com.example.garbagecollector.repository.web.dto.LocationDto
import java.time.LocalDate

class LocationsMapper {
    companion object {
        @SuppressLint("NewApi")
        fun mapLocationDtoToLocation(locationDtos: List<LocationDto>?): List<Location> {
            return locationDtos!!.map {
                Location(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    postalCode = it.postalCode,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    photo = it.photo?.let { photo -> BitmapFactory.decodeFile(photo) },
                    state = it.state,
                    createDate = it.creationDate?.let { date -> LocalDate.parse(date) },
                    claimDate = it.claimDate?.let { date -> LocalDate.parse(date) },
                    postedUser = it.postedUserId,
                    claimedUser = it.claimedUserId
                )
            }
        }

        @SuppressLint("NewApi")
        fun mapLocationToLocationDto(locations: List<Location>?): List<LocationDto> {
            return locations!!.map {
                LocationDto(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    postalCode = it.postalCode,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    photo = null,
                    state = it.state,
                    creationDate = it.createDate?.toString(),
                    claimDate = it.claimDate?.toString(),
                    postedUserId = it.postedUser,
                    claimedUserId = it.claimedUser
                )
            }
        }

        fun mapLocationDtoToClaimedLocation(locations: List<LocationDto>?): List<ClaimedLocation> {
            return locations!!.map { locationDto ->
                ClaimedLocation(
                    id = locationDto.id,
                    name = locationDto.name,
                    city = locationDto.city,
                    postalCode = locationDto.postalCode,
                    photo = locationDto.photo,
                    claimDate = locationDto.claimDate
                )
            }
        }

        fun mapLocationDtoToPostedLocation(locations: List<LocationDto>?): List<PostedLocation> {
            return locations!!.map { locationDto ->
                PostedLocation(
                    id = locationDto.id,
                    name = locationDto.name,
                    city = locationDto.city,
                    postalCode = locationDto.postalCode,
                    photo = locationDto.photo,
                    createDate = locationDto.creationDate
                )
            }
        }
    }
}