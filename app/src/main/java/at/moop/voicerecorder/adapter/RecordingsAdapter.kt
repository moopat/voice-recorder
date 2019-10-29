package at.moop.voicerecorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.moop.voicerecorder.R
import at.moop.voicerecorder.adapter.vh.RecordingViewHolder
import at.moop.voicerecorder.model.Recording

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RecordingsAdapter(private val clickHandler: (recording: Recording) -> Unit) :
    RecyclerView.Adapter<RecordingViewHolder>() {

    private val items = mutableListOf<Recording>()

    fun setData(data: List<Recording>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingViewHolder {
        return RecordingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recording,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecordingViewHolder, position: Int) {
        val item = items[position]

        holder.tvDate.text = item.startTime.toString()
        holder.tvDuration.text = item.getDuration(false)?.toString()

        holder.itemView.setOnClickListener {
            clickHandler.invoke(item)
        }
    }
}
