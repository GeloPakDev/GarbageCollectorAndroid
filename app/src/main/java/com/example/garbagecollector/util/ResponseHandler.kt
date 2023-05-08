package com.example.garbagecollector.util

import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.repository.web.dto.Location
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

    }
}