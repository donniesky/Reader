package me.donnie.reader.hots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.*
import me.donnie.reader.R
import me.donnie.reader.data.entities.Result
import me.donnie.reader.utils.gone
import me.donnie.reader.utils.observeNotNull

class HotsFragment : Fragment() {
  
  companion object {
    const val TAG = "HotsFragment"
    
    @JvmStatic
    fun newInstance(): HotsFragment {
      return HotsFragment()
    }
  }
  
  private lateinit var model: HotsViewModel
  
  private val adapter = HotsAdapter()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    model = ViewModelProviders.of(this).get(HotsViewModel::class.java)
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_home, container, false)
  }
  
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    recycler.setHasFixedSize(true)
    recycler.adapter = adapter
    
    refresh.setOnRefreshListener {
      model.getHotNews()
    }
    
    model.result.observeNotNull(this) {
      progress.gone()
      refresh.isRefreshing = false
      if (it is Result.Success) {
        adapter.submitList(it.data)
      }
    }
    
    if (savedInstanceState == null) {
      model.getHotNews()
    }
  }
}