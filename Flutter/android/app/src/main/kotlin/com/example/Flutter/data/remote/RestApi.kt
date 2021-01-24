package io.driverdoc.testapp.data.remote

import io.driverdoc.testapp.data.model.ItemSong
import io.driverdoc.testapp.data.model.LocationRespone
import io.driverdoc.testapp.data.model.VersionRespone
import io.driverdoc.testapp.data.model.document.DeleteDocumentRespone
import io.driverdoc.testapp.data.model.document.DocumentRespone
import io.driverdoc.testapp.data.model.document.ScoreCardRepone
import io.driverdoc.testapp.data.model.login.LoginRespone
import io.driverdoc.testapp.data.model.login.VerifiCodeRespone
import io.driverdoc.testapp.data.model.map.MapRespone
import io.driverdoc.testapp.data.model.trip.*
import io.driverdoc.testapp.data.model.user.UserRespone
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface RestApi {

    //    @POST("/api/user/check")
//    fun checkMail(@Body checkMail: RequestBody): Observable<CheckEmailRepone>
//   @POST("/api/sign-up-with-email")
//    fun createAcount(@Body checkMail: RequestBody): Observable<CreateAcountRespone>
//
    @POST("/api/v1/home/sendOtp")
    fun sendOtp(@Body checkMail: RequestBody): Observable<LoginRespone>

    @POST("/api/v1/home/verifyOtp")
    fun verifiCode(@Body body: RequestBody): Observable<VerifiCodeRespone>

    @GET("/api/v1/trips/active")
    fun getActiveTrip(): Observable<TripRespone>

    @GET("/api/v1/home/getInfoDriver")
    fun getInfor(): Observable<VerifiCodeRespone>

    @GET("/api/v1/trips/incomplete")
    fun getIncomple(): Observable<IncompleTripsRespone>

    @GET("/api/v1/trips/schedule")
    fun getSchedue(): Observable<ScheduleTripRespone>

    @GET("/api/v1/config/getConfig")
    fun getVersion(
            @Query("appVersion") appVersion: String,
            @Query("platform") platform: String

    ): Observable<VersionRespone>

    @GET("/api/v1/home/scoreCard")
    fun getScoreCard(): Observable<ScoreCardRepone>

    @POST("/api/v1/locations/")
    fun postLocation(@Body body: RequestBody): Observable<LocationRespone>

    @POST("/api/v1/locations/eta")
    fun postEta(@Body body: RequestBody): Observable<LocationRespone>

    @POST("/api/v1/documents/url")
    fun getPresign(@Body body: RequestBody): Observable<ViewDocumentRespone>

    @POST("/api/v1/documents/uploadFile")
    fun postDocument(@Body body: RequestBody): Observable<DeleteDocumentRespone>

    @POST("/api/v1/devices/")
    fun setupNotifi(@Body body: RequestBody): Observable<DocumentRespone>

    @PUT("/api/v1/home/updateUser")
    fun updateUser(@Body body: RequestBody): Observable<UserRespone>

    @PUT("/api/v1/documents/updateDocument")
    fun updateDocument(@Body body: RequestBody): Observable<DeleteDocumentRespone>

    @GET("/jOut.ashx")
    fun getSongs(
            @Query(value = "k") name: String,
            @Query(value = "k") webName: String = "nhaccuatui.com",
            @Query(value = "code") code: String = "sdfsdf"
    ): Observable<MutableList<ItemSong>>

    @GET("/maps/api/directions/json")
    fun getLaLo(
            @Query(value = "origin") origin: String,
            @Query(value = "destination") destination: String,
            @Query(value = "mode") mode: String,
            @Query(value = "key") key: String
    ): Observable<MapRespone>

    @HTTP(method = "DELETE", path = "/api/v1/devices/{id}", hasBody = true)
    fun deleteUser(@Body deleteUser: RequestBody, @Path("id") id: String): Observable<DocumentRespone>

    @PUT("/api/v1/documents/updateDocument")
    fun delete(@Body body: RequestBody): Observable<DeleteDocumentRespone>

    @PUT("/api/v1/home/updateDriver")
    fun updateDriver(@Body body: RequestBody): Observable<UserRespone>

    @PUT("/api/v1/home/updateManual")
    fun updateManual(@Body body: RequestBody): Observable<ManualResponse>
}