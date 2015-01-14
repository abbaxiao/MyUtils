package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.LoanInfo;
import com.mobileclient.domain.Book;
import com.mobileclient.service.BookService;
import com.mobileclient.domain.Reader;
import com.mobileclient.service.ReaderService;

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

public class LoanInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明图书对象下拉框
	private Spinner spinner_book;
	private ArrayAdapter<String> book_adapter;
	private static  String[] book_ShowText  = null;
	private List<Book> bookList = null; 
	/*图书管理业务逻辑层*/
	private BookService bookService = new BookService();
	// 声明读者对象下拉框
	private Spinner spinner_reader;
	private ArrayAdapter<String> reader_adapter;
	private static  String[] reader_ShowText  = null;
	private List<Reader> readerList = null; 
	/*读者管理业务逻辑层*/
	private ReaderService readerService = new ReaderService();
	/*查询过滤条件保存到这个对象中*/
	private LoanInfo queryConditionLoanInfo = new LoanInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询借阅信息条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.loaninfo_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_book = (Spinner) findViewById(R.id.Spinner_book);
		// 获取所有的图书
		try {
			bookList = bookService.QueryBook(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int bookCount = bookList.size();
		book_ShowText = new String[bookCount+1];
		book_ShowText[0] = "不限制";
		for(int i=1;i<=bookCount;i++) { 
			book_ShowText[i] = bookList.get(i-1).getBookName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		book_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, book_ShowText);
		// 设置图书对象下拉列表的风格
		book_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_book.setAdapter(book_adapter);
		// 添加事件Spinner事件监听
		spinner_book.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionLoanInfo.setBook(bookList.get(arg2-1).getBarcode()); 
				else
					queryConditionLoanInfo.setBook("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_book.setVisibility(View.VISIBLE);
		spinner_reader = (Spinner) findViewById(R.id.Spinner_reader);
		// 获取所有的读者
		try {
			readerList = readerService.QueryReader(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int readerCount = readerList.size();
		reader_ShowText = new String[readerCount+1];
		reader_ShowText[0] = "不限制";
		for(int i=1;i<=readerCount;i++) { 
			reader_ShowText[i] = readerList.get(i-1).getReaderName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		reader_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, reader_ShowText);
		// 设置读者对象下拉列表的风格
		reader_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_reader.setAdapter(reader_adapter);
		// 添加事件Spinner事件监听
		spinner_reader.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionLoanInfo.setReader(readerList.get(arg2-1).getReaderNo()); 
				else
					queryConditionLoanInfo.setReader("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_reader.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					/*操作完成后返回到借阅信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(LoanInfoQueryActivity.this, LoanInfoListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionLoanInfo", queryConditionLoanInfo);
					intent.putExtras(bundle);
					startActivity(intent);  
					LoanInfoQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
