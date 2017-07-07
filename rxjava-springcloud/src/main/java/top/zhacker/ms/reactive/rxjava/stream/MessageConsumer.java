package top.zhacker.ms.reactive.rxjava.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import rx.Observable;

/**
 * DATE: 2017/4/25 <br>
 * MAIL: hechengopen@gmail.com <br>
 * AUTHOR: zhacker
 */
@EnableBinding(Processor.class)
@Slf4j
public class MessageConsumer {

    @StreamListener(target = Processor.OUTPUT)
    public void receiveMsg(Observable<String> msg) {
        // handle the message
        msg.subscribe(x-> log.info(x));
    }
}
