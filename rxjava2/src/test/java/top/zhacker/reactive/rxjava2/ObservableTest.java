package top.zhacker.reactive.rxjava2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by zhacker.
 * Time 2017/6/24 下午1:47
 * Desc 文件描述
 */
@Slf4j
public class ObservableTest {
    
    @Test
    public void test() throws Exception{
        //创建一个上游 Observable：
        Observable<Integer> observable =  Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        });
        //创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                log.info("subscribe");
            }
        
            @Override
            public void onNext(Integer value) {
                log.info("" + value);
            }
        
            @Override
            public void onError(Throwable e) {
                log.info("error");
            }
        
            @Override
            public void onComplete() {
                log.info("complete");
            }
        };
        //建立连接
        observable.subscribe(observer);
    }
    
    @Test
    public void test2() throws Exception{
        
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
            
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                log.info("subscribe");
            }
        
            @Override
            public void onNext(Integer value) {
                log.info("" + value);
            }
        
            @Override
            public void onError(Throwable e) {
                log.info("error");
            }
        
            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }
    
    @Test
    public void testDisposable() throws Exception{
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            log.info("emit 1");
            emitter.onNext(1);
            log.info("emit 2");
            emitter.onNext(2);
            log.info("emit 3");
            emitter.onNext(3);
            log.info("emit complete");
            emitter.onComplete();
            log.info("emit 4");
            emitter.onNext(4);
            
        }).subscribe(new Observer<Integer>() {
            
            private Disposable mDisposable;
            private int i;
        
            @Override
            public void onSubscribe(Disposable d) {
                log.info("subscribe");
                mDisposable = d;
            }
        
            @Override
            public void onNext(Integer value) {
                log.info("onNext: " + value);
                i++;
                if (i == 2) {
                    log.info("dispose");
                    mDisposable.dispose();
                    log.info("isDisposed : " + mDisposable.isDisposed());
                }
            }
        
            @Override
            public void onError(Throwable e) {
                log.info("error");
            }
        
            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }
    
    @Test
    public void testSubscribeWithOneArg() throws Exception{
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            log.info("emit 1");
            emitter.onNext(1);
            log.info("emit 2");
            emitter.onNext(2);
            log.info("emit 3");
            emitter.onNext(3);
            log.info("emit complete");
            emitter.onComplete();
            log.info("emit 4");
            emitter.onNext(4);
        }).subscribe(integer -> log.info("onNext: " + integer));
    }
    
    @Test
    public void testThread() throws Exception{
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                log.info("Observable thread is : " + Thread.currentThread().getName());
                log.info("emit 1");
                emitter.onNext(1);
            }
        });
    
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                log.info("Observer thread is :" + Thread.currentThread().getName());
                log.info("onNext: " + integer);
            }
        };
    
        observable.subscribe(consumer);
    }
    
    @Test
    public void testThread2() throws Exception{
    
        CountDownLatch latch = new CountDownLatch(1);
        
        Observable<Integer> observable = Observable.create(emitter -> {
            log.info("Observable thread is : " + Thread.currentThread().getName());
            log.info("emit 1");
            emitter.onNext(1);
        });
    
        Consumer<Integer> consumer = integer -> {
            log.info("Observer thread is :" + Thread.currentThread().getName());
            log.info("onNext: " + integer);
            latch.countDown();
        };
        
        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .doOnNext(integer -> log.info("After observeOn(mainThread), current thread is: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.io())
                .doOnNext(integer -> log.info("After observeOn(io), current thread is : " + Thread.currentThread().getName()))
                .subscribe(consumer);
        
        latch.await();
    }
    
    boolean fun(){
        log.info("fun called");
        return true;
    }
    
    @Test
    public void test_fromCallable() throws Exception{
        Observable<Boolean> observable = Observable.fromCallable(this::fun); //延迟执行，直到subscribe
        log.info("build observable");
        observable.subscribe();
    }
    
    @Test
    public void test_just() throws Exception{
        Observable<Boolean> observable =  Observable.just(fun());
        log.info("build observable");
        observable.subscribe();
    }
    
    @Test
    public void test_defer() throws Exception{
        Observable<Boolean> observable = Observable.defer(()-> Observable.just(fun())); //延迟执行，直到subscribe
        log.info("build observable");
        observable.subscribe();
    }
    
    @Test
    public void test_map() throws Exception{
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                log.info("{}", s);
            }
        });
    }
    
    @Test
    public void test_flatMap() throws Exception{
        
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                log.info("{}", s);
            }
        });
        
        TimeUnit.SECONDS.sleep(1);
    }
    
    @Test
    public void test_concatMap() throws Exception{
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10,TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                log.info("{}", s);
            }
        });
    
        TimeUnit.SECONDS.sleep(1);
    }
    
    @Test
    public void test_zip() throws Exception{
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                log.info("{}", "emit 1");
                emitter.onNext(1);
                Thread.sleep(1000);
            
                log.info("{}", "emit 2");
                emitter.onNext(2);
                Thread.sleep(1000);
            
                log.info("{}", "emit 3");
                emitter.onNext(3);
                Thread.sleep(1000);
            
                log.info("{}", "emit 4");
                emitter.onNext(4);
                Thread.sleep(1000); //会被中断
            
                log.info("{}", "emit complete1");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                log.info("{}", "emit A");
                emitter.onNext("A");
                Thread.sleep(1000);
            
                log.info("{}", "emit B");
                emitter.onNext("B");
                Thread.sleep(1000);
            
                log.info("{}", "emit C");
                emitter.onNext("C");
                Thread.sleep(1000);
            
                log.info("{}", "emit complete2");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                log.info("{}", "onSubscribe");
            }
        
            @Override
            public void onNext(String value) {
                log.info("{}", "onNext: " + value);
            }
        
            @Override
            public void onError(Throwable e) {
                log.info("{}", "onError");
            }
        
            @Override
            public void onComplete() {
                log.info("{}", "onComplete");
            }
        });
        
        TimeUnit.SECONDS.sleep(10);
    }
    
    @Test
    public void test_backpressure() throws Exception{
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {   //无限循环发事件
                    emitter.onNext(i);
                }
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Thread.sleep(2000);
                log.info("{}", integer);
            }
        });
    }
    
    @Test
    public void test_backpressure2() throws Exception{
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {   //无限循环发事件
                    emitter.onNext(i);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Thread.sleep(2000);
                log.info("{}", integer);
            }
        });
        
        TimeUnit.MINUTES.sleep(1);
    }
    
    public static void main(String[] args) throws Exception{
        new ObservableTest().test_backpressure2();
    }
}
