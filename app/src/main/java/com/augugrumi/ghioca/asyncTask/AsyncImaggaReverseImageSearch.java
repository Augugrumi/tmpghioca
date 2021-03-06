package com.augugrumi.ghioca.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.augugrumi.ghioca.MyApplication;
import com.augugrumi.ghioca.R;
import com.augugrumi.ghioca.listener.ImaggaReverseImageSearchListener;

import it.polpetta.libris.image.ReverseImageSearch;
import it.polpetta.libris.image.imagga.contract.IImaggaImageSearchResult;

import java.net.URL;

/**
 * @author Marco Zanella
 * @version 0.01
 * @since 0.01
 */

public class AsyncImaggaReverseImageSearch extends AsyncTask<Void, Void, Void> {

    private static String imaggaKey =
            MyApplication.getAppContext().getString(R.string.IMAGGA_KEY);

    private ImaggaReverseImageSearchListener listener;
    private IImaggaImageSearchResult result;
    private boolean error;
    private String url;
    private Exception e;

    public AsyncImaggaReverseImageSearch(String url, ImaggaReverseImageSearchListener listener) {
        this.listener = listener;
        this.url = url;
        error = false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart();
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            result = ReverseImageSearch
                    .getImaggaServices(imaggaKey)
                    .imageSearchBuildQuery()
                    .setImage(new URL(url))
                    .build()
                    .search();
            Log.i("IMAGGA_SEARCH_RESULT", result.toJSONString());
        } catch (Exception exception) {
            e = exception;
            e.printStackTrace();
            error = true;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // TODO think if it could be the right thing to do
        if (error)
            listener.onFailure(e);
        else
            listener.onSuccess(result);
    }
}
