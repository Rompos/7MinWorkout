package com.rompos.a7minworkout


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rompos.a7minworkout.databinding.ItemHistoryRowBinding

//Our HistoryEntity has only one parameter(val date : String) which is a string so instead of using
// in the ArrayList<HistoryEntity> we only use ArrayList<String>
class HistoryAdapter(private val items : ArrayList<HistoryEntity>,
                     private val deleteListener:(HistoryEntity)-> Unit
    ): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        val llHistoryItemMain = binding.llHistoryItemMain
        val tvItem = binding.tvItem
        //val tvPosition = binding.tvPosition
        val ivDeleteLine = binding.ivDeleteLine
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(
            LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ////how every single row should be handled
        val context = holder.itemView.context
        val item = items[position]

        //holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = item.date


        if(position %2 == 0){
            holder.llHistoryItemMain.setBackgroundColor(ContextCompat.getColor(holder.tvItem.context,R.color.lightGrey))
        }else{
            holder.llHistoryItemMain.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
        }

        //how to add the delete function by adding the listener
        holder.ivDeleteLine.setOnClickListener{
            deleteListener.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}