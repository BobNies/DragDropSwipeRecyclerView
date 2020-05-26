package com.movemedical.recyclerview.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movemedical.recyclerview.library.drag.DragHandler
import com.movemedical.recyclerview.library.scroll.RecyclerViewScrollCallback
import com.movemedical.recyclerview.library.scroll.ScrollHandler
import com.movemedical.recyclerview.library.swipe.SwipeHandler
import com.movemedical.recyclerview.sample.databinding.ActivityMainBinding
import com.movemedical.recyclerview.sample.model.MainModel
import com.movemedical.recyclerview.sample.model.SampleAdapter

class MainActivity : AppCompatActivity(), SwipeHandler, DragHandler, ScrollHandler {


    private lateinit var binding: ActivityMainBinding
    private lateinit var sampleAdapter: SampleAdapter
    //private lateinit var viewModel: MainViewModel
    private lateinit var model: MainModel

    private var list = arrayListOf<MainModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewModel = ViewModelProviders.of(this).get(MainViewModel(application)::class.java)

        // binding = DataBindingUtil.setContentView(this, R.layout.activity_main, BindingComponent())
        binding = DataBindingUtil.setContentView(
            this@MainActivity,
            R.layout.activity_main
        )

        model = MainModel()
        model.visibleThreshold = 3

        binding.model = model

        binding.scrollHandler = this
        binding.dragHandler = this
        binding.swipeHandler = this
        //binding.lifecycleOwner = this

    }

    override fun onResume() {
        super.onResume()

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val list = arrayListOf<MainModel>()

        for (i in 0..20) {
            val model = MainModel()
            model.name = "Name: $i"

            list.add(
                model
            )
        }

        with(binding.lv) {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)

            sampleAdapter = SampleAdapter(list, null)
            adapter = sampleAdapter
        }
    }

    override fun onItemSwipe(position: Int, direction: Int) {
        Log.v("bob", "onItemSwipe pos=$position ,dir=$direction")
    }

    override fun onItemDragged(indexFrom: Int, indexTo: Int) {
        Log.v("bob", "onItemDragged from: $indexFrom -- to:$indexTo")
    }

    override fun onItemDropped(indexFrom: Int, indexTo: Int) {
        Log.v("bob", "onItemDropped from: $indexFrom -- to:$indexTo")
    }

    override fun onScrolledTo(direction: RecyclerViewScrollCallback.ScrollDirection) {
        Log.v("bob", "onScrolledTo:  $direction")
        when(direction) {
            RecyclerViewScrollCallback.ScrollDirection.BOTTOM -> {
                //showProgress()
                val list = arrayListOf<MainModel>()

                for (i in 0..20) {
                    val model = MainModel()
                    model.name = "New Item: $i"

                    list.add(
                        model
                    )
                }

                //hideProgress()
                binding.lv.post { sampleAdapter.addAll(list) }

            }
            RecyclerViewScrollCallback.ScrollDirection.TOP -> {
               //hideProgress()
            }
            else -> {
                // nothing for LEFT or RIGHT
            }
        }
    }

    fun showProgress(): Boolean {
        binding.lv.post { sampleAdapter.add(MainModel().apply { id = -1 }) }
        return true
    }

     fun hideProgress(): Boolean {
        if (list.size > 0 && list[list.size - 1].id == -1) {
            sampleAdapter.remove(list.size - 1)                       // remove progress loader (UserModel with id = -1) from bottom
        }
        if (binding.srl.isRefreshing) {
            sampleAdapter.clear()                     // clear list
            binding.srl.isRefreshing = false    // hide pull to refresh
            model.resetLoadingState = true      // reset loading state and callback
        }
        return false
    }

}
