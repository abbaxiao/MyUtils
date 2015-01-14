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
	// �������ذ�ť
	private Button btnReturn;
	// �����������ͱ�ſؼ�
	private TextView TV_readerTypeId;
	// �����������Ϳؼ�
	private TextView TV_readerTypeName;
	// �����ɽ�����Ŀ�ؼ�
	private TextView TV_number;
	/* Ҫ����Ķ���������Ϣ */
	ReaderType readerType = new ReaderType(); 
	/* �������͹���ҵ���߼��� */
	private ReaderTypeService readerTypeService = new ReaderTypeService();
	private int readerTypeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴������������");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.readertype_detail);
		// ͨ��findViewById����ʵ�������
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
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    readerType = readerTypeService.GetReaderType(readerTypeId); 
		this.TV_readerTypeId.setText(readerType.getReaderTypeId() + "");
		this.TV_readerTypeName.setText(readerType.getReaderTypeName());
		this.TV_number.setText(readerType.getNumber() + "");
	} 
}
