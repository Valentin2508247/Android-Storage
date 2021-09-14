package com.valentin.storage.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.valentin.storage.databinding.CatItemBinding
import com.valentin.storage.models.Cat

class CatViewHolder(
    private val binding: CatItemBinding,
    private val listener: CatListener): RecyclerView.ViewHolder(binding.root) {

    fun bind(cat: Cat) {
        binding.apply {
            tvAge.text = cat.age.toString()
            tvName.text = cat.name
            tvBreed.text = cat.breed

            // delete cat
            ivDelete.setOnClickListener {
                listener.deleteCat(cat)
            }

            // update cat
            ivEdit.setOnClickListener {
                listener.updateCat(cat)
            }
        }
        binding.tvAge.text = cat.age.toString()
        //Log.d(TAG, cat.toString())
    }

    private companion object {
        const val TAG = "CatViewHolder"
    }
}

