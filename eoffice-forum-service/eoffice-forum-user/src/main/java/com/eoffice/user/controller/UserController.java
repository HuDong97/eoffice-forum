package com.eoffice.user.controller;

import com.eoffice.common.advice.Result;
import com.eoffice.model.user.pojos.User;
import com.eoffice.user.service.UserService;
import com.eoffice.utils.common.JwtUtil;
import com.eoffice.utils.common.Md5Util;
import com.eoffice.utils.common.MessageValidator;
import com.eoffice.utils.thread.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;

    public UserController(UserService userService, StringRedisTemplate stringRedisTemplate) {
        this.userService = userService;
        this.stringRedisTemplate = stringRedisTemplate;
    }



    //article微服务通过id查询用户昵称,openfeign
    @GetMapping("/nickName")
    public String getNickNameByUserId(@RequestParam("userId") Integer userId) {
        return userService.getNickNameByUserId(userId);
    }

    //用户注册
    @PostMapping("/register")
    public Result<String> register(String username, String password, String email) {

        //调用校验工具类
        if (!MessageValidator.isValidUsername(username)) {
            return Result.error("用户名长度必须在4到16位之间,仅支持数字、中文、英文大小写字母以及@#$%");
        }
        if (!MessageValidator.isValidPassword(password)) {
            return Result.error("密码长度必须在5到16位之间,仅支持数字、英文大小写字母以及@#$%");
        }
        if (!MessageValidator.isValidEmail(email)) {
            return Result.error("仅支持@163.com,@qq.com,@gmail.com,@hotmail.com");
        }
        //通过输入邮箱查找用户
        if (userService.findByEmail(email) != null) {
            return Result.error("邮箱已占用");
        }



        User existingUserByUsername = userService.findByUserName(username);
        User existingUserByEmail = userService.findByEmail(email);

        if (existingUserByUsername == null && existingUserByEmail == null) {

            // 用户名和邮箱均未占用，开始注册
            userService.register(username, password, email);

            return Result.success();
        } else {
            // 构建错误信息
            String errorMessage;
            if (existingUserByUsername != null) {
                errorMessage = "用户名已被占用";
            } else {
                errorMessage = "邮箱已被占用";
            }
            return Result.error(errorMessage);
        }
    }


    //用户登录
    @PostMapping("/login")
    public Result<String> login(String username,
                                String password) {
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断该用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }

        //判断密码是否正确  loginUser对象中的password是密文，对比加密后的密码是否相等
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            //登录成功后，使用id和用户名生成jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            claims.put("permissions",loginUser.getPermissions());
            String token = JwtUtil.genToken(claims);

            //把token存储到redis中，过期时间为7天，与令牌过期时间相同
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,7, TimeUnit.DAYS);

            ThreadLocalUtil.setUser(claims);  // 设置用户信息到ThreadLocal

            return Result.success(token);//返回jwt令牌
        }
        return Result.error("密码错误");
    }

    //用户退出登录时清除redis缓存
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {

        if (token == null || token.isEmpty()) {
            return Result.error("缺少必要的参数");
        }

        // 从 Redis 中删除 token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        // 这里可以返回成功的消息
        return Result.success("退出登录成功");
    }



    //根据用户名查询用户,用户名从ThreadLocalUtil获取
    @GetMapping("/userInfo")
    public Result<User> userInfo() {
        String username = ThreadLocalUtil.getUser("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }



    //更新用户昵称
    @PatchMapping("/updateNickname")
    public Result<String> updateNickname(@RequestParam String nickname) {
        // 正则表达式校验：昵称必须是2-10位的非空字符串
        if (!nickname.matches("^\\S{2,10}$")) {
            return Result.error("昵称必须是2-10位");
        }

        // 如果校验通过，调用服务层更新昵称
        userService.updateNickname(nickname);

        // 返回成功信息，
        return Result.success();
    }


    //更新用户头像
    @PatchMapping("/updateAvatar")
    public Result<String> updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    //更新用户密码
    @PatchMapping("/updatePwd")
    public Result<String> updatePwd(@RequestBody Map<String, String> params,@RequestHeader("Authorization") String token) {
        //1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }
        //调用校验工具类处理文件名中的非法字符
        if (!MessageValidator.isValidPassword(newPwd)) {
            return Result.error("密码长度必须在5到16位之间,仅支持数字、英文大小写字母以及@#$%");
        }

        //调用userService根据用户名获取密码和old_pwd比对,用户名从ThreadLocalUtil获取
        String username = ThreadLocalUtil.getUser("username");
        User loginUser = userService.findByUserName(username);
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码填写不正确");
        }
        //newPwd和rePwd是否一样
        if (!rePwd.equals(newPwd)) {
            return Result.error("两次输入密码不一致");
        }

        //调用service完成密码更新
        userService.updatePwd(newPwd);
        //删除redis中对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();

    }

    //更新用户邮箱
    @PatchMapping("/updateEmail")
    public Result<String> updateEmail(@RequestBody Map<String, String> params) {
        try {
            // 1. 校验参数
            String reEmail = params.get("re_Email");
            String newEmail = params.get("new_Email");

            // 通过用户名获取邮箱和确认邮箱比对,用户名从ThreadLocalUtil获取
            String username = ThreadLocalUtil.getUser("username");
            String userEmail = userService.findEmailByUserName(username);

            // 字符串比较
            if (!userEmail.equals(reEmail)) {
                return Result.error("验证失败，邮箱填写错误");
            }
            //通过输入邮箱查找用户
            if (userService.findByEmail(newEmail) != null) {
                return Result.error("邮箱已占用");
            }
            // 调用邮箱校验工具类
            if (!MessageValidator.isValidEmail(newEmail)) {
                return Result.error("邮箱格式不正确，仅支持@163.com,@qq.com,@gmail.com,@hotmail.com");
            }

            // 调用service完成邮箱更新
            userService.updateEmail(newEmail,username);
            return Result.success();
        } catch (Exception e) {
            // 处理异常，例如记录日志或返回适当的错误信息
            return Result.error("更新邮箱失败");
        }
    }


}
