package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.common.Resource
import com.example.project_x.data.model.EditProfileRequest
import com.example.project_x.data.model.FollowMessage
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
  ViewModel() {

  private val _profileState = MutableStateFlow<Resource<ProfileResponse>>(Resource.Loading())
  val userProfileState: StateFlow<Resource<ProfileResponse>> = _profileState.asStateFlow()

  private val _loggedInUserProfileState =
    MutableStateFlow<Resource<ProfileResponse>>(Resource.Loading())
  val loggedInUserProfileState: StateFlow<Resource<ProfileResponse>> =
    _loggedInUserProfileState.asStateFlow()

  private val _followState = MutableStateFlow<Resource<FollowMessage>>(Resource.Loading())

  private val _isFollowing = MutableStateFlow(false)
  val isFollowing: StateFlow<Boolean> = _isFollowing.asStateFlow()

  private var isProfileFetched = false

  fun fetchUserProfile() {
    if (!isProfileFetched) {
      viewModelScope.launch {
        userRepository.getUserProfile().collect { resource ->
          _loggedInUserProfileState.value = resource
          isProfileFetched = true
        }
      }
    }
  }

  fun fetchUserProfileById(userId: String) {
    viewModelScope.launch {
      userRepository.getUserProfileById(userId).collect { resource ->
        _profileState.value = resource
      }
    }
  }

  fun editProfile(id: String, user: EditProfileRequest) {
    viewModelScope.launch {
      userRepository.editProfile(id, user).collect { resource -> _profileState.value = resource }
    }
  }

  fun checkIfFollowing(userId: String) {
    viewModelScope.launch {
      userRepository.isFollowingUser(userId).collect { resource ->
        if (resource is Resource.Success) {
          _isFollowing.value = resource.data?.isFollowing ?: false
        }
      }
    }
  }

  fun toggleFollowUser(userId: String) {
    viewModelScope.launch {
      userRepository.followUser(userId).collect { resource ->
        if (resource is Resource.Success) {
          // Update isFollowing based on the API response
          _isFollowing.value = !_isFollowing.value

          // Update followers count locally
          _profileState.value =
            _profileState.value.data
              ?.let { profile ->
                val updatedFollowersCount =
                  if (_isFollowing.value) {
                    profile.user?.followersCount?.plus(1)
                  } else {
                    profile.user?.followersCount?.minus(1)
                  }
                profile.copy(user = profile.user?.copy(followersCount = updatedFollowersCount))
              }
              ?.let { Resource.Success(it) } ?: _profileState.value

          fetchUserProfileById(userId) // Optionally refresh the profile from the server
        }
        _followState.value = resource
      }
    }
  }

  fun refreshProfile() {
    isProfileFetched = false // Reset flag to allow refresh
    fetchUserProfile()
  }
}
