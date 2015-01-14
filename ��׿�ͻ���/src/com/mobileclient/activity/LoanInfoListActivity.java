package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.LoanInfo;
import com.mobileclient.service.LoanInfoService;
import com.mobileclient.util.LoanInfoSimpleAdapter;
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

public class LoanInfoListActivity extends Activity {
	LoanInfoSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int loadId;
	/* 借阅信息操作业务逻辑层对象 */
	LoanInfoService loanInfoService = new LoanInfoService();
	/*保存查询参数条件的借阅信息对象*/
	private LoanInfo queryConditionLoanInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loaninfo_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--借阅信息列表");
		} else {
			setTitle("您好：" + username + "   当前位置---借阅信息列表");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionLoanInfo = (LoanInfo)extras.getSerializable("queryConditionLoanInfo");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new LoanInfoSimpleAdapter(this, list,
					R.layout.loaninfo_list_item,
					new String[] { "book","reader","borrowDate","returnDate" },
					new int[] { R.id.tv_book,R.id.tv_reader,R.id.tv_borrowDate,R.id.tv_returnDate,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// 添加长按点击
		lv.setOnCreateContextMenuListener(loanInfoListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int loadId = Integer.parseInt(list.get(arg2).get("loadId").toString());
            	Intent intent = new Intent();
            	intent.setClass(LoanInfoListActivity.this, LoanInfoDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("loadId", loadId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener loanInfoListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑借阅信息信息"); 
			menu.add(0, 1, 0, "删除借阅信息信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑借阅信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取借阅编号
			loadId = Integer.parseInt(list.get(position).get("loadId").toString());
			Intent intent = new Intent();
			intent.setClass(LoanInfoListActivity.this, LoanInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("loadId", loadId);
			intent.putExtras(bundle);
			startActivity(intent);
			LoanInfoListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除借阅信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取借阅编号
			loadId = Integer.parseInt(list.get(position).get("loadId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(LoanInfoListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = loanInfoService.DeleteLoanInfo(loadId);
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
			/* 查询借阅信息信息 */
			List<LoanInfo> loanInfoList = loanInfoService.QueryLoanInfo(queryConditionLoanInfo);
			for (int i = 0; i < loanInfoList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("loadId",loanInfoList.get(i).getLoadId());
				map.put("book", loanInfoList.get(i).getBook());
				map.put("reader", loanInfoList.get(i).getReader());
				map.put("borrowDate", loanInfoList.get(i).getBorrowDate());
				map.put("returnDate", loanInfoList.get(i).getReturnDate());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加借阅信息");
		menu.add(0, 2, 2, "查询借阅信息");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加借阅信息信息
			Intent intent = new Intent();
			intent.setClass(LoanInfoListActivity.this, LoanInfoAddActivity.class);
			startActivity(intent);
			LoanInfoListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询借阅信息信息*/
			Intent intent = new Intent();
			intent.setClass(LoanInfoListActivity.this, LoanInfoQueryActivity.class);
			startActivity(intent);
			LoanInfoListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(LoanInfoListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			LoanInfoListActivity.this.finish();
		}
		return true; 
	}
}
