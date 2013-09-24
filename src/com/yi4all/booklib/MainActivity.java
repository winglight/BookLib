package com.yi4all.booklib;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yi4all.booklib.db.BookModel;
import com.yi4all.booklib.db.BookStatus;
import com.yi4all.booklib.fragment.BookFragment;
import com.yi4all.booklib.service.DBService;
import com.yi4all.booklib.service.HttpUtils;
import com.yi4all.booklib.util.DateUtils;
import com.yi4all.booklib.zxing.IntentIntegrator;
import com.yi4all.booklib.zxing.IntentResult;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ListView listView;
	private List<BookModel> books = new ArrayList<BookModel>();
	private DBService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		service = DBService.getInstance(this);

		listView = (ListView) findViewById(R.id.bookList);

		listView.setAdapter(new MyListAdapter(this));

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long l) {
				popupBookFragment(books.get(position));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add: {
			// create book
			popupBookFragment(null);
			return true;
		}
		case R.id.action_scan: {
			// popup the qrscan
			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.initiateScan();
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			// handle scan result
			final String isbn = scanResult.getContents();
			toastMsg("isbn:" + isbn);
			if (isbn != null && isbn.length() == 13 || isbn.length() == 9) {
				// 1.find book in local lib
				List<BookModel> list = service.findBooksByIsbn(isbn);
				if (list != null && list.size() > 0) {
					if (list.size() == 1) {
						popupBookFragment(list.get(0));
					} else {
						// filter list
						books = list;
					}
					refreshList();
				} else {

					// search book by isbn
					HttpUtils.get(getUrl(isbn), null,
							new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										String response) {
									super.onSuccess(statusCode, response);

									BookModel bm = parseBook(response);
									bm.setIsbn(isbn);

									// create new book
									popupBookFragment(bm);
								}
							});
				}

			}
		}
		// else continue with any other code you need in the method
	}

	private String getUrl(String isbn) {
		return "http://www.amazon.cn/s?search-alias=stripbooks&field-isbn="
				+ isbn;
	}

	private BookModel parseBook(String content) {
		BookModel bm = new BookModel();
		Document source = Jsoup.parse(content);
		// TODO:parse content into book
		Element result = source.getElementById("result_0");
		Elements imgs = result.select("img");
		if (imgs != null && imgs.size() > 0) {
			bm.setCoverUrl(imgs.get(0).attr("src"));
		}
		Elements spans = result.select("span");
		if (spans != null && spans.size() > 1) {
			bm.setName(spans.get(0).text());
			bm.setMetaInfo(spans.get(1).text());
		}

		return bm;
	}

	private void popupBookFragment(final BookModel bm) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
				if (prev != null) {
					ft.remove(prev);
				}
				ft.addToBackStack(null);

				// Create and show the dialog.
				BookFragment newFragment = BookFragment.newInstance(bm);
				newFragment.show(ft, "dialog");
				
			}
		}, 1000);
		
		
	}

	public void refreshList() {
new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
		books = service.getBooks();

		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
			}
}, 1000);
	}

	public void toastMsg(int resId, String... args) {
		final String msg = this.getString(resId, args);
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	public void toastMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onResume() {
		super.onStart();

		refreshList();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(service != null){
			service.close();
		}
	}

	class MyListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyListAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return books.size();
		}

		public BookModel getItem(int i) {
			return books.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			if (books == null || position < 0 || position > books.size())
				return null;

			final View row = mInflater.inflate(R.layout.book_list_item, null);

			ViewHolder holder = (ViewHolder) row.getTag();
			if (holder == null) {
				holder = new ViewHolder(row);
				row.setTag(holder);
			}

			// other normal row
			final BookModel rm = books.get(position);

			// set name to label
			holder.title.setText(rm.getName());

			// set amount of images
			holder.meta.setText(rm.getMetaInfo());

			holder.status.setText(rm.getStatus().getDisplayName()
					+ (rm.getStatus() == BookStatus.OUT ? "\n"
							+ DateUtils.formatDate(rm.getLentAt()) 
							+ "\n" + rm.getLender(): ""));
			
			holder.code.setText(rm.getCode() + " | " + rm.getCategory());

			ImageLoader.getInstance().displayImage(rm.getCoverUrl(),
					holder.cover);

			return (row);
		}
	}

	class ViewHolder {
		TextView title = null;
		TextView meta = null;
		TextView status = null;
		TextView code = null;
		ImageView cover = null;

		ViewHolder(View base) {
			this.title = (TextView) base.findViewById(R.id.row_title);
			this.meta = (TextView) base.findViewById(R.id.row_meta);
			this.status = (TextView) base.findViewById(R.id.row_status);
			this.code = (TextView) base.findViewById(R.id.row_code);
			this.cover = (ImageView) base.findViewById(R.id.cover);
		}
	}
}
