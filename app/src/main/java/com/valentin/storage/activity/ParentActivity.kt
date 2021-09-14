package com.valentin.storage.activity

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.valentin.storage.R
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.NavHostFragment
import com.valentin.storage.fragment.*

open class ParentActivity: AppCompatActivity(),
        AddFragment.AddFragmentListener, SettingsFragment.ISettingsListener,
        CatsFragment.IListFragmentListener, UpdateFragment.IUpdateFragmentListener {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                Log.d(TAG, "Navigate to settings")
                navigate(R.id.action_global_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigate(actionId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(actionId)
    }

    // AddFragment
    override fun add() {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.popBackStack()
    }

    override fun goToUpdateCat(catId: Int) {
        val action = CatsFragmentDirections.actionListFragmentToUpdateFragment(catId)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(action)
    }

    // CatsFragment
    override fun goToAddCat() {
        navigate(R.id.action_listFragment_to_addFragment)
    }

    private companion object {
        const val TAG = "ParentActivity"
    }
}