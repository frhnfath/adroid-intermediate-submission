package com.frhnfath.storyappfix.network

import com.frhnfath.storyappfix.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") header: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : Call<AddStoryResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization")header: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): AllStoriesResponse

    @GET("stories")
    fun getStoriesLocation(
        @Header("Authorization") header: String,
        @Query("location") location: Int = 1
    ): Call<AllStoriesResponse>
}