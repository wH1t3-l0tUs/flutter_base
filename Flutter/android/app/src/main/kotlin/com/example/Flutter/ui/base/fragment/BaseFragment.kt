package io.driverdoc.testapp.ui.base.fragment

import android.app.Activity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.ui.base.AnimationScreen
import io.driverdoc.testapp.ui.base.activity.BaseActivity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import io.driverdoc.testapp.ui.customview.EditTextMedium
import io.driverdoc.testapp.ui.customview.EditTextRegular
import com.google.android.material.snackbar.Snackbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.data.model.DataNotifi
import io.driverdoc.testapp.databinding.DialogChangeTripBinding
import io.driverdoc.testapp.databinding.DialogNewTripBinding
import io.driverdoc.testapp.ui.main.MainActivity
import io.driverdoc.testapp.ui.main.dashboard.DashboardFragment
import io.driverdoc.testapp.ui.main.scheduletrips.ScheduledTripFragment
import android.R.attr.y
import android.R.attr.x
import android.util.DisplayMetrics
import android.os.Build
import android.annotation.TargetApi
import android.graphics.Point
import android.util.Log
import io.driverdoc.testapp.ui.main.acc.AccountFragment
import io.driverdoc.testapp.ui.main.scorecard.ScoreCardFragment
import io.driverdoc.testapp.ui.main.tripsactive.TripFragment


