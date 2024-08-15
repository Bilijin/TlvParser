package com.mobolajialabi.tlvparser.adapter

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobolajialabi.tlvparser.databinding.TlvItemBinding
import com.mobolajialabi.tlvparser.model.Tlv

class TlvRecyclerAdapter(private var items: List<Tlv>, private val context: Activity) :
    RecyclerView.Adapter<TlvRecyclerAdapter.MyViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        val binding = TlvItemBinding.inflate(context.layoutInflater, parent, false)
        return MyViewModel(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        val tlv = items[position]
        holder.binding.apply {
            tagTxt.text = tlv.tag.uppercase()
            lengthTxt.text = tlv.length.toString()
            valueTxt.text = tlv.value.uppercase()
        }
    }

    fun updateItems(newItems : List<Tlv>) {
        items = newItems
        notifyDataSetChanged()
    }

    class MyViewModel(val binding: TlvItemBinding) : RecyclerView.ViewHolder(binding.root)
}