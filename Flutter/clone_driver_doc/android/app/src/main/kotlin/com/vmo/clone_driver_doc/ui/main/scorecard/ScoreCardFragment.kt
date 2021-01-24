package io.driverdoc.testapp.ui.main.scorecard

import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.databinding.FragmentScorecardBinding
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.lang.ref.WeakReference

/**
 * Created by khaipc on 26,December,2019
 */
class ScoreCardFragment : BaseMvvmFragment<ScoreCardCallback, ScoreCardModel>(), ScoreCardCallback, View.OnClickListener {
    override fun onClick(p0: View?) {
        onBackRoot()
    }

    override fun onBackRoot() {
        super.onBackRoot()
        MVVMApplication.liveData.postValue(Constants.RELOAD_TRIP)
    }

    override fun createModel(): ScoreCardModel {
        val model = ScoreCardModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(ScoreCardModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_scorecard
    override fun setEvents() {
        getDataBindig().btnClose.setOnClickListener(this)
    }

    override fun initComponents() {
        Handler().postDelayed({
            SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getScoreCard(it) }
        }, 300)

        mModel.obScoreCard.observe(this, Observer {
            if (it.getSucces()) {

                it.getData()?.total_trips?.let { it1 ->
                    if (it1 < 2) {
                        getDataBindig().tvCount.setText(it1.toString() + " Trip")
                    } else {
                        getDataBindig().tvCount.setText(it1.toString() + " Trips")
                    }
                }
                it.getData()?.on_time?.let { it1 -> getDataBindig().tvOnTime.setText(it1) }
                it.getData()?.document?.let { it1 -> getDataBindig().tvDocument.setText(it1) }
            }
        })
        mModel.callBack = WeakReference(this)
    }

    fun getDataBindig() = mBinding as FragmentScorecardBinding
}