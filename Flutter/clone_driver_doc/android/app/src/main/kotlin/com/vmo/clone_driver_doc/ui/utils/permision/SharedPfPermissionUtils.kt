package io.driverdoc.testapp.ui.utils.permision

import android.content.Context
import io.driverdoc.testapp.common.MVVMApplication
import java.util.*

object SharedPfPermissionUtils {
    val SYSTEM_CONFIG = "SYSTEM_CONFIG"
    val TOKEN = "TOKEN"
    val USER_ID = "USER_ID"
    val LASTNAME = "LASTNAME"
    val FIRSTNAME = "FIRSTNAME"
    val PHONE = "PHONE"
    val ID_TRIP = "ID_TRIP"
    val HEIGHT = "HEIGHT"
    val INCOMPLETE = "INCOMPLETE"
    val HAS_TRIP = "HAS_TRIP"
    val NEW_TRIP = "NEW_TRIP"
    val LAT = "LAT"
    val LONG = "LONG"
    val NAME_DOCUMENT = "NAME_DOCUMENT"
    val HOME = "HOME"
    val TRIP = "TRIP"
    val TYPE_NOTI = "TYPE_NOTI"
    val EMAIL = "EMAIL"
    val STATE = "STATE"
    val CDL_NUMBER = "CDL_NUMBER"
    val CDL_DATE = "CDL_DATE"
    val IS_START = "IS_START"
    val SPEED = "SPEED"
    val TRIP_NUMBER = "TRIP_NUMBER"
    val RECENT_FILENAME = "RECENT_FILENAME"
    val IP_PUBLIC = "IP_PUBLIC"
    val TIME_CALL_LOCATION = "TIME_CALL_LOCATION"
    val IS_CALL_LOCATION = "IS_CALL_LOCATION"
    val IS_LOGIN = "IS_LOGIN"
    val VERIFY_APP_TOKEN_FAIL = "VERIFY_APP_TOKEN_FAIL"


    @JvmStatic
    fun getNumberDeniedPermission(context: Context, permissionType: String): Int {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getInt(permissionType, 0)
    }

