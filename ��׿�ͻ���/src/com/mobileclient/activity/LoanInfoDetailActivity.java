package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.LoanInfo;
import com.mobileclient.service.LoanInfoService;
import com.mobileclient.domain.Book;
import com.mobileclient.service.BookService;
import com.mobileclient.domain.Reader;
import com.mobileclient.service.ReaderService;
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

public class LoanInfoDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// �������ı�ſؼ�
	private TextView TV_loadId;
	// ����ͼ�����ؼ�
	private TextView TV_book;
	// �������߶���ؼ�
	private TextView TV_reader;
	// ��������ʱ��ؼ�
	private TextView TV_borrowDate;
	// �����黹ʱ��ؼ�
	private TextView TV_returnDate;
	/* Ҫ����Ľ�����Ϣ��Ϣ */
	LoanInfo loanInfo = new LoanInfo(); 
	/* ������Ϣ����ҵ���߼��� */
	private LoanInfoService loanInfoService = new LoanInfoService();
	private BookService bookService = new BookService();
	private ReaderService readerService = new ReaderService();
	private int loadId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴������Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.loaninfo_detail);
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_loadId = (TextView) findViewById(R.id.TV_loadId);
		TV_book = (TextView) findViewById(R.id.TV_book);
		TV_reader = (TextView) findViewById(R.id.TV_reader);
		TV_borrowDate = (TextView) findViewById(R.id.TV_borrowDate);
		TV_returnDate = (TextView) findViewById(R.id.TV_returnDate);
		Bundle extras = this.getIntent().getExtras();
		loadId = extras.getInt("loadId");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LoanInfoDetailActivity.this.finish();
			}
		}); 
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    loanInfo = loanInfoService.GetLoanInfo(loadId); 
		this.TV_loadId.setText(loanInfo.getLoadId() + "");
		Book book = bookService.GetBook(loanInfo.getBook());
		this.TV_book.setText(book.getBookName());
		Reader reader = readerService.GetReader(loanInfo.getReader());
		this.TV_reader.setText(reader.getReaderName());
		Date borrowDate = new Date(loanInfo.getBorrowDate().getTime());
		String borrowDateStr = (borrowDate.getYear() + 1900) + "-" + (borrowDate.getMonth()+1) + "-" + borrowDate.getDate();
		this.TV_borrowDate.setText(borrowDateStr);
		Date returnDate = new Date(loanInfo.getReturnDate().getTime());
		String returnDateStr = (returnDate.getYear() + 1900) + "-" + (returnDate.getMonth()+1) + "-" + returnDate.getDate();
		this.TV_returnDate.setText(returnDateStr);
	} 
}
