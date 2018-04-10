package edu.cmu.adroitness.client.commons.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.AsyncTask;

import edu.cmu.adroitness.comm.generic.model.MBRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by oscarr on 3/27/15.
 */
public class FileUploader {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;


    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public Thread  newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =  new LinkedBlockingQueue<Runnable>(128);

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                                     TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);



    public static void upload( MBRequest mbRequest ) {
        try {
            UploadFileTask task = new UploadFileTask( );
            task.executeOnExecutor( THREAD_POOL_EXECUTOR, mbRequest);
            //task.execute( mbRequest );
        } catch (RejectedExecutionException e) {
            try {
                //Thread pool
                UploadFileTask task = new UploadFileTask( );
                task.execute( mbRequest);
            } catch (RejectedExecutionException ex) {
                ex.printStackTrace();
            }
        }
    }


    static class UploadFileTask extends AsyncTask<MBRequest, Void, Void> {

        @Override
        protected Void doInBackground(MBRequest... requests){
            try {
                MBRequest mbRequest = requests[0];
                String url = (String) mbRequest.get(Constants.HTTP_REQUEST_SERVER_URL);
                Object body = mbRequest.get(Constants.HTTP_REQUEST_BODY);
                String name = mbRequest.get(Constants.HTTP_RESOURCE_NAME)
                        + "." + mbRequest.get(Constants.HTTP_FILE_EXTENSION);
                String mimeType = (String) mbRequest.get(Constants.HTTP_MIME_TYPE);

                if (!(body instanceof byte[])) {
                    if (body instanceof String) {
                        body = new File((String) body);
                    }
                    if (body instanceof File) {
                        body = BitmapFactory.decodeFile(((File) body).getAbsolutePath());
                    }
                    if (body instanceof Bitmap) {
                        int quality = mbRequest.get(Constants.IMG_QUALITY) == null ? 100 : (Integer) mbRequest.get(Constants.IMG_QUALITY);
                        Bitmap.CompressFormat cf = mbRequest.get(Constants.IMG_COMPRESS_FORMAT) == null ||
                                (mbRequest.get(Constants.IMG_COMPRESS_FORMAT)).equals("JPEG")
                                ? Bitmap.CompressFormat.JPEG
                                : Bitmap.CompressFormat.PNG;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ((Bitmap) body).compress(cf, quality, baos);
                        body = baos.toByteArray();
                    }
                }

                if (mbRequest.get(Constants.IMG_YUV_FORMAT) != null && ((Boolean) mbRequest.get(Constants.IMG_YUV_FORMAT)) == true) {
                    int width = mbRequest.get(Constants.IMG_WITH) != null
                            ? (Integer) mbRequest.get(Constants.IMG_WITH)
                            : body instanceof Bitmap ? ((Bitmap) body).getHeight()
                            : 640;
                    int height = mbRequest.get(Constants.IMG_HEIGHT) != null
                            ? (Integer) mbRequest.get(Constants.IMG_HEIGHT)
                            : body instanceof Bitmap ? ((Bitmap) body).getWidth()
                            : 480;
                    YuvImage im = new YuvImage((byte[]) body, ImageFormat.NV21, width, height, null);
                    Rect r = new Rect(0, 0, width, height);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    im.compressToJpeg(r, 100, baos);
                    body = baos.toByteArray();
                }
                // this is deprecated. If you want to go back to this version of HttpController, check
                // github repo version before 07/28/2016
                // HttpController.uploadFile((byte[]) body, url, name, mimeType);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
