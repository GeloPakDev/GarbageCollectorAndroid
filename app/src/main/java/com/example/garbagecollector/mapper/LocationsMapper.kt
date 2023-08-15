package com.example.garbagecollector.mapper

import com.example.garbagecollector.model.State
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import com.example.garbagecollector.repository.database.model.LocalLocation
import com.example.garbagecollector.repository.database.model.Location
import com.example.garbagecollector.repository.database.model.PostedLocation
import com.example.garbagecollector.repository.web.dto.LocationRequestDto
import com.example.garbagecollector.repository.web.dto.LocationResponseDto

class LocationsMapper {
    companion object {
        fun mapLocationDtoToClaimedLocation(locations: List<LocationResponseDto>?): List<ClaimedLocation> {
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

        fun mapLocationDtoToPostedLocation(locations: List<LocationResponseDto>?): List<PostedLocation> {
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