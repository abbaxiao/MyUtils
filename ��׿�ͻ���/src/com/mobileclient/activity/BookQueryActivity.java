package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Book;
import com.mobileclient.domain.BookType;
import com.mobileclient.service.BookTypeService;

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

public class BookQueryActivity extends Activity {
	// ������ѯ��ť
	private Button btnQuery;
	// ����ͼ�������������
	private EditText ET_barcode;
	// ����ͼ�����������
	private EditText ET_bookName;
	// ����ͼ���������������
	private Spinner spinner_bookType;
	private ArrayAdapter<String> bookType_adapter;
	private static  String[] bookType_ShowText  = null;
	private List<BookType> bookTypeList = null; 
	/*ͼ�����͹���ҵ���߼���*/
	private BookTypeService bookTypeService = new BookTypeService();
	/*��ѯ�����������浽���������*/
	private Book queryConditionBook = new Book();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯͼ������");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.book_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_barcode = (EditText) findViewById(R.id.ET_barcode);
		ET_bookName = (EditText) findViewById(R.id.ET_bookName);
		spinner_bookType = (Spinner) findViewById(R.id.Spinner_bookType);
		// ��ȡ���е�ͼ������
		try {
			bookTypeList = bookTypeService.QueryBookType(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int bookTypeCount = bookTypeList.size();
		bookType_ShowText = new String[bookTypeCount+1];
		bookType_ShowText[0] = "������";
		for(int i=1;i<=bookTypeCount;i++) { 
			bookType_ShowText[i] = bookTypeList.get(i-1).getBookTypeName();
		} 
		// ����ѡ������ArrayAdapter��������
		bookType_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, bookType_ShowText);
		// ����ͼ��������������б�ķ��
		bookType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_bookType.setAdapter(bookType_adapter);
		// ����¼�Spinner�¼�����
		spinner_bookType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionBook.setBookType(bookTypeList.get(arg2-1).getBookTypeId()); 
				else
					queryConditionBook.setBookType(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_bookType.setVisibility(View.VISIBLE);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionBook.setBarcode(ET_barcode.getText().toString());
					queryConditionBook.setBookName(ET_bookName.getText().toString());
					/*������ɺ󷵻ص�ͼ��������*/ 
					Intent intent = new Intent();
					intent.setClass(BookQueryActivity.this, BookListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionBook", queryConditionBook);
					intent.putExtras(bundle);
					startActivity(intent);  
					BookQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
