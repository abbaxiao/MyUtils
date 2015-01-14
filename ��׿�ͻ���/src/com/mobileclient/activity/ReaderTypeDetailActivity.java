package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.ReaderType;
import com.mobileclient.service.ReaderTypeService;
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

public class ReaderTypeDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明读者类型编号控件
	private TextView TV_readerTypeId;
	// 声明读者类型控件
	private TextView TV_readerTypeName;
	// 声明可借阅数目控件
	private TextView TV_number;
	/* 要保存的读者类型信息 */
	ReaderType readerType = new ReaderType(); 
	/* 读者类型管理业务逻辑层 */
	private ReaderTypeService readerTypeService = new ReaderTypeService();
	private int readerTypeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看读者类型详情");
		// 设置当前Activity界面布局
		setContentView(R.layout.readertype_detail);
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_readerTypeId = (TextView) findViewById(R.id.TV_readerTypeId);
		TV_readerTypeName = (TextView) findViewById(R.id.TV_readerTypeName);
		TV_number = (TextView) findViewById(R.id.TV_number);
		Bundle extras = this.getIntent().getExtras();
		readerTypeId = extras.getInt("readerTypeId");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ReaderTypeDetailActivity.this.finish();
			}
		}); 
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    readerType = readerTypeService.GetReaderType(readerTypeId); 
		this.TV_readerTypeId.setText(readerType.getReaderTypeId() + "");
		this.TV_readerTypeName.setText(readerType.getReaderTypeName());
		this.TV_number.setText(readerType.getNumber() + "");
	} 
}
