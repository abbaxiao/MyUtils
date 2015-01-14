package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.ReaderType;
import com.mobileclient.service.ReaderTypeService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class ReaderTypeEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明读者类型编号TextView
	private TextView TV_readerTypeId;
	// 声明读者类型输入框
	private EditText ET_readerTypeName;
	// 声明可借阅数目输入框
	private EditText ET_number;
	protected String carmera_path;
	/*要保存的读者类型信息*/
	ReaderType readerType = new ReaderType();
	/*读者类型管理业务逻辑层*/
	private ReaderTypeService readerTypeService = new ReaderTypeService();

	private int readerTypeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-修改读者类型");
		// 设置当前Activity界面布局
		setContentView(R.layout.readertype_edit); 
		TV_readerTypeId = (TextView) findViewById(R.id.TV_readerTypeId);
		ET_readerTypeName = (EditText) findViewById(R.id.ET_readerTypeName);
		ET_number = (EditText) findViewById(R.id.ET_number);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		readerTypeId = extras.getInt("readerTypeId");
		initViewData();
		/*单击修改读者类型按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取读者类型*/ 
					if(ET_readerTypeName.getText().toString().equals("")) {
						Toast.makeText(ReaderTypeEditActivity.this, "读者类型输入不能为空!", Toast.LENGTH_LONG).show();
						ET_readerTypeName.setFocusable(true);
						ET_readerTypeName.requestFocus();
						return;	
					}
					readerType.setReaderTypeName(ET_readerTypeName.getText().toString());
					/*验证获取可借阅数目*/ 
					if(ET_number.getText().toString().equals("")) {
						Toast.makeText(ReaderTypeEditActivity.this, "可借阅数目输入不能为空!", Toast.LENGTH_LONG).show();
						ET_number.setFocusable(true);
						ET_number.requestFocus();
						return;	
					}
					readerType.setNumber(Integer.parseInt(ET_number.getText().toString()));
					/*调用业务逻辑层上传读者类型信息*/
					ReaderTypeEditActivity.this.setTitle("正在更新读者类型信息，稍等...");
					String result = readerTypeService.UpdateReaderType(readerType);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*操作完成后返回到读者类型管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(ReaderTypeEditActivity.this, ReaderTypeListActivity.class);
					startActivity(intent); 
					ReaderTypeEditActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    readerType = readerTypeService.GetReaderType(readerTypeId);
		this.TV_readerTypeId.setText(readerTypeId+"");
		this.ET_readerTypeName.setText(readerType.getReaderTypeName());
		this.ET_number.setText(readerType.getNumber() + "");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
