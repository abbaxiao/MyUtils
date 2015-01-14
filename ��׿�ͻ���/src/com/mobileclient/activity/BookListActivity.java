package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Book;
import com.mobileclient.service.BookService;
import com.mobileclient.util.BookSimpleAdapter;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BookListActivity extends Activity {
	BookSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String barcode;
	/* ͼ�����ҵ���߼������ */
	BookService bookService = new BookService();
	/*�����ѯ����������ͼ�����*/
	private Book queryConditionBook;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("��ǰλ��--ͼ���б�");
		} else {
			setTitle("���ã�" + username + "   ��ǰλ��---ͼ���б�");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionBook = (Book)extras.getSerializable("queryConditionBook");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new BookSimpleAdapter(this, list,
					R.layout.book_list_item,
					new String[] { "barcode","bookName","bookType","bookPhoto" },
					new int[] { R.id.tv_barcode,R.id.tv_bookName,R.id.tv_bookType,R.id.iv_bookPhoto,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// ��ӳ������
		lv.setOnCreateContextMenuListener(bookListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	String barcode = list.get(arg2).get("barcode").toString();
            	Intent intent = new Intent();
            	intent.setClass(BookListActivity.this, BookDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("barcode", barcode);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener bookListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "�༭ͼ����Ϣ"); 
			menu.add(0, 1, 0, "ɾ��ͼ����Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭ͼ����Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡͼ��������
			barcode = list.get(position).get("barcode").toString();
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, BookEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("barcode", barcode);
			intent.putExtras(bundle);
			startActivity(intent);
			BookListActivity.this.finish();
		} else if (item.getItemId() == 1) {// ɾ��ͼ����Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡͼ��������
			barcode = list.get(position).get("barcode").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(BookListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = bookService.DeleteBook(barcode);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* ��ѯͼ����Ϣ */
			List<Book> bookList = bookService.QueryBook(queryConditionBook);
			for (int i = 0; i < bookList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("barcode", bookList.get(i).getBarcode());
				map.put("bookName", bookList.get(i).getBookName());
				map.put("bookType", bookList.get(i).getBookType());
				byte[] bookPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ bookList.get(i).getBookPhoto());// ��ȡͼƬ����
				BitmapFactory.Options bookPhoto_opts = new BitmapFactory.Options();  
				bookPhoto_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(bookPhoto_data, 0, bookPhoto_data.length, bookPhoto_opts); 
				bookPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(bookPhoto_opts, -1, 100*100); 
				bookPhoto_opts.inJustDecodeBounds = false; 
				try {
					Bitmap bookPhoto = BitmapFactory.decodeByteArray(bookPhoto_data, 0, bookPhoto_data.length, bookPhoto_opts);
					map.put("bookPhoto", bookPhoto);
				} catch (OutOfMemoryError err) { }
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "���ͼ��");
		menu.add(0, 2, 2, "��ѯͼ��");
		menu.add(0, 3, 3, "����������");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// ���ͼ����Ϣ
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, BookAddActivity.class);
			startActivity(intent);
			BookListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*��ѯͼ����Ϣ*/
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, BookQueryActivity.class);
			startActivity(intent);
			BookListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*����������*/
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			BookListActivity.this.finish();
		}
		return true; 
	}
}
