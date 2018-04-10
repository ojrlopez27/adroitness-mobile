package com.yahoo.inmind.adroitness; /**
 * Created by oscarr on 7/24/15.
 */

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import edu.cmu.adroitness.client.commons.control.Util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* Copyright (C) 2012 The Android Open Source Project
        *
        * Licensed under the Apache License, Version 2.0 (the "License");
        * you may not use this file except in compliance with the License.
        * You may obtain a copy of the License at
        *
        *      http://www.apache.org/licenses/LICENSE-2.0
        *
        * Unless required by applicable law or agreed to in writing, software
        * distributed under the License is distributed on an "AS IS" BASIS,
        * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        * See the License for the specific language governing permissions and
        * limitations under the License.
        */

public class EncoderTest extends AndroidTestCase {
    private static final String TAG = "EncoderTest";
    private static final boolean VERBOSE = false;
    private static int kNumInputBytes = 256 * 1024;
    private static final long kTimeoutUs = 10000;
    private static HashMap<String, ArrayList<Object>> configs =  new HashMap();
    public static byte[] dataInputAudio;
    public static byte[] dataOutputAudio;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
    }

//    public void testAMRNBEncoders() {
//        LinkedList<MediaFormat> formats = new LinkedList<MediaFormat>();
//        final int kBitRates[] =
//                { 4750, 5150, 5900, 6700, 7400, 7950, 10200, 12200 };
//        for (int j = 0; j < kBitRates.length; ++j) {
//            MediaFormat format  = new MediaFormat();
//            format.setString(MediaFormat.KEY_MIME, "audio/3gpp");
//            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, 8000);
//            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
//            format.setInteger(MediaFormat.KEY_BIT_RATE, kBitRates[j]);
//            formats.push(format);
//        }
//        testEncoderWithFormats("audio/3gpp", formats);
//        Log.e("","end");
//    }
//
//    public void testAMRWBEncoders() {
//        LinkedList<MediaFormat> formats = new LinkedList<MediaFormat>();
//        final int kBitRates[] =
//                { 6600, 8850, 12650, 14250, 15850, 18250, 19850, 23050, 23850 };
//        for (int j = 0; j < kBitRates.length; ++j) {
//            MediaFormat format  = new MediaFormat();
//            format.setString(MediaFormat.KEY_MIME, "audio/amr-wb");
//            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, 16000);
//            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
//            format.setInteger(MediaFormat.KEY_BIT_RATE, kBitRates[j]);
//            formats.push(format);
//        }
//        testEncoderWithFormats("audio/amr-wb", formats);
//        Log.e("","end");
//    }

    public void testAACEncoders() {
        LinkedList<MediaFormat> formats = new LinkedList<MediaFormat>();
        final int kAACProfiles[] = {
                2 /* OMX_AUDIO_AACObjectLC */    //,
//                5 /* OMX_AUDIO_AACObjectHE */,
//                39 /* OMX_AUDIO_AACObjectELD */
        };
        final int kSampleRates[] = { 8000 }; //, 11025, 22050, 44100, 48000
        final int kBitRates[] = { 64000 }; //, 128000
        for (int k = 0; k < kAACProfiles.length; ++k) {
            for (int i = 0; i < kSampleRates.length; ++i) {
                if (kAACProfiles[k] == 5 && kSampleRates[i] < 22050) {
                    // Is this right? HE does not support sample rates < 22050Hz?
                    continue;
                }
                for (int j = 0; j < kBitRates.length; ++j) {
                    for (int ch = 1; ch <= 1; ++ch) {
                        MediaFormat format  = new MediaFormat();
                        format.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm");
                        format.setInteger(
                                MediaFormat.KEY_AAC_PROFILE, kAACProfiles[k]);
                        format.setInteger(
                                MediaFormat.KEY_SAMPLE_RATE, kSampleRates[i]);
                        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, ch);
                        format.setInteger(MediaFormat.KEY_BIT_RATE, kBitRates[j]);
                        formats.push(format);
                    }
                }
            }
        }
        testEncoderWithFormats("audio/mp4a-latm", formats);
        for(ArrayList format : configs.values() ){
            String component = (String)format.get(0);
            MediaFormat f = (MediaFormat) format.get(1);
            Log.e("", "This config works: component: " + component + "  format: " + f);
        }
        Log.e("","end");
    }

    private void testEncoderWithFormats(String mime, List<MediaFormat> formats) {
        List<String> componentNames = getEncoderNamesForType(mime);
        for (String componentName : componentNames) {
            Log.e(TAG, "testing component '" + componentName + "'");
            for (MediaFormat format : formats) {
                Log.e(TAG, "  testing format '" + format + "'");
                assertEquals(mime, format.getString(MediaFormat.KEY_MIME));
                testEncoder(componentName, format);
            }
        }
    }

    private List<String> getEncoderNamesForType(String mime) {
        LinkedList<String> names = new LinkedList<String>();
        int n = MediaCodecList.getCodecCount();
        for (int i = 0; i < n; ++i) {
            MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
            if (!info.isEncoder()) {
                continue;
            }
            if (!info.getName().startsWith("OMX.")) {
                // Unfortunately for legacy reasons, "AACEncoder", a
                // non OMX component had to be in this list for the video
                // editor code to work... but it cannot actually be instantiated
                // using MediaCodec.
                Log.e(TAG, "skipping '" + info.getName() + "'.");
                continue;
            }
            String[] supportedTypes = info.getSupportedTypes();
            for (int j = 0; j < supportedTypes.length; ++j) {
                if (supportedTypes[j].equalsIgnoreCase(mime)) {
                    names.push(info.getName());
                    break;
                }
            }
        }
        return names;
    }

    private int queueInputBuffer(MediaCodec codec, int index) {
        ByteBuffer buffer = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP?
                        codec.getInputBuffer( index ) : codec.getInputBuffers()[index];
        buffer.clear();
        int size = buffer.limit();
//        byte[] zeroes = new byte[size];
//        buffer.put(zeroes);
        buffer.put(dataInputAudio);
        codec.queueInputBuffer(index, 0 /* offset */, size, 0 /* timeUs */, 0);
        return size;
    }

