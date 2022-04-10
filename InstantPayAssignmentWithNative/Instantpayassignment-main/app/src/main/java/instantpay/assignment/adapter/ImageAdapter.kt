package instantpay.assignment.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import instantpay.assignment.R
import java.util.*


class ImageAdapter(var context: Context, arrayList: ArrayList<String>) : BaseAdapter() {
    var arrayList: ArrayList<String>
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


        var item = LayoutInflater.from(context).inflate(R.layout.image_list, parent, false)

        val imageView: ImageView
        imageView = item?.findViewById<ImageView>(R.id.image) as ImageView
        Glide.with(context).load(arrayList[position]).placeholder(R.mipmap.ic_launcher).into(imageView);

        return item
    }

    init {
        this.arrayList = arrayList
    }
}
