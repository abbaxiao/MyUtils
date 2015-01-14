package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.ReaderType;
import com.mobileclient.service.ReaderTypeService;
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
public class ReaderTypeAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// �����������������
	private EditText ET_readerTypeName;
	// �����ɽ�����Ŀ�����
	private EditText ET_number;
	protected String carmera_path;
	/*Ҫ����Ķ���������Ϣ*/
	ReaderType readerType = new ReaderType();
	/*�������͹���ҵ���߼���*/
	private ReaderTypeService readerTypeService = new ReaderTypeService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-��Ӷ�������");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.readertype_add); 
		ET_readerTypeName = (EditText) findViewById(R.id.ET_readerTypeName);
		ET_number = (EditText) findViewById(R.id.ET_number);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*������Ӷ������Ͱ�ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ��������*/ 
					if(ET_readerTypeName.getText().toString().equals("")) {
						Toast.makeText(ReaderTypeAddActivity.this, "�����������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_readerTypeName.setFocusable(true);
						ET_readerTypeName.requestFocus();
						return;	
					}
					readerType.setReaderTypeName(ET_readerTypeName.getText().toString());
					/*��֤��ȡ�ɽ�����Ŀ*/ 
					if(ET_number.getText().toString().equals("")) {
						Toast.makeText(ReaderTypeAddActivity.this, "�ɽ�����Ŀ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_number.setFocusable(true);
						ET_number.requestFocus();
						return;	
					}
					readerType.setNumber(Integer.parseInt(ET_number.getText().toString()));
					/*����ҵ���߼����ϴ�����������Ϣ*/
					ReaderTypeAddActivity.this.setTitle("�����ϴ�����������Ϣ���Ե�...");
					String result = readerTypeService.AddReaderType(readerType);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص��������͹������*/ 
					Intent intent = new Intent();
					intent.setClass(ReaderTypeAddActivity.this, ReaderTypeListActivity.class);
					startActivity(intent); 
					ReaderTypeAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
