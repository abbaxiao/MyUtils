package com.mobileclient.activity;

import java.util.Date;
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

public class BookTypeDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明图书类别控件
	private TextView TV_bookTypeId;
	// 声明类别名称控件
	private TextView TV_bookTypeName;
	// 声明可借阅天数控件
	private TextView TV_days;
	/* 要保存的图书类型信息 */
	BookType bookType = new BookType(); 
	/* 图书类型管理业务逻辑层 */
	private BookTypeService bookTypeService = new BookTypeService();
	private int bookTypeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看图书类型详情");
		// 设置当前Activity界面布局
		setContentView(R.layout.booktype_detail);
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_bookTypeId = (TextView) findViewById(R.id.TV_bookTypeId);
		TV_bookTypeName = (TextView) findViewById(R.id.TV_bookTypeName);
		TV_days = (TextView) findViewById(R.id.TV_days);
		Bundle extras = this.getIntent().getExtras();
		bookTypeId = extras.getInt("bookTypeId");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BookTypeDetailActivity.this.finish();
			}
		}); 
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    bookType = bookTypeService.GetBookType(bookTypeId); 
		this.TV_bookTypeId.setText(bookType.getBookTypeId() + "");
		this.TV_bookTypeName.setText(bookType.getBookTypeName());
		this.TV_days.setText(bookType.getDays() + "");
	} 
}
