package com.example.project_x.data.datasource

import com.example.project_x.common.Resource
import com.example.project_x.data.api.ApiService
import com.example.project_x.data.api.AuthenticatedApiService
import com.example.project_x.data.model.EditProfileRequest
import com.example.project_x.data.model.FollowMessage
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.model.TokenResponse
import com.example.project_x.data.model.UserRequest
import com.example.project_x.data.model.UserResponse
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserDataSource
@Inject
constructor(
  private val apiService: ApiService,
  private val authenticatedApiService: AuthenticatedApiService,
) {

  suspend fun registerUser(user: UserRequest): Flow<Resource<UserResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = apiService.registerUser(user)
      if (response.isSuccessful) {
        emit(Resource.Success(response.body()))
      } else {
        emit(Resource.Error("Registration failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun loginUser(user: UserRequest): Flow<Resource<UserResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = apiService.loginUser(user)
      if (response.isSuccessful) {
        val userResponse = response.body()
        emit(Resource.Success(userResponse))
      } else {
        emit(Resource.Error("Login failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun editProfile(
    id: String,
    user: EditProfileRequest,
  ): Flow<Resource<ProfileResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.editProfile(id, user)
      if (response.isSuccessful) {
        emit(Resource.Success(response.body()))
      } else {
        emit(Resource.Error("Edit profile failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun logoutUser(): Flow<Resource<UserResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = apiService.logoutUser()
      if (response.isSuccessful) {
        emit(Resource.Success(response.body()))
      } else {
        emit(Resource.Error("Logout failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun getUserProfile(): Flow<Resource<ProfileResponse>> = flow {
    try {
      val response = authenticatedApiService.getUserProfile()
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to fetch user profile: Empty response body")) }
      } else {
        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
        emit(Resource.Error("Failed to fetch user profile: $errorMessage"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun getUserProfileById(id: String): Flow<Resource<ProfileResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.getUserProfileById(id)
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to fetch user profile: Empty response body")) }
      } else {
        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
        emit(Resource.Error("Failed to fetch user profile: $errorMessage"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun followUser(id: String): Flow<Resource<FollowMessage>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.followUser(id)
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to follow user: Empty response body")) }
      } else {
        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
        emit(Resource.Error("Failed to follow user: $errorMessage"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun isFollowingUser(id: String): Flow<Resource<FollowMessage>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.isFollowingUser(id)
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to follow user: Empty response body")) }
      } else {
        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
        emit(Resource.Error("Failed to follow user: $errorMessage"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun refreshToken(refreshToken: String): Resource<TokenResponse> {
    return try {
      val response = authenticatedApiService.refreshToken(refreshToken)
      if (response.isSuccessful) {
        Resource.Success(response.body()!!)
      } else {
        Resource.Error("Failed to refresh token")
      }
    } catch (e: Exception) {
      Resource.Error(e.localizedMessage ?: "Unknown error")
    }
  }
}
