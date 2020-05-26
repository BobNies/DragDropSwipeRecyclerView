package com.movemedical.recyclerview.sample.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.movemedical.recyclerview.sample.R
import com.movemedical.recyclerview.sample.model.SampleAdapter.ViewHolder

class SampleAdapter(private val list: MutableList<MainModel>, private val listener: Listener?) :
    RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(LayoutInflater.from(parent.context), parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: MainModel = list[position]
        holder.bindTo(item)
    }

    override fun getItemViewType(position: Int): Int {
        val item: MainModel = list[position]
        return when {
            item.id == -1 -> R.layout.item_progress
            else -> R.layout.item_main
        }
    }

    override fun getItemCount(): Int = list.size

    fun add(model: MainModel) {
        list.add(model)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(users: List<MainModel>) {
        list.addAll(users)
        notifyItemRangeChanged(users.size, list.size - 1)
    }

    fun remove(position: Int) {
        list.removeAt(position)
        notifyItemInserted(list.size)
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    interface Listener {
        fun onSelect(item: MainModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            fun create(
                inflater: LayoutInflater,
                parent: ViewGroup,
                viewType: Int
            ): ViewHolder {
                val binding: ViewDataBinding =
                    DataBindingUtil.inflate(inflater, viewType, parent, false)
                return ViewHolder(binding)
            }
        }

        private var binding: ViewDataBinding? = null

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bindTo(
            userModel: Any?
        ) {
            binding?.setVariable(BR.model, userModel)
            binding?.executePendingBindings()
        }
    }
}