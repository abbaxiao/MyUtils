package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Reader;
import com.mobileclient.service.ReaderService;
import com.mobileclient.util.ReaderSimpleAdapter;
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

public class ReaderListActivity extends Activity {
	ReaderSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String readerNo;
	/* 读者操作业务逻辑层对象 */
	ReaderService readerService = new ReaderService();
	/*保存查询参数条件的读者对象*/
	private Reader queryConditionReader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reader_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--读者列表");
		} else {
			setTitle("您好：" + username + "   当前位置---读者列表");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionReader = (Reader)extras.getSerializable("queryConditionReader");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new ReaderSimpleAdapter(this, list,
					R.layout.reader_list_item,
					new String[] { "readerNo","readerType","readerName","photo" },
					new int[] { R.id.tv_readerNo,R.id.tv_readerType,R.id.tv_readerName,R.id.iv_photo,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// 添加长按点击
		lv.setOnCreateContextMenuListener(readerListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	String readerNo = list.get(arg2).get("readerNo").toString();
            	Intent intent = new Intent();
            	intent.setClass(ReaderListActivity.this, ReaderDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("readerNo", readerNo);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener readerListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑读者信息"); 
			menu.add(0, 1, 0, "删除读者信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑读者信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取读者编号
			readerNo = list.get(position).get("readerNo").toString();
			Intent intent = new Intent();
			intent.setClass(ReaderListActivity.this, ReaderEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("readerNo", readerNo);
			intent.putExtras(bundle);
			startActivity(intent);
			ReaderListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除读者信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取读者编号
			readerNo = list.get(position).get("readerNo").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(ReaderListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = readerService.DeleteReader(readerNo);
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
			/* 查询读者信息 */
			List<Reader> readerList = readerService.QueryReader(queryConditionReader);
			for (int i = 0; i < readerList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("readerNo", readerList.get(i).getReaderNo());
				map.put("readerType", readerList.get(i).getReaderType());
				map.put("readerName", readerList.get(i).getReaderName());
				byte[] photo_data = ImageService.getImage(HttpUtil.BASE_URL+ readerList.get(i).getPhoto());// 获取图片数据
				BitmapFactory.Options photo_opts = new BitmapFactory.Options();  
				photo_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(photo_data, 0, photo_data.length, photo_opts); 
				photo_opts.inSampleSize = photoListActivity.computeSampleSize(photo_opts, -1, 100*100); 
				photo_opts.inJustDecodeBounds = false; 
				try {
					Bitmap photo = BitmapFactory.decodeByteArray(photo_data, 0, photo_data.length, photo_opts);
					map.put("photo", photo);
				} catch (OutOfMemoryError err) { }
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加读者");
		menu.add(0, 2, 2, "查询读者");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加读者信息
			Intent intent = new Intent();
			intent.setClass(ReaderListActivity.this, ReaderAddActivity.class);
			startActivity(intent);
			ReaderListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询读者信息*/
			Intent intent = new Intent();
			intent.setClass(ReaderListActivity.this, ReaderQueryActivity.class);
			startActivity(intent);
			ReaderListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(ReaderListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			ReaderListActivity.this.finish();
		}
		return true; 
	}
}
