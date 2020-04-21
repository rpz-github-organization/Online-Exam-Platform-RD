package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.service.SendMailService;
import org.sicnuafcs.online_exam_platform.util.VerCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync
@Service
public class SendMailServiceImpl implements SendMailService {
    //获取redis模板类
    @Autowired
    RedisTemplate redisTemplate;

    //获取邮件发送类
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    /**
     * 从配置文件中获取发件人
     */
    @Value("${spring.mail.username}")
    private  String sender;

    /**
     * 邮件发送
     * 异步请求
     */
    @Async
    @Override
    public void sendEmail(String receiver) throws MailSendException {
        //初始化邮件信息类
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("[Online Exam Platform] 邮箱验证");      //设置邮件标题
        simpleMailMessage.setTo(receiver);    //设置收件人
        simpleMailMessage.setFrom(sender);    //设置发件人

        //生成验证码
        String verification = VerCodeGenerateUtil.generateVerCode();

        //将验证码放入邮箱
        simpleMailMessage.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:"
                + verification
                + ",本验证码五分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");

        //发送邮件
        try{
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e) {
            log.info("邮件发送失败");
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"邮件发送失败");
        }
        try {
            //获取redis操作类
            ValueOperations valueOperations = redisTemplate.opsForValue();

            //设置缓存
            //如果存在的话就更新（删除之后再添加） 如果不存在的话就直接添加
            //处理 多次请求验证码的情况
            //始终保存最新的验证码(set可以实现)
            valueOperations.set(receiver, verification);
            redisTemplate.expire(receiver, 60 * 5, TimeUnit.SECONDS);
        }catch (Exception e) {
            log.info("redis出现问题");
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"redis出现问题");
        }
    }

    //验证用户邮箱与验证码
    @Override
    public Boolean verification(String email, String unverifiedCode) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //从redis中获取验证码
        String verificationCode = (String)valueOperations.get(email);

        //提交的信息是否正确
        if(verificationCode != null && verificationCode.equals(unverifiedCode)) {
            //删除缓存中的数据
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }
}
