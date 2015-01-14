package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ReaderType;
import com.mobileclient.service.ReaderTypeService;
import com.mobileclient.util.ReaderTypeSimpleAdapter;
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

public class ReaderTypeListActivity extends Activity {
	ReaderTypeSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int readerTypeId;
	/* 读者类型操作业务逻辑层对象 */
	ReaderTypeService readerTypeService = new ReaderTypeService();
	/*保存查询参数条件的读者类型对象*/
	private ReaderType queryConditionReaderType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readertype_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--读者类型列表");
		} else {
			setTitle("您好：" + username + "   当前位置---读者类型列表");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionReaderType = (ReaderType)extras.getSerializable("queryConditionReaderType");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new ReaderTypeSimpleAdapter(this, list,
					R.layout.readertype_list_item,
					new String[] { "readerTypeId","readerTypeName","number" },
					new int[] { R.id.tv_readerTypeId,R.id.tv_readerTypeName,R.id.tv_number,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// 添加长按点击
		lv.setOnCreateContextMenuListener(readerTypeListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int readerTypeId = Integer.parseInt(list.get(arg2).get("readerTypeId").toString());
            	Intent intent = new Intent();
            	intent.setClass(ReaderTypeListActivity.this, ReaderTypeDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("readerTypeId", readerTypeId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener readerTypeListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑读者类型信息"); 
			menu.add(0, 1, 0, "删除读者类型信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑读者类型信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取读者类型编号
			readerTypeId = Integer.parseInt(list.get(position).get("readerTypeId").toString());
			Intent intent = new Intent();
			intent.setClass(ReaderTypeListActivity.this, ReaderTypeEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("readerTypeId", readerTypeId);
			intent.putExtras(bundle);
			startActivity(intent);
			ReaderTypeListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除读者类型信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取读者类型编号
			readerTypeId = Integer.parseInt(list.get(position).get("readerTypeId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(ReaderTypeListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = readerTypeService.DeleteReaderType(readerTypeId);
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
			/* 查询读者类型信息 */
			List<ReaderType> readerTypeList = readerTypeService.QueryReaderType(queryConditionReaderType);
			for (int i = 0; i < readerTypeList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("readerTypeId", readerTypeList.get(i).getReaderTypeId());
				map.put("readerTypeName", readerTypeList.get(i).getReaderTypeName());
				map.put("number", readerTypeList.get(i).getNumber());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加读者类型");
		menu.add(0, 2, 2, "查询读者类型");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加读者类型信息
			Intent intent = new Intent();
			intent.setClass(ReaderTypeListActivity.this, ReaderTypeAddActivity.class);
			startActivity(intent);
			ReaderTypeListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询读者类型信息*/
			Intent intent = new Intent();
			intent.setClass(ReaderTypeListActivity.this, ReaderTypeQueryActivity.class);
			startActivity(intent);
			ReaderTypeListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(ReaderTypeListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			ReaderTypeListActivity.this.finish();
		}
		return true; 
	}
}
