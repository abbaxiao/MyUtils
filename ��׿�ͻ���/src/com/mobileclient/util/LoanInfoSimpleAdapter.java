package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.BookService;
import com.mobileclient.service.ReaderService;
import com.mobileclient.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class LoanInfoSimpleAdapter extends SimpleAdapter { 
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public LoanInfoSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
    ViewHolder holder = null; 
    /*第一次装载这个view时=null,就新建一个调用inflate宣誓一个view*/
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.loaninfo_list_item, null);
			holder = new ViewHolder();
			try { 
				/*绑定该view各个控件*/
				holder.tv_book = (TextView)convertView.findViewById(R.id.tv_book);
				holder.tv_reader = (TextView)convertView.findViewById(R.id.tv_reader);
				holder.tv_borrowDate = (TextView)convertView.findViewById(R.id.tv_borrowDate);
				holder.tv_returnDate = (TextView)convertView.findViewById(R.id.tv_returnDate);
			} catch(Exception ex) {}
			/*标记这个view*/
			convertView.setTag(holder);
		}else{
			/*直接取出标记的view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*设置各个控件的展示内容*/
		holder.tv_book.setText((new BookService()).GetBook(mData.get(position).get("book").toString()).getBookName());
		holder.tv_reader.setText((new ReaderService()).GetReader(mData.get(position).get("reader").toString()).getReaderName());
		holder.tv_borrowDate.setText(mData.get(position).get("borrowDate").toString().substring(0, 10));
		holder.tv_returnDate.setText(mData.get(position).get("returnDate").toString().substring(0, 10));
		/*返回修改好的view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_book;
    	TextView tv_reader;
    	TextView tv_borrowDate;
    	TextView tv_returnDate;
    }
} 
