import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class NavTabAdapter : RecyclerView.Adapter<NavTabAdapter.TabViewHolder>() {

    // 这里就是你要的4个名字
    private val tabNameList = listOf("推荐", "计划", "课程", "社区")

    inner class TabViewHolder(itemView: TextView) : RecyclerView.ViewHolder(itemView) {
        val tabTextView: TextView = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nav_tab, parent, false) as TextView
        return TabViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.tabTextView.text = tabNameList[position]
    }

    override fun getItemCount(): Int = tabNameList.size
}