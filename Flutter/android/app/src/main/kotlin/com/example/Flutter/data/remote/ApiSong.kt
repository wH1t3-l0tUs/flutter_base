package io.driverdoc.testapp.data.remote

import io.driverdoc.testapp.data.model.ItemSong
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSong {
    @GET("/jOut.ashx")
    fun getSongs(
            @Query(value = "k") name: String,
            @Query(value = "k") webName: String = "nhaccuatui.com",
            @Query(value = "code") code: String = "sdfsdf"
    ): Observable<MutableList<ItemSong>>
}