package OpenCJAPAuth.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class HttpUtils {
    private static final int TIMEOUT_IN_MILLIONS = 20000;

    public static String request(String url, String method, Map<String, String> params) {
        String result = "";
        try {
            if("GET".equals(method) || "DELETE".equals(method)){
                if(params == null){
                    params=new HashMap<String,String>();
                }
            }

            Map<String, String> header = new HashMap<String, String>();

            if ("GET".equals(method) || "DELETE".equals(method)) {
                if(params == null){
                    params=new HashMap<String,String>();
                }
                result = HttpUtils.doGet(url, header, params);
            } else {
                result = HttpUtils.doPost(url, header, joinParams(params));
            }
            return result;

        } catch (Exception err) {
            throw new RuntimeException(err.toString());
        }
    }

    public static String joinParams(Map<String, String> params) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue().isEmpty() ? "" : URLEncoder.encode(entry.getValue(), "utf-8");
                sb.append(entry.getKey()).append("=").append(value).append("&");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (sb.toString().endsWith("&")) {
            result = sb.substring(0, sb.length() - 1);
        }
        return result;
    }

    private static String doGet(String urlStr, Map<String, String> header, Map<String, String> params) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            //trustAllHosts for HTTPS
            trustAllHosts();
            String queryString = "";
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if(entry.getValue()!=null && !"".equals(entry.getValue())){
                        queryString += entry.getKey()
                            + "="
                            + URLEncoder.encode(entry.getValue().toString(),
                            "UTF-8") + "&";
                    }
                }
            }
            if (queryString.length() > 0) {
                queryString = queryString.substring(0, queryString.length() - 1);
                urlStr = urlStr + "?" + queryString;
            }

            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");

            Iterator<Map.Entry<String, String>> it = header.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                conn.setRequestProperty(entry.getKey(), entry.getValue().toString());
            }

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new RuntimeException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }
            conn.disconnect();
        }
    }

    public static String doPost(String url, Map<String, String> header, String param) {
        DataOutputStream out = null;
        BufferedReader in = null;
        String result = "";
        try {
            //trustAllHosts for HTTPS
            trustAllHosts();
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-Type","application/json; charset=UTF-8");
            //conn.addRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

            Iterator<Map.Entry<String, String>> it = header.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                conn.setRequestProperty(entry.getKey(), entry.getValue().toString());
            }

            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            if (param != null && !param.trim().equals("")) {
                out = new DataOutputStream(conn.getOutputStream());
                out.write(param.getBytes());
                out.flush();
            }

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e.printStackTrace(ps);
            //String msg = baos.toString();
            try {
                ps.close();
                baos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslsession) {
                    System.out.println("WARNING: Hostname is not matched for cert.");
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}