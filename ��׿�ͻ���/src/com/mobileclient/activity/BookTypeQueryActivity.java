package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.BookType;

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

public class BookTypeQueryActivity extends Activity {
	// ������ѯ��ť
	private Button btnQuery;
	/*��ѯ�����������浽���������*/
	private BookType queryConditionBookType = new BookType();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯͼ����������");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.booktype_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					/*������ɺ󷵻ص�ͼ�����͹������*/ 
					Intent intent = new Intent();
					intent.setClass(BookTypeQueryActivity.this, BookTypeListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionBookType", queryConditionBookType);
					intent.putExtras(bundle);
					startActivity(intent);  
					BookTypeQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
