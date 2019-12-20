package me.donnie.reader.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.donnie.reader.utils.fragmentTransaction

class MainActivity : AppCompatActivity() {
  
  private lateinit var mainFragment: MainFragment
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    findViewById<View>(android.R.id.content)
    
    if (savedInstanceState == null) {
      mainFragment = MainFragment.newInstance()
      fragmentTransaction(now = false) {
        add(android.R.id.content, mainFragment, null)
      }
    } else {
      mainFragment = supportFragmentManager.findFragmentById(android.R.id.content) as MainFragment
    }
  }
  
  override fun onBackPressed() {
    if (mainFragment.onBackPressed()) {
      return
    }
    super.onBackPressed()
  }
}
