package me.donnie.reader.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import me.donnie.reader.R
import me.donnie.reader.utils.fragmentTransaction

class MainFragment : Fragment() {
  
  companion object {
    
    @JvmStatic
    fun newInstance(): MainFragment {
      return MainFragment()
    }
  }
  
  private lateinit var current: Fragment
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }
  
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val activity = requireActivity() as AppCompatActivity
    activity.setSupportActionBar(toolbar)
    val toggle = ActionBarDrawerToggle(
      activity, drawer_layout, toolbar,
      R.string.navigation_drawer_open,
      R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()
  }
  
  private fun switchFragment(to: Fragment, tag: String) {
    if (current == to) return
    if (to.isAdded) {
      fragmentTransaction(now = false) {
        hide(current)
        show(to)
      }
    } else {
      fragmentTransaction(now = false) {
        hide(current)
        add(R.id.container, to, tag)
      }
    }
    current = to
  }
  
  fun onBackPressed(): Boolean {
    if (drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
      return true
    }
    return false
  }
}