    @JvmStatic
    fun clearData(context: Context) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        sf.edit().clear().commit()
    }

    @JvmStatic
    fun getToken(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(TOKEN, "")
    }

    @JvmStatic
    fun getUserId(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(USER_ID, "")
    }

    @JvmStatic
    fun getFirstName(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(FIRSTNAME, "")
    }


    @JvmStatic
    fun getLastName(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(LASTNAME, "")
    }

    @JvmStatic
    fun getPhone(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(PHONE, "")
    }

    @JvmStatic
    fun getNameDocument(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(NAME_DOCUMENT, "")
    }

    @JvmStatic
    fun getIncomplete(context: Context): Boolean {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getBoolean(INCOMPLETE, false)
    }


    @JvmStatic
    fun getHasTrips(context: Context): Boolean {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getBoolean(HAS_TRIP, false)
    }

    @JvmStatic
    fun getHome(context: Context): Boolean {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getBoolean(HOME, false)
    }

    @JvmStatic
    fun getTrip(context: Context): Boolean {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getBoolean(TRIP, false)
    }

    @JvmStatic
    fun getIDTrip(context: Context): Int {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getInt(ID_TRIP, 0)
    }

    @JvmStatic
    fun getTypeNoti(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(TYPE_NOTI, "")
    }

    @JvmStatic
    fun getEmail(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(EMAIL, "")
    }

    @JvmStatic
    fun getCdlNumber(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(CDL_NUMBER, "")
    }

    @JvmStatic
    fun getDate(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(CDL_DATE, "")
    }

    @JvmStatic
    fun getTripNumber(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(TRIP_NUMBER, "")
    }

    @JvmStatic
    fun getSpeed(context: Context): Float? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getFloat(SPEED, 0F)
    }

    @JvmStatic
    fun getState(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(STATE, "")
    }

    @JvmStatic
    fun getHeight(context: Context): Int {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getInt(HEIGHT, 0)
    }

    @JvmStatic
    fun getRecentFileName(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(RECENT_FILENAME, "")
    }

    @JvmStatic
    fun increaseNumberDeniedPermission(context: Context, permissionType: String, numberIncrease: Int) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val numberCurrent = sf.getInt(permissionType, 0)
        val editor = sf.edit()
        editor.putInt(permissionType, numberIncrease + numberCurrent)
        editor.apply()
    }

    @JvmStatic
    fun saveNumberDeniedPermission(context: Context, permissionType: String, number: Int) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putInt(permissionType, number)
        editor.apply()
    }

    @JvmStatic
    fun saveRecentFileName(context: Context, fileName: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(RECENT_FILENAME, fileName)
        editor.apply()
    }

    @JvmStatic
    fun saveTripNumber(context: Context, tripNumber: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(tripNumber, TRIP_NUMBER)
        editor.apply()
    }

    @JvmStatic
    fun saveIsStart(context: Context, isStart: Boolean) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean(IS_START, isStart)
        editor.apply()
    }

    @JvmStatic
    fun saveSpeed(context: Context, speed: Float) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putFloat(SPEED, speed)
        editor.apply()
    }

    @JvmStatic
    fun saveJwtToken(context: Context, token: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    @JvmStatic
    fun saveJwtToken(token: String) {
        val sf = MVVMApplication.getContext()?.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf?.edit()
        editor?.putString(TOKEN, token)
        editor?.apply()
    }

    @JvmStatic
    fun saveUserId(context: Context, token: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(USER_ID, token)
        editor.apply()
    }

    @JvmStatic
    fun saveTypeNoti(context: Context, token: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(TYPE_NOTI, token)
        editor.apply()
    }

    @JvmStatic
    fun saveTitleDocument(context: Context, token: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(NAME_DOCUMENT, token)
        editor.apply()
    }

    @JvmStatic
    fun saveHome(context: Context, token: Boolean) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean(HOME, token)
        editor.apply()
    }

    @JvmStatic
    fun saveTrip(context: Context, token: Boolean) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean(TRIP, token)
        editor.apply()
    }

    @JvmStatic
    fun saveLat(context: Context, token: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(LAT, token)
        editor.apply()
    }

    @JvmStatic
    fun saveLong(context: Context, token: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(LONG, token)
        editor.apply()
    }

    @JvmStatic
    fun saveFirstName(context: Context, firstName: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(FIRSTNAME, firstName)
        editor.apply()
    }

    @JvmStatic
    fun saveProfile(context: Context, firstName: String, lastName: String, email: String, phone: String, cdl_number: String, cdl_state: String, exp_date: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(FIRSTNAME, firstName)
        editor.putString(LASTNAME, lastName)
        editor.putString(EMAIL, email)
        editor.putString(PHONE, phone)
        editor.putString(CDL_NUMBER, cdl_number)
        editor.putString(STATE, cdl_state)
        editor.putString(CDL_DATE, exp_date)
        editor.apply()
    }

    @JvmStatic
    fun saveLastName(context: Context, last: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(LASTNAME, last)
        editor.apply()
    }

    @JvmStatic
    fun savePhone(context: Context, phone: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(PHONE, phone)
        editor.apply()
    }

    @JvmStatic
    fun saveIDTrip(context: Context, id: Int) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putInt(ID_TRIP, id)
        editor.apply()
    }

    @JvmStatic
    fun saveHeight(context: Context, id: Int) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putInt(HEIGHT, id)
        editor.apply()
    }

    @JvmStatic
    fun saveIncomplete(context: Context, incom: Boolean) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean(INCOMPLETE, incom)
        editor.apply()
    }

    @JvmStatic
    fun saveHasTrip(context: Context, incom: Boolean) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean(HAS_TRIP, incom)
        editor.apply()
    }

    @JvmStatic
    fun saveNewTrip(context: Context, incom: Boolean) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean(NEW_TRIP, incom)
        editor.apply()
    }

    @JvmStatic
    fun saveIpPublic(context: Context, ip: String) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putString(IP_PUBLIC, ip)
        editor.apply()
    }

    @JvmStatic
    fun getIpPublic(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(IP_PUBLIC, "")
    }

    @JvmStatic
    fun saveCallLocation(context: Context, isCall: Boolean) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean(IS_CALL_LOCATION, isCall)
        editor.apply()
    }

    @JvmStatic
    fun getCallLocation(context: Context): Boolean {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getBoolean(IS_CALL_LOCATION, false)
    }

    @JvmStatic
    fun saveTimeCallLocation(context: Context, time: Date) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putLong(TIME_CALL_LOCATION, time.time)
        editor.apply()
    }

    @JvmStatic
    fun getTimeCallLocation(context: Context): Long {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getLong(TIME_CALL_LOCATION, 0)
    }

    @JvmStatic
    fun saveLogin(context: Context, login: Boolean) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean(IS_LOGIN, login)
        editor.apply()
    }

    @JvmStatic
    fun getLogin(context: Context): Boolean {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getBoolean(IS_LOGIN, false)
    }

    @JvmStatic
    fun saveMessageVerifyTokenFail(message: String) {
        val sf = MVVMApplication.getContext()?.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf?.edit()
        editor?.putString(VERIFY_APP_TOKEN_FAIL, message)
        editor?.apply()
    }

    @JvmStatic
    fun getMessageVerifyTokenFail(context: Context): String? {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getString(VERIFY_APP_TOKEN_FAIL, "")
    }
}
