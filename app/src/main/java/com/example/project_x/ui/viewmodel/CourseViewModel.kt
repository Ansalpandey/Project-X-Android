package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.common.Resource
import com.example.project_x.data.model.CourseResponse
import com.example.project_x.data.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(private val courseRepository: CourseRepository) :
  ViewModel() {
  private val _courses = MutableStateFlow<Resource<List<CourseResponse>>>(Resource.Loading())
  val courses: StateFlow<Resource<List<CourseResponse>>> = _courses

  fun getCourses() {
    viewModelScope.launch {
      courseRepository.getCourses().collect { resource -> _courses.value = resource }
    }
  }
}
