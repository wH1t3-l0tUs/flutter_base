package io.driverdoc.testapp.ui.base.model

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.api.IBaseResponse
import io.driverdoc.testapp.data.model.base.error.ErrorResponse
import io.driverdoc.testapp.data.model.base.error.ResponseException
import io.driverdoc.testapp.data.model.trip.ManualResponse
import io.driverdoc.testapp.data.model.trip.TripRespone
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.ui.base.callback.BaseCallBack
import io.driverdoc.testapp.ui.main.MainActivity.Companion.isLogout
import io.driverdoc.testapp.ui.start.verifi.VerifiFragment
import io.driverdoc.testapp.ui.start.verifi.VerifiFragment.Companion.isShowDialog
import io.driverdoc.testapp.ui.start.verifi.VerifiFragment.Companion.isShowDialogBand
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException
import java.util.concurrent.Executor

abstract class BaseViewModel<CallBack : BaseCallBack> : ViewModel {
    protected val appDatabase: AppDatabase
    protected val interactCommon: InteractCommon
    protected val schedulers: Executor
    var callBack: WeakReference<CallBack>? = null
    protected val mDiableAll: CompositeDisposable
    protected val mIsLoading = MutableLiveData<Boolean>()
    protected var mIsDestroy: Boolean

    constructor(appDatabase: AppDatabase,
                interactCommon: InteractCommon,
                schedulers: Executor) {
        this.appDatabase = appDatabase
        this.schedulers = schedulers
        this.interactCommon = interactCommon
        this.mDiableAll = CompositeDisposable()
        mIsDestroy = false
    }


    override fun onCleared() {
        mDiableAll.dispose()
        super.onCleared()
    }