abstract class BaseFragment : Fragment(), ViewFragment {
    protected var mIsDestroyView = true
    protected var mAnimationContinueId: Int = 0
    private var isResume = false
    private var isVisibleView = false
    protected var actionWhenResume: (() -> Unit)? = null
    protected lateinit var mBinding: ViewDataBinding

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mIsDestroyView = false
        return onCreateViewControl(inflater, container, savedInstanceState)
    }

    override fun onCreateViewControl(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (inflater == null) {
            val creatInflater = LayoutInflater.from(context);
            mBinding = DataBindingUtil.inflate(creatInflater, getLayoutMain(), container, false)
        } else {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutMain(), container, false)
        }
        return mBinding.root
    }


    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedControl(view, savedInstanceState)
    }

    override fun onViewCreatedControl(view: View, savedInstanceState: Bundle?) {
        setEvents()
        initComponents()
    }


    fun setAnimationContinueId(runAnimationContitue: Int) {
        mAnimationContinueId = runAnimationContitue
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (mAnimationContinueId != 0) {
            val animation = AnimationUtils.loadAnimation(context, mAnimationContinueId)
            mAnimationContinueId = 0
            return animation
        }
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun showMessage(messageId: Int) {
        if (!mIsDestroyView) {
            getBaseActivity().showMessage(messageId)
        }
    }

    override fun showMessage(message: String) {
        if (!mIsDestroyView) {
            getBaseActivity().showMessage(message)
        }
    }


    final override fun onResume() {
        super.onResume()
        isResume = true
        isVisibleView = !isHidden
        if (isVisibleView && actionWhenResume != null) {
            actionWhenResume!!()
            actionWhenResume = null
        }
        onResumeControl()
    }

    override fun onResumeControl() {

    }

    final override fun onPause() {
        isResume = false
        isVisibleView = false
        onPauseControl()
        super.onPause()
    }

    override fun onPauseControl() {

    }

    fun isVisibleView(): Boolean {
        return isVisibleView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            isVisibleView = false
        } else {
            isVisibleView = isResume
        }
        if (isVisibleView && actionWhenResume != null) {
            actionWhenResume!!()
            actionWhenResume = null
        }
    }

    override fun onDestroyView() {
        mIsDestroyView = true
        onDestroyViewControl()
        super.onDestroyView()
    }


    override fun onDestroyViewControl() {

    }

    override fun hideKeyBoard(): Boolean {
        return getBaseActivity().hideKeyBoard()
    }

    override fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }

    override fun reload(bundle: Bundle) {

    }

    fun showKeyboardEdtMedium(edt: EditTextMedium) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT)
    }

    fun showKeyboardEdtRegular(edt: EditTextRegular) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onBackRoot() {
        getBaseActivity().onBackParent()
    }

    override val isDestroyView: Boolean
        get() = mIsDestroyView


    fun appDatabase() = (activity?.applicationContext as MVVMApplication).appDatabase()
    fun interactCommon() = (activity?.applicationContext as MVVMApplication).interactCommon()
    fun schedule() = (activity?.applicationContext as MVVMApplication).schedule()
    fun gson() = (activity?.applicationContext as MVVMApplication).gson()

    companion object {
        var dialogUpdateTrip: android.app.AlertDialog? = null

        @JvmStatic
        fun openFragment(manager: FragmentManager, transaction: FragmentTransaction, clazz: Class<out BaseFragment>, bundle: Bundle?,
                         hasAddbackstack: Boolean, hasCommitTransaction: Boolean, animations: AnimationScreen?,
                         fragmentContent: Int): Fragment? {
            val tag = clazz.name
            var fragment: Fragment?
            try {
                //if added backstack
                Log.d("BaseFragment....", getCurrentFragment(manager)?.tag.toString())
                Log.d("BaseFragment....", tag)
                if (null == getCurrentFragment(manager) || !getCurrentFragment(manager)?.tag.equals(tag)) {
                    fragment = manager.findFragmentByTag(tag)
                    if (hasAddbackstack) {
                        if (fragment == null || !fragment.isAdded) {
                            fragment = clazz.newInstance()
                            fragment.arguments = bundle
                            setAnimationFragment(transaction, animations)
                            transaction.add(fragmentContent, fragment, tag)
                        } else {
                            transaction.show(fragment)
                        }
                        transaction.addToBackStack(tag)
                    } else {
                        if (fragment != null) {
                            setAnimationFragment(transaction, animations)
                            transaction.show(fragment)
                        } else {
                            fragment = clazz.newInstance()
                            fragment.arguments = bundle
                            setAnimationFragment(transaction, animations)
                            transaction.add(fragmentContent, fragment, tag)
                        }
                    }
                    if (hasCommitTransaction) {
                        transaction.commit()
                    }

                    return fragment

                }
                return null
            } catch (e: java.lang.InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            return null
        }

        @JvmStatic
        fun openFragment(manager: FragmentManager, transaction: FragmentTransaction, fragment: BaseFragment, bundle: Bundle?,
                         hasAddbackstack: Boolean, hasCommitTransaction: Boolean, animations: AnimationScreen?,
                         fragmentContent: Int) {
            val tag = fragment.javaClass.name
            fragment.arguments = bundle
            setAnimationFragment(transaction, animations)
            if (null == getCurrentFragment(manager) || !getCurrentFragment(manager)?.tag.equals(tag)) {
                transaction.add(fragmentContent, fragment, tag)
                if (hasAddbackstack) {
                    transaction.addToBackStack(tag)
                }
                if (hasCommitTransaction) {
                    transaction.commit()
                }
            }
        }

        @JvmStatic
        fun hideFragment(manager: FragmentManager,
                         transaction: FragmentTransaction, animations: AnimationScreen?,
                         hasAddBackstack: Boolean, hasCommit: Boolean, tag: String) {
            val fragment = manager.findFragmentByTag(tag) as BaseFragment
            if (fragment.isVisible) {
                setAnimationFragment(transaction, animations)
                transaction.hide(fragment)
                if (hasAddBackstack) {
                    transaction.addToBackStack(tag)
                }
                if (hasCommit) {
                    transaction.commit()
                }
            }
        }

        @JvmStatic
        fun removeFragment(manager: FragmentManager, transaction: FragmentTransaction, animations: AnimationScreen,
                           hasAddBackStack: Boolean, hasCommit: Boolean, tag: String) {
            val fragment = manager.findFragmentByTag(tag) as BaseFragment
            setAnimationFragment(transaction, animations)
            transaction.remove(fragment)
            if (hasAddBackStack) {
                transaction.addToBackStack(tag)
            }
            if (hasCommit) {
                transaction.commit()
            }
        }

        private fun setAnimationFragment(transaction: FragmentTransaction, animations: AnimationScreen?) {
            if (animations != null) {
                transaction.setCustomAnimations(animations.enterToLeft, animations.exitToLeft, animations.enterToRight, animations.exitToRight)
            }
        }


        @JvmStatic
        fun getCurrentFragment(fragmentManager: FragmentManager): BaseFragment? {
            val frags = fragmentManager.fragments
            if (frags != null) {
                for (i in frags.indices.reversed()) {
                    val fr = frags[i]
                    if (fr != null && fr.isVisible && fr.tag != null) {
                        return fr as BaseFragment
                    }
                }
            }
            return null
        }

        fun showDialogNewTrip(activity: BaseActivity, type: String, mes: String, context: Context?) {
            val binding = DialogChangeTripBinding.inflate(LayoutInflater.from(context!!))
            dialogUpdateTrip?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
            dialogUpdateTrip = android.app.AlertDialog.Builder(context!!).create()
            dialogUpdateTrip!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (type.equals(Constants.UPDATE_TRIP)) {
                binding.tvContent.setText("Trip " + mes + " has been updated.")
            } else {
                binding.tvContent.setText("Trip " + mes + " has been deleted.")
            }
            dialogUpdateTrip!!.setView(binding.getRoot())

            binding.btnOk.setOnClickListener {
                dialogUpdateTrip!!.dismiss()
                getCurrentFragment(activity.supportFragmentManager)!!.tag!!.let {
                    if (it.equals(DashboardFragment::class.java.name)) {
                        liveData.postValue(Constants.RELOAD_TRIP)
                    } else if (it.equals(ScheduledTripFragment::class.java.name)) {
                        liveData.postValue(Constants.RELOAD_SCHEDULE_TRIP)
                    } else if (it.equals(TripFragment::class.java.name)) {
                        liveData.postValue(Constants.RELOAD_ACTIVE_TRIP)
                    } else {
                        liveData.postValue(Constants.RELOAD_TRIP)
                    }
                }

            }
            val window: Window = dialogUpdateTrip!!.window!!
            val wlp = window.getAttributes()

            wlp.gravity = Gravity.CENTER
            window.setAttributes(wlp)
            dialogUpdateTrip!!.setCancelable(false)
            if (null != dialogUpdateTrip && !dialogUpdateTrip!!.isShowing) {
                dialogUpdateTrip!!.show()
            }
            MVVMApplication.liveDataNotifi.postValue(DataNotifi())
        }

    }
}