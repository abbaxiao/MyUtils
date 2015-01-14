package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.LoanInfo;
import com.mobileclient.service.LoanInfoService;
import com.mobileclient.domain.Book;
import com.mobileclient.service.BookService;
import com.mobileclient.domain.Reader;
import com.mobileclient.service.ReaderService;
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
public class LoanInfoAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明图书对象下拉框
	private Spinner spinner_book;
	private ArrayAdapter<String> book_adapter;
	private static  String[] book_ShowText  = null;
	private List<Book> bookList = null;
	/*图书对象管理业务逻辑层*/
	private BookService bookService = new BookService();
	// 声明读者对象下拉框
	private Spinner spinner_reader;
	private ArrayAdapter<String> reader_adapter;
	private static  String[] reader_ShowText  = null;
	private List<Reader> readerList = null;
	/*读者对象管理业务逻辑层*/
	private ReaderService readerService = new ReaderService();
	// 出版借阅时间控件
	private DatePicker dp_borrowDate;
	// 出版归还时间控件
	private DatePicker dp_returnDate;
	protected String carmera_path;
	/*要保存的借阅信息信息*/
	LoanInfo loanInfo = new LoanInfo();
	/*借阅信息管理业务逻辑层*/
	private LoanInfoService loanInfoService = new LoanInfoService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-添加借阅信息");
		// 设置当前Activity界面布局
		setContentView(R.layout.loaninfo_add); 
		spinner_book = (Spinner) findViewById(R.id.Spinner_book);
		// 获取所有的图书对象
		try {
			bookList = bookService.QueryBook(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int bookCount = bookList.size();
		book_ShowText = new String[bookCount];
		for(int i=0;i<bookCount;i++) { 
			book_ShowText[i] = bookList.get(i).getBookName();
		}
		// 将可选内容与ArrayAdapter连接起来
		book_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, book_ShowText);
		// 设置图书类别下拉列表的风格
		book_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_book.setAdapter(book_adapter);
		// 添加事件Spinner事件监听
		spinner_book.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				loanInfo.setBook(bookList.get(arg2).getBarcode()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_book.setVisibility(View.VISIBLE);
		spinner_reader = (Spinner) findViewById(R.id.Spinner_reader);
		// 获取所有的读者对象
		try {
			readerList = readerService.QueryReader(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int readerCount = readerList.size();
		reader_ShowText = new String[readerCount];
		for(int i=0;i<readerCount;i++) { 
			reader_ShowText[i] = readerList.get(i).getReaderName();
		}
		// 将可选内容与ArrayAdapter连接起来
		reader_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, reader_ShowText);
		// 设置图书类别下拉列表的风格
		reader_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_reader.setAdapter(reader_adapter);
		// 添加事件Spinner事件监听
		spinner_reader.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				loanInfo.setReader(readerList.get(arg2).getReaderNo()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_reader.setVisibility(View.VISIBLE);
		dp_borrowDate = (DatePicker)this.findViewById(R.id.dp_borrowDate);
		dp_returnDate = (DatePicker)this.findViewById(R.id.dp_returnDate);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加借阅信息按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取出版日期*/
					Date borrowDate = new Date(dp_borrowDate.getYear()-1900,dp_borrowDate.getMonth(),dp_borrowDate.getDayOfMonth());
					loanInfo.setBorrowDate(new Timestamp(borrowDate.getTime()));
					/*获取出版日期*/
					Date returnDate = new Date(dp_returnDate.getYear()-1900,dp_returnDate.getMonth(),dp_returnDate.getDayOfMonth());
					loanInfo.setReturnDate(new Timestamp(returnDate.getTime()));
					/*调用业务逻辑层上传借阅信息信息*/
					LoanInfoAddActivity.this.setTitle("正在上传借阅信息信息，稍等...");
					String result = loanInfoService.AddLoanInfo(loanInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*操作完成后返回到借阅信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(LoanInfoAddActivity.this, LoanInfoListActivity.class);
					startActivity(intent); 
					LoanInfoAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
