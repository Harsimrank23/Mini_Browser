package org.example.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// creating enum to hold a list of download statuses
// these parameters are various status for downloading
enum DownloadStatus {IDLE,NOT_INITIALISED,FAILED_OR_EMPTY,OK,PROCESSING}

class GetRawData extends AsyncTask<String,Void,String> {
    private static final String TAG = "GetRawData";

    // fields to track our download status :
    private DownloadStatus mDownloadStatus; // m stands for member variable

    // callback variable :
    // declared callback object to be type of MainActivity so that we can pass any MainActivity class object to it.
    // in onPostExecute method we just need to call the onDownloadComplete method of this callback object,and give it the data and status result.
    // modified after creating interface
    private final OnDownloadComplete mCallback;

    // interface:so to define interface we just declare it in a very similar way to declaring a class then we specify the methods that must be implemented by anything that implements the interface
    interface OnDownloadComplete{
        void onDownloadComplete(String data,DownloadStatus status);
    }

    // constructor and initialize our download status:
    public GetRawData(OnDownloadComplete Callback) {
        this.mDownloadStatus=DownloadStatus.IDLE;
        mCallback=Callback;
    }

    // ------------------error resolve--------------
    // it's just doing what would have happened if we'd called the AsyncTask's execute method.
    // when we call the execute method of an asyncTask,it creates a new thread and runs the background method. when that completes the onPostExecute method is called on the main thread.so all we had to do is the same thing without creating a new background thread.
    // so we call doInBackground and pass the return value to onPostExecute.
    // if we're struggling to understand that then pretend getRawData class doesn't extend async task.
    // GetRawData class can be used as either an AsyncTask object or it can also be used as a regular getRawData object.
    // when we call the execute method we're treating it as an AsyncTask but when we runInSameThread method , we're treating it as GetRawData object.
    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread starts");

        // onPostExecute(doInBackground(s));
        // or
        if(mCallback!=null){
            // String result=doInBackground(s);
            // mCallback.onDownloadComplete(result,mDownloadStatus);
            // or
            mCallback.onDownloadComplete(doInBackground(s),mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread ends");
    }

    // override required async methods:

    @Override
    protected void onPostExecute(String s) {
//        Log.d(TAG, "onPostExecute: parameter="+s);
        if(mCallback!=null){
            mCallback.onDownloadComplete(s,mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        // create a url connection first. same as we did earlier...
        HttpURLConnection connection=null;
        BufferedReader reader=null;

        if(strings==null){ // if we didn't get url:
            mDownloadStatus=DownloadStatus.NOT_INITIALISED;
            return null;
        }
        try{
            mDownloadStatus=DownloadStatus.PROCESSING;
            URL url=new URL(strings[0]); // attempt to create URL from the string parameter and take only first element
            
            connection=(HttpURLConnection) url.openConnection(); // opening the connection.
            connection.setRequestMethod("GET"); // telling the connection that we are going to use GET method.
            connection.connect();
            int response=connection.getResponseCode(); // getting response code
            Log.d(TAG, "doInBackground: The response code was "+response);

            StringBuilder result=new StringBuilder();

            reader=new BufferedReader(new InputStreamReader(connection.getInputStream())); // setting up bufferedReader to read data on the input stream and then adding to String Builder.
            // instead of reading characters into a buffer and copy them into a string builder we're actually reading a line at a time.
            String line;
            while(null!=(line=reader.readLine())){
                result.append(line).append("\n");
            }

            mDownloadStatus=DownloadStatus.OK;
            return result.toString(); // resulting string that's been built up now.

        }
        // catch boxes for all the exceptions:
        catch(MalformedURLException e){
            Log.e(TAG, "doInBackground:Invalid URL "+e.getMessage() );
        }catch(IOException e){
            Log.e(TAG, "doInBackground: IO Exception reading data"+e.getMessage() );
        } catch(SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception.Needs permission?"+e.getMessage() );
        } finally { // it is guranteed block whether an exception is thrown or not. so it is a good place to do things like closing streams and readers. although it is after return statement of try block but is called before returning.
            if(connection!=null){
                connection.disconnect(); // disconnect the connections
            }
            if(reader!=null){
                try {
                    reader.close();
                }catch (IOException e){ // closing a reader can also throw IO exception
                    Log.e(TAG, "doInBackground: Error closing stream"+e.getMessage() );
                }
            }
        }
        mDownloadStatus=DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
