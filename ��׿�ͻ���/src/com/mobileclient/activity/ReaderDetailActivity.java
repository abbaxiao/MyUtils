package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Reader;
import com.mobileclient.service.ReaderService;
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

public class ReaderDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// �������߱�ſؼ�
	private TextView TV_readerNo;
	// �����������Ϳؼ�
	private TextView TV_readerType;
	// ���������ؼ�
	private TextView TV_readerName;
	// �����Ա�ؼ�
	private TextView TV_sex;
	// �����������տؼ�
	private TextView TV_birthday;
	// ������ϵ�绰�ؼ�
	private TextView TV_telephone;
	// ������ϵEmail�ؼ�
	private TextView TV_email;
	// ������ϵqq�ؼ�
	private TextView TV_qq;
	// �������ߵ�ַ�ؼ�
	private TextView TV_address;
	// ��������ͷ��ͼƬ��
	private ImageView iv_photo;
	/* Ҫ����Ķ�����Ϣ */
	Reader reader = new Reader(); 
	/* ���߹���ҵ���߼��� */
	private ReaderService readerService = new ReaderService();
	private ReaderTypeService readerTypeService = new ReaderTypeService();
	private String readerNo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴��������");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.reader_detail);
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_readerNo = (TextView) findViewById(R.id.TV_readerNo);
		TV_readerType = (TextView) findViewById(R.id.TV_readerType);
		TV_readerName = (TextView) findViewById(R.id.TV_readerName);
		TV_sex = (TextView) findViewById(R.id.TV_sex);
		TV_birthday = (TextView) findViewById(R.id.TV_birthday);
		TV_telephone = (TextView) findViewById(R.id.TV_telephone);
		TV_email = (TextView) findViewById(R.id.TV_email);
		TV_qq = (TextView) findViewById(R.id.TV_qq);
		TV_address = (TextView) findViewById(R.id.TV_address);
		iv_photo = (ImageView) findViewById(R.id.iv_photo); 
		Bundle extras = this.getIntent().getExtras();
		readerNo = extras.getString("readerNo");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ReaderDetailActivity.this.finish();
			}
		}); 
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    reader = readerService.GetReader(readerNo); 
		this.TV_readerNo.setText(reader.getReaderNo());
		ReaderType readerType = readerTypeService.GetReaderType(reader.getReaderType());
		this.TV_readerType.setText(readerType.getReaderTypeName());
		this.TV_readerName.setText(reader.getReaderName());
		this.TV_sex.setText(reader.getSex());
		Date birthday = new Date(reader.getBirthday().getTime());
		String birthdayStr = (birthday.getYear() + 1900) + "-" + (birthday.getMonth()+1) + "-" + birthday.getDate();
		this.TV_birthday.setText(birthdayStr);
		this.TV_telephone.setText(reader.getTelephone());
		this.TV_email.setText(reader.getEmail());
		this.TV_qq.setText(reader.getQq());
		this.TV_address.setText(reader.getAddress());
		byte[] photo_data = null;
		try {
			// ��ȡͼƬ����
			photo_data = ImageService.getImage(HttpUtil.BASE_URL + reader.getPhoto());
			Bitmap photo = BitmapFactory.decodeByteArray(photo_data, 0,photo_data.length);
			this.iv_photo.setImageBitmap(photo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
