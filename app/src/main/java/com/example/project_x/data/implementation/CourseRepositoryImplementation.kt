package com.example.project_x.data.implementation

import com.example.project_x.common.Resource
import com.example.project_x.data.datasource.CourseDataSource
import com.example.project_x.data.model.CourseResponse
import com.example.project_x.data.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CourseRepositoryImplementation
@Inject
constructor(private val courseDataSource: CourseDataSource) : CourseRepository {
  // Implementation of CourseRepository
  override suspend fun getCourses(page: Int, pageSize: Int): Flow<Resource<CourseResponse>> {
    return courseDataSource.getCourses(page, pageSize)
  }

  override suspend fun getUserCourses(): Flow<Resource<CourseResponse>> {
    return courseDataSource.getUserCourses()
  }
}
