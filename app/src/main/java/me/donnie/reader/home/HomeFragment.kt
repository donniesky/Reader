package me.donnie.reader.home

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

class HomeFragment : Fragment() {
  
  companion object {
    const val TAG = "HomeFragment"
    
    @JvmStatic
    fun newInstance(): HomeFragment {
      return HomeFragment()
    }
  }
  
  private lateinit var homeViewModel: HomeViewModel
  
  private val adapter = ItemAdapter()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_home, container, false)
  }
  
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    recycler.setHasFixedSize(true)
    recycler.adapter = adapter
    homeViewModel.result.observeNotNull(this) {
      progress.gone()
      refresh.isRefreshing = false
      if (it is Result.Success) {
        adapter.submitList(it.data)
      }
    }
    
    refresh.setOnRefreshListener {
      homeViewModel.getNews()
    }
    
    if (savedInstanceState == null) {
      homeViewModel.getNews()
    }
  }
}