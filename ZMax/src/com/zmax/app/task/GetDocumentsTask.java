package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.Documents;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;

public class GetDocumentsTask extends AsyncTask<String, Void, Documents> {
	private Context context;
	private TaskCallBack callBack;
	private Documents documents;
	
	public GetDocumentsTask(Context context, TaskCallBack callBack) {
		super();
		this.context = context;
		this.callBack = callBack;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (!NetWorkHelper.checkNetState(context))
			Toast.makeText(context, context.getResources().getString(R.string.httpProblem), 300).show();
	}
	
	@Override
	protected Documents doInBackground(String... params) {
		
		documents = NetAccessor.getDocuments(context, params[0], params[1]);
		// if (cityLocation != null) DBAccessor.saveObject(cityLocation);
		return documents;
	}
	
	@Override
	protected void onPostExecute(Documents result) {
		super.onPostExecute(result);
		callBack.onCallBack(documents);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(Documents document);
	}
}
