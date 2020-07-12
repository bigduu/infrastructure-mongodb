package com.bigduu.infrastructuremongodb.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author YiHao
 * @Date 2019/9/16 23:40
 * @Description
 */
@Slf4j
public class IpUtils {

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 从第三方网络接口获取Location
     *
     * @param ip
     * @return
     */
    public static String getLocation(String ip) {
        OkHttpClient okHttpClient = new OkHttpClient(); // 创建OkHttpClient对象
        String url = "http://whois.pconline.com.cn/ipJson.jsp?ip=" + ip + "&json=true";
        String location = "未知";
        Request okHttpRequest = new Request.Builder().url(url).build(); // 创建一个请求
        try {
            Response response = okHttpClient.newCall(okHttpRequest).execute(); // 返回实体
            if (response.isSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode jsonNode = objectMapper.readTree(response.body().string());
                    location = jsonNode.get("addr").toString();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * 从本地 IP数据库中查询IP对应的Location
     *
     * @param ip
     * @return
     */
    public static String getLocationFromDB(String ip) {
        DbSearcher searcher = null;
        try {
            String dbPath = IpUtils.class.getResource("/ip2region/ip2region.db").getPath();
            File file = new File(dbPath);
            if (!file.exists()) {
                String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                dbPath = tmpDir + "ip.db";
                file = new File(dbPath);
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(IpUtils.class.getClassLoader().getResourceAsStream("classpath:ip2region/ip2region.db")), file);
            }
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, file.getPath());
            Method method = searcher.getClass().getMethod("btreeSearch", String.class);
            if (!Util.isIpAddress(ip)) {
                log.error("Error: Invalid ip address");
            }
            DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);
            return dataBlock.getRegion();
        } catch (Exception e) {
            log.error("获取地址信息异常", e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 获取发起请求的浏览器名称
     */
    public static String getBrowserName(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        if (userAgent != null && userAgent.getBrowser() != null) {
            return userAgent.getBrowser().getName();
        }
        return "";
    }

    /**
     * 获取发起请求的浏览器版本号
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        if (userAgent == null) {
            return "";
        }
        //获取浏览器信息
        Browser browser = userAgent.getBrowser();
        if (browser == null) {
            return "";
        }
        //获取浏览器版本号
        Version version = browser.getVersion(header);
        if (version == null) {
            return "";
        }
        return version.getVersion();
    }

    /**
     * 获取发起请求的操作系统名称
     */
    public static String getSystemName(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        if (userAgent == null) {
            return "";
        }
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        if (operatingSystem == null) {
            return "";
        }
        return operatingSystem.getName();
    }

}
