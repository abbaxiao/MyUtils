package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.BookType;
import com.mobileclient.service.BookTypeService;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;
public class BookTypeAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明类别名称输入框
	private EditText ET_bookTypeName;
	// 声明可借阅天数输入框
	private EditText ET_days;
	protected String carmera_path;
	/*要保存的图书类型信息*/
	BookType bookType = new BookType();
	/*图书类型管理业务逻辑层*/
	private BookTypeService bookTypeService = new BookTypeService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-添加图书类型");
		// 设置当前Activity界面布局
		setContentView(R.layout.booktype_add); 
		ET_bookTypeName = (EditText) findViewById(R.id.ET_bookTypeName);
		ET_days = (EditText) findViewById(R.id.ET_days);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加图书类型按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取类别名称*/ 
					if(ET_bookTypeName.getText().toString().equals("")) {
						Toast.makeText(BookTypeAddActivity.this, "类别名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_bookTypeName.setFocusable(true);
						ET_bookTypeName.requestFocus();
						return;	
					}
					bookType.setBookTypeName(ET_bookTypeName.getText().toString());
					/*验证获取可借阅天数*/ 
					if(ET_days.getText().toString().equals("")) {
						Toast.makeText(BookTypeAddActivity.this, "可借阅天数输入不能为空!", Toast.LENGTH_LONG).show();
						ET_days.setFocusable(true);
						ET_days.requestFocus();
						return;	
					}
					bookType.setDays(Integer.parseInt(ET_days.getText().toString()));
					/*调用业务逻辑层上传图书类型信息*/
					BookTypeAddActivity.this.setTitle("正在上传图书类型信息，稍等...");
					String result = bookTypeService.AddBookType(bookType);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*操作完成后返回到图书类型管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(BookTypeAddActivity.this, BookTypeListActivity.class);
					startActivity(intent); 
					BookTypeAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
