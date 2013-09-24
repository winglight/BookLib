package com.yi4all.booklib.fragment;

import java.util.Date;

import org.apache.http.cookie.SM;

import com.yi4all.booklib.MainActivity;
import com.yi4all.booklib.R;
import com.yi4all.booklib.db.BookModel;
import com.yi4all.booklib.db.BookStatus;
import com.yi4all.booklib.service.DBService;
import com.yi4all.booklib.util.DateUtils;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by ChenYu on 13-8-8.
 */
public class BookFragment extends DialogFragment  {

    public final static String SM_DATA = "server";

    private BookModel bm;
    
    private boolean isNew;
    
    private EditText nameTxt;
    private EditText metaTxt;
    private EditText isbnTxt;
    private EditText codeTxt;
    private TextView statusTxt;
    private TextView lenderTxt;
    private AutoCompleteTextView categoryTxt;
    
    private Button lendBtn;
    
    private DBService dbService;

    public BookFragment() {
    }

    public static BookFragment newInstance(BookModel sm) {
        final BookFragment f = new BookFragment();

        final Bundle args = new Bundle();
        args.putSerializable(SM_DATA, sm);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        bm = getArguments() != null ? (BookModel) getArguments().getSerializable(SM_DATA) : new BookModel();
        
        isNew = (bm == null || bm.getId() <= 0);

        dbService = DBService.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_book, container, false);
        
        nameTxt = (EditText) v.findViewById(R.id.bookNameEditor);
        metaTxt = (EditText) v.findViewById(R.id.bookMetaEditor);
        isbnTxt = (EditText) v.findViewById(R.id.bookIsbnEditor);
        codeTxt = (EditText) v.findViewById(R.id.bookCodeEditor);
        statusTxt =  (TextView) v.findViewById(R.id.statusTxt);
        lenderTxt =  (TextView) v.findViewById(R.id.bookLenderTxt);
        categoryTxt = (AutoCompleteTextView) v.findViewById(R.id.bookCategoryAutoTxt);
        
        categoryTxt.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					String txt = ((AutoCompleteTextView)v).getText().toString();
					if(txt == null || txt.length() == 0){
					((AutoCompleteTextView)v).showDropDown();
					}
				}else{
					((AutoCompleteTextView)v).dismissDropDown();
				}
			}
		});
        
        Button saveBtn = (Button) v.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(bm == null){
					bm = new BookModel();
				}
				bm.setName(nameTxt.getText().toString());
				bm.setMetaInfo(metaTxt.getText().toString());
				bm.setIsbn(isbnTxt.getText().toString());
				bm.setCode(codeTxt.getText().toString());
				bm.setCategory(categoryTxt.getText().toString());
//				bm.setStatus((statusTxt.isChecked())?BookStatus.OUT:BookStatus.IN);
				if(bm.getId() <= 0){
					//new book
					dbService.createBook(bm);
				}else{
					//old book
					dbService.updateBook(bm);
				}
				BookFragment.this.dismiss();
				
				((MainActivity)getActivity()).refreshList();
			}
		});
        
        lendBtn = (Button) v.findViewById(R.id.lendBtn);
        
        lendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//judge book status is out
				if(bm.getStatus() == BookStatus.OUT){
					bm.setStatus(BookStatus.IN);
					dbService.updateBook(bm);
					dbService.returnBook(bm);
					toastMsg(R.string.hint_return_back, bm.getName());
					
					BookFragment.this.dismiss();
					((MainActivity)getActivity()).refreshList();
				}else{
					//book is in and will be lent out
					final AutoCompleteTextView lenderTxt = new AutoCompleteTextView(getActivity());
					lenderTxt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							((AutoCompleteTextView)v).showDropDown();
							
						}
					});
					lenderTxt.setOnFocusChangeListener(new OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus){
								String txt = ((AutoCompleteTextView)v).getText().toString();
								if(txt == null || txt.length() == 0){
								((AutoCompleteTextView)v).showDropDown();
								}
							}else{
								((AutoCompleteTextView)v).dismissDropDown();
							}
						}
					});
					lenderTxt.setAdapter(new ArrayAdapter<String>(getActivity(),
			                android.R.layout.simple_dropdown_item_1line, dbService.findPersons()));
					
					new AlertDialog.Builder(getActivity())
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle(R.string.book_lend2)
			        .setMessage(R.string.input_lender)
			        .setView(lenderTxt)
			        .setPositiveButton(R.string.book_lend2, new DialogInterface.OnClickListener() {

			            @Override
			            public void onClick(DialogInterface dialog, int which) {

			            	bm.setStatus(BookStatus.OUT);
			            	bm.setLender(lenderTxt.getText().toString());
			            	bm.setLentAt(new Date());
			            	dbService.updateBook(bm);
			            	dbService.lendBook(bm);
			            	toastMsg(R.string.hint_lent_out, bm.getName());
			            	
			            	BookFragment.this.dismiss();
							((MainActivity)getActivity()).refreshList();
			            }

			        })
			        .setNegativeButton(android.R.string.no, null)
			        .show();
					
					
				}
				
			}
		});

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if(isNew){
        	lendBtn.setVisibility(View.GONE);
        }else{
        	lendBtn.setVisibility(View.VISIBLE);
        }
        if(bm != null && bm.getName() != null){
        	nameTxt.setText(bm.getName());
        	metaTxt.setText(bm.getMetaInfo());
        	isbnTxt.setText(bm.getIsbn());
        	codeTxt.setText(bm.getCode());
        	categoryTxt.setText(bm.getCategory());
        	statusTxt.setText(bm.getStatus().getDisplayName());
        	if(bm.getStatus() == BookStatus.OUT){
        		lenderTxt.setVisibility(View.VISIBLE);
        		lenderTxt.setText(bm.getLender() + " / " + DateUtils.formatDate(bm.getLentAt()));
        	}else{
        		lenderTxt.setVisibility(View.GONE);
        	}
        }
        categoryTxt.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, dbService.findCategories()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    public void toastMsg(int resId, String... args) {
		final String msg = getActivity().getString(resId, args);
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
}
