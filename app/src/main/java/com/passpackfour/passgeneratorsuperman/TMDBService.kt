package com.passpackfour.passgeneratorsuperman

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY ="450fc2a680aa5693bf2e69ce39671d03"
        const val MOVIE_ID = 1452
    }

    @GET("movie/{movie_id}/keywords")
    suspend fun getMoviByID(
        @Path("movie_id") movieId:Int = MOVIE_ID,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en"
    ): Response<ResponseKeyvords>
}