package com.example.garbagecollector.mapper

import com.example.garbagecollector.model.State
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import com.example.garbagecollector.repository.database.model.LocalLocation
import com.example.garbagecollector.repository.database.model.Location
import com.example.garbagecollector.repository.database.model.PostedLocation
import com.example.garbagecollector.repository.web.dto.LocationDto
import java.time.LocalDate

class LocationsMapper {
    companion object {
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

        fun mapLocationToLocalLocation(locations: List<com.example.garbagecollector.repository.web.dto.Location>?): List<LocalLocation> {
            return locations!!.map { location ->
                LocalLocation(
                    id = location.id,
                    longitude = location.longitude,
                    latitude = location.latitude
                )
            }
        }

        fun mapLocalLocationToLocation(locations: List<LocalLocation>?): List<com.example.garbagecollector.repository.web.dto.Location> {
            return locations!!.map { location ->
                com.example.garbagecollector.repository.web.dto.Location(
                    id = location.id,
                    longitude = location.longitude,
                    latitude = location.latitude
                )
            }
        }

        fun mapLocationDtoToLocation(locationDto: LocationDto?): Location {
            return Location(
                id = locationDto?.id,
                name = locationDto?.name,
                city = locationDto?.city,
                postalCode = locationDto?.postalCode,
                latitude = locationDto?.latitude ?: 0.0,
                longitude = locationDto?.longitude ?: 0.0,
                photo = locationDto?.photo,
                state = locationDto?.state ?: State.NEW,
                createDate = locationDto?.creationDate?.let { LocalDate.parse(it) },
                claimDate = locationDto?.claimDate?.let { LocalDate.parse(it) },
                postedUser = locationDto?.postedUserId,
                claimedUser = locationDto?.claimedUserId
            )
        }

    }
}