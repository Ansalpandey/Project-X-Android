package com.example.project_x.data.datasource

import com.example.project_x.common.Resource
import com.example.project_x.data.api.AuthenticatedApiService
import com.example.project_x.data.model.CourseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CourseDataSource
@Inject
constructor(private val authenticatedApiService: AuthenticatedApiService) {

  suspend fun getCourses(page: Int, pageSize: Int): Flow<Resource<CourseResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.getCourses(page = page, pageSize = pageSize)
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
            ?: run { emit(Resource.Error("Failed to fetch courses: Empty response body")) }
      } else {
        emit(Resource.Error("Error fetching courses: ${response.message()}"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun getUserCourses(): Flow<Resource<CourseResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.getUserCourses()
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
            ?: run { emit(Resource.Error("Failed to fetch courses: Empty response body")) }
      } else {
        emit(Resource.Error("Error fetching courses: ${response.message()}"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error."))
    }
  }
}
