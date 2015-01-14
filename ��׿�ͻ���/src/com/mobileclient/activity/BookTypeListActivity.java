package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.BookType;
import com.mobileclient.service.BookTypeService;
import com.mobileclient.util.BookTypeSimpleAdapter;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BookTypeListActivity extends Activity {
	BookTypeSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int bookTypeId;
	/* 图书类型操作业务逻辑层对象 */
	BookTypeService bookTypeService = new BookTypeService();
	/*保存查询参数条件的图书类型对象*/
	private BookType queryConditionBookType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booktype_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--图书类型列表");
		} else {
			setTitle("您好：" + username + "   当前位置---图书类型列表");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionBookType = (BookType)extras.getSerializable("queryConditionBookType");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new BookTypeSimpleAdapter(this, list,
					R.layout.booktype_list_item,
					new String[] { "bookTypeId","bookTypeName","days" },
					new int[] { R.id.tv_bookTypeId,R.id.tv_bookTypeName,R.id.tv_days,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// 添加长按点击
		lv.setOnCreateContextMenuListener(bookTypeListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int bookTypeId = Integer.parseInt(list.get(arg2).get("bookTypeId").toString());
            	Intent intent = new Intent();
            	intent.setClass(BookTypeListActivity.this, BookTypeDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("bookTypeId", bookTypeId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener bookTypeListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑图书类型信息"); 
			menu.add(0, 1, 0, "删除图书类型信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑图书类型信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取图书类别
			bookTypeId = Integer.parseInt(list.get(position).get("bookTypeId").toString());
			Intent intent = new Intent();
			intent.setClass(BookTypeListActivity.this, BookTypeEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("bookTypeId", bookTypeId);
			intent.putExtras(bundle);
			startActivity(intent);
			BookTypeListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除图书类型信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取图书类别
			bookTypeId = Integer.parseInt(list.get(position).get("bookTypeId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(BookTypeListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = bookTypeService.DeleteBookType(bookTypeId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* 查询图书类型信息 */
			List<BookType> bookTypeList = bookTypeService.QueryBookType(queryConditionBookType);
			for (int i = 0; i < bookTypeList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bookTypeId", bookTypeList.get(i).getBookTypeId());
				map.put("bookTypeName", bookTypeList.get(i).getBookTypeName());
				map.put("days", bookTypeList.get(i).getDays());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加图书类型");
		menu.add(0, 2, 2, "查询图书类型");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加图书类型信息
			Intent intent = new Intent();
			intent.setClass(BookTypeListActivity.this, BookTypeAddActivity.class);
			startActivity(intent);
			BookTypeListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询图书类型信息*/
			Intent intent = new Intent();
			intent.setClass(BookTypeListActivity.this, BookTypeQueryActivity.class);
			startActivity(intent);
			BookTypeListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(BookTypeListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			BookTypeListActivity.this.finish();
		}
		return true; 
	}
}
