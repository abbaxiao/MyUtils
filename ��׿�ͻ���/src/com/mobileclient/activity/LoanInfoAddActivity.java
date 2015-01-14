package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.LoanInfo;
import com.mobileclient.service.LoanInfoService;
import com.mobileclient.domain.Book;
import com.mobileclient.service.BookService;
import com.mobileclient.domain.Reader;
import com.mobileclient.service.ReaderService;
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
public class LoanInfoAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// ����ͼ�����������
	private Spinner spinner_book;
	private ArrayAdapter<String> book_adapter;
	private static  String[] book_ShowText  = null;
	private List<Book> bookList = null;
	/*ͼ��������ҵ���߼���*/
	private BookService bookService = new BookService();
	// �������߶���������
	private Spinner spinner_reader;
	private ArrayAdapter<String> reader_adapter;
	private static  String[] reader_ShowText  = null;
	private List<Reader> readerList = null;
	/*���߶������ҵ���߼���*/
	private ReaderService readerService = new ReaderService();
	// �������ʱ��ؼ�
	private DatePicker dp_borrowDate;
	// ����黹ʱ��ؼ�
	private DatePicker dp_returnDate;
	protected String carmera_path;
	/*Ҫ����Ľ�����Ϣ��Ϣ*/
	LoanInfo loanInfo = new LoanInfo();
	/*������Ϣ����ҵ���߼���*/
	private LoanInfoService loanInfoService = new LoanInfoService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-��ӽ�����Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.loaninfo_add); 
		spinner_book = (Spinner) findViewById(R.id.Spinner_book);
		// ��ȡ���е�ͼ�����
		try {
			bookList = bookService.QueryBook(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int bookCount = bookList.size();
		book_ShowText = new String[bookCount];
		for(int i=0;i<bookCount;i++) { 
			book_ShowText[i] = bookList.get(i).getBookName();
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
				loanInfo.setBook(bookList.get(arg2).getBarcode()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_book.setVisibility(View.VISIBLE);
		spinner_reader = (Spinner) findViewById(R.id.Spinner_reader);
		// ��ȡ���еĶ��߶���
		try {
			readerList = readerService.QueryReader(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int readerCount = readerList.size();
		reader_ShowText = new String[readerCount];
		for(int i=0;i<readerCount;i++) { 
			reader_ShowText[i] = readerList.get(i).getReaderName();
		}
		// ����ѡ������ArrayAdapter��������
		reader_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, reader_ShowText);
		// ����ͼ����������б�ķ��
		reader_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_reader.setAdapter(reader_adapter);
		// ����¼�Spinner�¼�����
		spinner_reader.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				loanInfo.setReader(readerList.get(arg2).getReaderNo()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_reader.setVisibility(View.VISIBLE);
		dp_borrowDate = (DatePicker)this.findViewById(R.id.dp_borrowDate);
		dp_returnDate = (DatePicker)this.findViewById(R.id.dp_returnDate);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*������ӽ�����Ϣ��ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��������*/
					Date borrowDate = new Date(dp_borrowDate.getYear()-1900,dp_borrowDate.getMonth(),dp_borrowDate.getDayOfMonth());
					loanInfo.setBorrowDate(new Timestamp(borrowDate.getTime()));
					/*��ȡ��������*/
					Date returnDate = new Date(dp_returnDate.getYear()-1900,dp_returnDate.getMonth(),dp_returnDate.getDayOfMonth());
					loanInfo.setReturnDate(new Timestamp(returnDate.getTime()));
					/*����ҵ���߼����ϴ�������Ϣ��Ϣ*/
					LoanInfoAddActivity.this.setTitle("�����ϴ�������Ϣ��Ϣ���Ե�...");
					String result = loanInfoService.AddLoanInfo(loanInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص�������Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(LoanInfoAddActivity.this, LoanInfoListActivity.class);
					startActivity(intent); 
					LoanInfoAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
