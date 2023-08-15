package com.example.garbagecollector.util.network

import com.example.garbagecollector.model.User
import com.example.garbagecollector.repository.web.FirebaseResponse
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.repository.web.dto.Location
import com.example.garbagecollector.repository.web.dto.LocationResponseDto
import retrofit2.Response

class ResponseHandler {
    companion object {
        fun handleLocationsResponse(response: Response<List<Location>>): NetworkResult<List<Location>> {
            when {
                response.message().toString().contains("timeout") -> {
                    return NetworkResult.Error("Timeout")
                }
                response.code() == 402 -> {
                    return NetworkResult.Error("API Key Limited")
                }
                response.body()!!.isEmpty() -> {
                    return NetworkResult.Error("Locations Not Found")
                }
                response.isSuccessful -> {
                    val locations = response.body()
                    return NetworkResult.Success(locations!!)
                }
                else -> {
                    return NetworkResult.Error(response.message())
                }
            }
        }

        fun handleLocationsResponse(response: FirebaseResponse<List<LocationResponseDto>>): NetworkResult<List<LocationResponseDto>> {
            return when (response) {
                is FirebaseResponse.Success -> {
                    val locations = response.data
                    NetworkResult.Success(locations)
                }
                is FirebaseResponse.Error -> {
                    NetworkResult.Error("Failed to fetch locations count", null)
                }
            }
        }

        fun handleUsersResponse(response: Response<List<User>>): NetworkResult<List<User>> {
            when {
                response.message().toString().contains("timeout") -> {
                    return NetworkResult.Error("Timeout")
                }
                response.code() == 402 -> {
                    return NetworkResult.Error("API Key Limited")
                }
                response.body()!!.isEmpty() -> {
                    return NetworkResult.Error("Locations Not Found")
                }
                response.isSuccessful -> {
                    val users = response.body()
                    return NetworkResult.Success(users!!)
                }
                else -> {
                    return NetworkResult.Error(response.message())
                }
            }
        }

        fun handlePostedLocationsNumberResponse(response: Response<Int>): NetworkResult<Int> {
            when {
                response.message().toString().contains("timeout") -> {
                    return NetworkResult.Error("Timeout")
                }
                response.code() == 402 -> {
                    return NetworkResult.Error("API Key Limited")
                }
                response.body() == null -> {
                    return NetworkResult.Error("Locations Not Found")
                }
                response.isSuccessful -> {
                    val locations = response.body()
                    return NetworkResult.Success(locations!!)
                }
                else -> {
                    return NetworkResult.Error(response.message())
                }
            }
        }

        fun handlePostedLocationsNumberResponse(response: FirebaseResponse<Int>): NetworkResult<Int> {
            return when (response) {
                is FirebaseResponse.Success -> {
                    val locations = response.data
                    NetworkResult.Success(locations)
                }
                is FirebaseResponse.Error -> {
                    NetworkResult.Error("Failed to fetch locations count", null)
                }
            }
        }

    }
}