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
	// �������ذ�ť
	private Button btnReturn;
	// ����ͼ�����ؼ�
	private TextView TV_bookTypeId;
	// ����������ƿؼ�
	private TextView TV_bookTypeName;
	// �����ɽ��������ؼ�
	private TextView TV_days;
	/* Ҫ�����ͼ��������Ϣ */
	BookType bookType = new BookType(); 
	/* ͼ�����͹���ҵ���߼��� */
	private BookTypeService bookTypeService = new BookTypeService();
	private int bookTypeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴ͼ����������");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.booktype_detail);
		// ͨ��findViewById����ʵ�������
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
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    bookType = bookTypeService.GetBookType(bookTypeId); 
		this.TV_bookTypeId.setText(bookType.getBookTypeId() + "");
		this.TV_bookTypeName.setText(bookType.getBookTypeName());
		this.TV_days.setText(bookType.getDays() + "");
	} 
}
