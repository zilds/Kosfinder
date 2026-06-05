package com.example.kosfinder.api

import com.example.kosfinder.model.BookingRequest
import com.example.kosfinder.model.LoginRequest
import com.example.kosfinder.model.RegisterRequest
import com.example.kosfinder.model.Review
import com.example.kosfinder.model.ReviewRequest
import com.example.kosfinder.response.BookingResponse
import com.example.kosfinder.response.KostCreateResponse
import com.example.kosfinder.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.kosfinder.response.KostListResponse
import com.example.kosfinder.response.OwnerBookingResponse
import com.example.kosfinder.response.PaymentResponse
import com.example.kosfinder.response.RegisterResponse
import com.example.kosfinder.response.ReviewResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.http.Path
import com.example.kosfinder.response.ReviewStoreResponse
interface ApiService {

    @Headers("Accept: application/json", "ngrok-skip-browser-warning: true")
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @Headers("ngrok-skip-browser-warning: true")
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @Headers("ngrok-skip-browser-warning: true")
    @GET("kosts")
    fun getKosts(): Call<KostListResponse>

    @Headers("Accept: application/json", "ngrok-skip-browser-warning: true")
    @Multipart
    @POST("kosts")
    fun tambahKos(
        @Header("Authorization") token: String,

        @Part("nama_kost") namaKos: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("harga_per_bulan") harga: RequestBody,
        @Part("jenis_kost") jenisKos: RequestBody,
        @Part("jumlah_kamar") jumlahKamar: RequestBody,
        @Part("kamar_tersedia") kamarTersedia: RequestBody,

        @Part("fasilitas") fasilitas: RequestBody,

        @Part thumbnail: MultipartBody.Part?,
        @Part gambar_kamar_mandi: MultipartBody.Part?,
        @Part gambar_interior: MultipartBody.Part?,
        @Part gambar_depan: MultipartBody.Part?
    ): Call<KostCreateResponse>

    @Headers(
        "Accept: application/json",
        "ngrok-skip-browser-warning: true"
    )
    @GET("my-kosts")
    fun getMyKosts(
        @Header("Authorization") token: String
    ): Call<KostListResponse>


    @Headers(
        "Accept: application/json",
        "ngrok-skip-browser-warning: true"
    )
    @Multipart
    @POST("kosts/{id}")
    fun updateKost(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("_method") method: RequestBody,
        @Part("nama_kost") namaKos: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("harga_per_bulan") harga: RequestBody,
        @Part("jenis_kost") jenisKos: RequestBody,
        @Part("jumlah_kamar") jumlahKamar: RequestBody,
        @Part("kamar_tersedia") kamarTersedia: RequestBody,
        @Part("fasilitas") fasilitas: RequestBody,
        @Part thumbnail: MultipartBody.Part?,
        @Part gambar_kamar_mandi: MultipartBody.Part?,
        @Part gambar_interior: MultipartBody.Part?,
        @Part gambar_depan: MultipartBody.Part?
    ): Call<ResponseBody>


    @Headers(
        "Accept: application/json",
        "ngrok-skip-browser-warning: true"
    )
    @DELETE("kosts/{id}")
    fun deleteKost(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<KostCreateResponse>

    @Headers(
        "Accept: application/json",
        "ngrok-skip-browser-warning: true"
    )
    @POST("reviews")
    fun kirimReview(
        @Header("Authorization") token: String,
        @Body request: ReviewRequest
    ): Call<ReviewStoreResponse>

    @Headers(
        "Accept: application/json",
        "ngrok-skip-browser-warning: true"
    )
    @POST("bookings")
    fun createBooking(
        @Header("Authorization") token: String,
        @Body request: BookingRequest
    ): Call<BookingResponse>

    @GET("owner-bookings")
    fun getOwnerBookings(
        @Header("Authorization") token: String
    ): Call<OwnerBookingResponse>

    @Headers(
        "Accept: application/json",
        "ngrok-skip-browser-warning: true"
    )
    @POST("midtrans/payment/{bookingId}")
    fun createPayment(
        @Header("Authorization") token: String,
        @Path("bookingId") bookingId: Int
    ): Call<PaymentResponse>


    @Headers(
        "Accept: application/json",
        "ngrok-skip-browser-warning: true"
    )
    @GET("kosts/{id}/reviews")
    fun getReviewByKost(
        @Path("id") kostId: Int
    ): Call<ReviewResponse>
}