package io.driverdoc.testapp.ui.start.permision

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import io.driverdoc.testapp.ui.start.permision.pager.PermissionCamera
import io.driverdoc.testapp.ui.start.permision.pager.PermissionCheck
import io.driverdoc.testapp.ui.start.permision.pager.PermissionLocation
import io.driverdoc.testapp.ui.start.permision.pager.PermissionNotification

class AllPageFragment(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return PermissionLocation()
            1 -> return PermissionNotification()
            2 -> return PermissionCamera()
            else -> return PermissionCheck()
        }
    }

    override fun getCount() = 4
}

class CameraPageFragment(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return PermissionCamera()
            else -> return PermissionCheck()
        }
    }

    override fun getCount() = 2
}
class LocationPageFragment(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return PermissionLocation()
            else -> return PermissionCheck()

        }
    }

    override fun getCount() = 2
}

class NoPageFragment(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return PermissionCheck()
    }

    override fun getCount() = 1
}
