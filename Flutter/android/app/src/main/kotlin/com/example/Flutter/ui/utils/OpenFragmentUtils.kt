package io.driverdoc.testapp.ui.utils

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.ui.base.AnimationScreen
import io.driverdoc.testapp.ui.base.fragment.BaseFragment
import io.driverdoc.testapp.ui.main.HomeRootFragment
import io.driverdoc.testapp.ui.main.acc.AccountFragment
import io.driverdoc.testapp.ui.main.dashboard.DashboardFragment
import io.driverdoc.testapp.ui.main.documentview.DocumentViewFragment
import io.driverdoc.testapp.ui.main.incompletrip.TripCompleteFragment
import io.driverdoc.testapp.ui.main.incompltripdetail.IncompTripDetailFragment
import io.driverdoc.testapp.ui.main.scheduletrips.ScheduledTripFragment
import io.driverdoc.testapp.ui.main.scorecard.ScoreCardFragment
import io.driverdoc.testapp.ui.main.tripsactive.TripFragment
import io.driverdoc.testapp.ui.splash.SplashFragment
import io.driverdoc.testapp.ui.start.acctype.AccountTypeFragment
import io.driverdoc.testapp.ui.start.acctype.createacc.CreateAccountFragment
import io.driverdoc.testapp.ui.start.permision.PermissionFragment
import io.driverdoc.testapp.ui.start.started.StartedFragment
import io.driverdoc.testapp.ui.start.verifi.VerifiFragment


object OpenFragmentUtils {
    @JvmStatic
    fun getAnimationScreenFullOpen(): AnimationScreen {
        return AnimationScreen(R.anim.enter_to_left, R.anim.exit_to_left, R.anim.enter_to_right, R.anim.exit_to_right)

    }
// @JvmStatic
//    fun getAnimationScreenFullOpen(): AnimationScreen {
//        return AnimationScreen(R.anim.animate_swipe_left_enter, R.anim.animate_swipe_left_exit, R.anim.animate_swipe_right_enter, R.anim.animate_swipe_right_exit)
//    }

    @JvmStatic
    fun getAnimationBackScreenFullOpen(): AnimationScreen {
        return AnimationScreen(R.anim.enter_to_right, R.anim.exit_to_right, R.anim.enter_to_left, R.anim.exit_to_left)
    }
//    @JvmStatic
//    fun getAnimationBackScreenFullOpen(): AnimationScreen {
//        return AnimationScreen(R.anim.animate_swipe_right_enter, R.anim.animate_swipe_right_exit, R.anim.animate_swipe_left_enter, R.anim.animate_swipe_left_exit)
//    }

    @JvmStatic
    fun animationUpDown(): AnimationScreen {
        return AnimationScreen(R.anim.animate_slide_up_enter, R.anim.animate_slide_up_exit, R.anim.animate_slide_down_enter, R.anim.animate_slide_down_exit)
    }

    @JvmStatic
    fun animationFaded(): AnimationScreen {
        return AnimationScreen(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit, R.anim.animate_zoom_enter, R.anim.animate_zoom_exit)
    }


    @JvmStatic
    fun openHomeRootFragment(manager: FragmentManager, isActive: Boolean) {
        val transaction = manager.beginTransaction()
        val animation = animationFaded()
        val bundle = Bundle()
        bundle.putBoolean(Constants.IS_ACTIVE, isActive)
        BaseFragment.openFragment(manager, transaction, HomeRootFragment::class.java, bundle, true, true, animation, R.id.content)


    }

    @JvmStatic
    fun openSplashFragment(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        BaseFragment.openFragment(manager, transaction, SplashFragment::class.java, null, true, true, null, R.id.content)
    }

    @JvmStatic
    fun openStartedFragment(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        BaseFragment.openFragment(manager, transaction, StartedFragment::class.java, null, true, true, null, R.id.content)
    }

    @JvmStatic
    fun openPermistionFragment(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        BaseFragment.openFragment(manager, transaction, PermissionFragment::class.java, null, true, true, null, R.id.content)
    }

