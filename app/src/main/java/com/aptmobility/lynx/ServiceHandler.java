package com.aptmobility.lynx;

/**
 * Created by safiq on 14/08/15.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceHandler {
    static String response = null;


    HttpURLConnection httpcon;

    public ServiceHandler() {

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    public String makeServiceCall(String url,
                                  String jsonObject) {

        try {

            Log.v("jSon String", url + " - " + jsonObject);

            httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpcon.setRequestMethod("POST");

            // HTTP connection time out

            httpcon.setRequestProperty("Connection", "keep-alive");
            httpcon.setRequestProperty("User-Agent", "Android");
            httpcon.setConnectTimeout(100000);
            httpcon.connect();


//Write

            DataOutputStream wr = new DataOutputStream(
                    httpcon.getOutputStream());
            wr.writeBytes(jsonObject);
            wr.flush();
            wr.close();


//Read

            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            response = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (httpcon != null) {
                httpcon.disconnect();
            }
        }

        return response;
    }
/* Previous Service Handler class

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    HttpsURLConnection httpcon;
    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
*/
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
   /* public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }*/

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    /*public String makeServiceCall(String url,
                                  String jsonObject) {

        try {

            httpcon = (HttpsURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpcon.setRequestMethod("POST");
            httpcon.setDefaultHostnameVerifier(hostnameVerifier);
            // HTTP connection time out

            httpcon.setRequestProperty("Connection", "keep-alive");
            httpcon.setRequestProperty("IMEI", LynxManager.deviceId);
            httpcon.setRequestProperty("User-Agent","Android");
            httpcon.setConnectTimeout(10000);
            httpcon.connect();

                    Log.v("jSon String", url + " - "+jsonObject + "--DeviceId" +LynxManager.deviceId);
//Write
*//*            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject);
            writer.close();
            os.close();*//*
            //Send request
            DataOutputStream wr = new DataOutputStream (
                    httpcon.getOutputStream());
            wr.writeBytes(jsonObject);
            wr.flush();
            wr.close();

//Read
            *//*BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();*//*

            //response = sb.toString();
            InputStream ir = httpcon.getInputStream();

            ByteArrayBuffer baf = new ByteArrayBuffer(50);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = ir.read(buffer)) >= 0) {
// process the buffer, "bytesRead" have been read, no more, no less
                baf.append(buffer, 0, bytesRead);
            }
            ir.close();
            //response = baf.toString();
            response = new String(baf.toByteArray(),"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            if(httpcon != null) {
                httpcon.disconnect();
            }
        }

        return response;
    }*/

        /**
         * Making service call
         * @url - url to make request
         * @method - http request method
         * @params - http request params
         * */
    /*public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            Log.v("jSon_Url_namevaluepair", url + " - "+params + "--DeviceId" +LynxManager.deviceId);
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }*/
}

