package top.zhacker.reactive.rxjava2;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.CountDownLatch;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by zhacker.
 * Time 2017/7/7 上午10:42
 * Desc 文件描述
 */
@Slf4j
public class FileDisplayer {
    
    public static CountDownLatch latch = new CountDownLatch(1);
    
    public static void main(String[] args) {
        practice1();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void practice1() {
        Flowable
                .create((FlowableOnSubscribe<String>) emitter -> {
                    try {

                        File file = ResourceUtils.getFile("classpath:test.txt");
                        
                        FileReader reader = new FileReader(file);
                        BufferedReader br = new BufferedReader(reader);
                        
                        String str;
                        
                        while ((str = br.readLine()) != null && !emitter.isCancelled()) {
                            while (emitter.requested() == 0) {
                                if (emitter.isCancelled()) {
                                    break;
                                }
                            }
                            emitter.onNext(str);
                        }
                        
                        br.close();
                        reader.close();
                        
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    
                    Subscription mSubscription;
                    
                    @Override
                    public void onSubscribe(Subscription s) {
                        mSubscription = s;
                        s.request(1);
                    }
                    
                    @Override
                    public void onNext(String string) {
                        System.out.println(string);
                        try {
                            Thread.sleep(1000);
                            mSubscription.request(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        System.out.println(t);
                        latch.countDown();
                    }
                    
                    @Override
                    public void onComplete() {
                        latch.countDown();
                    }
                });
    }
}
