package com.example.project_x.data.api

import com.example.project_x.data.model.CourseResponse
import com.example.project_x.data.model.EditProfileRequest
import com.example.project_x.data.model.FollowMessage
import com.example.project_x.data.model.PostLikeResponse
import com.example.project_x.data.model.PostRequest
import com.example.project_x.data.model.PostResponse
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthenticatedApiService {

  @GET("users/profile") suspend fun getUserProfile(): Response<ProfileResponse>

  @GET("users/profile/{id}")
  suspend fun getUserProfileById(@Path("id") id: String): Response<ProfileResponse>

  @POST("users/follow/{id}") suspend fun followUser(@Path("id") id: String): Response<FollowMessage>

  @GET("users/is-following/{id}")
  suspend fun isFollowingUser(@Path("id") id: String): Response<FollowMessage>

  @GET("courses")
  suspend fun getCourses(
    @Query("page") page: Int,
    @Query("pageSize") pageSize: Int,
  ): Response<CourseResponse>

  @GET("users/courses") suspend fun getUserCourses(): Response<CourseResponse>

  @POST("users/refresh-token")
  suspend fun refreshToken(@Body token: String): Response<TokenResponse>

  @GET("posts")
  suspend fun getPosts(@Query("page") page: Int, @Query("pageSize") pageSize: Int): PostResponse

  @POST("posts/create") suspend fun createPost(@Body post: PostRequest): Response<PostResponse>

  @POST("posts/{id}/like") suspend fun likePost(@Path("id") id: String): Response<PostLikeResponse>

  @POST("posts/{id}/unlike") suspend fun unLikePost(@Path("id") id: String): Response<PostLikeResponse>

  @PUT("users/edit-profile/{id}") suspend fun editProfile(@Path("id") id: String, @Body user: EditProfileRequest): Response<ProfileResponse>
}
