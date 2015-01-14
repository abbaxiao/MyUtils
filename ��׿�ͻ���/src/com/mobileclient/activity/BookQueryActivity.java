package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Book;
import com.mobileclient.domain.BookType;
import com.mobileclient.service.BookTypeService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class BookQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明图书条形码输入框
	private EditText ET_barcode;
	// 声明图书名称输入框
	private EditText ET_bookName;
	// 声明图书所在类别下拉框
	private Spinner spinner_bookType;
	private ArrayAdapter<String> bookType_adapter;
	private static  String[] bookType_ShowText  = null;
	private List<BookType> bookTypeList = null; 
	/*图书类型管理业务逻辑层*/
	private BookTypeService bookTypeService = new BookTypeService();
	/*查询过滤条件保存到这个对象中*/
	private Book queryConditionBook = new Book();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询图书条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.book_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_barcode = (EditText) findViewById(R.id.ET_barcode);
		ET_bookName = (EditText) findViewById(R.id.ET_bookName);
		spinner_bookType = (Spinner) findViewById(R.id.Spinner_bookType);
		// 获取所有的图书类型
		try {
			bookTypeList = bookTypeService.QueryBookType(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int bookTypeCount = bookTypeList.size();
		bookType_ShowText = new String[bookTypeCount+1];
		bookType_ShowText[0] = "不限制";
		for(int i=1;i<=bookTypeCount;i++) { 
			bookType_ShowText[i] = bookTypeList.get(i-1).getBookTypeName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		bookType_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, bookType_ShowText);
		// 设置图书所在类别下拉列表的风格
		bookType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_bookType.setAdapter(bookType_adapter);
		// 添加事件Spinner事件监听
		spinner_bookType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionBook.setBookType(bookTypeList.get(arg2-1).getBookTypeId()); 
				else
					queryConditionBook.setBookType(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_bookType.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionBook.setBarcode(ET_barcode.getText().toString());
					queryConditionBook.setBookName(ET_bookName.getText().toString());
					/*操作完成后返回到图书管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(BookQueryActivity.this, BookListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionBook", queryConditionBook);
					intent.putExtras(bundle);
					startActivity(intent);  
					BookQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
