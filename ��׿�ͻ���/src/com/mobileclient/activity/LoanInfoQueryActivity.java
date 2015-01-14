package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.LoanInfo;
import com.mobileclient.domain.Book;
import com.mobileclient.service.BookService;
import com.mobileclient.domain.Reader;
import com.mobileclient.service.ReaderService;

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

public class LoanInfoQueryActivity extends Activity {
	// ������ѯ��ť
	private Button btnQuery;
	// ����ͼ�����������
	private Spinner spinner_book;
	private ArrayAdapter<String> book_adapter;
	private static  String[] book_ShowText  = null;
	private List<Book> bookList = null; 
	/*ͼ�����ҵ���߼���*/
	private BookService bookService = new BookService();
	// �������߶���������
	private Spinner spinner_reader;
	private ArrayAdapter<String> reader_adapter;
	private static  String[] reader_ShowText  = null;
	private List<Reader> readerList = null; 
	/*���߹���ҵ���߼���*/
	private ReaderService readerService = new ReaderService();
	/*��ѯ�����������浽���������*/
	private LoanInfo queryConditionLoanInfo = new LoanInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯ������Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.loaninfo_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_book = (Spinner) findViewById(R.id.Spinner_book);
		// ��ȡ���е�ͼ��
		try {
			bookList = bookService.QueryBook(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int bookCount = bookList.size();
		book_ShowText = new String[bookCount+1];
		book_ShowText[0] = "������";
		for(int i=1;i<=bookCount;i++) { 
			book_ShowText[i] = bookList.get(i-1).getBookName();
		} 
		// ����ѡ������ArrayAdapter��������
		book_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, book_ShowText);
		// ����ͼ����������б�ķ��
		book_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_book.setAdapter(book_adapter);
		// ����¼�Spinner�¼�����
		spinner_book.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionLoanInfo.setBook(bookList.get(arg2-1).getBarcode()); 
				else
					queryConditionLoanInfo.setBook("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_book.setVisibility(View.VISIBLE);
		spinner_reader = (Spinner) findViewById(R.id.Spinner_reader);
		// ��ȡ���еĶ���
		try {
			readerList = readerService.QueryReader(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int readerCount = readerList.size();
		reader_ShowText = new String[readerCount+1];
		reader_ShowText[0] = "������";
		for(int i=1;i<=readerCount;i++) { 
			reader_ShowText[i] = readerList.get(i-1).getReaderName();
		} 
		// ����ѡ������ArrayAdapter��������
		reader_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, reader_ShowText);
		// ���ö��߶��������б�ķ��
		reader_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_reader.setAdapter(reader_adapter);
		// ����¼�Spinner�¼�����
		spinner_reader.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionLoanInfo.setReader(readerList.get(arg2-1).getReaderNo()); 
				else
					queryConditionLoanInfo.setReader("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_reader.setVisibility(View.VISIBLE);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					/*������ɺ󷵻ص�������Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(LoanInfoQueryActivity.this, LoanInfoListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionLoanInfo", queryConditionLoanInfo);
					intent.putExtras(bundle);
					startActivity(intent);  
					LoanInfoQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
