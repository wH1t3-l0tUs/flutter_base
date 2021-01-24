package io.driverdoc.testapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ItemSong")
data class ItemSong(
        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("Id")
        var id: String = "",

        @ColumnInfo(name = "keySearch")
        var keySearch: String = "",

        @ColumnInfo(name = "title")
        @SerializedName("Title")
        var title: String? = null,

        @ColumnInfo(name = "avatar")
        @SerializedName("Avatar")
        var avatar: String? = null,

        @ColumnInfo(name = "urlJunDownload")
        @SerializedName("UrlJunDownload")
        var urlJunDownload: String? = null,

        @ColumnInfo(name = "lyricsUrl")
        @SerializedName("LyricsUrl")
        var lyricsUrl: String? = null,

        @ColumnInfo(name = "urlSource")
        @SerializedName("UrlSource")
        var urlSource: String? = null,

        @ColumnInfo(name = "siteId")
        @SerializedName("SiteId")
        var siteId: String? = null,

        @ColumnInfo(name = "artist")
        @SerializedName("Artist")
        var artist: String? = null
) {
}