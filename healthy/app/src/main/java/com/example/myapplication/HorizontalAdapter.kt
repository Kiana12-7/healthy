import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class HorizontalAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(itemView.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // LayoutInflater.from(parent.context)将转化xml为对象
        // inflat函数开始进行创建循环中的子项目， 第一个参数获取子项目的样式，第二个参数获取父盒子的样式，第三个参数表示是否构造完一个个就塞到父盒子里
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.textView.text = item
        // 可以设置点击事件
        holder.itemView.setOnClickListener {
            // 处理点击
        }
    }

    override fun getItemCount() = dataList.size
}