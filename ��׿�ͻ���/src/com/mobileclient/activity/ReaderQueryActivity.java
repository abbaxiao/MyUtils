package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Reader;
import com.mobileclient.domain.ReaderType;
import com.mobileclient.service.ReaderTypeService;

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

public class ReaderQueryActivity extends Activity {
	// ������ѯ��ť
	private Button btnQuery;
	// �������߱�������
	private EditText ET_readerNo;
	// ������������������
	private Spinner spinner_readerType;
	private ArrayAdapter<String> readerType_adapter;
	private static  String[] readerType_ShowText  = null;
	private List<ReaderType> readerTypeList = null; 
	/*�������͹���ҵ���߼���*/
	private ReaderTypeService readerTypeService = new ReaderTypeService();
	// �������������
	private EditText ET_readerName;
	// �������տؼ�
	private DatePicker dp_birthday;
	private CheckBox cb_birthday;
	// ������ϵ�绰�����
	private EditText ET_telephone;
	/*��ѯ�����������浽���������*/
	private Reader queryConditionReader = new Reader();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯ��������");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.reader_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_readerNo = (EditText) findViewById(R.id.ET_readerNo);
		spinner_readerType = (Spinner) findViewById(R.id.Spinner_readerType);
		// ��ȡ���еĶ�������
		try {
			readerTypeList = readerTypeService.QueryReaderType(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int readerTypeCount = readerTypeList.size();
		readerType_ShowText = new String[readerTypeCount+1];
		readerType_ShowText[0] = "������";
		for(int i=1;i<=readerTypeCount;i++) { 
			readerType_ShowText[i] = readerTypeList.get(i-1).getReaderTypeName();
		} 
		// ����ѡ������ArrayAdapter��������
		readerType_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, readerType_ShowText);
		// ���ö������������б�ķ��
		readerType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_readerType.setAdapter(readerType_adapter);
		// ����¼�Spinner�¼�����
		spinner_readerType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionReader.setReaderType(readerTypeList.get(arg2-1).getReaderTypeId()); 
				else
					queryConditionReader.setReaderType(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_readerType.setVisibility(View.VISIBLE);
		ET_readerName = (EditText) findViewById(R.id.ET_readerName);
		dp_birthday = (DatePicker) findViewById(R.id.dp_birthday);
		cb_birthday = (CheckBox) findViewById(R.id.cb_birthday);
		ET_telephone = (EditText) findViewById(R.id.ET_telephone);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionReader.setReaderNo(ET_readerNo.getText().toString());
					queryConditionReader.setReaderName(ET_readerName.getText().toString());
					if(cb_birthday.isChecked()) {
						/*��ȡ��������*/
						Date birthday = new Date(dp_birthday.getYear()-1900,dp_birthday.getMonth(),dp_birthday.getDayOfMonth());
						queryConditionReader.setBirthday(new Timestamp(birthday.getTime()));
					} else {
						queryConditionReader.setBirthday(null);
					} 
					queryConditionReader.setTelephone(ET_telephone.getText().toString());
					/*������ɺ󷵻ص����߹������*/ 
					Intent intent = new Intent();
					intent.setClass(ReaderQueryActivity.this, ReaderListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionReader", queryConditionReader);
					intent.putExtras(bundle);
					startActivity(intent);  
					ReaderQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