    fun isLoading() = mIsLoading
    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.postValue(isLoading)
    }


    companion object {
        fun checkExpire(data: IBaseResponse): Boolean {
            return data.getErrorCode() == io.driverdoc.testapp.common.Constants.CODE_TOKEN_EXPIRE || data.getStatus() == io.driverdoc.testapp.common.Constants.CODE_TOKEN_EXPIRE
        }

        fun checkExpire(data: Throwable): Boolean {
            if (data is retrofit2.HttpException) {
                if (data.code() == io.driverdoc.testapp.common.Constants.CODE_TOKEN_EXPIRE) {
                    return true
                }
            }
            return false
        }
    }


    protected fun <T : IBaseResponse> subscribeHasDispose(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        if (observable == null) {
            return
        }
        mDiableAll.add(observable.subscribe(
                {
                    if (mIsDestroy) {
                        return@subscribe
                    }

                    onNext(it)
                },
                {
                    run {
                        if (io.driverdoc.testapp.common.Constants.DEBUG) {
                            it.printStackTrace()
                        }
                        if (mIsDestroy) {
                            return@run
                        }
                    }
                    onError(it)
                }))

    }

    protected fun <T : MutableList<out IBaseResponse>> subscribeListHasDispose(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        if (observable == null) {
            return
        }
        mDiableAll.add(observable.subscribe(
                {
                    if (mIsDestroy) {
                        return@subscribe
                    }
                    onNext(it)
                },
                {
                    run {
                        if (io.driverdoc.testapp.common.Constants.DEBUG) {
                            it.printStackTrace()
                        }
                        if (mIsDestroy) {
                            return@run
                        }
                        onError(it)
                    }
                }));

    }

    protected fun <T> subscribeListHasResultDispose(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable? {
        if (observable == null) {
            return null
        }
        return observable.subscribe(
                {
                    if (mIsDestroy) {
                        return@subscribe
                    }
                    onNext(it)
                },
                {
                    run {
                        if (io.driverdoc.testapp.common.Constants.DEBUG) {
                            it.printStackTrace()
                        }
                        if (mIsDestroy) {
                            return@run;
                        }
                        onError(it)
                    }
                })

    }


    @SuppressLint("CheckResult")
    protected inline fun <reified T> subscribeNotDisposeTrip(observable: Observable<T>?, crossinline onNext: (T) -> Unit) {
        if (observable == null) {
            return
        }
        setIsLoading(true)
        val isDestroy: () -> Boolean = { mIsDestroy }
        val className = T::class.java.name
        observable.subscribe(
                {
                    if (isDestroy()) {
                        return@subscribe
                    }
                    setIsLoading(false)
                    onNext(it)
                },
                {
                    if (io.driverdoc.testapp.common.Constants.DEBUG) {
                        it.printStackTrace()
                    }
                    if (isDestroy()) {
                        return@subscribe
                    }
                    setIsLoading(false)
                    if (it is HttpException) {
                        if ((it as HttpException).code() == 400) {
                            val resStrError = it.response().errorBody()?.string()
                            val errorResponse = callBack?.get()?.gson()?.fromJson<ErrorResponse>(resStrError, ErrorResponse::class.java)
                            val dataConvert = ManualResponse()
                            errorResponse?.let {
                                dataConvert.setMessage(errorResponse.messages!!)
                                dataConvert.setSuccess(errorResponse.success!!)
                                onNext(dataConvert as T)
                            }

                            return@subscribe
                        }else{
                            handlerErrorExecption(it, { className })
                            return@subscribe
                        }
                    }
                })
    }

    @SuppressLint("CheckResult")
    protected inline fun <reified T> subscribeNotDispose(observable: Observable<T>?, crossinline onNext: (T) -> Unit) {
        if (observable == null) {
            return
        }
        setIsLoading(true)
        val isDestroy: () -> Boolean = { mIsDestroy }
        val className = T::class.java.name
        observable.subscribe(
                {
                    if (isDestroy()) {
                        return@subscribe
                    }
                    setIsLoading(false)
                    onNext(it)
                },
                {
                    if (io.driverdoc.testapp.common.Constants.DEBUG) {
                        it.printStackTrace()
                    }
                    if (isDestroy()) {
                        return@subscribe
                    }
                    setIsLoading(false)
                    handlerErrorExecption(it, { className })
                })
    }

    @SuppressLint("CheckResult")
    protected inline fun <reified T> subscribeNotDisposeNotLoading(observable: Observable<T>?, crossinline onNext: (T) -> Unit) {
        if (observable == null) {
            return
        }
        setIsLoading(false)
        val isDestroy: () -> Boolean = { mIsDestroy }
        val className = T::class.java.name
        observable.subscribe(

                {
                    setIsLoading(false)

                    if (isDestroy()) {
                        return@subscribe
                    }
                    onNext(it)
                    setIsLoading(false)
                },
                {
                    setIsLoading(false)
                    if (io.driverdoc.testapp.common.Constants.DEBUG) {
                        it.printStackTrace()
                    }
                    if (isDestroy()) {
                        return@subscribe
                    }
                    handlerErrorExecption(it, { className })
                })
    }

    @SuppressLint("CheckResult")
    protected inline fun <reified T> subscribeNotDisposeNotLoadingActiveTrip(observable: Observable<T>?, crossinline onNext: (T) -> Unit) {
        if (observable == null) {
            return
        }
        setIsLoading(false)
        val isDestroy: () -> Boolean = { mIsDestroy }
        val className = T::class.java.name
        observable.subscribe(

                {
                    setIsLoading(false)

                    if (isDestroy()) {
                        return@subscribe
                    }
                    onNext(it)
                    setIsLoading(false)
                },
                {
                    setIsLoading(false)
                    if (io.driverdoc.testapp.common.Constants.DEBUG) {
                        it.printStackTrace()
                    }
                    if (isDestroy()) {
                        return@subscribe
                    }
                    setIsLoading(false)
                    if (it is HttpException) {
                        if ((it as HttpException).code() == 404) {
                            val resStrError = it.response().errorBody()?.string()
                            val errorResponse = callBack?.get()?.gson()?.fromJson<ErrorResponse>(resStrError, ErrorResponse::class.java)
                            val dataConvert = TripRespone()
                            errorResponse?.let {
                                dataConvert.setMessage(errorResponse.messages!!)
                                dataConvert.setSuccess(errorResponse.success!!)
                                dataConvert.setErrorCode(errorResponse.errorCode!!)
                                onNext(dataConvert as T)
                            }

                            return@subscribe
                        }else{
                            try {
                                handlerErrorExecption(it, { className })
                                return@subscribe
                            }catch (e: Exception){

                            }
                        }
                    }
                })
    }

    protected inline fun <reified T> subscribeHasResultDispose(observable: Observable<T>?, crossinline onNext: (T) -> Unit): Disposable? {
        if (observable == null) {
            return null
        }
        setIsLoading(true)
        val isDestroy: () -> Boolean = { mIsDestroy }
        val className = T::class.java.name
        return observable.subscribe(
                {
                    setIsLoading(false)

                    if (isDestroy()) {
                        return@subscribe
                    }
                    onNext(it)
                },
                {
                    setIsLoading(false)
                    run {
                        if (io.driverdoc.testapp.common.Constants.DEBUG) {
                            it.printStackTrace()
                        }
                        if (isDestroy()) {
                            return@run
                        }
                        handlerErrorExecption(it, { className })
                    }
                })
    }

    protected inline fun <reified T> subscribeHasResultDisposeLocation(observable: Observable<T>?, crossinline onNext: (T) -> Unit): Disposable? {
        if (observable == null) {
            return null
        }
        val isDestroy: () -> Boolean = { mIsDestroy }
        val className = T::class.java.name
        return observable.subscribe(
                {

                    if (isDestroy()) {
                        return@subscribe
                    }
                    onNext(it)
                },
                {
                    run {
                        if (io.driverdoc.testapp.common.Constants.DEBUG) {
                            it.printStackTrace()
                        }
                        if (isDestroy()) {
                            return@run
                        }
                        handlerErrorExecption(it, { className })
                    }
                })
    }

    protected fun makeFunOnOtherThread(method: () -> Unit) {
        Observable.create((ObservableOnSubscribe<Boolean> {
            method()
            it.onNext(true)
            it.onComplete()
        }))
                .subscribeOn(Schedulers.from(schedulers))
                .subscribe()
    }

    protected fun handlerErrorExecption(error: Throwable, className: () -> String) {
        if (error is HttpException) {
            if (error.code() == 500) {

            } else {
                try {
                    val resStrError = error.response().errorBody()?.string()
                    val errorResponse = callBack?.get()?.gson()?.fromJson<ErrorResponse>(resStrError, ErrorResponse::class.java)
                    if (errorResponse != null) {
                        errorResponse.errorCode.let {
                            if (it.equals("DRIVER_IS_LOCKED") || it.equals("TWILIO_SEND_CODE_FAIL")) {
                                isShowDialogBand.postValue(true)
                                SharedPfPermissionUtils.saveMessageVerifyTokenFail(errorResponse.messages!!)
                                SharedPfPermissionUtils.saveJwtToken("")
                            } else if (it.equals("CODE_INVALID")) {
                                isShowDialog.postValue(true)
                            } else if (it.equals("DRIVER_FORBIDDEN")) {
                                liveData.postValue(5)
                            } else if (it.equals("VERIFY_TOKEN_FAIL")) {
                                isLogout.postValue(true)
                            } else if (it.equals("VERIFY_APP_VERSION")) {
                                liveData.postValue(Constants.UPDATE_VERSION_REQUIRED)
                            } else if (it.equals("VERIFY_APP_TOKEN_FAIL")) {
                                SharedPfPermissionUtils.saveMessageVerifyTokenFail(errorResponse.messages!!)
                                SharedPfPermissionUtils.saveJwtToken("")
                                isShowDialogBand.postValue(true)
                            } else if (it.equals("INVALID_APP_TOKEN")) {
                                isShowDialogBand.postValue(true)
                                SharedPfPermissionUtils.saveMessageVerifyTokenFail(errorResponse.messages!!)
                                SharedPfPermissionUtils.saveJwtToken("")
                            } else if (it.equals("ACTIVE_TRIPS_NOT_FOUND")) {
                                MVVMApplication.isNotrip = false
                                liveData.postValue(4)
                            } else if (it.equals("GET_ACTIVE_TRIPS_FAIL")) {
                                liveData.postValue(4)
                            } else {
                                errorResponse.messages?.let { it1 -> ResponseException(it1, errorResponse) }?.let { it2 -> callBack?.get()?.onError(className(), it2) }
                            }
                        }
                    } else {
                        callBack?.get()?.onError(className(), ResponseException(error.message(), ResponseException.ERROR_UNKNOWN))
                        if (error.code() == 401){
                            SharedPfPermissionUtils.saveJwtToken("")
                            isLogout.postValue(true)
                            isShowDialogBand.postValue(true)
                        }
                    }
                } catch (e: JsonSyntaxException) {

                } catch (e: TypeCastException) {

                }
            }
        } else {
            if (error is SocketTimeoutException) {
                error.message?.let { ResponseException(it, ResponseException.ERROR_TIME_OUT) }?.let { callBack?.get()?.onError(className(), it) }
            } else {
                if (error is IOException) {
                    callBack?.get()?.onError(className(), ResponseException(ResponseException.ERROR_INTERNET_MESSAGE, ResponseException.ERROR_INTERNET))
                } else {
                    error.message?.let { ResponseException(it, ResponseException.ERROR_UNKNOWN) }?.let { callBack?.get()?.onError(className(), it) }
                }
            }
        }


    }

    protected fun handlerListErrorExecption(error: Throwable, className: () -> String) {
        if (error is HttpException) {
            val resStrError = error.response().errorBody()?.string()
            val errorResponse = callBack?.get()?.gson()?.fromJson<ErrorResponse>(resStrError, ErrorResponse::class.java)
            if (errorResponse != null) {
                errorResponse.errorCode?.let { ResponseException(it, errorResponse) }?.let { callBack?.get()?.onErrorList(className(), it) }
            } else {
                callBack?.get()?.onErrorList(className(), ResponseException(error.message(), ResponseException.ERROR_UNKNOWN))
            }
        } else {
            if (error is SocketTimeoutException) {
                callBack?.get()?.onErrorList(className(), ResponseException(error.message!!, ResponseException.ERROR_TIME_OUT))
            } else {
                if (error is IOException) {
                    callBack?.get()?.onErrorList(className(), ResponseException(ResponseException.ERROR_INTERNET_MESSAGE, ResponseException.ERROR_INTERNET))
                } else {
                    callBack?.get()?.onErrorList(className(), ResponseException(error.message!!, ResponseException.ERROR_UNKNOWN))
                }
            }
        }
    }

}
