package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.Book;
import com.mobileclient.service.BookService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class BookEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明图书条形码TextView
	private TextView TV_barcode;
	// 声明图书名称输入框
	private EditText ET_bookName;
	// 声明图书所在类别下拉框
	private Spinner spinner_bookType;
	private ArrayAdapter<String> bookType_adapter;
	private static  String[] bookType_ShowText  = null;
	private List<BookType> bookTypeList = null;
	/*图书所在类别管理业务逻辑层*/
	private BookTypeService bookTypeService = new BookTypeService();
	// 声明图书价格输入框
	private EditText ET_price;
	// 声明库存输入框
	private EditText ET_count;
	// 声明出版社输入框
	private EditText ET_publish;
	// 声明图书简介输入框
	private EditText ET_introduction;
	// 声明图书图片图片框控件
	private ImageView iv_bookPhoto;
	private Button btn_bookPhoto;
	protected int REQ_CODE_SELECT_IMAGE_bookPhoto = 1;
	private int REQ_CODE_CAMERA_bookPhoto = 2;
	protected String carmera_path;
	/*要保存的图书信息*/
	Book book = new Book();
	/*图书管理业务逻辑层*/
	private BookService bookService = new BookService();

	private String barcode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-修改图书");
		// 设置当前Activity界面布局
		setContentView(R.layout.book_edit); 
		TV_barcode = (TextView) findViewById(R.id.TV_barcode);
		ET_bookName = (EditText) findViewById(R.id.ET_bookName);
		spinner_bookType = (Spinner) findViewById(R.id.Spinner_bookType);
		// 获取所有的图书所在类别
		try {
			bookTypeList = bookTypeService.QueryBookType(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int bookTypeCount = bookTypeList.size();
		bookType_ShowText = new String[bookTypeCount];
		for(int i=0;i<bookTypeCount;i++) { 
			bookType_ShowText[i] = bookTypeList.get(i).getBookTypeName();
		}
		// 将可选内容与ArrayAdapter连接起来
		bookType_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, bookType_ShowText);
		// 设置图书类别下拉列表的风格
		bookType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_bookType.setAdapter(bookType_adapter);
		// 添加事件Spinner事件监听
		spinner_bookType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				book.setBookType(bookTypeList.get(arg2).getBookTypeId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_bookType.setVisibility(View.VISIBLE);
		ET_price = (EditText) findViewById(R.id.ET_price);
		ET_count = (EditText) findViewById(R.id.ET_count);
		ET_publish = (EditText) findViewById(R.id.ET_publish);
		ET_introduction = (EditText) findViewById(R.id.ET_introduction);
		iv_bookPhoto = (ImageView) findViewById(R.id.iv_bookPhoto);
		/*单击图片显示控件时进行图片的选择*/
		iv_bookPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(BookEditActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_bookPhoto);
			}
		});
		btn_bookPhoto = (Button) findViewById(R.id.btn_bookPhoto);
		btn_bookPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_bookPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_bookPhoto);  
			}
		});
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		barcode = extras.getString("barcode");
		initViewData();
		/*单击修改图书按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取图书名称*/ 
					if(ET_bookName.getText().toString().equals("")) {
						Toast.makeText(BookEditActivity.this, "图书名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_bookName.setFocusable(true);
						ET_bookName.requestFocus();
						return;	
					}
					book.setBookName(ET_bookName.getText().toString());
					/*验证获取图书价格*/ 
					if(ET_price.getText().toString().equals("")) {
						Toast.makeText(BookEditActivity.this, "图书价格输入不能为空!", Toast.LENGTH_LONG).show();
						ET_price.setFocusable(true);
						ET_price.requestFocus();
						return;	
					}
					book.setPrice(Float.parseFloat(ET_price.getText().toString()));
					/*验证获取库存*/ 
					if(ET_count.getText().toString().equals("")) {
						Toast.makeText(BookEditActivity.this, "库存输入不能为空!", Toast.LENGTH_LONG).show();
						ET_count.setFocusable(true);
						ET_count.requestFocus();
						return;	
					}
					book.setCount(Integer.parseInt(ET_count.getText().toString()));
					/*验证获取出版社*/ 
					if(ET_publish.getText().toString().equals("")) {
						Toast.makeText(BookEditActivity.this, "出版社输入不能为空!", Toast.LENGTH_LONG).show();
						ET_publish.setFocusable(true);
						ET_publish.requestFocus();
						return;	
					}
					book.setPublish(ET_publish.getText().toString());
					/*验证获取图书简介*/ 
					if(ET_introduction.getText().toString().equals("")) {
						Toast.makeText(BookEditActivity.this, "图书简介输入不能为空!", Toast.LENGTH_LONG).show();
						ET_introduction.setFocusable(true);
						ET_introduction.requestFocus();
						return;	
					}
					book.setIntroduction(ET_introduction.getText().toString());
					if (!book.getBookPhoto().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						BookEditActivity.this.setTitle("正在上传图片，稍等...");
						String bookPhoto = HttpUtil.uploadFile(book.getBookPhoto());
						BookEditActivity.this.setTitle("图片上传完毕！");
						book.setBookPhoto(bookPhoto);
					} 
					/*调用业务逻辑层上传图书信息*/
					BookEditActivity.this.setTitle("正在更新图书信息，稍等...");
					String result = bookService.UpdateBook(book);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*操作完成后返回到图书管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(BookEditActivity.this, BookListActivity.class);
					startActivity(intent); 
					BookEditActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    book = bookService.GetBook(barcode);
		this.TV_barcode.setText(barcode);
		this.ET_bookName.setText(book.getBookName());
		for (int i = 0; i < bookTypeList.size(); i++) {
			if (book.getBookType() == bookTypeList.get(i).getBookTypeId()) {
				this.spinner_bookType.setSelection(i);
				break;
			}
		}
		this.ET_price.setText(book.getPrice() + "");
		this.ET_count.setText(book.getCount() + "");
		this.ET_publish.setText(book.getPublish());
		this.ET_introduction.setText(book.getIntroduction());
		byte[] bookPhoto_data = null;
		try {
			// 获取图片数据
			bookPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + book.getBookPhoto());
			Bitmap bookPhoto = BitmapFactory.decodeByteArray(bookPhoto_data, 0, bookPhoto_data.length);
			this.iv_bookPhoto.setImageBitmap(bookPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_bookPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_bookPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_bookPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_bookPhoto.setImageBitmap(booImageBm);
				this.iv_bookPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.book.setBookPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_bookPhoto && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_bookPhoto.setImageBitmap(bm); 
				this.iv_bookPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			book.setBookPhoto(filename); 
		}
	}
}
