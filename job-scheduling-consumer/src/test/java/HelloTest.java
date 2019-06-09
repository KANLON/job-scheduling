import com.kanlon.App;
import com.kanlon.common.MailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;

/**
 * 测试1
 *
 * @author zhangcanlong
 * @since 2019/6/5 19:41
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class HelloTest {

    @Autowired
    private MailUtil mailUtil;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        try {
            mailUtil.sendHtmlMail("s19961234@126.com", "测试的邮件", "测试的内容");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


}
