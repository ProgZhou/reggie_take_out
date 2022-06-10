package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.reggie.common.CommonResult;
import com.project.reggie.entity.User;
import com.project.reggie.service.UserService;
import com.project.reggie.utils.SMSUtils;
import com.project.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ProgZhou
 * @createTime 2022/06/08
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /*
    * 发送验证码
    * 由于没有阿里云短信服务，这个就做个样子看看
    * */
    public CommonResult<String> sendMessage(@RequestBody User user, HttpSession session) {

        String phone = user.getPhone();

        if(!StringUtils.isEmpty(phone)) {
            //使用工具类生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}", code);
            //使用阿里云短信服务发送服务
            //SMSUtils.sendMessage("", "", phone, code);

            //方案一：将生成的验证码保存起来，暂时保存在session中
//            session.setAttribute(phone, code);

            //方案二：将生成的验证码缓存到redis中，并设置过期时间为5min
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return CommonResult.success("短信发送成功");


        }

        return CommonResult.error("短信发送失败");
    }

    /*
    * 登录方法
    * */
    @PostMapping("/login")
    public CommonResult<User> login(@RequestBody Map<String, Object> map, HttpSession session) {
        log.info("接收到的数据: {}", map);

        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        //String code = map.get("code");
        //方案一：对比接收到的验证码和session中保存的code
        //String codeInSession = session.getAttribute(phone);

        //方案二：从redis中获取缓存的校验码
        //String codeInRedis = redisTemplate.opsForValue().get(phone).toString();

        //if(codeInSession != null && code.equals(codeInSession)) {}

        //判断当前手机号是否是新用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = userService.getOne(wrapper);
        if(null == user) {
            //如果是新用户，则自动注册
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }
        session.setAttribute("user", user.getId());

        //用户登录成功，删除缓存中的验证码
        redisTemplate.delete(phone);

        return CommonResult.success(user);
    }

}
