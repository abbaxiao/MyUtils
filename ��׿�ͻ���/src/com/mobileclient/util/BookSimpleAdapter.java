package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.BookTypeService;
import com.mobileclient.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class BookSimpleAdapter extends SimpleAdapter { 
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public BookSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
    ViewHolder holder = null; 
    /*��һ��װ�����viewʱ=null,���½�һ������inflate����һ��view*/
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.book_list_item, null);
			holder = new ViewHolder();
			try { 
				/*�󶨸�view�����ؼ�*/
				holder.tv_barcode = (TextView)convertView.findViewById(R.id.tv_barcode);
				holder.tv_bookName = (TextView)convertView.findViewById(R.id.tv_bookName);
				holder.tv_bookType = (TextView)convertView.findViewById(R.id.tv_bookType);
				holder.iv_bookPhoto = (ImageView)convertView.findViewById(R.id.iv_bookPhoto);
			} catch(Exception ex) {}
			/*������view*/
			convertView.setTag(holder);
		}else{
			/*ֱ��ȡ����ǵ�view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*���ø����ؼ���չʾ����*/
		holder.tv_barcode.setText(mData.get(position).get("barcode").toString());
		holder.tv_bookName.setText(mData.get(position).get("bookName").toString());
		holder.tv_bookType.setText((new BookTypeService()).GetBookType(Integer.parseInt(mData.get(position).get("bookType").toString())).getBookTypeName());
		holder.iv_bookPhoto.setImageBitmap((Bitmap)mData.get(position).get("bookPhoto"));
		/*�����޸ĺõ�view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_barcode;
    	TextView tv_bookName;
    	TextView tv_bookType;
    	ImageView iv_bookPhoto;
    }
} 