    @JvmStatic
    fun openVerifiFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>, phone: String, countryCode: String) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = VerifiFragment()
        val bundle = Bundle()
        bundle.putString(Constants.PHONE, phone)
        bundle.putString(Constants.COUNTRY_CODE, countryCode)
        BaseFragment.openFragment(manager, transaction, fragment, bundle, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openAccountFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = AccountFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openScoreCardFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, false, false, clazzHide.name)
        val fragment = ScoreCardFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openStartFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationBackScreenFullOpen()
        BaseFragment.removeFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = StartedFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openCreateAccountFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = CreateAccountFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openBackHomeRootFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>, isActive: Boolean) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationBackScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = HomeRootFragment()
        val bundle = Bundle()
        bundle.putBoolean(Constants.IS_ACTIVE, isActive)
        BaseFragment.openFragment(manager, transaction, fragment, bundle, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openIncompleDetailFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>, id: String?, id_trip: Int?, time: String?, departure: String?, listUrl: ArrayList<String>?,
                                   departure_status: Boolean?, arrival_status: Boolean, document_status: Boolean, type: String, document_type: String) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = IncompTripDetailFragment()
        val bundle = Bundle()
        bundle.putString(Constants.NUMBER_TRIP, id!!)
        bundle.putInt(Constants.ID_TRIP, id_trip!!)
        bundle.putString(Constants.TIME_TRIP, time)
        bundle.putString(Constants.DEPARTURE, departure)
        bundle.putString(Constants.TYPE, type)
        bundle.putBoolean(Constants.DE_DEP, departure_status!!)
        bundle.putBoolean(Constants.DE_STATUS, arrival_status)
        bundle.putBoolean(Constants.DE_DOCUMENT, document_status)
        bundle.putString(Constants.TYPE_DOCUMENT, document_type)
        bundle.putStringArrayList(Constants.LIST_URL, listUrl)
        BaseFragment.openFragment(manager, transaction, fragment, bundle, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openDocumentViewFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>, listUrl: ArrayList<String>?, isLocal: Boolean, id: Int, isDelete: Boolean) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = DocumentViewFragment()
        val bundle = Bundle()
        bundle.putStringArrayList(Constants.LIST_URL, listUrl)
        bundle.putBoolean(Constants.LOCAL, isLocal)
        bundle.putInt(Constants.ID_TRIP, id)
        bundle.putBoolean(Constants.IS_DELETE, isDelete)
        BaseFragment.openFragment(manager, transaction, fragment, bundle, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openIncompleteFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = TripCompleteFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openScheduleFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = ScheduledTripFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openTripFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = animationFaded()
        BaseFragment.hideFragment(manager, transaction, animation, false, false, clazzHide.name)
        val fragment = TripFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }


    @JvmStatic
    fun openDashboardFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = animationFaded()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = DashboardFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openHomeRootFragment(manager: FragmentManager, isActive: Boolean, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = animationFaded()
        BaseFragment.removeFragment(manager, transaction, animation, true, false, clazzHide.name)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IS_ACTIVE, isActive)
        BaseFragment.openFragment(manager, transaction, DashboardFragment::class.java, bundle, true, true, animation, R.id.content)


    }

    @JvmStatic
    fun openDashboardAnimationFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = DashboardFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openDashboardAnimationFragmentNotBack(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.removeFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = DashboardFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openPermisstionFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = PermissionFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }

    @JvmStatic
    fun openAccountTypeFragment(manager: FragmentManager, clazzHide: Class<out BaseFragment>) {
        val transaction = manager.beginTransaction()
        val animation = getAnimationScreenFullOpen()
        BaseFragment.hideFragment(manager, transaction, animation, true, false, clazzHide.name)
        val fragment = AccountTypeFragment()
        BaseFragment.openFragment(manager, transaction, fragment, null, true, true, animation, R.id.content)
    }
}