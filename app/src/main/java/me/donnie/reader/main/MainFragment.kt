package me.donnie.reader.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import me.donnie.reader.R
import me.donnie.reader.home.HomeFragment
import me.donnie.reader.hots.HotsFragment
import me.donnie.reader.utils.fragmentTransaction

class MainFragment : Fragment() {
  
  companion object {
    
    @JvmStatic
    fun newInstance(): MainFragment {
      return MainFragment()
    }
  }
  
  private lateinit var current: Fragment
  private lateinit var home: Fragment
  private lateinit var hots: Fragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }
  
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
    
    nav_view.setNavigationItemSelectedListener {
      drawer_layout.closeDrawer(GravityCompat.START)
      return@setNavigationItemSelectedListener when (it.itemId) {
        R.id.nav_home -> {
          switchFragment(home, HomeFragment.TAG)
          true
        }
        R.id.nav_hots -> {
          switchFragment(hots, HotsFragment.TAG)
          true
        }
        R.id.nav_gallery -> {
          false
        }
        else -> false
      }
    }
    
    if (savedInstanceState == null) {
      home = HomeFragment.newInstance()
      hots = HotsFragment.newInstance()
      fragmentTransaction(now = false) {
        add(R.id.container, home, HomeFragment.TAG)
      }
      current = home
      nav_view.menu.getItem(0).isChecked = true
    } else {
      home = childFragmentManager.findFragmentByTag(HomeFragment.TAG) as Fragment
      hots = childFragmentManager.findFragmentByTag(HotsFragment.TAG) as Fragment
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.main, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
     android.R.id.home -> {
       drawer_layout.openDrawer(GravityCompat.START)
     }
    }
    return super.onOptionsItemSelected(item)
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