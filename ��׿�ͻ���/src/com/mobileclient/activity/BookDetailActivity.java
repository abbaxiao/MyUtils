package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Book;
import com.mobileclient.service.BookService;
import com.mobileclient.domain.BookType;
import com.mobileclient.service.BookTypeService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明图书条形码控件
	private TextView TV_barcode;
	// 声明图书名称控件
	private TextView TV_bookName;
	// 声明图书所在类别控件
	private TextView TV_bookType;
	// 声明图书价格控件
	private TextView TV_price;
	// 声明库存控件
	private TextView TV_count;
	// 声明出版社控件
	private TextView TV_publish;
	// 声明图书简介控件
	private TextView TV_introduction;
	// 声明图书图片图片框
	private ImageView iv_bookPhoto;
	/* 要保存的图书信息 */
	Book book = new Book(); 
	/* 图书管理业务逻辑层 */
	private BookService bookService = new BookService();
	private BookTypeService bookTypeService = new BookTypeService();
	private String barcode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看图书详情");
		// 设置当前Activity界面布局
		setContentView(R.layout.book_detail);
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_barcode = (TextView) findViewById(R.id.TV_barcode);
		TV_bookName = (TextView) findViewById(R.id.TV_bookName);
		TV_bookType = (TextView) findViewById(R.id.TV_bookType);
		TV_price = (TextView) findViewById(R.id.TV_price);
		TV_count = (TextView) findViewById(R.id.TV_count);
		TV_publish = (TextView) findViewById(R.id.TV_publish);
		TV_introduction = (TextView) findViewById(R.id.TV_introduction);
		iv_bookPhoto = (ImageView) findViewById(R.id.iv_bookPhoto); 
		Bundle extras = this.getIntent().getExtras();
		barcode = extras.getString("barcode");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BookDetailActivity.this.finish();
			}
		}); 
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    book = bookService.GetBook(barcode); 
		this.TV_barcode.setText(book.getBarcode());
		this.TV_bookName.setText(book.getBookName());
		BookType bookType = bookTypeService.GetBookType(book.getBookType());
		this.TV_bookType.setText(bookType.getBookTypeName());
		this.TV_price.setText(book.getPrice() + "");
		this.TV_count.setText(book.getCount() + "");
		this.TV_publish.setText(book.getPublish());
		this.TV_introduction.setText(book.getIntroduction());
		byte[] bookPhoto_data = null;
		try {
			// 获取图片数据
			bookPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + book.getBookPhoto());
			Bitmap bookPhoto = BitmapFactory.decodeByteArray(bookPhoto_data, 0,bookPhoto_data.length);
			this.iv_bookPhoto.setImageBitmap(bookPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
