package crm.om.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import crm.om.enums.ResultCode;
import crm.om.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成JSON Web令牌工具类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Component
public class JwtHelper {
    @Value("${jwt.signKey}")
    private String signKey;
    @Value("${jwt.expired}")
    private String expired;

    @Value("${jwt.authPrefix}")
    private String authPrefix;

    private final static String JWT_ISS = "OM";
    private final static String SUBJECT = "OM_M";
    private final static String PAYLOAD_ID = "user_id";
    private final static String PAYLOAD_NAME = "user_name";

    /**
     * 创建token
     *
     * @param userId   用户ID
     * @param userName 用户名
     * @return token字符串
     */
    public String createToken(String userId, String userName) {
        DateTime date = DateUtil.date();
        Map<String, Object> map = new HashMap<>(4);
        map.put(PAYLOAD_ID, userId);
        map.put(PAYLOAD_NAME, userName);
        return JWT.create()
                // 自定义负载
                .addPayloads(map)
                .setSigner(JWTSignerUtil.hs256(signKey.getBytes()))
                // 设置签发时间
                .setIssuedAt(date)
                // 过期时间（秒）
                .setExpiresAt(DateUtil.offsetSecond(date, Integer.parseInt(expired)))
                // 接收者
                .setAudience(userName)
                // 签发者
                .setIssuer(JWT_ISS)
                // 主题
                .setSubject(SUBJECT)
                .sign();
    }

    /**
     * 解析Header
     *
     * @param authHeader 鉴权字符串
     * @return token
     */
    public String parseHeader(String authHeader) {
        String token = null;
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(authPrefix)) {
            token = authHeader.substring(authPrefix.length()).trim();
        }
        return token;
    }

    /**
     * 解析token
     *
     * @param token token
     * @return 解析值
     */
    private JWT parseToken(String token) {
        return JWTUtil.parseToken(token);
    }

    /**
     * 解析token中的用户名
     *
     * @param token token
     * @return 用户名
     */
    public String parseUserName(String token) {
        return parseToken(token).getPayload(PAYLOAD_NAME).toString();
    }

    /**
     * 解析token中的用户ID
     *
     * @param token token
     * @return 用户ID
     */
    public String parseUserId(String token) {
        return parseToken(token).getPayload(PAYLOAD_ID).toString();
    }

    /**
     * 验证token的有效性
     *
     * @param token token
     */
    public void verified(String token) {
        JWTSigner jwtSigner = JWTSignerUtil.hs256(signKey.getBytes());
        // 1.验证Token的签名
        if (!JWTUtil.verify(token, jwtSigner)) {
            throw new BaseException(ResultCode.TOKEN_ERROR);
        }
        // 2.验证Token的有效期
        try {
            JWTValidator.of(token).validateDate();
        } catch (ValidateException e) {
            throw new BaseException(ResultCode.TOKEN_EXPIRE);
        }
    }
}
