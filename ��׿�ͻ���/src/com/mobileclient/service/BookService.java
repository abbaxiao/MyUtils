package com.mobileclient.service;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.mobileclient.domain.Book;
import com.mobileclient.handler.BookListHandler;
import com.mobileclient.util.HttpUtil;

/*图书管理业务逻辑层*/
public class BookService {
	/* 添加图书 */
	public String AddBook(Book book) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barcode", book.getBarcode());
		params.put("bookName", book.getBookName());
		params.put("bookType", book.getBookType() + "");
		params.put("price", book.getPrice() + "");
		params.put("count", book.getCount() + "");
		params.put("publish", book.getPublish());
		params.put("introduction", book.getIntroduction());
		params.put("bookPhoto", book.getBookPhoto());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "BookServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 查询图书 */
	public List<Book> QueryBook(Book queryConditionBook) throws Exception {
		String urlString = HttpUtil.BASE_URL + "BookServlet?action=query";
		if(queryConditionBook != null) {
			urlString += "&barcode=" + URLEncoder.encode(queryConditionBook.getBarcode(), "UTF-8") + "";
			urlString += "&bookName=" + URLEncoder.encode(queryConditionBook.getBookName(), "UTF-8") + "";
			urlString += "&bookType=" + queryConditionBook.getBookType();
		}
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		BookListHandler bookListHander = new BookListHandler();
		xr.setContentHandler(bookListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Book> bookList = bookListHander.getBookList();
		return bookList;
	}
	/* 更新图书 */
	public String UpdateBook(Book book) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barcode", book.getBarcode());
		params.put("bookName", book.getBookName());
		params.put("bookType", book.getBookType() + "");
		params.put("price", book.getPrice() + "");
		params.put("count", book.getCount() + "");
		params.put("publish", book.getPublish());
		params.put("introduction", book.getIntroduction());
		params.put("bookPhoto", book.getBookPhoto());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "BookServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 删除图书 */
	public String DeleteBook(String barcode) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barcode", barcode);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "BookServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "图书信息删除失败!";
		}
	}
	/* 根据图书条形码获取图书对象 */
	public Book GetBook(String barcode)  {
		List<Book> bookList = new ArrayList<Book>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barcode", barcode);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "BookServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Book book = new Book();
				book.setBarcode(object.getString("barcode"));
				book.setBookName(object.getString("bookName"));
				book.setBookType(object.getInt("bookType"));
				book.setPrice((float) object.getDouble("price"));
				book.setCount(object.getInt("count"));
				book.setPublish(object.getString("publish"));
				book.setIntroduction(object.getString("introduction"));
				book.setBookPhoto(object.getString("bookPhoto"));
				bookList.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = bookList.size();
		if(size>0) return bookList.get(0); 
		else return null; 
	}
}