//    private void dequeueOutputBuffer(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
//        codec.releaseOutputBuffer(index, false /* render */);
//    }

    private ArrayList<Byte> dequeueOutputBuffer(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
        if (index >= 0) {
            ByteBuffer outBuf = Util.getOutputBuffer( codec, index);
            byte[] data = new byte[info.size];
            outBuf.get(data);
            outBuf.clear();
            codec.releaseOutputBuffer(index, false /* render */);
            return getbyteToByte(data);
        } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
//            Log.e(TAG, "INFO_OUTPUT_BUFFERS_CHANGED");
        } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//            Log.e(TAG, "INFO_OUTPUT_FORMAT_CHANGED");
        } else if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
//            Log.e(TAG, "INFO_TRY_AGAIN_LATER");
        }
        return new ArrayList<>();
    }


    private ArrayList<Byte> getbyteToByte(byte[] data){
        ArrayList<Byte> bytesList = new ArrayList<>();
        for( byte b : data ){
            bytesList.add( Byte.valueOf(b) );
        }
        return bytesList;
    }


    private byte[] getByteTobyte(ArrayList<Byte> data){
        byte[] byteArray = new byte[data.size()];
        for(int i = 0; i < data.size(); i++  ){
            byteArray[i] = data.get(i);
        }
        return byteArray;
    }



    private void testEncoder(String componentName, MediaFormat format) {
        try {
            MediaCodec codec = MediaCodec.createByCodecName(componentName);
            codec.configure(
                    format,
                    null /* surface */,
                    null /* crypto */,
                    MediaCodec.CONFIGURE_FLAG_ENCODE);

            codec.start();

            int numBytesSubmitted = 0;
            boolean doneSubmittingInput = false;
            int numBytesDequeued = 0;
            kNumInputBytes = dataInputAudio.length;
            ArrayList<Byte> dataStream = new ArrayList<>();

            while (true) {
                int index;
                if (!doneSubmittingInput) {
                    index = codec.dequeueInputBuffer(kTimeoutUs /* timeoutUs */);
                    if (index != MediaCodec.INFO_TRY_AGAIN_LATER) {
                        if (numBytesSubmitted >= kNumInputBytes) {
                            codec.queueInputBuffer(
                                    index,
                                    0 /* offset */,
                                    0 /* size */,
                                    0 /* timeUs */,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            if (VERBOSE) {
                                Log.e(TAG, "queued input EOS.");
                            }
                            doneSubmittingInput = true;
                        } else {
                            int size = queueInputBuffer(codec, index);
                            numBytesSubmitted += size;
                            if (VERBOSE) {
                                //Log.e(TAG, "queued " + size + " bytes of input data.");
                            }
                        }
                    }
                }
                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                index = codec.dequeueOutputBuffer(info, kTimeoutUs /* timeoutUs */);
                if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
                } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                    Log.e(TAG, "INFO_OUTPUT_FORMAT_CHANGED");
                } else {
                    dataStream.addAll( dequeueOutputBuffer(codec, index, info) );
                    numBytesDequeued += info.size;
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        if (VERBOSE) {
                            Log.e(TAG, "dequeued output EOS.");
                        }
                        break;
                    }
                    if (true) { //VERBOSE
                        String key = componentName + format.toString();
                        if (configs.get(key) == null) {
                            ArrayList value = new ArrayList();
                            value.add(componentName);
                            value.add(format);
                            configs.put(key, value);
                        }
                        //Log.e(TAG, "dequeued " + info.size + " bytes of output data.");
                    }
                }
            }
            if (VERBOSE) {
                Log.e(TAG, "queued a total of " + numBytesSubmitted + "bytes, "
                        + "dequeued " + numBytesDequeued + " bytes.");
            }
            int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            int inBitrate = sampleRate * channelCount * 16;  // bit/sec
            int outBitrate = format.getInteger(MediaFormat.KEY_BIT_RATE);
            float desiredRatio = (float) outBitrate / (float) inBitrate;
            float actualRatio = (float) numBytesDequeued / (float) numBytesSubmitted;
//            if (actualRatio < 0.9 * desiredRatio || actualRatio > 1.1 * desiredRatio) {
                Log.e(TAG, "desiredRatio = " + desiredRatio + ", actualRatio = " + actualRatio
                        + "  format: " + format );
//            }
            codec.release();
            codec = null;
            dataOutputAudio = getByteTobyte( dataStream );
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "codec '" + componentName + "' failed configuration.");
            assertTrue("codec '" + componentName + "' failed configuration.", false);
        }
    }
}
